package org.xjtu.framework.core.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
//import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xjtu.framework.core.base.constant.SystemConfig;
import org.xjtu.framework.core.base.model.*;
import org.xjtu.framework.core.util.StringUtil;
import org.xjtu.framework.core.util.UUIDGenerator;
import org.xjtu.framework.core.util.XMLUtil;
import org.xjtu.framework.core.util.Xml2Model;
import org.xjtu.framework.modules.user.dao.ClusterManageCommand;
import org.xjtu.framework.modules.user.dao.ConfigurationDao;
import org.xjtu.framework.modules.user.dao.impl.ShenweiCommandImpl;
import org.xjtu.framework.modules.user.service.*;
//import org.eclipse.jetty.server.Authentication.User;


public class Test {
	

	/**
	 * @param args
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws IOException, Exception {
	

	
		//ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext-server.xml");	
		//UnitService unitService=(UnitService)ctx.getBean("unitService");
		
		//List<Unit> units=unitService.findAllUnits();

		

		//System.out.println(units.get(1).getUnitNodesInfo());
		
		//ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext-server.xml");					
		/*FrameService frameService=(FrameService)ctx.getBean("frameService");
		Frame frameClass=frameService.findFrameByNameAndTaskId("QJZL_the Hanging Gardens_Mol_end_changjing_directionalLightShape1SHD.0145.rib", "1d9c905202c746d8ad7705857f339c76");
		System.out.println(frameClass.getFrameName());
		if(frameClass == null){
			System.out.println("nullll");
		}else{
			System.out.println("not null");
		}*/
		//Frame frameClass=frameService.findFrameByNameAndTaskId("1", "1");
		//frameClass.setFrameProgress(12);
		//frameService.updateFrameInfo(frameClass);
		/*
		UserService userService=(UserService)ctx.getBean("userService");
		User tmpUser=userService.loginByEmail("312914352@qq.com", "082e738be42f076d84027217115f1482");
		System.out.println(tmpUser.getEmail());
		
		
		FrameService frameService=(FrameService)ctx.getBean("frameService");
		List<Frame> frames = frameService.findFrameByName("QJZL_the Hanging Gardens_Mol_end_changjing_directionalLightShape1SHD.0150.rib");
		
		for(int j = 0; j < frames.size(); j++){
			
			if((frames.get(j).getTask().getId()).equals("00bbbe6a57c64a3cb42af968d5367f38")){
				
				frames.get(j).setFrameProgress(22);
				frameService.updateFrameInfo(frames.get(j));
			}
		}
		*/
		
		/*String scriptName=UUIDGenerator.getUUID()+".pbs";
		
		String openPBSCommand="#PBS -q batch\n" +
								"#PBS -l nodes="+5+":ppn=12\n" +
								"#echo $PBS_O_WORKDIR\n" +
								"export MV2_ENABLE_AFFINITY=0\n"+
								"export PATH=/home/RenderFarm/RenderWing/bin/:/home/RenderFarm/openmpi-install/bin/:$PATH\n"+
								"export LD_LIBRARY_PATH=/home/RenderFarm/RenderWing/lib/:/home/RenderFarm/openmpi-install/lib/\n"+
								"export PIXIEHOME=/home/RenderFarm/RenderWing/\n"+
								"cat $PBS_NODEFILE | uniq > nodefile.txt\n"+
								"NP=`cat nodefile.txt | wc -l`\n"+
								"#NP=`expr $NP + $NP` # use 2*node processes\n"+
								"cat nodefile.txt > nodefile.txt.tmp\n"+
								"#cat nodefile.txt >> nodefile.txt.tmp\n"+
								"sort nodefile.txt.tmp > nodefile.txt\n"+
								"rm nodefile.txt.tmp -f\n"+
								"mpirun -hostfile nodefile.txt -n $NP rUnit -t 8 -d /home/RenderFarm/building/ -s 192.168.1.100\n";
	    
		System.out.println(openPBSCommand);*/
		/*Integer k=Integer.parseInt("1000");
		int i =2;
		Integer j=k+i;
		System.out.println(j);
		//Integer i =1;
		
		long temp2 = 3000000;
		int  mins= (int)(temp2 / 1000 / 60); 
		if (mins<10){
			System.out.println("cuole");
		}
		System.out.println(mins);
		
		String a=" 127.1.1.0";
		a=a.replaceAll("\\.", "\\\\.");
		System.out.println(a);
		int num=999999999;
		int num2=10000000;
		int n=num/num2;
		System.out.println(n);*/
		
