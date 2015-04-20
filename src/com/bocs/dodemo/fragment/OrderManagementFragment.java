package com.bocs.dodemo.fragment;

import java.util.List;

import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.Order;
import com.bocs.dodemo.entity.OrderItem;
import com.bocs.dodemo.util.AmountTextWatcher;
import com.bocs.dodemo.util.BluetoothUtils;
import com.bocs.dodemo.util.CustomDialog;
import com.bocs.dodemo.util.PublicUtils;
import com.bocs.dodemo.util.ShowToast;
import com.bocs.dodemo.util.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class OrderManagementFragment extends Fragment {

	private Button order_manage_btn_pay;
	// 对话框中的控件
	private TextView dialog_pay_tv_amount, dialog_pay_tv_tip,
			dialog_pay_tv_total;
	private EditText dialog_pay_et_discount, dialog_pay_et_pwd;
	private LinearLayout dialog_pay_ll_discount;
	private RadioGroup dialog_pay_rg;

	private List<Order> orders;
	private String tableID = "";
	private DinnerOrderManager dom;
	private Order order;

	private static SharedPreferences sp;
	private ShowToast st = new ShowToast();
	private FragmentManager fm;
	private Fragment fragment;

	long money;

	public static OrderManagementFragment newInstance(String tableID) {
		Bundle args = new Bundle();
		args.putString(Constants.ARGS_ORDER_TABLE_ID, tableID);
		OrderManagementFragment fragment = new OrderManagementFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tableID = getArguments().getString(Constants.ARGS_ORDER_TABLE_ID);
		dom = new DinnerOrderManagerImpl(getActivity());
		sp = getActivity().getSharedPreferences(Constants.SP_FILE_NAME, 0);
		new FetchOrdersTask().execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_order_manager, container,
				false);
		// order_manage_btn_print = (Button) v
		// .findViewById(R.id.order_manage_btn_print);
		order_manage_btn_pay = (Button) v
				.findViewById(R.id.order_manage_btn_pay);

		initFragment();

		OnClickListener omOnclickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				// case R.id.order_manage_btn_print:
				// if (orders.size() > 0) {
				// BluetoothUtils.checkPrintSlip(getActivity(),
				// orders.get(0), false);
				// }
				// break;
				case R.id.order_manage_btn_pay:
					onClickPay();
					break;
				}
			}
		};

		// order_manage_btn_print.setOnClickListener(omOnclickListener);
		order_manage_btn_pay.setOnClickListener(omOnclickListener);

		return v;
	}

	private void initFragment() {
		fm = getChildFragmentManager();
		fragment = fm.findFragmentById(R.id.order_manage_fl);

		if (fragment == null) {
			fragment = OrderManagerListFragment.newInstance(tableID);
			fm.beginTransaction().add(R.id.order_manage_fl, fragment).commit();
		} else {
			fm.beginTransaction().remove(fragment).commit();
			fragment = OrderManagerListFragment.newInstance(tableID);
			fm.beginTransaction().add(R.id.order_manage_fl, fragment).commit();
		}
	}

	public class FetchOrdersTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... Boolean) {
			orders = dom.queryOrderDetailByTableId(tableID);
			if (orders.size() > 0) {
				for (Order o : orders) {
					o.setItems(dom.queryOrderListByOrderID(o.getOrderID()));
				}
			}
			if (orders.size() > 0)
				return true;
			else
				return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// if (result) {
			// order_manage_btn_print.setEnabled(true);
			// } else {
			// order_manage_btn_print.setEnabled(false);
			// }

			initFragment();
		}

	}

	private void onClickPay() {
		order = mergeOrders();
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.dialog_pay, null);

		dialog_pay_tv_amount = (TextView) v
				.findViewById(R.id.dialog_pay_tv_amount);
		dialog_pay_tv_tip = (TextView) v.findViewById(R.id.dialog_pay_tv_tip);
		dialog_pay_tv_total = (TextView) v
				.findViewById(R.id.dialog_pay_tv_total);

		dialog_pay_tv_amount.setText(order.getTotalAmount() + ".00");
		dialog_pay_tv_tip.setText(StringUtils.roundDouble2Int(StringUtils
				.getAmount(order.getTotalAmount() * 10 + ""))+ ".00");

		dialog_pay_tv_total.setText(StringUtils.roundDouble2Int(StringUtils
				.getAmount(order.getTotalAmount() * 110 + ""))+ ".00");

		dialog_pay_ll_discount = (LinearLayout) v
				.findViewById(R.id.dialog_pay_ll_discount);
		dialog_pay_et_discount = (EditText) v
				.findViewById(R.id.dialog_pay_et_discount);
		dialog_pay_et_pwd = (EditText) v.findViewById(R.id.dialog_pay_et_pwd);
		dialog_pay_et_discount
				.addTextChangedListener(new DisCountTextWatcher());

		dialog_pay_rg = (RadioGroup) v.findViewById(R.id.dialog_pay_rg);
		dialog_pay_rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.dialog_pay_rb_no_discount) {
					dialog_pay_ll_discount.setVisibility(View.GONE);
				} else {
					dialog_pay_ll_discount.setVisibility(View.VISIBLE);
				}
			}
		});

		CustomDialog.Builder customBuilder = new CustomDialog.Builder(
				getActivity());
		customBuilder
				.setContentView(v)
				.setNegativeButton("现金支付",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dealWithSettlement(false);
								dialog.dismiss();
							}
						})
				.setPositiveButton("mPOS支付",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dealWithSettlement(true);
								dialog.dismiss();

							}
						});
		customBuilder.create().show();
	}

	private Order mergeOrders() {
		Order temp = new Order();
		long total = 0;
		if (orders.size() > 0) {
			temp = orders.get(0);
			total = temp.getTotalAmount();
			// 设置账单号
			int num = sp.getInt(Constants.SP_ITEM_ORDER_MAIN_NO, 1000000);
			temp.setOrderMainNo(num + "");
			sp.edit().putInt(Constants.SP_ITEM_ORDER_MAIN_NO, num++).commit();
			if (orders.size() != 1) {
				// 合并餐桌的所有订单
				for (int i = 1; i < orders.size(); i++) {
					for (OrderItem oi : orders.get(i).getItems()) {
						temp.getItems().add(oi);
					}
					total = total + orders.get(i).getTotalAmount();
				}
				temp.setItems(StringUtils.mergeOrders(temp.getItems()));
				temp.setTotalAmount(total);
			} else {
				temp = orders.get(0);
			}

		}
		return temp;
	}

	// 结账后处理
	// 0-打印总订单
	// 1-删除该座位的所有订单；
	// 2-finish该页面；
	private void dealWithSettlement(boolean isMPos) {
		money = order.getTotalAmount() * 110;

		if (dialog_pay_rg.getCheckedRadioButtonId() == R.id.dialog_pay_rb_discount) {
			if (TextUtils.isEmpty(dialog_pay_et_discount.getText())) {
				st.showToastCENTERAndLONG(getActivity(), "请填写折扣金额");
				return;
			}
			// 有折扣
			order.setDiscount((long) (Double.parseDouble(dialog_pay_et_discount
					.getText().toString()) * 100));
			if (TextUtils.isEmpty(dialog_pay_et_pwd.getText())) {
				st.showToastCENTERAndLONG(getActivity(), "请填写折扣密码");
				return;
			} else if (!dialog_pay_et_pwd.getText().toString()
					.equals(sp.getString(Constants.SP_PWD_DISCOUNT, "123456"))) {
				st.showToastCENTERAndLONG(getActivity(), "折扣密码错误");
				return;
			}
		}
		if (isMPos) {
			order.setSettlementType(2);
			boolean mposflag = false;
			Intent intent_mpos = new Intent();
			if (PublicUtils.checkMposDemo(getActivity())) {
				intent_mpos.setClassName("com.bocs.mposdemo",
						"com.bocs.mposdemo.activity.SalesActivity");
				mposflag = true;
			} else if (PublicUtils.checkMpos(getActivity())) {
				intent_mpos.setClassName("com.bocs.mpos",
						"com.bocs.mpos.activity.SalesActivity");
				mposflag = true;
			}
			if (mposflag) {
				try {

					Bundle buddle_mpos = new Bundle();
					buddle_mpos.putString("sAmount", money + "");
					buddle_mpos.putString("sOrderNo", "");
					buddle_mpos.putString("sPhoneNO", "");
					buddle_mpos.putString("sEmailAddr", "");
					buddle_mpos.putString("sCurrencyCode", "");

					intent_mpos.putExtras(buddle_mpos);

					startActivityForResult(intent_mpos, 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

				st.showToastCENTERAndSHORT(getActivity(),
						getString(R.string.omMessage4));
			}
		} else {
			// settleType = 1：现金支付
			order.setSettlementType(1);
			BluetoothUtils.checkPrintSlip(getActivity(), order, true);

		}
	}

	private class DisCountTextWatcher extends AmountTextWatcher {

		@Override
		public void afterTextChanged(Editable edt) {
			super.afterTextChanged(edt);
			long total = order.getTotalAmount() * 110
					- Long.valueOf(edt.toString().replace(".", ""));
			dialog_pay_tv_total.setText(StringUtils.getAmount(total + ""));
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 2:// mPOS支付返回
			if (resultCode == Activity.RESULT_OK) {
				BluetoothUtils.checkPrintSlip(getActivity(), order, true);
			} else {
				st.showToastCENTERAndLONG(getActivity(), "支付失败，请重新支付");
			}
			break;
		case 3:
			if (resultCode == Activity.RESULT_OK) {
				new FetchOrdersTask().execute();
			}
			break;
		}

	}

}
