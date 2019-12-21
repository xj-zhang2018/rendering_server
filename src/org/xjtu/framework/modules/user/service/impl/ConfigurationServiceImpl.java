package org.xjtu.framework.modules.user.service.impl;

import javax.annotation.Resource;


import org.springframework.stereotype.Service;
import org.xjtu.framework.core.base.model.Configuration;
import org.xjtu.framework.modules.user.dao.ConfigurationDao;
import org.xjtu.framework.modules.user.service.ConfigurationService;


@Service("configurationService")
public class ConfigurationServiceImpl  implements ConfigurationService{
	
	 private @Resource ConfigurationDao configurationDao;
	 
		@Override
		public double findUnitPrice(){
			double unitPrice=0.00;
			unitPrice=configurationDao.queryUnitPrice();
			return unitPrice;
		}
		@Override
		public Configuration findAllConfiguration(){
			Configuration configuration=configurationDao.queryAllConfiguration();
			Configuration configuration1=new Configuration();
			if(configuration==null){//没有配置的情况下
				configuration1.setUnitPrice(0.0);
				configuration1.setId("1");
				configuration1.setNodesNumPerUnit(1);
				configuration1.setSceneMemory("200");
				configurationDao.persist(configuration1);
				return configuration1;
			}
			if(configuration.getNodesNumPerUnit()==null){
				configuration.setNodesNumPerUnit(1);
				
			}
			if(configuration.getFuWuListName()==null){
				configuration.setFuWuListName("q_sw_hpcag");//默认没有名字的情况下是在神威下执行的
				
			} 
			if(configuration.getSceneMemory()==null){
				configuration.setSceneMemory("200");
			}
			if(configuration.getHostStack()==null){
				configuration.setHostStack("5120");
			}
			if(configuration.getShareSize()==null){
				configuration.setShareSize("1024");
			}
		
			configurationDao.updateConfiguration(configuration);
		
			return configurationDao.queryAllConfiguration();
			
		}
		@Override
		public void updateConfigurationInfo(Configuration configuration){
			configurationDao.updateConfiguration(configuration);
		}

}
