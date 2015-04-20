package com.bocs.dodemo.activity;

import com.bocs.dodemo.fragment.ModifyPwdFragment;

import android.support.v4.app.Fragment;

public class ModifyPwdActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		return new ModifyPwdFragment();
	}

}
