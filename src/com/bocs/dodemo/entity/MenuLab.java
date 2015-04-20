package com.bocs.dodemo.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;

import android.content.Context;

public class MenuLab {
	private List<MenuItem> menu;
	private static MenuLab sMenuLab;
	private Context mAppContext;
	private DinnerOrderManager dom;

	public MenuLab(Context c) {
		mAppContext = c;
		dom = new DinnerOrderManagerImpl(c);
		menu = dom.queryMenu(false);
	}

	public static MenuLab get(Context c) {

		if (sMenuLab == null) {
			sMenuLab = new MenuLab(c.getApplicationContext());
		}

		return sMenuLab;
	}

	public List<MenuItem> getMenu() {
		return menu;
	}

	public Map<Integer, List<MenuItem>> getMenuClassItem() {
		Map<Integer, List<MenuItem>> map = new HashMap<Integer, List<MenuItem>>();
		List<MenuItem> classMenu ;
		for (int i = 0; i < 8; i++) {
			classMenu = new ArrayList<MenuItem>();
			for (MenuItem mi : menu) {
				if (mi.getItemClass() == i) {
					classMenu.add(mi);
				}else {
					if(classMenu.size()>0){
						break;
					}
				}

			}
			map.put(i, classMenu);
		}
		return map;
	}

}
