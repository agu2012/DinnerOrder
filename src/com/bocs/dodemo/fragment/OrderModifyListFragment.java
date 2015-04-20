package com.bocs.dodemo.fragment;

import java.util.ArrayList;

import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.OrderItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class OrderModifyListFragment extends ListFragment {

	private ArrayList<OrderItem> choose;// 已选项

	// private String orderID;
	private Callbacks mCallbacks;
	private DinnerOrderManager dom;

	// private boolean isSettled;

	public static OrderModifyListFragment newInstance(String orderID,
			ArrayList<OrderItem> choose) {
		Bundle args = new Bundle();
		args.putString(Constants.ARGS_ORDER_ID, orderID);
		args.putParcelableArrayList(Constants.ARGS_MENU_CHOOSE, choose);
		// args.putBoolean(Constants.ARGS_IS_SETTLED, isSettled);
		OrderModifyListFragment fragment = new OrderModifyListFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// orderID = getArguments().getString(Constants.ARGS_ORDER_ID);
		// isSettled = getArguments().getBoolean(Constants.ARGS_IS_SETTLED);
		dom = new DinnerOrderManagerImpl(getActivity());
		// choose = dom.queryOrderListByOrderID(orderID);// 仅查询当前订单号的的内容
		choose = getArguments().getParcelableArrayList(
				Constants.ARGS_MENU_CHOOSE);
		setListAdapter(new MofifyItemAdapter());

	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int position, long arg3) {
				doRemoveItem(position);
				return false;
			}
		});
	}

	private void doRemoveItem(final int position) {
		new AlertDialog.Builder(getActivity())
				.setTitle(getString(R.string.dTitle1))
				.setMessage("确定删除该项？")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(getString(R.string.dConfirm),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
								dom.deleteOrderItem(choose.get(position));
								choose.remove(position);
								((MofifyItemAdapter) getListAdapter())
										.notifyDataSetChanged();
								mCallbacks.onUpdateModifyInfo(choose);

							}
						}).setNegativeButton(getString(R.string.dCancel), null)
				.show();
	}

	private class MofifyItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return choose.size();
		}

		@Override
		public OrderItem getItem(int position) {
			return choose.get(position);
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
						R.layout.row_menu_temp_list, null);
				// 获取控件对象
				listItemView.menu_temp_row_iv = (ImageView) convertView
						.findViewById(R.id.menu_temp_row_iv);
				listItemView.menu_temp_row_tv_name = (TextView) convertView
						.findViewById(R.id.menu_temp_row_tv_name);
				listItemView.menu_temp_row_tv_price = (TextView) convertView
						.findViewById(R.id.menu_temp_row_tv_price);
				listItemView.menu_temp_row_tv_unit = (TextView) convertView
						.findViewById(R.id.menu_temp_row_tv_unit);
				// listItemView.menu_temp_row_ll2 = (LinearLayout) convertView
				// .findViewById(R.id.menu_temp_row_ll2);
				listItemView.menu_temp_row_btn_deduce = (Button) convertView
						.findViewById(R.id.menu_temp_row_btn_deduce);
				listItemView.menu_temp_row_btn_add = (Button) convertView
						.findViewById(R.id.menu_temp_row_btn_add);
				listItemView.menu_temp_row_et_cnt = (EditText) convertView
						.findViewById(R.id.menu_temp_row_et_cnt);

				convertView.setTag(listItemView);
			} else {
				listItemView = (ListViewItem) convertView.getTag();
			}

			if (getItem(position) != null) {
				listItemView.menu_temp_row_iv
						.setImageResource(getItem(position).getMenuItem()
								.getItemPic());

				listItemView.menu_temp_row_tv_name.setText(getItem(position)
						.getMenuItem().getItemName());
				listItemView.menu_temp_row_tv_price.setText("￥"
						+ getItem(position).getMenuItem().getItemPrice()
						+ ".00");
				listItemView.menu_temp_row_tv_unit.setText("/ "
						+ getItem(position).getMenuItem().getItemUnit());

				// if (!isSettled) {
				// listItemView.menu_temp_row_ll2.setVisibility(View.VISIBLE);
				View.OnClickListener onClickListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						int cnt = getItem(position).getItemCnt();
						switch (v.getId()) {
						case R.id.menu_temp_row_btn_deduce:
							if (cnt > 1) {
								cnt--;
								getItem(position).setItemCnt(cnt);
							}
							break;
						case R.id.menu_temp_row_btn_add:
							cnt++;
							getItem(position).setItemCnt(cnt);
							break;
						}
						((MofifyItemAdapter) getListAdapter())
								.notifyDataSetChanged();
						mCallbacks.onUpdateModifyInfo(choose);

					}
				};
				listItemView.menu_temp_row_btn_deduce
						.setOnClickListener(onClickListener);
				listItemView.menu_temp_row_btn_add
						.setOnClickListener(onClickListener);
				listItemView.menu_temp_row_et_cnt.setText(getItem(position)
						.getItemCnt() + "");
				// } else {
				//
				// listItemView.menu_temp_row_ll2.setVisibility(View.GONE);
				// }
			}
			return convertView;
		}

		public final class ListViewItem {
			public ImageView menu_temp_row_iv;
			public TextView menu_temp_row_tv_name;
			public TextView menu_temp_row_tv_price;
			public TextView menu_temp_row_tv_unit;
			public Button menu_temp_row_btn_deduce;
			public Button menu_temp_row_btn_add;
			public EditText menu_temp_row_et_cnt;
			// public LinearLayout menu_temp_row_ll2;
		}
	}

	public interface Callbacks {
		void onUpdateModifyInfo(ArrayList<OrderItem> choose);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
}
