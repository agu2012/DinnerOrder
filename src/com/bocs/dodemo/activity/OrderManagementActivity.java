package com.bocs.dodemo.activity;

import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.fragment.OrderManagementFragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class OrderManagementActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		return OrderManagementFragment.newInstance(getIntent().getStringExtra(
				Constants.ARGS_ORDER_TABLE_ID));
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

}
