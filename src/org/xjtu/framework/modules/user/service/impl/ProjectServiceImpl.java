package org.xjtu.framework.modules.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Project;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.core.util.StringUtil;
import org.xjtu.framework.modules.user.dao.FrameDao;
import org.xjtu.framework.modules.user.dao.UserDao;
import org.xjtu.framework.modules.user.dao.ProjectDao;
import org.xjtu.framework.modules.user.service.ProjectService;
import org.xjtu.framework.modules.user.service.UserService;
import org.xjtu.framework.modules.user.vo.RegisterUserInfo;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService{

   private @Resource ProjectDao projectDao;
   private @Resource FrameDao frameDao;

	@Override
	public List<Project> findProjectsByUserName(String username) {
		return projectDao.queryProjectsByUserName(username);
	}
	
	@Override
	public List<Project> findProjectsByUserId(String userId) {
		return projectDao.queryProjectsByUserId(userId);
	}
	
	@Override
	public void addProject(Project project) {
		projectDao.persist(project);
	}
	
	@Override
	public void deleteProject(Project project){
		projectDao.removeProject(project);
	}
	
	@Override
	public Project findProjectById(String projectId){
		return projectDao.queryProjectById(projectId);
	}
	
	@Override
	public int findTotalCountByQuery(String searchText,String searchType) {
		int num = 0;
		if(StringUtils.isBlank(searchText)){
			num = projectDao.queryCount();
		}else if(StringUtils.isBlank(searchType)){
			num = projectDao.queryCount();
		}else{
			if(searchType.equals("name")){
				num = projectDao.queryCountByProjectName(searchText);
			}else if(searchType.equals("accurateUserId")){
				num = projectDao.queryCountByAccurateUserId(searchText);
			}else{
				num = projectDao.queryCount();
			}
		}
		return num;
	}
	
	@Override
	public List<Project> listProjectsByQuery(String searchText,String searchType, int pageNum, int pageSize) {
		List<Project> projects = new ArrayList<Project>();
		if(StringUtils.isBlank(searchText)){
			projects = projectDao.pagnate(pageNum, pageSize);
		}else if(StringUtils.isBlank(searchType)){
			projects = projectDao.pagnate(pageNum, pageSize);
		}else{
			if(searchType.equals("name")){
				projects = projectDao.pagnateByProjectName(searchText, pageNum, pageSize);
			}else if(searchType.equals("accurateUserId")){
				projects = projectDao.pagnateByAccurateUserId(searchText, pageNum, pageSize);
			}else{
				projects = projectDao.pagnate(pageNum, pageSize);
			}
		}
		return projects;
	}

	@Override
	public void updateProjectInfo(Project project) {
		projectDao.updateProject(project);	
	}

	@Override
	public Project findProjectByProjectNameAndUserId(String projectName,
			String userId) {
		return projectDao.queryProjectByProjectNameAndUserId(projectName,userId);
	}
	@Override
	public int findFinishFramesNum(){
		
		return frameDao.queryFinishFramesNum();
	}
	@Override
	public String findEarlystartTime(){
		return frameDao.queryEarlystartTime();
		
	}
	@Override
	public String findLastendTime(){
		return frameDao.queryLastendTime();
		
	}
}
