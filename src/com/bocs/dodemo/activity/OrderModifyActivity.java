package com.bocs.dodemo.activity;

import java.util.ArrayList;

import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.OrderItem;
import com.bocs.dodemo.fragment.OrderModifyFragment;
import com.bocs.dodemo.fragment.OrderModifyListFragment;

import android.support.v4.app.Fragment;

public class OrderModifyActivity extends SingleFragmentActivity implements
		OrderModifyListFragment.Callbacks {

	@Override
	public Fragment createFragment() {
		String orderID = getIntent().getStringExtra(Constants.ARGS_ORDER_ID);
		boolean isSettled = getIntent().getBooleanExtra(
				Constants.ARGS_IS_SETTLED, true);
		ArrayList<OrderItem> choose = getIntent().getParcelableArrayListExtra(
				Constants.ARGS_ORDER_CHOOSE);
		return OrderModifyFragment.newInstance(orderID, isSettled, choose);
	}

	@Override
	public void onUpdateModifyInfo(ArrayList<OrderItem> choose) {
		
	}

}
