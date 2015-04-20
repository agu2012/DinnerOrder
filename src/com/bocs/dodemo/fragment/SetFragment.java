package com.bocs.dodemo.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bocs.dodemo.activity.EmployeeListActivity;
import com.bocs.dodemo.activity.ModifyPwdActivity;
import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.application.ExitApplication;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.util.BluetoothUtils;
import com.bocs.dodemo.util.ShowToast;

public class SetFragment extends Fragment {
	TextView tv_title;
	RelativeLayout set_rl_modify_pwd, set_rl_employee, set_rl_exit,
			set_rl_print;
	EditText set_et_init_orderno;
	SharedPreferences sp;
	ShowToast st = new ShowToast();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_set, container, false);

		v.findViewById(R.id.btn_next).setVisibility(View.GONE);

		tv_title = (TextView) v.findViewById(R.id.tv_title);
		tv_title.setText("设置");

		View.OnClickListener click = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.set_rl_print:
					onClickConnectPrinter();
					break;
				case R.id.set_rl_modify_pwd:
					Intent intent = new Intent();
					intent.setClass(getActivity(), ModifyPwdActivity.class);
					startActivity(intent);
					break;
				case R.id.set_rl_employee:
					Intent intent2 = new Intent();
					intent2.setClass(getActivity(), EmployeeListActivity.class);
					startActivity(intent2);
					break;
				case R.id.set_rl_exit:
					onClickExit();
					break;
				default:
					break;
				}
			}
		};

		set_rl_modify_pwd = (RelativeLayout) v
				.findViewById(R.id.set_rl_modify_pwd);
		set_rl_employee = (RelativeLayout) v.findViewById(R.id.set_rl_employee);
		set_rl_exit = (RelativeLayout) v.findViewById(R.id.set_rl_exit);
		set_rl_print = (RelativeLayout) v.findViewById(R.id.set_rl_print);

		set_rl_modify_pwd.setOnClickListener(click);
		set_rl_employee.setOnClickListener(click);
		set_rl_exit.setOnClickListener(click);
		set_rl_print.setOnClickListener(click);

		set_et_init_orderno = (EditText) v
				.findViewById(R.id.set_et_init_orderno);
		set_et_init_orderno
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == KeyEvent.ACTION_DOWN
								|| actionId == EditorInfo.IME_ACTION_DONE) {

							if (TextUtils.isEmpty(v.getText())) {
								st.showToastCENTERAndLONG(getActivity(),
										"值不能为空！");
								return false;
							}
							if (v.getText().length() < 7) {
								st.showToastCENTERAndLONG(getActivity(),
										"有效长度为7！");
								return false;
							}
							sp = getActivity().getSharedPreferences(
									Constants.SP_FILE_NAME, 0);
							sp.edit()
									.putInt(Constants.SP_ITEM_ORDER_MAIN_NO,
											Integer.valueOf(v.getText()
													.toString())).commit();
						}
						return true;
					}
				});
		return v;
	}

	public void onClickExit() {
		new AlertDialog.Builder(getActivity())
				.setTitle(getString(R.string.dTitle1))
				.setMessage(getString(R.string.mMessage))
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(getString(R.string.mExit),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
								ExitApplication.exitApplication();

							}
						}).setNegativeButton(getString(R.string.dCancel), null)
				.show();
	}

	private void onClickConnectPrinter() {
		// 蓝牙打印机设置
		if (BluetoothUtils.BluetoothEnableChecking(getActivity())) {
			BluetoothUtils.bluetoothSetting(getActivity());
		} else {
			st.showToastCENTERAndLONG(getActivity(),
					getString(R.string.prompt_open_bluetooth_pls));
		}
	}
}
