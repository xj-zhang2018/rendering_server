package org.xjtu.framework.modules.user.service;

import java.util.Date;
import java.util.List;

import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.modules.user.vo.RegisterUserInfo;

public interface ProjectService {
    public List<Project> findProjectsByUserName(String username);
    public List<Project> findProjectsByUserId(String userId);
    public void addProject(Project project);
    public void deleteProject(Project project);
    public Project findProjectById(String projectId);
    public Project findProjectByProjectNameAndUserId(String projectName,String userId);
    public void updateProjectInfo(Project project);
    
	public int findTotalCountByQuery(String searchText,String searchType);
	public List<Project> listProjectsByQuery(String searchText,String searchType, int pageNum, int pageSize);
	//2016-01
	public int findFinishFramesNum();
	public String findEarlystartTime();
	public String findLastendTime();
	
}
