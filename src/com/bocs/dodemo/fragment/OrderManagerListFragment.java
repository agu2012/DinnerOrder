package com.bocs.dodemo.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bocs.dodemo.activity.OrderModifyActivity;
import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.Order;
import com.bocs.dodemo.entity.OrderItem;
import com.bocs.dodemo.util.PublicUtils;

public class OrderManagerListFragment extends ListFragment {

	private DinnerOrderManager dom;
	private String tableID;
	private ArrayList<Order> orders;

	public static OrderManagerListFragment newInstance(String tableID) {
		Bundle args = new Bundle();
		args.putString(Constants.ARGS_ORDER_TABLE_ID, tableID);
		OrderManagerListFragment fragment = new OrderManagerListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tableID = getArguments().getString(Constants.ARGS_ORDER_TABLE_ID);
		dom = new DinnerOrderManagerImpl(getActivity());
		orders = new ArrayList<Order>();
		new FetchOrdersTask().execute();

	}

	public class FetchOrdersTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... Boolean) {
			orders = dom.queryOrderByTableId(tableID);
			if (orders.size() > 0) {
				for (Order o : orders) {
					ArrayList<OrderItem> choose = dom.queryOrderListByOrderID(o.getOrderID());
					choose = PublicUtils.getOrderDetails(getActivity(),choose);
					o.setItems(choose);
				}
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			setListAdapter(new OrderManagerAdapter());
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), OrderModifyActivity.class);
		intent.putExtra(Constants.ARGS_ORDER_ID, orders.get(position)
				.getOrderID());
		intent.putExtra(Constants.ARGS_ORDER_CHOOSE, orders.get(position)
				.getItems());
		startActivityForResult(intent, 3);

	}

	private class OrderManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return orders.size();
		}

		@Override
		public Order getItem(int position) {
			return orders.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ListViewItem listItemView = null;
			if (convertView == null) {
				listItemView = new ListViewItem();
				// 获取list_item布局文件的视图
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.row_om_main, null);
				// 获取控件对象
				listItemView.row_om_tv_img = (TextView) convertView
						.findViewById(R.id.row_om_tv_img);
				listItemView.row_om_tv_date = (TextView) convertView
						.findViewById(R.id.row_om_tv_order_date);
				listItemView.row_om_tv_detail = (TextView) convertView
						.findViewById(R.id.row_om_tv_detail);
				listItemView.row_om_tv_total = (TextView) convertView
						.findViewById(R.id.row_om_tv_total);
				listItemView.row_om_tv_order_id = (TextView) convertView
						.findViewById(R.id.row_om_tv_order_id);

				convertView.setTag(listItemView);
			} else {
				listItemView = (ListViewItem) convertView.getTag();
			}

			if (getItem(position) != null) {
				if (getItem(position).isSettled()) {
					listItemView.row_om_tv_img
							.setBackgroundResource(android.R.color.darker_gray);
					listItemView.row_om_tv_img.setText("已下单");
				} else {
					listItemView.row_om_tv_img
							.setBackgroundResource(R.color.TEXT_RED);
					listItemView.row_om_tv_img.setText("未下单");
				}
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:MM:ss");
				listItemView.row_om_tv_date.setText(df.format(getItem(position)
						.getOrderDate()));
				String content = "";
				if (getItem(position).getItems().size() > 0) {
					int i = 0;
					for (OrderItem oi : getItem(position).getItems()) {
						i++;
						content = content + oi.getMenuItem().getItemName()
								+ " * " + oi.getItemCnt()
								+ oi.getMenuItem().getItemUnit() + "; ";
						if (i == 2) {// 最多显示2项
							if (getItem(position).getItems().size() != 2) {
								content = content + "……";
							}
							break;
						}
					}
				}
				listItemView.row_om_tv_detail.setText(content);
				listItemView.row_om_tv_total.setText("￥"
						+ getItem(position).getTotalAmount() + ".00");
				listItemView.row_om_tv_order_id.setText(getItem(position)
						.getOrderID());
			}
			return convertView;
		}

		class ListViewItem {
			public TextView row_om_tv_img;
			public TextView row_om_tv_date;
			public TextView row_om_tv_detail;
			public TextView row_om_tv_total;
			public TextView row_om_tv_order_id;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 3) {
			if (resultCode == Activity.RESULT_OK) {
				new FetchOrdersTask().execute();
			}
		}

	}

	public void onActivityResultListener(int requestCode, int resultCode,
			Intent data) {
		new FetchOrdersTask().execute();

	}
}
