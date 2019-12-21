package org.xjtu.framework.core.base.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name="calculate")
public class Calculate {
	private String id;
	private String xmlName;
	private Date xmlCreateTime;
	private int xmlStatus;
	private int xmlProgress;
	private int xmlPriority;
	private String xmlFilePath;
	private User user;
	private Project project;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "projectId")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	
	
	
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	public User getUser() {
		return user;
	}
	
	

	public void setUser(User user) {
		this.user = user;
	}
	public String getXmlFilePath() {
		return xmlFilePath;
	}
	public void setXmlFilePath(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getXmlName() {
		return xmlName;
	}
	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}
	
	public Date getXmlCreateTime() {
		return xmlCreateTime;
	}
	public void setXmlCreateTime(Date xmlCreateTime) {
		this.xmlCreateTime = xmlCreateTime;
	}
	public int getXmlStatus() {
		return xmlStatus;
	}
	public void setXmlStatus(int xmlStatus) {
		this.xmlStatus = xmlStatus;
	}
	public int getXmlProgress() {
		return xmlProgress;
	}
	public void setXmlProgress(int xmlProgress) {
		this.xmlProgress = xmlProgress;
	}
	public int getXmlPriority() {
		return xmlPriority;
	}
	public void setXmlPriority(int xmlPriority) {
		this.xmlPriority = xmlPriority;
	}
	
	
}
