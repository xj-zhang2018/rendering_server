package org.xjtu.framework.modules.user.dao;

import java.util.List;

import org.xjtu.framework.core.base.model.Calculate;
import org.xjtu.framework.core.base.model.Job;

public interface CalculateDao {

	public int queryCount();

	public int queryCountByCalculateName(String searchText);

	public int queryCountByAccurateUserId(String searchText);

	public List<Calculate> pagnate(int pageNum, int pageSize);

	public List<Calculate> pagnateByCalculateName(String searchText,int pageNum, int pageSize);

	public List<Calculate> pagnateByAccurateUserId(String searchText,int pageNum, int pageSize);

	public Calculate queryCalculateById(String calculateId);

	public void removeCalculate(Calculate calculate);

	public void persist(Calculate c);

	public Calculate queryCalculateByCalculateNameAndUserId(String param,
			String id);

	public List<Calculate> queryCalculates();
	public List<Calculate> queryDistributedCalculate();
	public int queryCountcopyCalculateName(String xmlName);
	public void updateCalculate(Calculate calculate);
	public Calculate queryHeadQueuingJob();
	
	
	
}
