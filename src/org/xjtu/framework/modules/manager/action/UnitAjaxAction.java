package org.xjtu.framework.modules.manager.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.modules.user.service.UnitService;

@ParentPackage("json-default")
@Namespace("/web/manager")
public class UnitAjaxAction extends ManagerBaseAction{
	
	private @Resource UnitService unitService;
	
	private List<Unit> units;
	
    private int pageSize = 10;
   
	private int pageNum = 1;
	
	private int unitStatus=-2;

	private String searchText = "";
    
    private String searchType = "name";
    


	@Action(value = "unitStatus", results = {@Result(name = SUCCESS, type = "json")})
	public String unitStatus(){
		units = unitService.listUnitsByQuery(searchText, searchType, pageNum, pageSize, unitStatus);
		return SUCCESS;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}
	
	public int getUnitStatus() {
		return unitStatus;
	}

	public void setUnitStatus(int unitStatus) {
		this.unitStatus = unitStatus;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}


	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> jobs) {
		this.units = units;
	}

	
}

