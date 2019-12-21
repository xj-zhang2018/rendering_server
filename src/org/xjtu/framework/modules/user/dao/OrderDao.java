package org.xjtu.framework.modules.user.dao;

import java.util.List;

import org.xjtu.framework.core.base.model.OrderForm;


public interface OrderDao {

	public void persist(OrderForm order);
	public List<OrderForm> queryByName(String name);
	public OrderForm queryById(String orderId);
	public List<OrderForm> queryAllOrders();
	public void updateOrder(OrderForm order);
	public void removeOrdersById(String orderId);
	public void removeOrdersByIds(List<String> orderIds);
	
	
}
