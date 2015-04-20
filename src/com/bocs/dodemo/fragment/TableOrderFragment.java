package com.bocs.dodemo.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.bocs.dodemo.activity.MenuActivity;
import com.bocs.dodemo.activity.OrderManagementActivity;
import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.application.ApplicationVariable;
import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.EmployeeLab;
import com.bocs.dodemo.entity.Order;
import com.bocs.dodemo.entity.OrderItem;
import com.bocs.dodemo.util.ShowToast;

public class TableOrderFragment extends Fragment implements
		OnCheckedChangeListener {
	private TextView table_tv_table_no;
	private EditText table_et_cnt;// number of clients
	private Spinner table_sp_waiter;
	private RadioGroup table_rg;
	private RadioButton table_rb_s, table_rb_n;
	private ImageView table_iv_selected;
	private ViewPager table_vp;
	private LinearLayout table_ll_info, table_ll_btn;
	private Button table_btn_switch_table, table_btn_manage;

	private Order order;
	private ArrayAdapter<String> adapter2;
	private DinnerOrderManager dom;

	private ArrayList<View> mViews;
	private float mCurrentCheckedRadioLeft;// 当前被选中的RadioButton距离左侧的距离
	private List<String> tablesOrdered;
	private List<String> names;
	private String checkedNo;
	private String checkedNoNew;
	private ShowToast st = new ShowToast();
	private boolean isManager = false;
	private boolean isShiftMode = false;// 是否处于换桌状态
	public static final int REQUEST_FROM_TABLE_ORDER = 41;
	public static final int REQUEST_FROM_TABLE_MANAGER = 42;
	private ApplicationVariable av;
	Calendar c ;

	public static TableOrderFragment newInstance(String where) {
		Bundle args = new Bundle();
		args.putString(Constants.ARGS_WHERE, where);
		TableOrderFragment fragment = new TableOrderFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);// save instance
		dom = new DinnerOrderManagerImpl(getActivity());
		av = (ApplicationVariable) getActivity().getApplication();

		c = Calendar.getInstance();
		isManager = getArguments().getString(Constants.ARGS_WHERE).equals(
				Constants.INTENT_ORDER_MANAGEMENT);
		tablesOrdered = dom.queryOrderedTables();
		names = EmployeeLab.get(getActivity()).getlEmployeeNames();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_table_order, container,
				false);

		table_ll_info = (LinearLayout) v.findViewById(R.id.table_ll_info);// 桌位信息面板
		table_ll_btn = (LinearLayout) v.findViewById(R.id.table_ll_btn);

		if (isManager) {
			table_ll_info.setVisibility(View.GONE);
			table_ll_btn.setVisibility(View.VISIBLE);

			table_btn_switch_table = (Button) v
					.findViewById(R.id.table_btn_switch_table);
			table_btn_manage = (Button) v.findViewById(R.id.table_btn_manage);
			table_btn_manage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(getActivity(),
							OrderManagementActivity.class);
					intent.putExtra(Constants.ARGS_ORDER_TABLE_ID, TextUtils
							.isEmpty(checkedNoNew) ? checkedNo : checkedNoNew);
					startActivityForResult(intent, REQUEST_FROM_TABLE_MANAGER);
				}
			});
			table_btn_switch_table.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Button switchBtn = (Button) v;
					if (switchBtn.getText().equals("确定")) {
						isShiftMode = false;
						table_btn_switch_table.setText("调换餐台");
					} else {
						isShiftMode = true;
						table_btn_switch_table.setText("确定");
					}

					initViewsInViewPager();
				}
			});

		} else {
			table_ll_info.setVisibility(View.VISIBLE);
			table_ll_btn.setVisibility(View.GONE);

			table_tv_table_no = (TextView) v
					.findViewById(R.id.table_tv_table_no);
			table_et_cnt = (EditText) v.findViewById(R.id.table_et_cnt);
			table_sp_waiter = (Spinner) v.findViewById(R.id.table_sp_waiter);
			adapter2 = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item, EmployeeLab.get(
							getActivity()).getlEmployeeNames());

			adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			table_sp_waiter.setAdapter(adapter2);
			table_sp_waiter.setVisibility(View.VISIBLE);
		}

		initViewPager(v);

		return v;
	}

	public void initViewPager(View v) {

		table_rg = (RadioGroup) v.findViewById(R.id.table_rg);
		table_rb_s = (RadioButton) v.findViewById(R.id.table_rb_s);
		table_rb_n = (RadioButton) v.findViewById(R.id.table_rb_n);
		table_iv_selected = (ImageView) v.findViewById(R.id.table_iv_selected);
		table_vp = (ViewPager) v.findViewById(R.id.table_vp);

		table_rg.setOnCheckedChangeListener(this);
		table_vp.setOnPageChangeListener(new MyPagerOnPageChangeListener());

		initViewsInViewPager();

	}

	public void initViewsInViewPager() {

		mViews = new ArrayList<View>();

		// 座位按钮控制
		View.OnClickListener tableButtonListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Button btn = (Button) v;
				if (!isManager) {
					table_tv_table_no.setText(btn.getText());
					checkedNo = btn.getText().toString();
				} else {
					if (isShiftMode) {
						checkedNoNew = btn.getText().toString();
					} else {
						checkedNo = btn.getText().toString();
					}
					table_btn_switch_table.setEnabled(true);
					table_btn_manage.setEnabled(true);
				}
				createOrderCheck(btn);
			}
		};

		// Smoke View
		View vS = getActivity().getLayoutInflater().inflate(
				R.layout.vp_table_smoke, null);
		TableLayout tlS = (TableLayout) vS.findViewById(R.id.vp_table_tl_s);

		for (int i = 0; i < tlS.getChildCount(); i++) {
			TableRow row = (TableRow) tlS.getChildAt(i);
			if (i == 0) {
				for (int j = 0; j < row.getChildCount(); j++) {
					Button button = (Button) row.getChildAt(j);
					button.setOnClickListener(tableButtonListener);
					checkTable(button);
				}
			} else {
				for (int j = 0; j < 2; j++) {
					Button button = (Button) row.getChildAt(j);
					button.setOnClickListener(tableButtonListener);
					checkTable(button);
				}
			}
		}

		// No-Smoke View
		View vN = getActivity().getLayoutInflater().inflate(
				R.layout.vp_table_no_smoke, null);
		TableLayout tlN = (TableLayout) vN.findViewById(R.id.vp_table_tl_n);

		int number = 1;
		for (int i = 0; i < tlN.getChildCount(); i++) {
			TableRow row = (TableRow) tlN.getChildAt(i);
			for (int j = 0; j < row.getChildCount(); j++) {
				Button button = (Button) row.getChildAt(j);
				if (number < 10) {
					button.setText("N0" + number);
				} else {
					button.setText("N" + number);
				}
				button.setOnClickListener(tableButtonListener);
				checkTable(button);
				number++;
			}
		}

		mViews.add(vS);
		mViews.add(vN);

		table_vp.setAdapter(new MyPagerAdapter());

		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
	}

	// TODO Check table is Availiable
	private boolean checkTable(Button button) {
		// if (isShiftMode) {
		// tablesOrdered = dom.queryOrderedTables();
		// }
		if (tablesOrdered.size() > 0) {
			if (isManager) {
				if (isShiftMode) {// 订单管理-换桌
					if (checkTable(button.getText().toString())) {
						button.setBackgroundResource(R.drawable.button_shape_pressed);
						button.setEnabled(false);
					} else {
						button.setBackgroundResource(R.drawable.button_shape_shadowed);
						button.setEnabled(true);
					}

				} else {// 订单管理
					if (checkTable(button.getText().toString())) {
						button.setBackgroundResource(R.drawable.button_shape_shadowed);
						button.setEnabled(true);
					} else {
						button.setBackgroundResource(R.drawable.button_shape_pressed);
						button.setEnabled(false);
					}
				}
			} else {// 订餐开桌
				if (checkTable(button.getText().toString())) {
					button.setBackgroundResource(R.drawable.button_shape_shadowed_red);
				} else {
					button.setBackgroundResource(R.drawable.button_shape_shadowed);
				}
			}
		} else {
			if (isManager) {// 订单管理
				button.setBackgroundResource(R.drawable.button_shape_pressed);
				button.setEnabled(false);
				return false;
			} else {

				button.setBackgroundResource(R.drawable.button_shape_shadowed);
				return false;

			}
		}
		return false;
	}

	private boolean checkTable(String tableId) {

		// tablesOrdered = dom.queryOrderedTables();
		for (String ordered : tablesOrdered) {
			if (ordered.equals(tableId)) {
				return true;
			}
		}
		return false;
	}

	public class FetchOrderTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			// order = dom.queryOrderByTableId(checkedNo).get(0);
			if (isShiftMode && !TextUtils.isEmpty(checkedNoNew)) {
				dom.updateTableId(checkedNo, checkedNoNew);
				tablesOrdered = dom.queryOrderedTables();
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean flag) {
			if (isShiftMode && !TextUtils.isEmpty(checkedNoNew)) {
				isShiftMode = false;
				checkedNo = checkedNoNew;
				table_btn_switch_table.setText("调换餐台");
				initViewsInViewPager();
			}
		}

	}

	public void createOrderCheck(final Button btn) {
		if (isManager) {
			new FetchOrderTask().execute();
		} else {// 点菜界面
			new AlertDialog.Builder(getActivity())
					.setTitle(getString(R.string.dTitle1))
					.setMessage(getString(R.string.mMessageTable))
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(getString(R.string.confirm),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.dismiss();
									if (TextUtils.isEmpty(table_et_cnt.getText())) {
										st.showToastCENTERAndLONG(
												getActivity(), "请填写人数，谢谢！");
										return;
									}
									if (TextUtils.isEmpty(table_sp_waiter
											.getSelectedItem().toString())) {
										st.showToastCENTERAndLONG(
												getActivity(), "请选择服务员，谢谢！");
										return;
									}

									btn.setBackgroundResource(R.drawable.button_shape_yellow);
									new CreateOrQueryOrderTask().execute();

								}
							})
					.setNegativeButton(getString(R.string.dCancel), null)
					.show();
		}
	}

	/**
	 * 1 - 如果该座没有订单，则新增全新订单（添加orderMainNo）; 2 -
	 * 如果该座已提交过订单（未付款），则新增订单（添加orderID）;
	 */
	public class CreateOrQueryOrderTask extends AsyncTask<Void, Void, Order> {

		@Override
		protected Order doInBackground(Void... Boolean) {
			if (!checkTable(checkedNo)) {
				order = new Order();
				if (!TextUtils.isEmpty(table_et_cnt.getText()))
					order.setCnt(Integer.parseInt(table_et_cnt.getText()
							.toString()));
				order.setWaiter(table_sp_waiter.getSelectedItem().toString());
				order.setTableNo(checkedNo);
				order.setOrderID(String.valueOf(c.get(Calendar.YEAR))
						+ String.valueOf(c.get(Calendar.MONTH))
						+ String.valueOf(c.get(Calendar.DAY_OF_MONTH))
						+ String.valueOf(c.get(Calendar.HOUR_OF_DAY))
						+ String.valueOf(c.get(Calendar.MINUTE))
						+ String.valueOf(c.get(Calendar.SECOND)) + checkedNo);
				dom.addOrder(order);
				return null;
			} else {// 该座已有订单
				List<Order> orders = dom.queryOrderByTableId(checkedNo);
				List<Order> unsubmittedOrders = hasUnSubmittedOrder(orders);
				if (unsubmittedOrders.size() > 0) {
					// 有订单未提交
					order = unsubmittedOrders.get(0);
				} else {
					// 订单已提交
					order = new Order();
					order.setCnt(orders.get(0).getCnt());
					order.setTableNo(orders.get(0).getTableNo());
					order.setOrderID(String.valueOf(c.get(Calendar.YEAR))
							+ String.valueOf(c.get(Calendar.MONTH))
							+ String.valueOf(c.get(Calendar.DAY_OF_MONTH))
							+ String.valueOf(c.get(Calendar.HOUR_OF_DAY))
							+ String.valueOf(c.get(Calendar.MINUTE))
							+ String.valueOf(c.get(Calendar.SECOND)) + checkedNo);
					order.setWaiter(orders.get(0).getWaiter());
					dom.addOrder(order);
				}

				return order;

			}
		}

		@Override
		protected void onPostExecute(Order o) {
			// TODO
			if (o != null) {
				table_et_cnt.setText(o.getCnt() + "");
				table_sp_waiter.setSelection(getEmployeeId(o.getWaiter()));
			}
			av.setCHOOSE(new ArrayList<OrderItem>());
			Intent intent = new Intent();
			intent.setClass(getActivity(), MenuActivity.class);
			intent.putExtra(Constants.ARGS_ORDER_ID, order.getOrderID());
			intent.putExtra(Constants.ARGS_ORDER_TABLE_ID, order.getTableNo());
			startActivityForResult(intent, REQUEST_FROM_TABLE_ORDER);
		}

	}

	private int getEmployeeId(String name) {
		int position = 0;
		for (int i = 0; i < names.size(); i++) {
			if (names.get(i).equals(name)) {
				position = i;
				break;
			}
		}
		return position;
	}

	private List<Order> hasUnSubmittedOrder(List<Order> orders) {
		List<Order> unsubmittedOrders = new ArrayList<Order>();
		for (Order o : orders) {
			if (!o.isSubmitted()) {
				unsubmittedOrders.add(o);
			}
		}
		return unsubmittedOrders;
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View v, int position, Object obj) {
			((ViewPager) v).removeView(mViews.get(position));
		}

		@Override
		public int getCount() {
			return mViews.size();
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public Object instantiateItem(View v, int position) {
			((ViewPager) v).addView(mViews.get(position));
			return mViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		AnimationSet _AnimationSet = new AnimationSet(true);
		TranslateAnimation _TranslateAnimation;

		if (checkedId == R.id.table_rb_s) {
			_TranslateAnimation = new TranslateAnimation(
					mCurrentCheckedRadioLeft, getResources().getDimension(
							R.dimen.rdo), 0f, 0f);
			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(false);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);
			table_iv_selected.startAnimation(_AnimationSet);// 开始上面蓝色横条图片的动画切换
			table_vp.setCurrentItem(0);// 让下方ViewPager跟随上面的HorizontalScrollView切换
		} else if (checkedId == R.id.table_rb_n) {
			_TranslateAnimation = new TranslateAnimation(
					mCurrentCheckedRadioLeft, getResources().getDimension(
							R.dimen.rdo_table), 0f, 0f);

			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(false);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);

			table_iv_selected.startAnimation(_AnimationSet);

			table_vp.setCurrentItem(1);
		}
		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();// 更新当前蓝色横条距离左边的距离
	}

	/**
	 * 获得当前被选中的RadioButton距离左侧的距离
	 */
	private float getCurrentCheckedRadioLeft() {
		if (table_rb_s.isChecked()) {
			return getResources().getDimension(R.dimen.rdo);
		} else if (table_rb_n.isChecked()) {
			return getResources().getDimension(R.dimen.rdo_table);
		}
		return 0f;
	}

	/**
	 * ViewPager的PageChangeListener(页面改变的监听器)
	 */
	private class MyPagerOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			if (position == 0) {
				table_vp.setCurrentItem(0);
				table_rb_s.performClick();
			} else if (position == 1) {
				table_vp.setCurrentItem(1);
				table_rb_n.performClick();
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TableOrderFragment.REQUEST_FROM_TABLE_ORDER) {
			if (resultCode == Activity.RESULT_OK) {
				st.showToastCENTERAndLONG(getActivity(),
						getResources().getString(R.string.oMessage2));
			}
		}
		getActivity().finish();
	}
}
