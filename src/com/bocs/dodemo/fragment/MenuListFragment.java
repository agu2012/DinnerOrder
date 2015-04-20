package com.bocs.dodemo.fragment;

import java.util.List;

import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.application.ApplicationVariable;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.MenuItem;
import com.bocs.dodemo.entity.MenuLab;
import com.bocs.dodemo.entity.OrderItem;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MenuListFragment extends ListFragment {
	private List<MenuItem> menu;
	// private ArrayList<OrderItem> choose;

	private Callbacks mCallbacks;
	private OrderItem oi;
	private SharedPreferences sharedPreferences;
	private String lan = "";
	private int iClass;// 种类编号
	private boolean isDrink = false;

	private ApplicationVariable av;

	public static MenuListFragment newInstance(int iClass) {
		Bundle args = new Bundle();
		args.putInt(Constants.ARGS_MENU_CLASS, iClass);
		// args.putBoolean(Constants.ARGS_IS_DRINK, isDrink);
		MenuListFragment fragment = new MenuListFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		av = (ApplicationVariable) getActivity().getApplication();
		sharedPreferences = getActivity().getSharedPreferences(
				Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
		lan = sharedPreferences.getString(Constants.SP_LOCALE, "zh");

		isDrink = getArguments().getBoolean(Constants.ARGS_IS_DRINK);
		iClass = getArguments().getInt(Constants.ARGS_MENU_CLASS);

		// if (isDrink) {
		// menu = DrinkLab.get(getActivity()).getDrinkClassItem().get(iClass+8);
		// } else {
		menu = MenuLab.get(getActivity()).getMenuClassItem().get(iClass);
		// }
		setListAdapter(new DishItemAdapter());

	}

	private class DishItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return menu.size();
		}

		@Override
		public MenuItem getItem(int position) {
			return menu.get(position);
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
						R.layout.row_menu_list, null);
				// 获取控件对象
				listItemView.menu_row_iv = (ImageView) convertView
						.findViewById(R.id.menu_row_iv);
				listItemView.menu_row_tv_name = (TextView) convertView
						.findViewById(R.id.menu_row_tv_name);
				listItemView.menu_row_tv_desc = (TextView) convertView
						.findViewById(R.id.menu_row_tv_desc);
				listItemView.menu_row_tv_price = (TextView) convertView
						.findViewById(R.id.menu_row_tv_price);
				listItemView.menu_row_tv_unit = (TextView) convertView
						.findViewById(R.id.menu_row_tv_unit);
				listItemView.menu_row_ck = (CheckBox) convertView
						.findViewById(R.id.menu_row_ck);

				convertView.setTag(listItemView);
			} else {
				listItemView = (ListViewItem) convertView.getTag();
			}

			if (getItem(position) != null) {
				if (getItem(position).getItemPic() != 0) {
					listItemView.menu_row_iv.setVisibility(View.VISIBLE);
					listItemView.menu_row_iv.setImageResource(getItem(position)
							.getItemPic());
				} else {
					listItemView.menu_row_iv.setVisibility(View.GONE);
				}

				// TODO gxy-国际化
				if (lan.equals("zh")) {
					listItemView.menu_row_tv_name.setText(getItem(position)
							.getItemName());
					listItemView.menu_row_tv_desc.setText(getItem(position)
							.getItemDesc());
				} else {
					listItemView.menu_row_tv_name.setText(getItem(position)
							.getItemNameEn());
					listItemView.menu_row_tv_desc.setText(getItem(position)
							.getItemDescEn());

				}
				listItemView.menu_row_tv_price.setText("￥"
						+ getItem(position).getItemPrice() + ".00");
				listItemView.menu_row_tv_unit.setText("/ "
						+ getItem(position).getItemUnit());
				listItemView.menu_row_ck
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {

									if (!isContainMenuID(getItem(position))) {// 如果没有加过这个菜
										oi = new OrderItem();
										oi.setItemCnt(1);
										oi.setMenuItem(new MenuItem());
										oi.getMenuItem().setItemPrice(
												getItem(position)
														.getItemPrice());
										oi.getMenuItem().set_id(
												getItem(position).get_id());
										oi.getMenuItem().setItemClass(
												getItem(position).getItemClass());
										av.getCHOOSE().add(oi);
									}
								} else {
									for (OrderItem temp : av.getCHOOSE()) {
										if (getItem(position).get_id() == temp
												.getMenuItem().get_id()) {
											Log.i("gxy", "position:" + position);
											av.getCHOOSE().remove(temp);

											break;
										}
									}
								}
								mCallbacks.onUpdateOrderInfo();
							}
						});
				listItemView.menu_row_ck
						.setChecked(isContainMenuID(getItem(position)));
			}
			return convertView;
		}

		// 判断选中的列表choose中是否包含该项
		private boolean isContainMenuID(MenuItem mi) {
			boolean flag = false;
			if (av.getCHOOSE().size() > 0) {
				for (OrderItem temp : av.getCHOOSE()) {
					if (mi.get_id() == temp.getMenuItem().get_id()) {
						flag = true;
					}
				}
			}
			return flag;
		}

		public final class ListViewItem {
			public ImageView menu_row_iv;
			public TextView menu_row_tv_name;
			public TextView menu_row_tv_desc;
			public TextView menu_row_tv_price;
			public TextView menu_row_tv_unit;
			public CheckBox menu_row_ck;

		}
	}

	public interface Callbacks {
		void onUpdateOrderInfo();
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
