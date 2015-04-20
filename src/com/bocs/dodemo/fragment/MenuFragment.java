package com.bocs.dodemo.fragment;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
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
import com.bocs.dodemo.entity.MenuItem;
import com.bocs.dodemo.entity.MenuLab;
import com.bocs.dodemo.entity.OrderItem;
import com.bocs.dodemo.util.PublicUtils;

public class MenuFragment extends Fragment implements OnCheckedChangeListener {
	private ViewPager menu_vp;
	private RadioGroup menu_rg;
	private ScrollView menu_hs;// 上面的水平滚动控件
	private Button menu_btn_order, menu_btn_lan;// 点单按钮,切换语言
	private TextView menu_tv_total_num;// 总价格

	private float mCurrentCheckedRadioLeft;// 当前被选中的RadioButton距离左侧的距离
	private Map<Integer, List<MenuItem>> menuMap;// 分类的菜单
	private String[] classNames;
	private String orderID;
	private String sLan = "";
	// private boolean isDrink = false;

	// private DinnerOrderManager dom;
	private SharedPreferences sharedPreferences;
	private ApplicationVariable av;

	public static MenuFragment newInstance(String tableID, String orderID) {
		Bundle args = new Bundle();
		args.putString(Constants.ARGS_ORDER_TABLE_ID, tableID);
		args.putString(Constants.ARGS_ORDER_ID, orderID);
		MenuFragment fragment = new MenuFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i("gxy", "---------menu in create------------");
		av = (ApplicationVariable) getActivity().getApplication();
		sharedPreferences = getActivity().getSharedPreferences(
				Constants.SP_FILE_NAME, Context.MODE_PRIVATE);

		menuMap = MenuLab.get(getActivity()).getMenuClassItem();
		classNames = getResources().getStringArray(R.array.menuClass);
		orderID = getArguments().get(Constants.ARGS_ORDER_ID).toString();

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		Log.i("gxy", "---------menu in visible------------");
		super.setUserVisibleHint(isVisibleToUser);// 每次切换fragment时调用的方法
		if (isVisibleToUser) {
			menu_tv_total_num.setText("￥" + av.getOrderTotal() + ".00");
		}
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		Log.i("gxy", "---------menu in getView------------");
		return super.getView();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		Log.i("gxy", "---------menu in onAttach------------");
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("gxy", "---------menu in onDestroy------------");
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		Log.i("gxy", "---------menu in onDestroyView------------");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		Log.i("gxy", "---------menu in onDetach------------");
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.i("gxy", "---------menu in onPause------------");
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.i("gxy", "---------menu in onResume------------");
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Log.i("gxy", "---------menu in onStart------------");
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.i("gxy", "---------menu in onStop------------");
		super.onStop();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.i("gxy", "---------menu in createView------------");
		View v = inflater.inflate(R.layout.fragment_menu, container, false);
		menu_hs = (ScrollView) v.findViewById(R.id.menu_hs);
		menu_rg = (RadioGroup) v.findViewById(R.id.menu_rg_class);

		for (int i = 0; i < classNames.length; i++) {
			RadioButton rb = (RadioButton) (menu_rg.getChildAt(i));
			if (i < classNames.length) {
				rb.setText(classNames[i]);
			} else {
				rb.setVisibility(View.GONE);
			}
		}

		menu_vp = (ViewPager) v.findViewById(R.id.menu_vp);
		menu_rg.setOnCheckedChangeListener(this);

		initMenuViewsInViewPager();

		menu_rg.getChildAt(0).setSelected(true);
		menu_vp.setCurrentItem(0);
		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();

		// 点单功能-初始化
		menu_btn_order = (Button) v.findViewById(R.id.menu_btn_order);// 点单按钮
		menu_btn_lan = (Button) v.findViewById(R.id.menu_btn_lan);
		sLan = sharedPreferences.getString(Constants.SP_LOCALE, "zh");

		if (!Locale.getDefault().toString().equals(sLan)) {
			PublicUtils.changeLanguage(getActivity(), sLan);
		}

		menu_tv_total_num = (TextView) v.findViewById(R.id.menu_tv_total_num);// 总价格

		menu_btn_order.setOnClickListener(new OnClickListener() {

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
		menu_btn_lan.setOnClickListener(new OnClickListener() {

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

		menu_vp.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return menuMap.size();
			}

			@Override
			public Fragment getItem(int pos) {
				return MenuListFragment.newInstance(pos);
			}
		});

		menu_vp.setOnPageChangeListener(new MenuPagerOnPageChangeListener());
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < group.getChildCount() - 1; i++) {
			RadioButton rb = (RadioButton) (group.getChildAt(i));
			if (checkedId == rb.getId()) {
				menu_vp.setCurrentItem(i);
				break;
			}
		}

		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();//
		// 更新当前蓝色横条距离左边的距离
		menu_hs.smoothScrollTo(0, (int) mCurrentCheckedRadioLeft
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
			((RadioButton) menu_rg.getChildAt(position)).performClick();
		}

	}

	private float getCurrentCheckedRadioLeft() {
		// TODO getCurrentCheckedRadioLeft
		switch (menu_rg.getCheckedRadioButtonId()) {
		case R.id.menu_rb1:
			return getResources().getDimension(R.dimen.rdo);
		case R.id.menu_rb2:
			return getResources().getDimension(R.dimen.rdo2);
		case R.id.menu_rb3:
			return getResources().getDimension(R.dimen.rdo3);
		case R.id.menu_rb4:
			return getResources().getDimension(R.dimen.rdo4);
		case R.id.menu_rb5:
			return getResources().getDimension(R.dimen.rdo5);
		case R.id.menu_rb6:
			return getResources().getDimension(R.dimen.rdo6);
		case R.id.menu_rb7:
			return getResources().getDimension(R.dimen.rdo7);
		case R.id.menu_rb8:
			return getResources().getDimension(R.dimen.rdo8);
		case R.id.menu_rb9:
			return getResources().getDimension(R.dimen.rdo9);
		case R.id.menu_rb10:
			return getResources().getDimension(R.dimen.rdo10);
		case R.id.menu_rb11:
			return getResources().getDimension(R.dimen.rdo11);
		case R.id.menu_rb12:
			return getResources().getDimension(R.dimen.rdo12);
		case R.id.menu_rb13:
			return getResources().getDimension(R.dimen.rdo13);
			// case R.id.menu_rb14:
			// return getResources().getDimension(R.dimen.rdo14);
			// case R.id.menu_rb15:
			// return getResources().getDimension(R.dimen.rdo15);
			// case R.id.menu_rb16:
			// return getResources().getDimension(R.dimen.rdo16);
			// case R.id.menu_rb17:
			// return getResources().getDimension(R.dimen.rdo17);
			// case R.id.menu_rb18:
			// return getResources().getDimension(R.dimen.rdo18);
			// case R.id.menu_rb19:
			// return getResources().getDimension(R.dimen.rdo19);
			// case R.id.menu_rb20:
			// return getResources().getDimension(R.dimen.rdo20);
			// case R.id.menu_rb21:
			// return getResources().getDimension(R.dimen.rdo21);
		default:
			return 0f;
		}

	}

	public void updateOrderInfo() {
		// 图标
		if (av.getCHOOSE() != null && av.getCHOOSE().size() > 0) {
			menu_btn_order.setEnabled(true);
		} else {
			menu_btn_order.setEnabled(false);
		}
		// 订单信息更新
		menu_tv_total_num.setText("￥" + av.getOrderTotal() + ".00");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		getActivity().setResult(resultCode, data);
		getActivity().finish();
		super.onActivityResult(requestCode, resultCode, data);
	}

}
