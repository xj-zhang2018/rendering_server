package org.xjtu.framework.modules.user.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xjtu.framework.core.base.model.EmailLink;
import org.xjtu.framework.modules.user.dao.EmailLinkDao;
import org.xjtu.framework.modules.user.service.EmailLinkService;

@Service("emailLinkService")
public class EmailLinkServiceImpl implements EmailLinkService{
	private @Resource EmailLinkDao emailLinkDao;

	@Override
	public void addEmailLink(EmailLink emailLink) {
		emailLinkDao.persist(emailLink);
	}

	@Override
	public EmailLink findEmailLinkbyIdAndName(String id, String name) {
		return emailLinkDao.queryEmailLinkbyIdAndName(id,name);
	}
}
