package org.xjtu.framework.modules.user.dao;

import java.util.List;

import org.xjtu.framework.core.base.model.Message;

public interface MessageDao {
	public void addMessage(Message message);
	public void updateMessage(Message message);
	public List<Message> queryMessageByCount(int count);
	public List<Message> queryUncheckedMessage();
	public void setAllMessageChecked();
}
