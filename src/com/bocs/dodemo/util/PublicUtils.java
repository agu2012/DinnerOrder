package com.bocs.dodemo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.bocs.dodemo.application.ApplicationVariable;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.DrinkLab;
import com.bocs.dodemo.entity.MenuItem;
import com.bocs.dodemo.entity.MenuLab;
import com.bocs.dodemo.entity.OrderItem;

public class PublicUtils {

	public static int getTotalAmount(List<OrderItem> choose) {
		// 订单信息更新
		int total = 0;
		for (OrderItem oi : choose) {
			total += oi.getMenuItem().getItemPrice() * oi.getItemCnt();
		}
		return total;
	}

	public static void changeLanguage(Activity activity, String lan) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
		Locale locales[] = Locale.getAvailableLocales();
		Locale locale = Locale.SIMPLIFIED_CHINESE;
		for (Locale l : locales) {
			if (l.toString().equalsIgnoreCase(lan)) {
				locale = l;
				break;
			}
		}

		Locale.setDefault(locale);// set local language
		Configuration config = new Configuration();
		config.locale = locale;
		activity.getResources().updateConfiguration(config,
				activity.getResources().getDisplayMetrics());
		activity.finish();
		activity.startActivity(activity.getIntent());

		sharedPreferences.edit()
				.putString(Constants.SP_LOCALE, locale.toString()).commit();
	}

	/**
	 * 补充详情 至 全局变量中的订单简表
	 * 
	 * @param mAppContext
	 * @param oi
	 * @return
	 */
	public static ArrayList<OrderItem> getOrderDetails(Context mAppContext,
			ArrayList<OrderItem> _choose) {
		ArrayList<OrderItem> choose = new ArrayList<OrderItem>();
		for (OrderItem oi : _choose) {
			if (oi.getMenuItem().getItemClass() < 8) {
				for (MenuItem mi : MenuLab.get(mAppContext).getMenu()) {
					if (oi.getMenuItem().get_id() == mi.get_id()) {
						oi.setMenuItem(mi);
						choose.add(oi);
						break;
					}
				}
			} else {
				for (MenuItem mi : DrinkLab.get(mAppContext).getMenu()) {
					if (oi.getMenuItem().get_id() == mi.get_id()) {
						oi.setMenuItem(mi);
						choose.add(oi);
						break;
					}
				}
			}

		}
		return choose;
	}
	/**
	 * 补充详情 至 一个OrderItem
	 * 
	 * @param mAppContext
	 * @param oi
	 * @return
	 */
	public static OrderItem getOrderItemDetail(Context mAppContext, OrderItem oi) {
		if (oi.getMenuItem().getItemClass() < 8) {
			for (MenuItem mi : MenuLab.get(mAppContext).getMenu()) {
				if (oi.getMenuItem().get_id() == mi.get_id()) {
					oi.setMenuItem(mi);
					break;
				}
			}
		} else {
			for (MenuItem mi : DrinkLab.get(mAppContext).getMenu()) {
				if (oi.getMenuItem().get_id() == mi.get_id()) {
					oi.setMenuItem(mi);
					break;
				}
			}
		}

		return oi;
	}

	// public static OrderItem getMenuItemInOrderItem(Context mAppContext,
	// OrderItem oi) {
	// for (MenuItem mi : MenuLab.get(mAppContext).getMenu()) {
	// if (oi.getMenuItem().get_id() == mi.get_id()) {
	// oi.setMenuItem(mi);
	// break;
	// }
	// }
	// return oi;
	// }
	public static boolean checkMpos(Activity a) {
		int num = 100;
		String processName = "com.bocs.mpos";
		ActivityManager activityManager = (ActivityManager) a
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServiceInfos = activityManager
				.getRunningServices(num);
		for (RunningServiceInfo rsi : runningServiceInfos) {
			if (rsi.process.equals(processName))
				return true;
		}
		return false;

	}

	public static boolean checkMposDemo(Activity a) {
		int num = 100;
		String processName = "com.bocs.mposdemo";
		ActivityManager activityManager = (ActivityManager) a
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServiceInfos = activityManager
				.getRunningServices(num);
		for (RunningServiceInfo rsi : runningServiceInfos) {
			if (rsi.process.equals(processName))
				return true;
		}
		return false;

	}
}
