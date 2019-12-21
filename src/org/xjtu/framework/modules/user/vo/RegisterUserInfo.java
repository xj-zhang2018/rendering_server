package org.xjtu.framework.modules.user.vo;

import java.util.Date;

public class RegisterUserInfo {
	
	private String name;
	private String email;
	private String mobile;
	private Double cardBalances;
	private String password;
	private String repassword;
	
	public Double getCardBalances() {
		return cardBalances;
	}
	public void setCardBalances(Double cardBalances) {
		this.cardBalances = cardBalances;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRepassword() {
		return repassword;
	}
	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

}
