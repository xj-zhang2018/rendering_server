package org.xjtu.framework.core.base.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configuration")
public class Configuration {
	private String id;
	private Double unitPrice;//渲染每针的单价
	private Integer nodesNumPerUnit;
	private String fuWuListName;
	private String sceneMemory;
	private String hostStack;
	private String shareSize;

	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getNodesNumPerUnit() {
		return nodesNumPerUnit;
	}
	public void setNodesNumPerUnit(Integer nodesNumPerUnit) {
		this.nodesNumPerUnit = nodesNumPerUnit;
	}
	public String getFuWuListName() {
		return fuWuListName;
	}
	public void setFuWuListName(String fuWuListName) {
		this.fuWuListName = fuWuListName;
	}
	public String getSceneMemory() {
		return sceneMemory;
	}
	public void setSceneMemory(String sceneMemory) {
		this.sceneMemory = sceneMemory;
	}
	public String getHostStack() {
		return hostStack;
	}
	public void setHostStack(String hostStack) {
		this.hostStack = hostStack;
	}
	public String getShareSize() {
		return shareSize;
	}
	public void setShareSize(String shareSize) {
		this.shareSize = shareSize;
	}



	
}
