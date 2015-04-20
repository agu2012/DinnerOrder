package com.bocs.dodemo.bussiness.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.entity.Employee;
import com.bocs.dodemo.entity.MenuItem;
import com.bocs.dodemo.entity.Order;
import com.bocs.dodemo.entity.OrderItem;
import com.bocs.dodemo.sqlite.DinnerOrderSQLiteOpenHelper;
import com.bocs.dodemo.sqlite.DinnerOrderSQLiteOpenHelper.EmployeeCursor;
import com.bocs.dodemo.sqlite.DinnerOrderSQLiteOpenHelper.MenuItemCursor;
import com.bocs.dodemo.sqlite.DinnerOrderSQLiteOpenHelper.OrderCursor;
import com.bocs.dodemo.sqlite.DinnerOrderSQLiteOpenHelper.OrderItemCursor;
import com.bocs.dodemo.util.PublicUtils;

public class DinnerOrderManagerImpl implements DinnerOrderManager {
	private DinnerOrderSQLiteOpenHelper mHelper;
	private Context mAppContext;
	private boolean successFlag = false;

	public DinnerOrderManagerImpl(Context appContext) {
		mAppContext = appContext;
		mHelper = DinnerOrderSQLiteOpenHelper.getInstance(appContext);
	}

	@Override
	public boolean deleteEmployee(Employee employee) {
		// TODO Delete Employee
		successFlag = mHelper.deleteEmployee(employee.getEmployeeName());

		return successFlag;
	}

	@Override
	public boolean addEmployee(Employee employee) {
		// TODO Add Employee
		successFlag = mHelper.insertEmployee(employee.getEmployeeName());

		return successFlag;
	}

	@Override
	public List<Employee> queryEmployees() {
		// TODO Query Employees
		EmployeeCursor ec = mHelper.queryEmployees();
		List<Employee> employees = new ArrayList<Employee>();
		if (ec.moveToFirst() == false) {
			// 为空的Cursor
			ec.close();
			return employees;
		} else {
			employees.add(ec.getEmployee());
			while (ec.moveToNext()) {
				employees.add(ec.getEmployee());
			}
		}

		ec.close();
		return employees;
	}

	@Override
	public List<String> queryOrderedTables() {

		// TODO Query Ordered Tables
		List<String> tables = new ArrayList<String>();
		OrderCursor oc = mHelper.queryOrder();

		if (oc.moveToFirst() == false) {
			// 为空的Cursor
			oc.close();
			return tables;
		} else {
			tables.add(oc.getOrder().getTableNo());
			while (oc.moveToNext()) {
				tables.add(oc.getOrder().getTableNo());
			}
		}
		oc.close();

		// 去除重复数据
		HashSet<String> hSet = new HashSet<String>(tables);
		tables.clear();
		tables.addAll(hSet);

		return tables;
	}

	@Override
	public ArrayList<Order> queryOrderByTableId(String table_id) {

		// TODO Query Order
		ArrayList<Order> orders = new ArrayList<Order>();
		OrderCursor oc;
		oc = mHelper.queryOrderByTableId(table_id);
		if (oc.moveToFirst() == false) {
			// 为空的Cursor
			oc.close();
			return orders;
		} else {
			orders.add(oc.getOrder());
			while (oc.moveToNext()) {
				orders.add(oc.getOrder());
			}
		}
		oc.close();

		return orders;
	}

	/**
	 * 查询订单详情（包括点单内容）
	 * 
	 * @param table_id
	 * @return
	 */
	@Override
	public List<Order> queryOrderDetailByTableId(String table_id) {
		List<Order> orders = queryOrderByTableId(table_id);
		if (orders.size() > 0) {
			for (Order o : orders) {
				o.setItems(queryOrderListByOrderID(o.getOrderID()));
			}
		}
		return orders;
	}

	@Override
	public Order queryOrderByOrderID(String orderID) {
		Order order = new Order();
		OrderCursor oc = mHelper.queryOrderByOrderID(orderID);

		if (oc.moveToFirst()) {
			order = oc.getOrder();
		}
		oc.close();
		return order;
	}

	@Override
	public boolean addOrder(Order order) {
		// TODO Add Order
		successFlag = mHelper.insertOrder(order);
		return successFlag;
	}

	@Override
	public boolean deleteOrderByTableID(String tableID) {
		// TODO Delete Order
		successFlag = mHelper.deleteOrderByTableID(tableID);
		return successFlag;
	}
	
	@Override
	public boolean deleteOrderByOrderID(String orderID){
		successFlag = mHelper.deleteOrderByOrderID(orderID);
		return successFlag;
	}

	@Override
	public boolean updateOrderByTableID(Order order) {
		// TODO Update Order
		successFlag = mHelper.updateOrderByTableID(order);
		return successFlag;
	}

	@Override
	public boolean updateOrderByOrderID(Order order) {
		// TODO Update Order
		successFlag = mHelper.updateOrderByOrderID(order);
		return successFlag;
	}

