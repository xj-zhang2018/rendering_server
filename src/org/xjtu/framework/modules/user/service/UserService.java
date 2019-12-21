package org.xjtu.framework.modules.user.service;

import java.util.List;

import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.modules.user.vo.RegisterUserInfo;

public interface UserService {

    public int validateUser(RegisterUserInfo registerUserInfo);
    public boolean validateUsername(String username);
    public boolean validatePassword(String password);
    public boolean validateEmail(String email);
    public User findUserByName(String name);
    public List<User> findAllUsers();
    public User findUserByEmail(String email);    
    public User findUserById(String userId);
    public void deleteUsersById(String userId);
    public void deleteUsersByIds(List<String> userIds);
    public void addUser(User user);
	public User loginByEmail(String email, String md5Password);
	public User loginByLoginName(String username, String md5Password);
	public void updateUserInfo(User user);
	
	public int findTotalCountByQuery(String searchText,String searchType, int userType);
	public List<User> listUsersByQuery(String searchText,String searchType, int pageNum, int pageSize, int userType);
}
