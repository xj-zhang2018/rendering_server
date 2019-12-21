package org.xjtu.framework.modules.user.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xjtu.framework.core.base.model.OrderForm;
import org.xjtu.framework.modules.user.dao.OrderDao;

@Repository("orderDao")
public class OrderDaoImpl implements OrderDao{
	
	@Autowired
	private SessionFactory sessionFactory;
    
	@Override
	public void persist(OrderForm order){
		sessionFactory.getCurrentSession().persist(order);//order存入数据库
	}
	
	@Override
	public List<OrderForm> queryByName(String name){
		List orders = sessionFactory.getCurrentSession().createQuery("from OrderForm where userName = ? ").setString(0, name).list();//setString(int num,String ss),num为第ss替换第num个？
		if(orders != null && orders.size() >0){
			return orders;
		}
		
		return null;
	}
	
	@Override
	public OrderForm queryById(String orderId){
		List orders = sessionFactory.getCurrentSession().createQuery("from OrderForm where orderNo = ? ").setString(0, orderId).list();//setString(int num,String ss),num为第ss替换第num个？
		if(orders != null && orders.size() ==0){
			return (OrderForm)orders.get(0);
		}	
		return null;
	}
	
	@Override
	public void updateOrder(OrderForm order){
		sessionFactory.getCurrentSession().update(order);
	}
	
	@Override
	public void removeOrdersById(String orderId){
		Session session=sessionFactory.getCurrentSession();
		session.createQuery("delete from OrderForm j where j.orderNo=?").setString(0, orderId).executeUpdate();
	}
	
	@Override
	public void removeOrdersByIds(List<String> orderIds){
		Session session=sessionFactory.getCurrentSession();

		for(int i=0; i<orderIds.size(); i++){
			session.createQuery("delete from User j where j.orderNo=?").setString(0, orderIds.get(i)).executeUpdate();
		}
	}
	
	@Override
	public List<OrderForm> queryAllOrders(){
		List orders = sessionFactory.getCurrentSession().createQuery("from OrderForm").list();
		if(orders != null && orders.size() >0){
			return orders;
		}	
		return null;
		}
}
