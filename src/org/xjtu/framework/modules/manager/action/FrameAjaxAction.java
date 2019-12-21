package org.xjtu.framework.modules.manager.action;

import java.util.List;
import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.model.Frame;
import org.xjtu.framework.modules.user.service.FrameService;

@ParentPackage("json-default")
@Namespace("/web/manager")
public class FrameAjaxAction extends ManagerBaseAction{
	
	private @Resource FrameService frameService;

    private int pageSize = 10;
    
	private int pageNum = 1;
    
    private String searchText = "";
    
    private String searchType = "name";
    
    private int frameStatus=-1;
	
	private List<Frame> frames;

	@Action(value = "frameProgress", results = {@Result(name = SUCCESS, type = "json")})
	public String frameProgress(){
		frames = frameService.listFramesByQuery(searchText, searchType, pageNum, pageSize, frameStatus);
		return SUCCESS;
	}

	public List<Frame> getFrames() {
		return frames;
	}

	public void setFrames(List<Frame> frames) {
		this.frames = frames;
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

	public int getFrameStatus() {
		return frameStatus;
	}

	public void setFrameStatus(int frameStatus) {
		this.frameStatus = frameStatus;
	}

	
}

