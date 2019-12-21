package org.xjtu.framework.modules.user.dao;
import java.util.List;

import org.xjtu.framework.core.base.model.Configuration;
import org.xjtu.framework.core.base.model.Project;

public interface ConfigurationDao {
	public  double queryUnitPrice();
	public Configuration  queryAllConfiguration();
	public void updateConfiguration(Configuration configuration);
	public void persist(Configuration configuration);
}
