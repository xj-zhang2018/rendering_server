package org.xjtu.framework.modules.user.dao;

import java.util.List;

import org.xjtu.framework.core.base.model.Project;

public interface ProjectDao {
	public List<Project> queryProjectsByUserName(String username);
	public List<Project> queryProjectsByUserId(String userId);
	public void persist(Project project);
	public void removeProject(Project project);
	public Project queryProjectById(String projectId);
	public Project queryProjectByProjectNameAndUserId(String projectName,String userId);
	public void updateProject(Project project);
	
	public int queryCount();
	public int queryCountByProjectName(String searchText);
	public int queryCountByAccurateUserId(String searchText);
	public List<Project> pagnate(int pageNum, int pageSize);
	public List<Project> pagnateByProjectName(String searchText, int pageNum, int pageSize);
	public List<Project> pagnateByAccurateUserId(String searchText, int pageNum, int pageSize);
	
	
}
