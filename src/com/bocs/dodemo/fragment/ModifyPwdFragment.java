package com.bocs.dodemo.fragment;

import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.util.ShowToast;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ModifyPwdFragment extends Fragment {
	private EditText pwd_et;
	private Button pwd_btn;

	private ShowToast st = new ShowToast();
	private SharedPreferences sharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getActivity().getSharedPreferences(
				Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_pwd, container, false);
		pwd_et = (EditText) v.findViewById(R.id.pwd_et);
		pwd_btn = (Button) v.findViewById(R.id.pwd_btn);

		View.OnClickListener savePwdOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.pwd_btn:
					if (TextUtils.isEmpty(pwd_et.getText())) {
						st.showToastCENTERAndLONG(getActivity(), "请输入新密码");
						return;
					}
					sharedPreferences.edit().putString(
							Constants.SP_PWD_DISCOUNT, "");
					st.showToastCENTERAndLONG(getActivity(), "新密码保存成功！");
					pwd_et.setText("");
					break;
				}

			}
		};

		pwd_btn.setOnClickListener(savePwdOnClickListener);
		return v;
	}

}
