package com.bocs.dodemo.activity;

import com.bocs.dodemo.fragment.EmployeeListFragment;

import android.support.v4.app.Fragment;

public class EmployeeListActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		return new EmployeeListFragment();
	}

}
