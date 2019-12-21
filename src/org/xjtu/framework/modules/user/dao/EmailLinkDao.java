package org.xjtu.framework.modules.user.dao;

import org.xjtu.framework.core.base.model.EmailLink;

public interface EmailLinkDao {
	public void persist(EmailLink emailLink);
	public EmailLink queryEmailLinkbyIdAndName(String id,String name);
}