		/*String s="cbox.xml";
		String[]res=s.split("\\.");
		for(String str:res)System.out.println(str);*/
		
		/*SystemConfig systemConfig=new SystemConfig();
		String str=systemConfig.getSystemPasswd();*/

		//Files.createDirectories(Paths.get("C:\\Users\\zxj\\Desktop\\图材\\new"));
		
		//System.out.println("结果是"+Files.exists(Paths.get("C:\\Users\\zxj\\Desktop\\图材\\new")));
		
	
		/*File file=new File("C:\\Users\\xjtu4\\Desktop\\lucy.log");
		String lineInfo=StringUtil.readLastLine(file);
		String ss[]=lineInfo.split("\t");
		String rato="";
		if(ss.length==3)
		{
			rato=ss[1].split(":")[1];
			System.out.println(rato.substring(0, rato.indexOf("%")));
		}*/
		//eg修改采样率
		//XMLUtil.changeAttribute("C:/Users/xjtu4/Desktop/1.xml","scene/sensor/sampler/integer","name:sampleCount","value:1000");
		//eg修改像素
		//XMLUtil.changeAttribute("C:/Users/xjtu4/Desktop/1.xml","scene/sensor/film/integer","name:height","value:10000");
	/*String ss="1,2,3,4,5,6,5-18";
	String res[]=StringUtil.getSelectFrame(ss,4);
	for(String str:res)
	System.out.println(str);*/
		/*String cmd="";
	try {
		cmd="mv /Pictures/";
		System.out.println(1/0);
		
	} catch (Exception e) {
	e.printStackTrace();
		cmd="mv errror";
		
	}
	
	System.out.println(cmd);*/
	
    /*String Old_homePath="/home/export/online1/systest/swsdu/xijiao/Users/camra";
	Old_homePath=Old_homePath.substring(0,Old_homePath.lastIndexOf("/"));
	System.out.println(Old_homePath);*/
		/*String filePath="/home/export/online1/systest/swsdu/xijiao/Users/xjzhang/camera1";
		int x=222;
		int y=333;
		int str=1;
		String cpCmd="cp "+filePath+"/frame"+str+".xml"+" "+filePath+"/frame"+str+"_pre.xml";
		String sampleCountCmd="sed -i 's;<integer name=\"sampleCount\" value=\".*\"/>;<integer name=\"sampleCount\" value=\"111\"/>;g' "+ filePath+"/frame"+str+"_pre.xml";
		String heightCmd="sed -i 's;<integer name=\"height\" value=\".*\"/>;<integer name=\"height\" value=\""+x+"\"/>;g' "+filePath+"/frame"+str+"_pre.xml";
		String widthCmd="sed -i 's;<integer name=\"width\" value=\".*\"/>;<integer name=\"width\" value=\""+y+"\"/>;g' "+filePath+"/frame"+str+"_pre.xml";
		String Cmd=cpCmd+" && "+sampleCountCmd+" && "+heightCmd+" && "+widthCmd;
		System.out.println(Cmd);*/
		
		/*String filePath="/home/export/online1/systest/swsdu/xijiao/Users/xjzhang/camera1";
		String renderInstruct="/home/export/online1/systest/swsdu/xijiao/mitsuba-0420/dist/mitsuba";
		
		String shenweiPbsCommand="-q q_sw_share -I -b -share_size 7168 -n 31 "+ renderInstruct+" "+filePath+" / |tee result-frame-n21";
*/
		
		
		//System.out.println(shenweiPbsCommand);
		
		
		
		/*String str="2019-06-17 22:03:08 94.333% 64.000";
		int ratio=(int)Double.parseDouble(GetRation(str));
		System.out.println("ratio is "+ratio);*/
		
		
		
		
		
	
		StringBuilder builder=new StringBuilder();
	
			for(int i=0;i<10;i++){
				builder.append("frame"+i+".xml\n");
			}
		
		
		System.out.println(builder.toString());
		
		
		
		
		
		
		
		
		
		
	}
	
	
	public static String GetRation(String logLastLine) {
		String rato="";
		if(logLastLine==null||logLastLine.length()==0)return null;
		String ss[]=logLastLine.split(" ");
		String tmp[]=null;
		if(ss.length==4)
		{
			rato= ss[2];
			return rato.substring(0, rato.indexOf("%"));
		}
		return null;
	}
}
