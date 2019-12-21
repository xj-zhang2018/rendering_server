package org.xjtu.framework.modules.user.dao;

import java.util.List;
import org.xjtu.framework.core.base.model.Unit;

public interface UnitDao {
	public List<Unit> queryAllUnits();
	public List<Unit> queryUnitsByStatus(int unitStatus);
	public List<Unit> queryNotDeadUnits();
	public void persist(Unit unit);
	public Unit queryUnitsByPbsId(String pbsId);
	public List<Unit> queryUnitsByJobId(String jobId);
	public Unit queryById(String unitId);
	public void updateUnit(Unit unit);
	public void removeUnit(Unit unit);
	
	public int queryCount(int unitStatus);
	
	public List<Unit> pagnate(int pageNum, int pageSize,int unitStatus);
	public int queryCountByUnitName(String searchText,int unitStatus);
	public List<Unit> pagnateByUnitName(String searchText, int pageNum, int pageSize,int unitStatus);
	public List<Unit> pagnateByAccurateUnitName(String searchText, int pageNum,int pageSize,int unitStatus);

}
