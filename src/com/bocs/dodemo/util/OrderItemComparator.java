package com.bocs.dodemo.util;

import java.util.Comparator;

import com.bocs.dodemo.entity.OrderItem;

/**
 * 对同一客户的多个订单的OrderItem进行排序
 * 
 * @author guxiayi
 * 
 */
public class OrderItemComparator implements Comparator<Object> {

	@Override
	public int compare(Object lhs, Object rhs) {

		OrderItem oi1 = (OrderItem) lhs;
		OrderItem oi2 = (OrderItem) rhs;

		int flag = 0;
		if (oi1.getMenuItem().get_id() > oi2.getMenuItem().get_id())
			flag = 1;
		else if (oi1.getMenuItem().get_id() < oi1.getMenuItem().get_id())
			flag = -1;
		else
			flag = 0;

		return flag;

	}

}
