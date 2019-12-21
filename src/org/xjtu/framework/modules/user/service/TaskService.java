package org.xjtu.framework.modules.user.service;

import java.util.List;

import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.core.base.model.Task;
import org.xjtu.framework.modules.protocol.ProgressMessage;

public interface TaskService {
	public void addTask(Task task);
	public void updateTaskInfo(Task task);
	public Task findTasksById(String taskId);
	public List<Task> findTasksByJobId(String jobId);
	public List<Task> findTasksByUnitId(String unitId);
	public List<Task> findTasksHasProgress();
	
}
