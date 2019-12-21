package org.xjtu.framework.modules.user.service;

import java.util.List;

import org.xjtu.framework.core.base.model.EmailLink;
import org.xjtu.framework.core.base.model.Unit;

public interface EmailLinkService {
	public void addEmailLink(EmailLink emailLink);
	public EmailLink findEmailLinkbyIdAndName(String id,String name);
}
