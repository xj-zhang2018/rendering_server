package org.xjtu.framework.modules.user.service;

import java.util.List;

import org.xjtu.framework.core.base.model.OrderForm;

public interface OrderService {

	public List<OrderForm> findOrderByName(String name);
	public OrderForm findOrderById(String orderId);
	public List<OrderForm> findAllOrders();
	public void addOrder(OrderForm order);
	public void updateOrderinfo(OrderForm order);
	public void deleteOrdersById(String orderId);
	public void deleteOrdersByIds(List<String> orderIds);
}
