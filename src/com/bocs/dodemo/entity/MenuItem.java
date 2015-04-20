package com.bocs.dodemo.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 菜品（饮料）
 * 
 * @author guxiayi
 * 
 */
public class MenuItem implements Parcelable, Cloneable{

	private int _id;
	private String itemName;
	private String itemNameEn;

	private String itemDesc;// 配料
	private String itemDescEn;
	private int itemClass;/*
						 * 分类: 0-头盘；1-沙拉；3-汤；4三明治；5-主菜；6-亚洲美食；7-比萨精选；8-甜品；
						 * 9-干白；10-干红；11-光明特选；12-干邑；13-威士忌；14-金酒；15-啤酒；
						 * 16-软饮；17-鲜榨；18-奶昔；19-矿泉水；20-茶水；21-咖啡；
						 */
	private int itemPrice;

	private String itemDesc2;// 产地
	private String itemDescEn2;
	private String itemUnit;// 单位（份，杯，瓶）
	private int itemPic;// 图片

	public int getItemPic() {
		return itemPic;
	}

	public void setItemPic(int itemPic) {
		this.itemPic = itemPic;
	}

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemNameEn() {
		return itemNameEn;
	}

	public void setItemNameEn(String itemNameEn) {
		this.itemNameEn = itemNameEn;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getItemDescEn() {
		return itemDescEn;
	}

	public void setItemDescEn(String itemDescEn) {
		this.itemDescEn = itemDescEn;
	}

	public int getItemClass() {
		return itemClass;
	}

	public void setItemClass(int itemClass) {
		this.itemClass = itemClass;
	}

	public int getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(int itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getItemDesc2() {
		return itemDesc2;
	}

	public void setItemDesc2(String itemDesc2) {
		this.itemDesc2 = itemDesc2;
	}

	public String getItemDescEn2() {
		return itemDescEn2;
	}

	public void setItemDescEn2(String itemDescEn2) {
		this.itemDescEn2 = itemDescEn2;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeInt(_id);
		dest.writeInt(itemClass);
		dest.writeInt(itemPrice);
		dest.writeInt(itemPic);
		
		dest.writeString(itemName);
		dest.writeString(itemNameEn);
		dest.writeString(itemDesc);
		dest.writeString(itemDescEn);
		dest.writeString(itemDesc2);
		dest.writeString(itemDescEn2);
		dest.writeString(itemUnit);

	}

	public static final Parcelable.Creator<MenuItem> CREATOR = new Creator<MenuItem>() {

		@Override
		public MenuItem[] newArray(int size) {
			return new MenuItem[size];
		}

		@Override
		public MenuItem createFromParcel(Parcel source) {
			MenuItem mi = new MenuItem();

			mi._id = source.readInt();
			mi.itemClass = source.readInt();
			mi.itemPrice = source.readInt();
			mi.itemPic = source.readInt();

			mi.itemName = source.readString();
			mi.itemNameEn = source.readString();
			mi.itemDesc = source.readString();
			mi.itemDescEn = source.readString();
			mi.itemDesc2 = source.readString();
			mi.itemDescEn2 = source.readString();
			mi.itemUnit = source.readString();

			return mi;
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
