package com.bocs.dodemo.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;

import android.content.Context;

public class DrinkLab {
	private List<MenuItem> menu;
	private static DrinkLab sMenuLab;
	private Context mAppContext;
	private DinnerOrderManager dom;

	public DrinkLab(Context c) {
		mAppContext = c;
		dom = new DinnerOrderManagerImpl(c);
		menu = dom.queryMenu(true);
	}

	public static DrinkLab get(Context c) {

		if (sMenuLab == null) {
			sMenuLab = new DrinkLab(c.getApplicationContext());
		}

		return sMenuLab;
	}

	public List<MenuItem> getMenu() {
		return menu;
	}

	public Map<Integer, List<MenuItem>> getDrinkClassItem() {
		Map<Integer, List<MenuItem>> map = new HashMap<Integer, List<MenuItem>>();
		List<MenuItem> classMenu;
		for (int i = 8; i < 21; i++) {
			classMenu = new ArrayList<MenuItem>();
			for (MenuItem mi : menu) {
				if (mi.getItemClass() == i) {
					classMenu.add(mi);
				} else {
					if (classMenu.size() > 0) {
						break;
					}
				}

			}
			map.put(i, classMenu);
		}
		return map;
	}

}
