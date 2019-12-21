package org.xjtu.framework.modules.user.service;

import org.xjtu.framework.core.base.model.Configuration;

public interface ConfigurationService {
	public double findUnitPrice();
	public Configuration findAllConfiguration();
	public void updateConfigurationInfo(Configuration configuration);

}
