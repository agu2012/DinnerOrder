package com.bocs.dodemo.entity;

import java.util.ArrayList;
import java.util.List;

import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;

import android.content.Context;

public class EmployeeLab {
	private static EmployeeLab sEmployeeLab;
	private Context mAppContext;
	private List<Employee> employees;
	private DinnerOrderManager dom;

	public EmployeeLab(Context c) {
		mAppContext = c;
		dom = new DinnerOrderManagerImpl(c);
		employees = dom.queryEmployees();
	}

	public static EmployeeLab get(Context c) {

		if (sEmployeeLab == null) {
			sEmployeeLab = new EmployeeLab(c.getApplicationContext());
		}

		return sEmployeeLab;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public List<String> getlEmployeeNames() {
		List<String> names = new ArrayList<String>();
		for (Employee e : employees) {
			names.add(e.getEmployeeName());
		}
		return names;
	}

}
