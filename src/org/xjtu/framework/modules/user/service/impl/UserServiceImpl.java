package org.xjtu.framework.modules.user.service.impl;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.xjtu.framework.core.base.constant.RegistReturnInfo;
import org.xjtu.framework.core.base.model.User;

import org.xjtu.framework.core.util.MD5;

import org.xjtu.framework.core.util.LinuxInvoker;
import org.xjtu.framework.core.util.StringUtil;
import org.xjtu.framework.core.util.UUIDGenerator;
import org.xjtu.framework.modules.user.dao.UserDao;
import org.xjtu.framework.modules.user.service.UserService;
import org.xjtu.framework.modules.user.vo.RegisterUserInfo;


@Service("userService")
public class UserServiceImpl implements UserService{

   private @Resource UserDao userDao;
      
	@Override
	public void deleteUsersById(String userId) {
		userDao.removeUsersById(userId);
	}
	
	@Override
	public void deleteUsersByIds(List<String> userIds) {
		userDao.removeUsersByIds(userIds);
	}
	  
   @Override
   public boolean validateEmail(String email) {
	   Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
	   Matcher matcher = pattern.matcher(email);
	   return matcher.matches();
   }
   
   @Override
   public boolean validateUsername(String username){
    	if(StringUtils.isEmpty(username)){
    		return false;
    	}else{
    		if(username.length() <= 20){
    			if(StringUtil.isLetterOrNumber(username)){
    				return true;
    			}else{
        			return false;    				
    			}
    		}else{
    			return false;
    		}
    	}
    }
	
    @Override
    public boolean validatePassword(String password){
    	if(StringUtils.isEmpty(password)){
    		return false;
    	}else{
    		if(password.length() <= 20 && password.length() >= 6){
    			return true;
    		}else{
    			return false;
    		}
    	}
    	
    }
	
	@Override
	public int validateUser(RegisterUserInfo registerUserInfo) {
   	
    	if(registerUserInfo == null){
    		return RegistReturnInfo.nullInfo;
    	}else{
    		if(!StringUtils.isEmpty(registerUserInfo.getName())){
    			if(registerUserInfo.getName().length() >= 20){
    				return RegistReturnInfo.illegalName;
    			}
    		}else{
				return RegistReturnInfo.nullName;
    		}
    		
    		if(!StringUtils.isEmpty(registerUserInfo.getPassword())){
    			if(!validatePassword(registerUserInfo.getPassword())){
    				return RegistReturnInfo.illegalPassword;
    			} 
    			if(!registerUserInfo.getPassword().equals(registerUserInfo.getRepassword())){
    				return RegistReturnInfo.diffRepassword;
    			}
    		}else{
    			return RegistReturnInfo.nullPassword;
    		}
    		
    		if(!StringUtils.isEmpty(registerUserInfo.getEmail())){
    			if(!StringUtil.isEmail(registerUserInfo.getEmail())){
    				return RegistReturnInfo.illegalEmail;
    			}
    		}else{
    			return RegistReturnInfo.nullEmail;
    		}
    		
    		if(!StringUtils.isEmpty(registerUserInfo.getMobile())){
    			if(!StringUtil.isInteger(registerUserInfo.getMobile()) || registerUserInfo.getMobile().length() != 11){
    				return RegistReturnInfo.illegalMobile;
    			}
    		}
    		return RegistReturnInfo.success;
    	}  	    	

	}

	@Override
	public User findUserByName(String name) {
		return userDao.queryByName(name);
	}
	
	@Override
	public User findUserById(String userId) {
		return userDao.queryById(userId);
	}
	
	@Override
	public void addUser(User user) {
		userDao.persist(user);	
	}

	@Override
	public User loginByEmail(String email, String md5Password) {
		return userDao.queryByEmailAndPasswd(email, md5Password);
	}

	@Override
	public User loginByLoginName(String username, String md5Password) {
		return userDao.queryByLoginName(username, md5Password);
	}

	@Override
	public void updateUserInfo(User user){
		userDao.updateUser(user);
	}

	@Override
	public User findUserByEmail(String email) {
		return userDao.queryByEmail(email);
	}
	
	@Override
	public int findTotalCountByQuery(String searchText,String searchType, int userType) {
		int num = 0;
		if(StringUtils.isBlank(searchText)){
			num = userDao.queryCountByUserType(userType);
		}else if(StringUtils.isBlank(searchType)){
			num = userDao.queryCountByUserType(userType);
		}else{
			if(searchType.equals("name")){
				num = userDao.queryCountByUserName(searchText, userType);
			}else{
				num = userDao.queryCountByUserType(userType);
			}
		}
		return num;
	}
	
	@Override
	public List<User> listUsersByQuery(String searchText,String searchType, int pageNum, int pageSize, int userType) {
		List<User> users = new ArrayList<User>();
		if(StringUtils.isBlank(searchText)){
			users = userDao.pagnateByUserType(pageNum, pageSize, userType);
		}else if(StringUtils.isBlank(searchType)){
			users = userDao.pagnateByUserType(pageNum, pageSize, userType);
		}else{
			if(searchType.equals("name")){
				users = userDao.pagnateByUserName(searchText, pageNum, pageSize, userType);
			}else{
				users = userDao.pagnateByUserType(pageNum, pageSize, userType);
			}
		}
		
		System.out.println("get users size is"+users);
		return users;
	}
	
	@Override
	public List<User> findAllUsers() {
		return userDao.queryAllUsers();
	}
}
