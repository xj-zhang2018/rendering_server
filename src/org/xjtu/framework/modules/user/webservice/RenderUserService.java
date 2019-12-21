package org.xjtu.framework.modules.user.webservice;

import java.util.List;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.xjtu.framework.modules.user.vo.CameraInfo;
import org.xjtu.framework.modules.user.vo.FrameInfo;
import org.xjtu.framework.modules.user.vo.LoginUserInfo;
import org.xjtu.framework.modules.user.vo.ProjectInfo;
import org.xjtu.framework.modules.user.vo.RegisterUserInfo;

@WebService
@SOAPBinding(style = Style.RPC)
public interface RenderUserService {
	public int doRegister(RegisterUserInfo registerUserInfo);
	public int doLogin(LoginUserInfo loginUserInfo);
	public int doGetNewPasswdByUsername(String username);
	public int doAddProject(ProjectInfo projectInfo,String userName);
	public int doDelProject(String projectId);
	public List<ProjectInfo> doGetProjectList(String userName);
	public List<CameraInfo> doGetCameraList(String projectId);
	public int doAddCamera(CameraInfo cameraInfo,String projectId);
	public List<String> doGetCameraPathList(String localname);
	public int doStartCameraRender(String cameraId);
	public List<FrameInfo> doGetFrameList(String cameraId);
	public String doGetUsernameByEmail(String email);
	public DataHandler doGetRenderResult(String userName,String frameId,int xResolution,int yResolution);
	public double doGetCameraProgress(String cameraId);
}