	@Override
	public void updateTableId(String tableIdOrg, String newTableId) {
		List<Order> orders = queryOrderByTableId(tableIdOrg);// 所有订单包括已提交的订单
		for (Order o : orders) {
			o.setTableNo(newTableId);
			mHelper.updateOrderByTableId(newTableId, tableIdOrg);
		}
	}

	/**
	 * Menu
	 * 
	 * @param menu
	 */
	public void createMenu(List<MenuItem> menu) {
		// TODO 创建菜单
		for (MenuItem mi : menu)
			mHelper.insertMenuItem(mi);
	}

	// 初始化菜单
	@Override
	public List<MenuItem> queryMenu(boolean isDrink) {
		// TODO Query Menu
		List<MenuItem> menu = new ArrayList<MenuItem>();
		
		MenuItemCursor mic;
		if(isDrink)
			mic = mHelper.queryDrinkItems();
		else
			mic = mHelper.queryMenuItems();
		if (mic.moveToFirst() == false) {
			// 为空的Cursor
			mic.close();
			return menu;
		} else {
			menu.add(mic.getMenuItem());
			while (mic.moveToNext()) {
				menu.add(mic.getMenuItem());
			}
		}
		mic.close();

		return menu;
	}

	/**
	 * 点菜管理
	 */

	@Override
	public void addOrderList(ArrayList<OrderItem> choose, String orderID) {
		List<OrderItem> order = queryOrderListByOrderID(orderID);
		if (order.size() > 0) {
			for (OrderItem newItem : choose) {
				boolean flag = false;// 是否已存在
				for (OrderItem oldItem : order) {
					if (oldItem.getMenuItem().get_id() == newItem.getMenuItem()
							.get_id()) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					mHelper.insertOrderList(newItem);
				}
			}
		} else {
			for (OrderItem newItem : choose) {
				mHelper.insertOrderList(newItem);
			}
		}

		printAllOrderList();
	}

	@Override
	public ArrayList<OrderItem> queryOrderListByOrderID(String orderID) {

		// TODO Query orderList by OrderID
		OrderItemCursor omc = mHelper.queryOrderListByOrderID(orderID);

		return getOrderItemDetail(omc);

	}

	@Override
	public ArrayList<OrderItem> queryOrderListByTableId(String tableID) {
		// TODO Query orderList by tableID

		OrderItemCursor omc = mHelper.queryOrderListByTableId(tableID);

		return getOrderItemDetail(omc);
	}

	@Override
	public void updateOrderListByOrderId(String orderID,
			ArrayList<OrderItem> newOrders) {
		ArrayList<OrderItem> ordersOrg = queryOrderListByOrderID(orderID);
		boolean flag = true;// 是否删除
		for (int i = 0; i < ordersOrg.size(); i++) {
			for (OrderItem oi : newOrders) {
				if (oi.getMenuItem().get_id() == ordersOrg.get(i).getMenuItem()
						.get_id()) {
					mHelper.updateOrderListByOrderIDMenuID(oi);
					flag = false;
					break;
				}
			}
			if (flag) {
				mHelper.deleteOrderItem(ordersOrg.get(i));
			}
		}

		// 更新订单总价
		Order order = queryOrderByOrderID(orderID);
		// for (OrderItem oi : newOrders) {
		// oi = PublicUtils.getMenuItemInOrderItem(mAppContext, newOrders);
		// }
		order.setItems(PublicUtils.getOrderDetails(mAppContext, newOrders));
		order.setTotalAmount(PublicUtils.getTotalAmount(newOrders));
		order.setSettled(true);
		updateOrderByOrderID(order);
	}

	private ArrayList<OrderItem> getOrderItemDetail(OrderItemCursor omc) {

		ArrayList<OrderItem> loi = new ArrayList<OrderItem>();

		if (omc.moveToFirst() == false) {
			// 没有值
			omc.close();
			return loi;
		} else {
			loi.add(PublicUtils.getOrderItemDetail(mAppContext,
					omc.getOrderItem()));
			while (omc.moveToNext()) {
				loi.add(PublicUtils.getOrderItemDetail(mAppContext,
						omc.getOrderItem()));
			}
		}
		omc.close();

		return loi;
	}

	@Override
	public void printAllOrderList() {
		OrderItemCursor omc = mHelper.queryOrderListAll();
		List<OrderItem> loi = getOrderItemDetail(omc);
		if (loi.size() > 0) {
			int i = 1;
			Log.i("gxy", "点单列表*" + loi.size() + "\n------------------------");
			for (OrderItem oi : loi) {
				Log.i("gxy", "（" + oi.get_id() + "）OrderID: " + oi.getOrderID()
						+ "|tableID: " + oi.getTableID() + "|菜名："
						+ oi.getMenuItem().get_id() + "|数量：" + oi.getItemCnt());
			}
		}
	}

	@Override
	public void deleteOrderItem(OrderItem oi) {
		// TODO delete Order Item
		mHelper.deleteOrderItem(oi);
	}

	@Override
	public void removeAllInfoByTabeleID(String tableID) {
		deleteOrderByTableID(tableID);
		mHelper.deleteOrderListByTableID(tableID);
	}
}
