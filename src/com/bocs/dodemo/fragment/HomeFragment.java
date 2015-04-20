package com.bocs.dodemo.fragment;

import java.util.List;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.activity.SetActivity;
import com.bocs.dodemo.activity.TableOrderActivity;
import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.Employee;
import com.bocs.dodemo.util.ShowToast;

public class HomeFragment extends Fragment {

	private TextView textView2, textView11;
	private Button home_btn_order;
	private Button home_btn_order_manage;
	private Button home_btn_set;

	private static final String TAG = "gxy";

	private String desc = "Depuis 1845, le restaurant Frostmourne est"
			+ " heureux de vous accueillir et de vous proposer une"
			+ " cuisine traditionnelle typiquement française.\n\n"
			+ "Cette institution du quartier latin, située à 2 pas du"
			+ " théâtre de l'Odéon, a su garder son décor authentique,"
			+ " qui a vu passer les générations.\n\n"
			+ "Des lieux secrets, des lieux sacrés. Il y a aussi des"
			+ " lieux où, dit-on, souffle l'esprit. Mais attention ! "
			+ "Il y a diverses formes d'esprit. L'esprit de sel"
			+ " s'accommode mal aux aliments. Le sel de l'esprit au"
			+ " contraire assaisonne bien un repas. Enfin, le souffle"
			+ " de l'esprit s'exerce de maintes manières. Par exemple,"
			+ " il lui arrive de souffler sur la soupe pour la refroidir"
			+ " ou de trancher un faux-filet ou d'ajouter du poivre au ragoût.";

	ShowToast st = new ShowToast();
	private DinnerOrderManager dom;
	private List<Employee> employees;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dom = new DinnerOrderManagerImpl(getActivity());
		Log.i(TAG, "=========this is onCreate in Fragment===============");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "=========this is onResume in Fragment===============");
		changeButtonState();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_home, container, false);

		textView2 = (TextView) v.findViewById(R.id.textView2);
		textView2.setText(desc);
		Typeface type_desc = Typeface.createFromAsset(
				getActivity().getAssets(), "font/VLADIMIR.TTF");
//		Typeface type_name = Typeface.createFromAsset(
//				getActivity().getAssets(), "font/CURLZ.TTF");
		textView11 = (TextView) v.findViewById(R.id.textView11);
		textView11.setTypeface(type_desc);

		View.OnClickListener click = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				switch (v.getId()) {
				case R.id.home_btn_order:
					intent.setClass(getActivity(), TableOrderActivity.class);
					intent.putExtra(Constants.ARGS_WHERE,
							Constants.INTENT_ORDER);
					break;
				case R.id.home_btn_order_manage:
					intent.setClass(getActivity(), TableOrderActivity.class);
					intent.putExtra(Constants.ARGS_WHERE,
							Constants.INTENT_ORDER_MANAGEMENT);
					break;
				case R.id.home_btn_set:
					intent.setClass(getActivity(), SetActivity.class);
					break;
				default:
					break;
				}
				startActivity(intent);
			}
		};

		home_btn_order = (Button) v.findViewById(R.id.home_btn_order);
		home_btn_order_manage = (Button) v
				.findViewById(R.id.home_btn_order_manage);
		home_btn_set = (Button) v.findViewById(R.id.home_btn_set);

		home_btn_order.setOnClickListener(click);
		home_btn_order_manage.setOnClickListener(click);
		home_btn_set.setOnClickListener(click);

		changeButtonState();

		return v;
	}

	private void changeButtonState() {
		employees = dom.queryEmployees();
		// dom.printAllOrderList();
		if (employees != null && employees.size() > 0) {
			home_btn_order.setEnabled(true);
			home_btn_order_manage.setEnabled(true);
		} else {
			st.showToastCENTERAndLONG(getActivity(), "请先添加员工，谢谢！");
			home_btn_order.setEnabled(false);
			home_btn_order_manage.setEnabled(false);
		}
	}

}
