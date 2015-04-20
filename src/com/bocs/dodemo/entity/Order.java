package com.bocs.dodemo.entity;

import java.util.ArrayList;
import java.util.Date;

/**
 * 订单
 * 
 * @author guxiayi
 * 
 */
public class Order {

	private String orderID;
	private String tableNo;
	private int cnt;// 人数
	private Date orderDate;
	private String orderMainNo;
	private String waiter;
	private int settlementType;// 结算方式：1-cash；2-mpos
	private boolean isSettled = false;// 是否已结账
	private boolean isSubmitted = false;// 是否已提交
	private int serviceChargePercent;// 服务费百分比，ie.10%-10
	private long discount = 0;// 优惠金额
	private long totalAmount;
	private ArrayList<OrderItem> items;

	public Order() {
		// Calendar c = Calendar.getInstance();
		// orderID = String.valueOf(c.get(Calendar.HOUR_OF_DAY))
		// + String.valueOf(c.get(Calendar.MINUTE))
		// + String.valueOf(c.get(Calendar.SECOND));
		orderDate = new Date(System.currentTimeMillis());
	}

	public boolean isSubmitted() {
		return isSubmitted;
	}

	public void setSubmitted(boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderMainNo() {
		return orderMainNo;
	}

	public void setOrderMainNo(String orderMainNo) {
		this.orderMainNo = orderMainNo;
	}

	public String getWaiter() {
		return waiter;
	}

	public void setWaiter(String waiter) {
		this.waiter = waiter;
	}

	public int getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(int settlementType) {
		this.settlementType = settlementType;
	}

	public boolean isSettled() {
		return isSettled;
	}

	public void setSettled(boolean isSettled) {
		this.isSettled = isSettled;
	}

	public int getServiceChargePercent() {
		return serviceChargePercent;
	}

	public void setServiceChargePercent(int serviceChargePercent) {
		this.serviceChargePercent = serviceChargePercent;
	}

	public long getDiscount() {
		return discount;
	}

	public void setDiscount(long discount) {
		this.discount = discount;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public ArrayList<OrderItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<OrderItem> items) {
		this.items = items;
	}
	
	

}
