package com.bocs.dodemo.fragment;

import java.util.ArrayList;
import java.util.Locale;

import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.application.ApplicationVariable;
import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.Order;
import com.bocs.dodemo.entity.OrderItem;
import com.bocs.dodemo.util.PublicUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 点菜列表(临时)
 * 
 * @author guxiayi
 * 
 */
public class MenuTempFragment extends Fragment {

	private Button menu_temp_btn_order;
	private TextView menu_temp_tv_info;

	private ArrayList<OrderItem> choose;// 已选项
	private String orderID = "";
	// private String tableID = "";
	private String sLan = "";

	private DinnerOrderManager dom;
	private SharedPreferences sharedPreferences;
	private ApplicationVariable av;

	public static MenuTempFragment newInstance(String orderID) {
		Bundle args = new Bundle();
		args.putString(Constants.ARGS_ORDER_ID, orderID);
		MenuTempFragment fragment = new MenuTempFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		orderID = getArguments().getString(Constants.ARGS_ORDER_ID);
		dom = new DinnerOrderManagerImpl(getActivity());
		av = (ApplicationVariable) getActivity().getApplication();
		choose = av.getCHOOSE();

		sharedPreferences = getActivity().getSharedPreferences(
				Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
		sLan = sharedPreferences.getString(Constants.SP_LOCALE, "zh");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.fragment_menu_temp, container, false);
		menu_temp_btn_order = (Button) v.findViewById(R.id.menu_temp_btn_order);
		menu_temp_tv_info = (TextView) v.findViewById(R.id.menu_temp_tv_info);

		if (!Locale.getDefault().toString().equals(sLan)) {
			PublicUtils.changeLanguage(getActivity(), sLan);
		}

		menu_temp_tv_info.setText("￥" + PublicUtils.getTotalAmount(choose)
				+ ".00");

		FragmentManager fm = getChildFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.menu_temp_fl);

		if (fragment == null) {
			fragment = MenuTempListFragment.newInstance(orderID);
			fm.beginTransaction().add(R.id.menu_temp_fl, fragment).commit();
		}
		// FragmentManager fm = getChildFragmentManager();
		// Fragment fragment = MenuTempListFragment.newInstance(orderID);
		// fm.beginTransaction().add(R.id.menu_temp_fl, fragment).commit();

		View.OnClickListener menuTempOnClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.menu_temp_btn_order:
					new SubmitOrderTask().execute();
					break;
				}

			}
		};
		menu_temp_btn_order.setOnClickListener(menuTempOnClickListener);

		return v;
	}

	public class SubmitOrderTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... Boolean) {
			Order order = dom.queryOrderByOrderID(orderID);
			order.setTotalAmount(PublicUtils.getTotalAmount(choose));
			order.setSubmitted(true);
			dom.addOrderList(choose, orderID);
			dom.updateOrderByOrderID(order);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			av.setSUCCESS_ORDER(true);
			getActivity().setResult(Activity.RESULT_OK);
			getActivity().finish();
		}
	}

	public void updateOrderInfo() {
		if (choose.size() == 0) {
			getActivity().finish();
			return;
		}
		// 订单信息更新

		menu_temp_tv_info.setText("￥" + PublicUtils.getTotalAmount(choose)
				+ ".00");
	}

}
