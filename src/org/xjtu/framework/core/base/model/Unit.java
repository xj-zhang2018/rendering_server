package org.xjtu.framework.core.base.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;

@Entity
@Table(name="units")
public class Unit {
	private String id;
	private String pbsId;
	private Integer unitStatus;
	private Integer unitNodesNum;
	private String unitNodesInfo;
	private String unitMasterName;
	private Integer idleNumber;
	
	private List<Task> tasks;

	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public String getPbsId() {
		return pbsId;
	}
	public void setPbsId(String pbsId) {
		this.pbsId = pbsId;
	}
	public Integer getUnitStatus() {
		return unitStatus;
	}
	public void setUnitStatus(Integer unitStatus) {
		this.unitStatus = unitStatus;
	}
	public Integer getUnitNodesNum() {
		return unitNodesNum;
	}
	public void setUnitNodesNum(Integer unitNodesNum) {
		this.unitNodesNum = unitNodesNum;
	}
	
	@Lob
	@Type(type="text")
	public String getUnitNodesInfo() {
		return unitNodesInfo;
	}
	public void setUnitNodesInfo(String unitNodesInfo) {
		this.unitNodesInfo = unitNodesInfo;
	}
	public String getUnitMasterName() {
		return unitMasterName;
	}
	public void setUnitMasterName(String unitMasterName) {
		this.unitMasterName = unitMasterName;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "unit")
	@Cascade(value = {CascadeType.SAVE_UPDATE,CascadeType.DELETE_ORPHAN,CascadeType.ALL}) 
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public Integer getIdleNumber() {
		return idleNumber;
	}
	public void setIdleNumber(Integer idleNumber) {
		this.idleNumber = idleNumber;
	}
	
}
