package org.xjtu.framework.modules.user.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.modules.user.dao.FrameDao;
import org.xjtu.framework.modules.user.dao.TaskDao;
import org.xjtu.framework.modules.user.dao.UnitDao;
import org.xjtu.framework.modules.user.service.ClusterManageService;
import org.xjtu.framework.modules.user.service.TaskService;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

	private @Resource TaskDao taskDao;
	
	private @Resource FrameDao frameDao;
	
	private @Resource UnitDao unitDao;
	
	private @Resource ClusterManageService clusterManageService;

	@Override
	public void addTask(Task task) {
		taskDao.persist(task);
	}

	@Override
	public void updateTaskInfo(Task task) {
		taskDao.updateTask(task);
	}

	@Override
	public Task findTasksById(String taskId) {
		return  taskDao.queryTasksById(taskId);
	}

	@Override
	public List<Task> findTasksByJobId(String jobId) {
		return taskDao.queryTasksByJobId(jobId);
	}

	@Override
	public List<Task> findTasksByUnitId(String unitId) {
		return taskDao.queryTasksByUnitId(unitId);
	}
	@Override
	public List<Task> findTasksHasProgress(){
		return taskDao.queryTasksHasProgress();
	}
	
}
