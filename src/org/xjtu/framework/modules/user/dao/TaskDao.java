package org.xjtu.framework.modules.user.dao;

import java.util.List;

import org.xjtu.framework.core.base.model.Task;

public interface TaskDao {
	public void persist(Task task);
	public void updateTask(Task task);
	public Task queryTasksById(String taskId);
	public List<Task> queryTasksByJobId(String jobId);
	public List<Task> queryTasksByUnitId(String unitId);
	public List<Task> queryTasksHasProgress();
}
