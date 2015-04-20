package com.bocs.dodemo.bussiness;

import java.util.ArrayList;
import java.util.List;

import com.bocs.dodemo.entity.Employee;
import com.bocs.dodemo.entity.MenuItem;
import com.bocs.dodemo.entity.Order;
import com.bocs.dodemo.entity.OrderItem;

public interface DinnerOrderManager {
	/**
	 * 员工管理
	 */
	public boolean addEmployee(Employee employee);

	public boolean deleteEmployee(Employee employee);

	public List<Employee> queryEmployees();

	/**
	 * 订单管理
	 */
	public List<String> queryOrderedTables();

	public ArrayList<Order> queryOrderByTableId(String tableID);// 查询当前桌的所有订单

	public List<Order> queryOrderDetailByTableId(String table_id);// 查询当前桌的所有订单,包括订单详情

	public Order queryOrderByOrderID(String orderID);// 查询当前桌的所有订单

	public boolean addOrder(Order order);

	public boolean deleteOrderByTableID(String tableID);
	public boolean deleteOrderByOrderID(String orderID);

	public boolean updateOrderByTableID(Order order);

	public boolean updateOrderByOrderID(Order order);

	public void updateTableId(String tableIdOrg, String newTableId);// 换桌

	/**
	 * 菜单管理
	 */
	public List<MenuItem> queryMenu(boolean isDrink);// 查询当前菜单所有内容

	/**
	 * 点菜管理
	 */
	public void addOrderList(ArrayList<OrderItem> choose, String orderID);// 保存点菜

	public ArrayList<OrderItem> queryOrderListByTableId(String tableID);// 根据票据号查询

	public ArrayList<OrderItem> queryOrderListByOrderID(String orderID);// 根据订单号查询

	public void updateOrderListByOrderId(String orderID,
			ArrayList<OrderItem> newOrders);// 更新修改后的订单，包括删除订单

	public void printAllOrderList();

	public void deleteOrderItem(OrderItem oi);// 删除某项点菜

	/**
	 * 混合
	 */
	public void removeAllInfoByTabeleID(String tableID);// 交易成功后删除该桌的订单信息
}
