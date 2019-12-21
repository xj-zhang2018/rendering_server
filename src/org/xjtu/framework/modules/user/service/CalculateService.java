package org.xjtu.framework.modules.user.service;

import java.util.List;

import org.xjtu.framework.core.base.model.Calculate;
import org.xjtu.framework.core.base.model.Job;

public interface CalculateService {
	public int findTotalCountByQuery(String searchText,String searchType);
	
	public List<Calculate> listCalculatesByQuery(String searchText,String searchType, int pageNum, int pageSize);

	public Calculate findCalculateById(String calculateId);

	public void deleteCalculate(Calculate calculate);

	public void addCalculate(Calculate c);

	public Calculate findCalculateByCalculateNameAndUserId(String param,String id);

	public List<Calculate> findCalculates();

	public void doCopyCalculates(List<String> xmlIds);
	
	public Calculate findHeadQueuingCalculate();
	
	public void updateCalculateProgress();
	
	public void updateCalculateInfo(Calculate calculate);
	
	
	public void doStartCalculate(List<String> xmlIds);
	
	
	public void doStopCalculate(String xmlId);
}
