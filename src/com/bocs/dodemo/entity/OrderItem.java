package com.bocs.dodemo.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem implements Parcelable, Cloneable {

	private int _id;
	private int itemCnt;
	private MenuItem menuItem;
	private String orderID;
	private String tableID;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public int getItemCnt() {
		return itemCnt;
	}

	public void setItemCnt(int itemCnt) {
		this.itemCnt = itemCnt;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getTableID() {
		return tableID;
	}

	public void setTableID(String tableID) {
		this.tableID = tableID;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(_id);
		dest.writeInt(itemCnt);
		
		dest.writeParcelable(menuItem, PARCELABLE_WRITE_RETURN_VALUE);

		dest.writeString(orderID);
		dest.writeString(tableID);

	}

	public static final Parcelable.Creator<OrderItem> CREATOR = new Creator<OrderItem>() {

		@Override
		public OrderItem[] newArray(int size) {
			return new OrderItem[size];
		}

		@Override
		public OrderItem createFromParcel(Parcel source) {
			OrderItem oi = new OrderItem();

			oi._id = source.readInt();
			oi.itemCnt = source.readInt();
			oi.menuItem = source.readParcelable(MenuItem.class.getClassLoader());
			oi.orderID = source.readString();
			oi.tableID = source.readString();

			return oi;
		}
	};

	@Override
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return o;

	}

	@Override
	public int describeContents() {
		return 0;
	}
}
