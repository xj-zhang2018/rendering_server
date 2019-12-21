package org.xjtu.framework.modules.normalUser.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xjtu.framework.core.base.model.OrderForm;
import org.xjtu.framework.core.base.model.User;
import org.xjtu.framework.modules.user.service.OrderService;
import org.xjtu.framework.modules.user.service.UserService;

import com.unionpay.upop.sdk.QuickPaySampleServLet;


@ParentPackage("struts-default")
@Namespace("/web/user")
public class ChongzhiAction extends UserBaseAction {
	
	Logger log = Logger.getLogger(ChongzhiAction.class);
	
	static int i=0;
	
	private User user;
	 
	 private OrderForm order;
	 
	 private String username;
	  
	 private double money;
	 
	 private  QuickPaySampleServLet qps;
	 
	 private @Resource UserService userService;
	 
	 private @Resource OrderService orderService;
	 
	  @Action(value = "chongzhi", results = { @Result(name = SUCCESS, location = "/web/user/ackOrderform.jsp"),
	    		@Result(name = ERROR, location = "/web/user/ackOrderform.jsp") })
	 public String chongzhi(){
		  if(session.get("user") != null){
			 user=(User) session.get("user");
			 if(user.getName()!=username){
				 return ERROR;
			 }else{
			     order.setOrderNo( new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(++i));
			     order.setUserName(username);
			     order.setMoney(money);
			     order.setRechargeTime(new Date());
			     order.setTrade("充值");
			     order.setState("交易中");
			     orderService.addOrder(order);
			     qps.service(httpServletRequest, httpServletResponse);
				 return SUCCESS;
			 }
		  }else 
				 return ERROR;
	  }
}

	  


