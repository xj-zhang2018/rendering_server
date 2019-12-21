package org.xjtu.framework.modules.manager.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.constant.SystemConfig;
import org.xjtu.framework.core.base.constant.UnitStatus;
import org.xjtu.framework.core.base.model.Unit;
import org.xjtu.framework.modules.user.service.ClusterManageService;
import org.xjtu.framework.modules.user.service.UnitService;
import org.xjtu.framework.modules.user.vo.NodesInfo;

@ParentPackage("struts-default")
@Namespace("/web/manager")
public class UnitManageAction extends ManagerBaseAction{
	
	private @Resource UnitService unitService;
	
	private @Resource ClusterManageService clusterManageService;
	private @Resource SystemConfig systemConfig;
	
    private int pageTotal = 0;
    
    private int pageSize = 10;
   
	private int pageNum = 1;
	
	private int unitStatus=-2;
       
	private String searchText = "";
    
    private String searchType = "name";
    
    private String unitId;
    
    String unitNodesInfo;
    
	private List<Unit> units;
    
	private List<String> unitIds;
	
	private List<NodesInfo> nodesInfos;
	
	@Action(value = "unitList", results = { @Result(name = "login", location = "/web/login.jsp"),			
    		@Result(name = SUCCESS, location = "/web/manager/unitList.jsp")})
	public String unitList(){

		this.menuName="unitManage";

		int totalSize = unitService.findTotalCountByQuery(searchText, searchType,unitStatus);
		this.pageTotal = (totalSize-1)/this.pageSize + 1;
		units = unitService.listUnitsByQuery(searchText, searchType, pageNum, pageSize,unitStatus);
	
		return SUCCESS;
	}
	
	
	@Action(value = "unitNodesList", results = { @Result(name = "login", location = "/web/login.jsp"),	
			@Result(name ="a", location = "/web/manager/jobList.jsp"),
    		@Result(name = SUCCESS, location = "/web/manager/unitNodesList.jsp")})
	public String unitNodesList() throws Exception{
		
		this.menuName="unitManage";
		
		//String[] nodenames=unitNodesInfo.split("\\|");
		nodesInfos=new ArrayList<NodesInfo>();
		if(systemConfig.getJobManageService().equals("shenweiPBS")){
			int unitNumber=0;
			String[] ss=unitNodesInfo.split(",");
			if(ss!=null&&ss.length>0){
				for(int i=0;i<ss.length;i++){
					String[]nodenames=ss[i].split("-");
				//	for(int j=0;j<nodenames.length;j++){
						if(nodenames.length==2){
								Integer end=Integer.parseInt(nodenames[1]);
								Integer begin=Integer.parseInt(nodenames[0]);
								unitNumber=end-begin+1;
								for(int k=0;k<unitNumber;k++){
									Integer preUnitName=begin+k;
									NodesInfo nodesInfo=clusterManageService.getNodeInfo(preUnitName.toString());
									nodesInfos.add(nodesInfo);
								}
						}
						else{
						NodesInfo nodesInfo=clusterManageService.getNodeInfo(nodenames[0]);
						nodesInfos.add(nodesInfo);
						}
						
					//}
				}
			}
		}
		else if(systemConfig.getJobManageService().equals("openPBS")){
			String[] nodenames=unitNodesInfo.split("\\|");
			for(int i=0 ; i<nodenames.length ; i++){
			NodesInfo nodesInfo=clusterManageService.getNodeInfo(nodenames[i]);
			nodesInfos.add(nodesInfo);
			}
		}
			
	
		
		return SUCCESS;
		
	}
	
	
	@Action(value = "unitsDelete", results = { @Result(name = "login", location = "/web/login.jsp"),
			@Result(name = SUCCESS, location = "/unitList.action", type="redirectAction")})
	public String unitsDelete() throws Exception{

		this.menuName="unitManage";
		
		if(unitIds!=null){
			
			for(int i=0;i<unitIds.size();i++){
				
				Unit unit=unitService.findUnitById(unitIds.get(i));
				
				if(unit!=null){				
					clusterManageService.dismissUnit(unit.getPbsId());
					unit.setUnitStatus(UnitStatus.dead);
					unitService.updateUnitInfo(unit);
				}
				
			}						
			return SUCCESS;
		}
		else
			return ERROR;
	}
		
	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
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

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	 public int getUnitStatus() {
		return unitStatus;
	}

	public void setUnitStatus(int unitStatus) {
		this.unitStatus = unitStatus;
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
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}
	
	public List<String> getUnitIds() {
		return unitIds;
	}

	public void setUnitIds(List<String> unitIds) {
		this.unitIds = unitIds;
	}


	public String getUnitNodesInfo() {
		return unitNodesInfo;
	}


	public void setUnitNodesInfo(String unitNodesInfo) {
		this.unitNodesInfo = unitNodesInfo;
	}


	public List<NodesInfo> getNodesInfos() {
		return nodesInfos;
	}


	public void setNodesInfos(List<NodesInfo> nodesInfos) {
		this.nodesInfos = nodesInfos;
	}
	
	
}
