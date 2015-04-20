package com.bocs.dodemo.activity;

import java.util.ArrayList;

import com.bocs.dodemo.application.ExitApplication;
import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.OrderItem;
import com.bocs.dodemo.fragment.DrinkFragment;
import com.bocs.dodemo.fragment.DrinkListFragment;
import com.bocs.dodemo.fragment.MenuFragment;
import com.bocs.dodemo.fragment.MenuListFragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MenuActivity extends FragmentActivity implements
		MenuListFragment.Callbacks, DrinkListFragment.Callbacks {

	private RadioGroup menu_rg_dish_or_wine;
	private RadioButton menu_btn_dish;

	private String tableID = "";
	private String orderID = "";
	private boolean isDrink = false;

	private FragmentManager fm;
	private Fragment mContent;
	private Fragment dFragment;
	private Fragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ExitApplication.add(this);
		setContentView(R.layout.activity_menu);

		tableID = getIntent().getStringExtra(Constants.ARGS_ORDER_TABLE_ID);
		orderID = getIntent().getStringExtra(Constants.ARGS_ORDER_ID);

		menu_rg_dish_or_wine = (RadioGroup) findViewById(R.id.menu_rg_dish_or_wine);
		menu_btn_dish = (RadioButton) findViewById(R.id.menu_btn_dish);

		fm = getSupportFragmentManager();
		mContent = fm.findFragmentById(R.id.menu_fl);
		mFragment = MenuFragment.newInstance(tableID, orderID);
		dFragment = DrinkFragment.newInstance(tableID, orderID);
		OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.menu_btn_dish:
					isDrink = false;
					switchContent(dFragment, mFragment);
					break;
				case R.id.menu_btn_wine:
					isDrink = true;
					switchContent(mFragment, dFragment);
					break;
				}

			}
		};
		menu_rg_dish_or_wine
				.setOnCheckedChangeListener(onCheckedChangeListener);
		menu_btn_dish.setChecked(true);

	}

	public void switchContent(Fragment from, Fragment to) {

		FragmentTransaction transaction = fm.beginTransaction();
		if (mContent != null) {
			if (mContent != to) {
				// 先判断是否被add过
				if (!to.isAdded()) {
					// 隐藏当前的fragment，add下一个到Activity中
					transaction.hide(from).add(R.id.menu_fl, to);
					Log.d("gxy",
							"hide " + from.getClass() + " , add "
									+ to.getClass());
				} else {
					// 隐藏当前的fragment，显示下一个
					transaction.hide(from).show(to);
					Log.d("gxy",
							"hide " + from.getClass() + " , show "
									+ to.getClass());
					onUpdateOrderInfo();
				}
			}
		} else {
			transaction.add(R.id.menu_fl, to);

			Log.d("gxy", "first add" + to.getClass());
		}

		mContent = to;
		// transaction.addToBackStack(null);
		// transaction.replace(R.id.menu_fl, to);
		// 提交修改
		transaction.commit();
	}

	@Override
	public void onUpdateOrderInfo() {
		// FragmentManager fm = getSupportFragmentManager();
		if (!isDrink) {
			((MenuFragment) mFragment).updateOrderInfo();
		} else {
			((DrinkFragment) dFragment).updateOrderInfo();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ExitApplication.remove(this);
	}

	public void onClickToBack(View v) {
		finish();
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setTitle(getString(R.string.dTitle1))
				.setMessage("是否放弃点菜？")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(getString(R.string.confirm),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new CheckEmptyTableTask().execute();
								dialog.dismiss();

							}
						}).setNegativeButton(getString(R.string.dCancel), null)
				.show();

	}

	public class CheckEmptyTableTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... Boolean) {

			DinnerOrderManager dom = new DinnerOrderManagerImpl(
					MenuActivity.this);
			ArrayList<OrderItem> items = dom.queryOrderListByOrderID(orderID);
			if (items == null || items.size() == 0) {
				dom.deleteOrderByOrderID(orderID);
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_FIRST_USER) {
			setResult(resultCode, data);
			finish();
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

}
