package org.xjtu.framework.modules.user.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.xjtu.framework.core.base.constant.MessageState;
import org.xjtu.framework.core.base.model.Message;
import org.xjtu.framework.modules.user.dao.MessageDao;
import org.xjtu.framework.modules.user.service.MessageService;


@Service("messageService")
public class MessageServiceImpl implements MessageService{

   private @Resource MessageDao messageDao;
   
	@Override
	public void insertMessage(Message message) {
		messageDao.addMessage(message);
	}

	@Override
	public List<Message> getMessageByCount(int count) {
		return messageDao.queryMessageByCount(count);
	}

	@Override
	public void setAllMessageChecked() {
		messageDao.setAllMessageChecked();
	}

	@Override
	public List<Message> getUncheckedMessageAndSetChecked() {
		List<Message> msgs=messageDao.queryUncheckedMessage();
		
		if(msgs!=null&&msgs.size()>0){
			for(int i=0;i<msgs.size();i++){
				
				Message m=msgs.get(i);
				m.setState(MessageState.CHECKED);
				
				messageDao.updateMessage(m);
			}
			
			return msgs;
		}else{
			return null;
		}
	}

	
}
