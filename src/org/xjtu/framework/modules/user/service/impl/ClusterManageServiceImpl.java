package org.xjtu.framework.modules.user.service.impl;
import java.io.StringReader;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Service;
import org.xjtu.framework.core.base.constant.SystemConfig;
import org.xjtu.framework.modules.user.dao.ClusterManageCommand;
import org.xjtu.framework.modules.user.service.ClusterManageService;
import org.xjtu.framework.modules.user.vo.NodesInfo;
import org.xml.sax.InputSource;

/**
 * @author zxj
 *
 */
/**
 * @author zxj
 *
 */
@Service("clusterManageService")
public class ClusterManageServiceImpl implements ClusterManageService{
	private static final Log log = LogFactory.getLog(ClusterManageServiceImpl.class);

	private @Resource ClusterManageCommand pbsCommand;
	
	private @Resource ClusterManageCommand shenweiCommand;
	
	private @Resource SystemConfig systemConfig;
	
	@Override
	public int getFreeNodesNum() {
		if(systemConfig.getJobManageService().equals("openPBS")){
			return pbsCommand.getFreeNodesNum();
		}else if(systemConfig.getJobManageService().equals("shenweiPBS")){
		return shenweiCommand.getFreeNodesNum();
		}else{
			return -1;
		}
	}
	
	@Override
	public int getAllNodesNum() {
		if(systemConfig.getJobManageService().equals("openPBS")){
			return pbsCommand.getAllNodesNum();
		}else if(systemConfig.getJobManageService().equals("shenweiPBS")){
			return shenweiCommand.getAllNodesNum();
		}else{
			return -1;
		}
	}
	
	@Override
	public int getJobExclusiveNodesNum() {
		if(systemConfig.getJobManageService().equals("openPBS")){
			return pbsCommand.getJobExclusiveNodesNum();
		}else if(systemConfig.getJobManageService().equals("shenweiPBS")){
			return shenweiCommand.getJobExclusiveNodesNum();
		}else{
			return -1;
		}
	}
	
	@Override
	public int getDownNodesNum() {
		if(systemConfig.getJobManageService().equals("openPBS")){
			return pbsCommand.getDownNodesNum();
		}else if(systemConfig.getJobManageService().equals("shenweiPBS")){
			return shenweiCommand.getDownNodesNum();
		}else{
			return -1;
		}
	}

	@Override
	public boolean isReady(String id) {
		if(systemConfig.getJobManageService().equals("openPBS")){
			String result=pbsCommand.getJobInfo(id);
			if (result.contains("job_state = R")){
				return true;
			}
			return false;
		}else{
			return false;
		}
	}

	@Override
	public boolean isFault(String id) {
		if(systemConfig.getJobManageService().equals("openPBS")){
			String result=pbsCommand.getJobInfo(id);
			
			if(result!=null){
				if (result.contains("job_state = R")||result.contains("job_state = Q")){
					return false;
				}else{
					return true;
				}
			}else{
				return true;
			}
		}else if(systemConfig.getJobManageService().equals("shenweiPBS")){
			String result=shenweiCommand.getJobInfo(id);
			if(result!=null){
				if (result.contains("RUN")||result.contains("STARTING")||result.contains("PEND")){
					return false;
				}else{
					return true;
				}
			}else{
				return true;
			}
			
		}else{
			return false;
		}

	}

	@Override
	public void dismissUnit(String id) {
		if(systemConfig.getJobManageService().equals("openPBS")){
			pbsCommand.delJob(id);
		}else if(systemConfig.getJobManageService().equals("shenweiPBS")){
			shenweiCommand.delJob(id);
		}else{
			return;
		}
	}

	@Override
	public String submit(String scriptPath) {
		if(systemConfig.getJobManageService().equals("openPBS")){
			return pbsCommand.submit(scriptPath);
		}else if(systemConfig.getJobManageService().equals("shenweiPBS")){
			//转化神威环境到pbs测试，
			String shenweiPbsCommand=scriptPath;
			String result=shenweiCommand.submit(shenweiPbsCommand);
			result=result.substring(result.indexOf('<')+1,result.indexOf('>'));
			return result;
		}else{
			return null;
		}
	}
	
	@Override
	public String getNodeString(String id) throws Exception {
		if(systemConfig.getJobManageService().equals("openPBS")){
			String result=pbsCommand.getJobInfoByXmlFormat(id);
			
			if (result!=null) {
				//解析result获得节点名
				StringReader read = new StringReader(result);			
				InputSource source = new InputSource(read);			
				SAXBuilder sb = new SAXBuilder();
				Document doc = sb.build(source);
				
				Element data = doc.getRootElement();
				List list = data.getChildren();
				
				Element job = (Element)list.get(0);
				
				Element sta = job.getChild("exec_host");
	
				String exec_host = sta.getText();
				
				//exec_host字符串具体到每个节点每个核，因此较长，需要处理
				//处理后只定位到节点  eg：slaver1/0+master/0
         	String[] hosts=exec_host.split("\\+");  
				
				for(int i=0;i<hosts.length;i++){
					int endIndex=hosts[i].indexOf("/");
					hosts[i]=hosts[i].substring(0,endIndex);
				}
				List<String> tempList = new LinkedList<String>();
				//去除重复字符串，放到tempList中
				for (int i = 0; i < hosts.length; i++) {
					if (!tempList.contains(hosts[i])) {
						tempList.add(hosts[i]);
					}
				}
				String ret="";
				for(int i=0;i<tempList.size();i++){
					ret+=tempList.get(i)+"|";
				}
				return ret;
			}else
				return null;
		}else if(systemConfig.getJobManageService().equals("shenweiPBS")){		
			//修改代码，统一为pbs
			String result=shenweiCommand.getJobInfoByXmlFormat(id);
			System.out.print(result);
			return result;
			
		}else{
			return null;
		}
		
	}
	
