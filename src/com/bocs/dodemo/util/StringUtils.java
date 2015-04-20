package com.bocs.dodemo.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bocs.dodemo.entity.OrderItem;

public class StringUtils {

	public static boolean isEmpty(String content) {
		return content == null || "".equals(content);
	}

	public static ArrayList<OrderItem> mergeOrders(List<OrderItem> orderItems) {
		ArrayList<OrderItem> ois = new ArrayList<OrderItem>();
		ois.add(orderItems.get(0));
		// 直接插入排序
		boolean flag;
		for (int i = 1; i < orderItems.size(); i++) {
			OrderItem temp = orderItems.get(i);
			flag = false;
			for (OrderItem oi : ois) {
				if (temp.getMenuItem().get_id() == oi.getMenuItem().get_id()) {
					oi.setItemCnt(oi.getItemCnt() + temp.getItemCnt());
					flag = true;// 有相同值
					break;
				}
			}

			if (!flag)
				ois.add(temp);

		}
		return ois;
	}

	public static String getAmount(String amount) {
		return amount.substring(0, amount.length() - 2) + "."
				+ amount.substring(amount.length() - 2, amount.length());
	}

	/**
	 * 四舍五入，保留整数
	 * 
	 * @param tip
	 * @return
	 */
	public static String roundDouble2Int(String tip) {
		return Math.round(Double.valueOf(tip)) + "";
	}
}
