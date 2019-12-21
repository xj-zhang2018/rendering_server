package org.xjtu.framework.modules.user.service;

import java.util.List;
import org.xjtu.framework.core.base.model.Message;

public interface MessageService {

	public void insertMessage(Message message);
	public List<Message> getMessageByCount(int count);
	public List<Message> getUncheckedMessageAndSetChecked();
	public void setAllMessageChecked();
	
}
