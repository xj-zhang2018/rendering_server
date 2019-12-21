package org.xjtu.framework.modules.user.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xjtu.framework.core.base.model.OrderForm;
import org.xjtu.framework.modules.user.dao.OrderDao;
import org.xjtu.framework.modules.user.service.OrderService;

@Service("orderService")
public class OrderServiceImpl implements OrderService{
	 private @Resource OrderDao orderDao;
	   
	    @Override
	    public List<OrderForm> findOrderByName(String name){
	    	return orderDao.queryByName(name);
	    }
	    
	    @Override
		public OrderForm findOrderById(String orderId){
	    	return orderDao.queryById(orderId);
	    }
	    
	    @Override
		public List<OrderForm> findAllOrders(){
	    	return orderDao.queryAllOrders();
	    }
	    
	    @Override
		public void addOrder(OrderForm order){
	    	orderDao.persist(order);
	    }
	    
	    @Override
		public void updateOrderinfo(OrderForm order){
	    	orderDao.updateOrder(order);
	    }
	    
	    @Override
		public void deleteOrdersById(String orderId){
	    	orderDao.removeOrdersById(orderId);
	    }
	    
	    @Override
		public void deleteOrdersByIds(List<String> orderIds){
	    	orderDao.removeOrdersByIds(orderIds);
	    }

}
