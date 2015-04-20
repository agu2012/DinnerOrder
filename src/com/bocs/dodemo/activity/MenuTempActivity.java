package com.bocs.dodemo.activity;

import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.fragment.MenuTempListFragment;
import com.bocs.dodemo.fragment.MenuTempFragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MenuTempActivity extends SingleFragmentActivity implements
		MenuTempListFragment.Callbacks {

	@Override
	public Fragment createFragment() {
		// String tableID = getIntent().getStringExtra(
		// Constants.ARGS_ORDER_TABLE_ID);
		String orderID = getIntent().getStringExtra(Constants.ARGS_ORDER_ID);
		// ArrayList<OrderItem> choose =
		// getIntent().getParcelableArrayListExtra(
		// Constants.ARGS_ORDER_CHOOSE);
		return MenuTempFragment.newInstance(orderID);
	}

	@Override
	public void onUpdateOrderInfo() {

		FragmentManager fm = getSupportFragmentManager();
		MenuTempFragment mtf = (MenuTempFragment) fm
				.findFragmentById(R.id.fragmentContainer);
		mtf.updateOrderInfo();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		setResult(resultCode, data);
		finish();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		setResult(RESULT_FIRST_USER);
		finish();
	}

}
