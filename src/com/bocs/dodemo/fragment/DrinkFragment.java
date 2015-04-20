package com.bocs.dodemo.fragment;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.bocs.dodemo.activity.MenuTempActivity;
import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.application.ApplicationVariable;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.DrinkLab;
import com.bocs.dodemo.entity.MenuItem;
import com.bocs.dodemo.entity.OrderItem;
import com.bocs.dodemo.util.PublicUtils;

public class DrinkFragment extends Fragment implements OnCheckedChangeListener {
	private ViewPager drink_vp;
	private RadioGroup drink_rg;
	private ScrollView drink_hs;// 上面的水平滚动控件
	private Button drink_btn_order, drink_btn_lan;// 点单按钮,切换语言
	private TextView drink_tv_total_num;// 总价格

	private float mCurrentCheckedRadioLeft;// 当前被选中的RadioButton距离左侧的距离
	private Map<Integer, List<MenuItem>> menuMap;// 分类的菜单
	private String[] classNames;
	private String orderID;
	private String sLan = "";
	// private boolean isDrink = false;

	// private DinnerOrderManager dom;
	private SharedPreferences sharedPreferences;
	private ApplicationVariable av;

	public static DrinkFragment newInstance(String tableID, String orderID) {
		Bundle args = new Bundle();
		args.putString(Constants.ARGS_ORDER_TABLE_ID, tableID);
		args.putString(Constants.ARGS_ORDER_ID, orderID);
		DrinkFragment fragment = new DrinkFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		av = (ApplicationVariable) getActivity().getApplication();
		sharedPreferences = getActivity().getSharedPreferences(
				Constants.SP_FILE_NAME, Context.MODE_PRIVATE);

		menuMap = DrinkLab.get(getActivity()).getDrinkClassItem();
		classNames = getResources().getStringArray(R.array.drinkClass);
		orderID = getArguments().get(Constants.ARGS_ORDER_ID).toString();

	}


	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);// 每次切换fragment时调用的方法
		if (isVisibleToUser) {
			drink_tv_total_num.setText("￥" + av.getOrderTotal() + ".00");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_drink, container, false);
		drink_hs = (ScrollView) v.findViewById(R.id.drink_hs);
		drink_rg = (RadioGroup) v.findViewById(R.id.drink_rg_class);

		for (int i = 0; i < classNames.length; i++) {
			RadioButton rb = (RadioButton) (drink_rg.getChildAt(i));
			rb.setVisibility(View.VISIBLE);
			rb.setText(classNames[i]);
		}

		drink_vp = (ViewPager) v.findViewById(R.id.drink_vp);
		drink_rg.setOnCheckedChangeListener(this);

		initMenuViewsInViewPager();

		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();

		// 点单功能-初始化
		drink_btn_order = (Button) v.findViewById(R.id.drink_btn_order);// 点单按钮
		drink_btn_lan = (Button) v.findViewById(R.id.drink_btn_lan);
		sLan = sharedPreferences.getString(Constants.SP_LOCALE, "zh");

		if (!Locale.getDefault().toString().equals(sLan)) {
			PublicUtils.changeLanguage(getActivity(), sLan);
		}

		drink_tv_total_num = (TextView) v.findViewById(R.id.drink_tv_total_num);// 总价格
		drink_btn_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (av.getCHOOSE().size() > 0) {
					for (OrderItem oi : av.getCHOOSE()) {
						oi.setOrderID(orderID);
						oi.setTableID(getArguments().get(
								Constants.ARGS_ORDER_TABLE_ID).toString());
					}

					Intent intent = new Intent(getActivity(),
							MenuTempActivity.class);
					intent.putExtra(Constants.ARGS_ORDER_ID, orderID);
					intent.putExtra(Constants.ARGS_ORDER_TABLE_ID,
							getArguments().get(Constants.ARGS_ORDER_TABLE_ID)
									.toString());
					// intent.putParcelableArrayListExtra(
					// Constants.ARGS_ORDER_CHOOSE, av.getCHOOSE());
					startActivityForResult(intent,
							TableOrderFragment.REQUEST_FROM_TABLE_ORDER);
				}
			}
		});
		drink_btn_lan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Button btnLan = (Button) v;
				if (btnLan.getText().equals("中")) {
					btnLan.setText("En");
					PublicUtils.changeLanguage(getActivity(), "en");
				} else {
					btnLan.setText("中");
					PublicUtils.changeLanguage(getActivity(), "zh");
				}
			}
		});

		return v;
	}

	private void initMenuViewsInViewPager() {
		FragmentManager fm = getActivity().getSupportFragmentManager();

		drink_vp.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return menuMap.size();
			}

			@Override
			public Fragment getItem(int pos) {
				return DrinkListFragment.newInstance(pos);
			}
		});

		drink_vp.setOnPageChangeListener(new MenuPagerOnPageChangeListener());

		drink_rg.getChildAt(0).setSelected(true);
		drink_vp.setCurrentItem(0);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < group.getChildCount() - 1; i++) {
			RadioButton rb = (RadioButton) (group.getChildAt(i));
			if (checkedId == rb.getId()) {
				drink_vp.setCurrentItem(i);
				break;
			}
		}

		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();//
		// 更新当前蓝色横条距离左边的距离
		drink_hs.smoothScrollTo(0, (int) mCurrentCheckedRadioLeft
				- (int) getResources().getDimension(R.dimen.rdo2));

	}

	private class MenuPagerOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
		 */
		@Override
		public void onPageSelected(int position) {
			((RadioButton) drink_rg.getChildAt(position)).performClick();
		}

	}

	private float getCurrentCheckedRadioLeft() {
		// TODO getCurrentCheckedRadioLeft
		switch (drink_rg.getCheckedRadioButtonId()) {
		case R.id.drink_rb1:
			return getResources().getDimension(R.dimen.rdo);
		case R.id.drink_rb2:
			return getResources().getDimension(R.dimen.rdo2);
		case R.id.drink_rb3:
			return getResources().getDimension(R.dimen.rdo3);
		case R.id.drink_rb4:
			return getResources().getDimension(R.dimen.rdo4);
		case R.id.drink_rb5:
			return getResources().getDimension(R.dimen.rdo5);
		case R.id.drink_rb6:
			return getResources().getDimension(R.dimen.rdo6);
		case R.id.drink_rb7:
			return getResources().getDimension(R.dimen.rdo7);
		case R.id.drink_rb8:
			return getResources().getDimension(R.dimen.rdo8);
		case R.id.drink_rb9:
			return getResources().getDimension(R.dimen.rdo9);
		case R.id.drink_rb10:
			return getResources().getDimension(R.dimen.rdo10);
		case R.id.drink_rb11:
			return getResources().getDimension(R.dimen.rdo11);
		case R.id.drink_rb12:
			return getResources().getDimension(R.dimen.rdo12);
		case R.id.drink_rb13:
			return getResources().getDimension(R.dimen.rdo13);
		default:
			return 0f;
		}

	}

	public void updateOrderInfo() {
		// 图标
		if (av.getCHOOSE() != null && av.getCHOOSE().size() > 0) {
			drink_btn_order.setEnabled(true);
		} else {
			drink_btn_order.setEnabled(false);
		}

		// 订单信息更新
		drink_tv_total_num.setText("￥" + av.getOrderTotal() + ".00");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		getActivity().setResult(resultCode, data);
		getActivity().finish();
		super.onActivityResult(requestCode, resultCode, data);
	}

}
