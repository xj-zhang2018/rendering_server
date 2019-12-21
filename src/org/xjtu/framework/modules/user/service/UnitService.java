package org.xjtu.framework.modules.user.service;

import java.util.List;

import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.core.base.model.User;

public interface UnitService {
	public List<Unit> findUnitsByStatus(int unitStatus);
	public void addUnit(Unit unit);
	public List<Unit> findAllUnits();
	public List<Unit> findNotDeadUnits();
	public Unit findUnitById(String unitId);
	public List<Unit> findUnitsByJobId(String jobId);
	public Unit findUnitsByPbsId(String pbsId);
	public void updateUnitInfo(Unit unit);
	
	
	public int findTotalCountByQuery(String searchText,String searchType,int unitStatus);
	public List<Unit> listUnitsByQuery(String searchText,String searchType, int pageNum, int pageSize,int unitStatus);
}
