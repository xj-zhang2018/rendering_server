package org.xjtu.framework.modules.manager.action;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.model.Message;
import org.xjtu.framework.modules.user.service.ClusterManageService;
import org.xjtu.framework.modules.user.service.MessageService;
import org.xjtu.framework.modules.user.vo.NodesInfo;


@ParentPackage("struts-default")
@Namespace("/web/manager")
public class HomeAction extends ManagerBaseAction{
	
	private @Resource MessageService messageService;

	private List<Message> msgs;
	
	private int count=30;
	
	/*分页所需参数*/
	private int pageNum=1;
	
	private int pageSize = 42;
	private int nodenumPerColumn=6;
	
	private int pageTotal = 0;
	
	private List<List<NodesInfo>> llnodesInfos;
	
	private @Resource ClusterManageService clusterManageService;
  
	@Action(value = "home", results = { @Result(name = ERROR, location = "/web/manager/homePage.jsp"),
    		@Result(name = SUCCESS, location = "/web/manager/homePage.jsp")})
	public String home(){

		msgs=messageService.getMessageByCount(count);
		
		messageService.setAllMessageChecked();
		
		return SUCCESS;
	}
	
	
	@Action(value = "listNodesInfo", results = {@Result(name = SUCCESS, location = "/web/manager/screen/screen.jsp")})
	public String listNodesInfo(){
					//get the 弄得sum's的start and end 
		List<NodesInfo> nodesInfos=clusterManageService.getNodesInfoByStartAndEnd((pageNum-1)*pageSize+1,(pageNum)*pageSize);
		if(nodesInfos!=null){
			llnodesInfos=new ArrayList<List<NodesInfo>>();
			int j=0;
			while(j<nodesInfos.size()){
				int k=0;
				List<NodesInfo> tempNodesInfos=new ArrayList<NodesInfo>();
				while(k<nodenumPerColumn&&j<nodesInfos.size()){
					tempNodesInfos.add(nodesInfos.get(j));
					k++;
					j++;
				}
				llnodesInfos.add(tempNodesInfos);
			}
		}
		return SUCCESS;
	}
	
	@Action(value = "yscreen", results = {@Result(name = SUCCESS, location = "/web/manager/screen/labVideo.jsp")})
	public String yscreen(){
		
		return SUCCESS;
	}

	public List<Message> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<Message> msgs) {
		this.msgs = msgs;
	}


	public int getPageNum() {
		return pageNum;
	}


	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public int getPageTotal() {
		return pageTotal;
	}


	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}


	public List<List<NodesInfo>> getLlnodesInfos() {
		return llnodesInfos;
	}


	public void setLlnodesInfos(List<List<NodesInfo>> llnodesInfos) {
		this.llnodesInfos = llnodesInfos;
	}
	
}

