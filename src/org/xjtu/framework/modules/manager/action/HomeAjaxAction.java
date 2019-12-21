package org.xjtu.framework.modules.manager.action;

import java.lang.management.ManagementFactory;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.model.Message;
import org.xjtu.framework.modules.user.service.MessageService;
import com.sun.management.OperatingSystemMXBean;
import javax.annotation.Resource;

@ParentPackage("json-default")
@Namespace("/web/manager")

public class HomeAjaxAction extends ManagerBaseAction {
	 
	private @Resource MessageService messageService;
	
	private List<Message> msgs;
	
	 /*y为cpu利用率*/
	 private double y;
	 
	 /*mem为内存*/
	 private double freeMem;
	 private double busyMem;

  	@Action(value = "queryNewMsg", results = {@Result(name = SUCCESS, type = "json")})
	public String queryNewMsg(){ 
  		
  		msgs=messageService.getUncheckedMessageAndSetChecked();
  		
	    return SUCCESS;			
	}
  	
  	@Action(value = "queryWebServerCPU", results = {@Result(name = SUCCESS, type = "json")})
	public String queryWebServerCPU (){

        OperatingSystemMXBean crtSys = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        y=crtSys.getSystemCpuLoad()*100.0;
        
       

	    return SUCCESS;			
	}

  	@Action(value = "queryWebServerMEM", results = {@Result(name = SUCCESS, type = "json")})
	public String queryWebServerMEM (){
		
        OperatingSystemMXBean crtSys = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        freeMem=Double.parseDouble(String.format("%.2f",crtSys.getFreePhysicalMemorySize()/1000000000.0));
        busyMem=Double.parseDouble(String.format("%.2f",(crtSys.getTotalPhysicalMemorySize()-crtSys.getFreePhysicalMemorySize())/1000000000.0));
        
	    return SUCCESS;			
	}

	public List<Message> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<Message> msgs) {
		this.msgs = msgs;
	}
		
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getFreeMem() {
		return freeMem;
	}

	public void setFreeMem(double freeMem) {
		this.freeMem = freeMem;
	}

	public double getBusyMem() {
		return busyMem;
	}

	public void setBusyMem(double busyMem) {
		this.busyMem = busyMem;
	}
}

	
	


