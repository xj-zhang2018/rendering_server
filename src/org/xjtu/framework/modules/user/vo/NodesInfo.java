package org.xjtu.framework.modules.user.vo;

import java.util.Date;

public class NodesInfo {
	private String name;
	private String state;
	private String info;
	
	private String nodesNcpus;
	private String nodesAvailmem;
	private String nodesNetload;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getNodesNcpus() {
		return nodesNcpus;
	}
	public void setNodesNcpus(String nodesNcpus) {
		this.nodesNcpus = nodesNcpus;
	}
	public String getNodesAvailmem() {
		return nodesAvailmem;
	}
	public void setNodesAvailmem(String nodesAvailmem) {
		this.nodesAvailmem = nodesAvailmem;
	}
	public String getNodesNetload() {
		return nodesNetload;
	}
	public void setNodesNetload(String nodesNetload) {
		this.nodesNetload = nodesNetload;
	}
	
  @Override
public String toString() {
	return this.name+"："+this.nodesAvailmem+", "+this.nodesNcpus+", "+this.state;
}
	

}
