package com.bocs.dodemo.application;

import java.util.ArrayList;

import com.bocs.dodemo.entity.OrderItem;
import com.bocs.dodemo.util.PublicUtils;

import android.app.Application;

public class ApplicationVariable extends Application {
	private static ApplicationVariable applicationVariable;
	private ArrayList<OrderItem> CHOOSE = new ArrayList<OrderItem>();
	private boolean SUCCESS_ORDER = false;
	private int orderTotal = 0;

	public static ApplicationVariable getInstance() {
		return applicationVariable;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		applicationVariable = this;
	}

	public ArrayList<OrderItem> getCHOOSE() {
		return CHOOSE;
	}

	public void setCHOOSE(ArrayList<OrderItem> cHOOSE) {
		CHOOSE = cHOOSE;
	}

	public boolean isSUCCESS_ORDER() {
		return SUCCESS_ORDER;
	}

	public void setSUCCESS_ORDER(boolean sUCCESS_ORDER) {
		SUCCESS_ORDER = sUCCESS_ORDER;
	}

	public int getOrderTotal() {
		return PublicUtils.getTotalAmount(getCHOOSE());
	}

}
