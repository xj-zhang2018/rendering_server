package org.xjtu.framework.modules.user.webservice;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;

/**
 * <b>function:</b> 自定义消息拦截器
 * @author hoojo
 * @createDate Mar 17, 2011 8:10:49 PM
 * @file MessageInterceptor.java
 * @package com.hoo.interceptor
 * @project CXFWebService
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MessageInterceptor extends AbstractPhaseInterceptor<Message> {
	
	//至少要一个带参的构造函数
	public MessageInterceptor(String phase) {
		super(phase);
	}

	public void handleMessage(Message message) throws Fault {
		System.out.println("############handleMessage##########");
		System.out.println(message);
		if (message.getDestination() != null) {
			System.out.println(message.getId() + "#" + message.getDestination().getMessageObserver());
		}
		if (message.getExchange() != null) {
			System.out.println(message.getExchange().getInMessage() + "#" + message.getExchange().getInFaultMessage());
			System.out.println(message.getExchange().getOutMessage() + "#" + message.getExchange().getOutFaultMessage());
		}
	}
}
