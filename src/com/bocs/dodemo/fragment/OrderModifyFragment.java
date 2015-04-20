package com.bocs.dodemo.fragment;

import java.util.ArrayList;
import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.Order;
import com.bocs.dodemo.entity.OrderItem;
import com.bocs.dodemo.util.BluetoothUtils;
import com.bocs.dodemo.util.PublicUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class OrderModifyFragment extends Fragment {
	private RelativeLayout order_modify_rl;
	private Button order_modify_btn;

	private String orderID = "";
	private boolean isSettled;
	private ArrayList<OrderItem> choose;// 已选项
	private DinnerOrderManager dom;
	private Order order;

	public static OrderModifyFragment newInstance(String orderID,
			boolean isSettled, ArrayList<OrderItem> choose) {
		Bundle args = new Bundle();
		args.putString(Constants.ARGS_ORDER_ID, orderID);
		args.putBoolean(Constants.ARGS_IS_SETTLED, isSettled);
		args.putParcelableArrayList(Constants.ARGS_ORDER_CHOOSE, choose);
		OrderModifyFragment fragment = new OrderModifyFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		orderID = getArguments().getString(Constants.ARGS_ORDER_ID);
		// isSettled = getArguments().getBoolean(Constants.ARGS_IS_SETTLED);
		choose = getArguments().getParcelableArrayList(
				Constants.ARGS_ORDER_CHOOSE);
		dom = new DinnerOrderManagerImpl(getActivity());
		order = dom.queryOrderByOrderID(orderID);
		// for (OrderItem oi : choose) {
		// oi = PublicUtils.getMenuItemInOrderItem(getActivity(), oi);
		// }

		choose = PublicUtils.getOrderDetails(getActivity(), choose);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_order_modify, container,
				false);
		order_modify_rl = (RelativeLayout) v.findViewById(R.id.order_modify_rl);
		if (isSettled) {
			order_modify_rl.setVisibility(View.GONE);
		} else {
			order_modify_rl.setVisibility(View.VISIBLE);
			order_modify_btn = (Button) v.findViewById(R.id.order_modify_btn);

			View.OnClickListener menuTempOnClickListener = new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.order_modify_btn:
						new ModifyOrderTask().execute();
						break;
					}

				}
			};
			order_modify_btn.setOnClickListener(menuTempOnClickListener);
		}

		FragmentManager fm = getChildFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.order_modify_fl);

		if (fragment == null) {
			fragment = OrderModifyListFragment.newInstance(orderID, choose);
			fm.beginTransaction().add(R.id.order_modify_fl, fragment).commit();
		}

		return v;
	}

	public class ModifyOrderTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... Boolean) {
			// 更新订单内容+设置为“已下单”
			dom.updateOrderListByOrderId(orderID, choose);
			order.setItems(choose);
			order.setTotalAmount(PublicUtils.getTotalAmount(choose));
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			showPrintDialog();
		}

	}

	public void showPrintDialog() {
		new AlertDialog.Builder(getActivity())
				.setTitle(getResources().getString(R.string.dTitle1))
				.setMessage("已下单成功，请问是否打印单据？")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setNegativeButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (choose.size() > 0) {
									BluetoothUtils.checkPrintSlip(
											getActivity(), order, false);
								}
							}

						})
				.setPositiveButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								getActivity().setResult(Activity.RESULT_OK);
								getActivity().finish();
							}
						}).show();
	}

}
