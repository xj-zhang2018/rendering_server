package org.xjtu.framework.modules.user.dao;

import java.util.List;

import org.xjtu.framework.core.base.model.User;

public interface UserDao {
	
	public void persist(User userinfo);
	public User queryByName(String name);
	public List<User> queryAllUsers();
	public User queryById(String userId);
	public User queryByEmailAndPasswd(String email,String md5Password);
	public User queryByEmail(String email);
	public User queryByLoginName(String username,String md5Password);
	public void updateUser(User user);
	public void removeUsersById(String userId);
	public void removeUsersByIds(List<String> userIds);///////
	public int queryCountByUserType(int userType);
	public int queryCountByUserName(String searchText, int userType);
	public List<User> pagnateByUserType(int pageNum, int pageSize, int userType);
	public List<User> pagnateByUserName(String searchText, int pageNum, int pageSize, int userType);
}