	@Override
	public String getJobnameByIp(String ip) {
		if(systemConfig.getJobManageService().equals("openPBS")){
			
			try{
				//根据ip地址找到主机名
				InetAddress ad;
				ad = InetAddress.getByName(ip);
				String nodename = ad.getHostName();
				
				String result=pbsCommand.getXMLNodeInfoByNodename(nodename);
				
				if (result!=null) {
					//解析result获得节点名
					StringReader read = new StringReader(result);			
					InputSource source = new InputSource(read);			
					SAXBuilder sb = new SAXBuilder();
					Document doc = sb.build(source);
					
					Element data = doc.getRootElement();
					List list = data.getChildren();
					
					Element nodes = (Element)list.get(0);
					
					Element sta = nodes.getChild("jobs");
					
					//exec_host字符串具体到每个节点每个核，因此较长，需要处理
					//处理后只定位到节点
					String[] jobs=sta.getText().split(",");
					
	
					int startIndex=jobs[0].indexOf("/");
					return jobs[0].substring(startIndex+1,jobs[0].length());
					
				}else
					return null;
			
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			
		}else if(systemConfig.getJobManageService().equals("shenweiPBS")){
			
			return shenweiCommand.getJobNameByIp(ip);
			
		}else{
			return null;
		}
	}

	@Override
	public NodesInfo getNodeInfo(String name) {
		if(systemConfig.getJobManageService().equals("openPBS")){			
			
			try{
				String result=pbsCommand.getXMLNodeInfoByNodename(name);
				
				if (result!=null) {
					//解析result获得节点名
					StringReader read = new StringReader(result);			
					InputSource source = new InputSource(read);			
					SAXBuilder sb = new SAXBuilder();
					Document doc = sb.build(source);
					
					Element data = doc.getRootElement();
					List list = data.getChildren();
					
					Element nodes = (Element)list.get(0);
					
					Element sta = nodes.getChild("state");
					
					if(sta.getText().equals("down")){

						NodesInfo nodesInfo=new NodesInfo();
						nodesInfo.setName(name);
						nodesInfo.setState(sta.getText());
						nodesInfo.setInfo("state=down");
						return nodesInfo;
						
					}else{
						
						Element status = nodes.getChild("status");
						
						String[] ss=status.getText().split(",");
						
						NodesInfo nodesInfo=new NodesInfo();
						nodesInfo.setName(name);
						nodesInfo.setState(ss[13].substring(ss[13].indexOf('=')+1,ss[13].length()));
						nodesInfo.setInfo(ss[7]+"&#13"+ss[9]+"&#13"+ss[11]+"&#13"+ss[13]);
						
						nodesInfo.setNodesAvailmem(ss[7].substring(ss[7].indexOf('=')+1,ss[7].length()));
						nodesInfo.setNodesNcpus(ss[9].substring(ss[9].indexOf('=')+1,ss[9].length()));
						nodesInfo.setNodesNetload(ss[11].substring(ss[11].indexOf('=')+1,ss[11].length()));
						
						
						return nodesInfo;
					}
					
				}else
					return null;
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			
		}else if(systemConfig.getJobManageService().equals("shenweiPBS")){
			
			String result=shenweiCommand.getXMLNodeInfoByNodename(name);
			
			if (result!=null) {
				
				String[] ss=result.split(",");
				
				if(ss.length<5)
				{
					NodesInfo nodesInfo=new NodesInfo();
					nodesInfo.setName(ss[0]);
					return nodesInfo;
				}
				NodesInfo nodesInfo=new NodesInfo();
				nodesInfo.setName(ss[0]);
				nodesInfo.setState(ss[4]);
				
				if(!ss[4].equals("down")){
					nodesInfo.setInfo("MEM="+ss[6]+"&#13"+"CORES="+ss[5]+"&#13"+"STATUS="+ss[4]);
					
					nodesInfo.setNodesAvailmem(ss[6]);
					nodesInfo.setNodesNcpus(ss[5]);
					
				}else{
					nodesInfo.setInfo("STATUS="+ss[4]);
				}
				
				
				return nodesInfo;
				
			}else{
				return null;
			}
			
		}else{
			return null;
		}
	}
	
	@Override
	public List<NodesInfo> getNodesInfoByStartAndEnd(int start,int end){
		String result="";
		log.info("start is "+start+",end is "+end);
		if(systemConfig.getJobManageService().equals("openPBS")){			
			//待完成,新增代码
		
			 result=pbsCommand.getNodesInfoByStartAndEnd(start,end);
			 List<NodesInfo>res=getSubNodeList(start,end,result);
			 if(res!=null)return res;
			 return null;
		}else if(systemConfig.getJobManageService().equals("shenweiPBS"))
          {
			
			 result=shenweiCommand.getNodesInfoByStartAndEnd(start,end);
			
			if (result!=null) {
							
				String[] rows=result.split("\\n");
				
				if(rows!=null&&rows.length>0){
					
					List<NodesInfo> nodesInfos=new ArrayList<NodesInfo>();
					
					for(int i=0;i<rows.length;i++){
						String[] ss=rows[i].split(",");
						
						if(ss.length<5)
						{
							NodesInfo nodesInfo=new NodesInfo();
							nodesInfo.setName(ss[0]);
							nodesInfos.add(nodesInfo);
							continue;
						}else{
							NodesInfo nodesInfo=new NodesInfo();
							nodesInfo.setName(ss[0]);
							nodesInfo.setState(ss[5]);
						
							if(ss[5].equals("idle")||ss[5].equals("busy")){
								nodesInfo.setInfo("MEM="+ss[6]+"&#13"+"CORES="+ss[7]+"&#13"+"STATUS="+ss[5]);
								
								nodesInfo.setNodesAvailmem(ss[9]);
								nodesInfo.setNodesNcpus(ss[7]);
								
							}else{
								nodesInfo.setInfo("STATUS="+ss[5]);
							}													
							nodesInfos.add(nodesInfo);
						}
					}
					return nodesInfos;
				}else{
					return null;
				}				
			}else{
				return null;
			}
			
		}else{
			return null;
		}
}
	
	
	//更具其实和结束从宗的字符串中取出制定长度的字符串节点的值
 public List<NodesInfo> getSubNodeList(int start,int end,String nodeStr){
	 if (nodeStr!=null) {
			//解析result获得节点名
			StringReader read = new StringReader(nodeStr);			
			InputSource source = new InputSource(read);			
			SAXBuilder sb = new SAXBuilder();
			DecimalFormat  df   = new DecimalFormat("#0.00");   
			try {
				Document doc = sb.build(source);
				Element data = doc.getRootElement();
              List<Element> list = (List<Element>)data.getChildren();
              List<NodesInfo> nodesInfos=new ArrayList<NodesInfo>();
              String[] ss=null;
              Element elem=null;
              //预防起始和结束的节点数大于宗的长的的情况
              if(start>list.size())return null;
              if(end>list.size())end=list.size();
             for (int i = start-1; i <end; i++) {
            	 elem=list.get(i);
             	 Element sta = elem.getChild("status");
             	 Element nameEle = elem.getChild("name");
             	 String statusStr = sta.getText();
             	 String name = nameEle.getText();
             	 if(statusStr!=null&&statusStr.length()>0)
             		ss=statusStr.split(",");
             	 if(ss.length==20){  
             	 NodesInfo nodesInfo=new NodesInfo();
						nodesInfo.setName(name);
						nodesInfo.setState(ss[13].split("=")[1]);
						String tmp=ss[7].split("=")[1];
						String Availmem=df.format(Double.parseDouble(tmp.substring(0,tmp.indexOf("kb")))/1024)+"G";
						nodesInfo.setNodesAvailmem(Availmem);
						nodesInfo.setInfo("MEM="+Availmem+"CORES="+ss[9].split("=")[1]+"STATUS="+ss[13].split("=")[1]);
						nodesInfo.setNodesNcpus(ss[9].split("=")[1]);
						nodesInfo.setNodesNetload(ss[12].split("=")[1]);
						nodesInfos.add(nodesInfo);
						 log.info("get node length info"+nodesInfo.getName()+"info is"+nodesInfo.getInfo());
             	 }
             	 if(ss.length==19){  
	                	 NodesInfo nodesInfo=new NodesInfo();
							nodesInfo.setName(name);
							nodesInfo.setState(ss[12].split("=")[1]);
							String tmp=ss[6].split("=")[1];
							String Availmem=df.format(Double.parseDouble(tmp.substring(0,tmp.indexOf("kb")))/1024)+"G";
							nodesInfo.setNodesAvailmem(Availmem);
							nodesInfo.setInfo("MEM="+Availmem+"CORES="+ss[8].split("=")[1]+"STATUS="+ss[12].split("=")[1]);
							nodesInfo.setNodesNcpus(ss[8].split("=")[1]);
							nodesInfo.setNodesNetload(ss[11].split("=")[1]);
							nodesInfos.add(nodesInfo);
							 log.info("get node length info"+nodesInfo.getName()+"info is"+nodesInfo.getInfo());
	                	 }
             	 }
              log.info("get node length is "+nodesInfos.size()+"info is"+nodesInfos);
              return nodesInfos;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}else return null;
	}
	
	
	
	
	
}
