package org.xjtu.framework.modules.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.core.base.model.Job;
import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.modules.user.dao.UnitDao;
import org.xjtu.framework.modules.user.service.UnitService;

@Service("unitService")
public class UnitServiceImpl implements UnitService {

	private @Resource UnitDao unitDao;

	@Override
	public List<Unit> findUnitsByStatus(int unitStatus) {
		return unitDao.queryUnitsByStatus(unitStatus);
	}
	
	@Override
	public List<Unit> findNotDeadUnits() {
		return unitDao.queryNotDeadUnits();
	}
	
	@Override
	public void addUnit(Unit unit) {
		unitDao.persist(unit);
	}
	
	@Override
	public List<Unit> findAllUnits(){
		return unitDao.queryAllUnits();
	}
	
	@Override
	public Unit findUnitById(String unitId) {
		return unitDao.queryById(unitId);
	}
	
	@Override
	public Unit findUnitsByPbsId(String pbsId){
		return unitDao.queryUnitsByPbsId(pbsId);
	}
	
	@Override
	public void updateUnitInfo(Unit unit){
		unitDao.updateUnit(unit);
	}
	
	@Override
	public int findTotalCountByQuery(String searchText,String searchType,int unitStatus) {
		int num = 0;
		if(StringUtils.isBlank(searchText)){
			num = unitDao.queryCount(unitStatus);
		}else if(StringUtils.isBlank(searchType)){
			num = unitDao.queryCount(unitStatus);
		}else{
			if(searchType.equals("name")){
				num = unitDao.queryCountByUnitName(searchText,unitStatus);
			}else{
				num = unitDao.queryCount(unitStatus);
			}
		}
		return num;
	}
	
	@Override
	public List<Unit> listUnitsByQuery(String searchText,String searchType, int pageNum, int pageSize,int unitStatus) {
		List<Unit> units = new ArrayList<Unit>();
		if(StringUtils.isBlank(searchText)){
			units = unitDao.pagnate(pageNum, pageSize,unitStatus);
		}else if(StringUtils.isBlank(searchType)){
			units = unitDao.pagnate(pageNum, pageSize,unitStatus);
		}else{
			if(searchType.equals("name")){
				units = unitDao.pagnateByUnitName(searchText, pageNum, pageSize,unitStatus);
			}else if(searchType.equals("accurateUnitName")){
				units = unitDao.pagnateByAccurateUnitName(searchText, pageNum, pageSize,unitStatus);
			}else{
				units = unitDao.pagnate(pageNum, pageSize,unitStatus);
			}
		}
		
		return units;
	}

	@Override
	public List<Unit> findUnitsByJobId(String jobId) {
		return unitDao.queryUnitsByJobId(jobId);
	}

	
	
}
