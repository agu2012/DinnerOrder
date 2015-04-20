package com.bocs.dodemo.activity;

import com.bocs.dodemo.fragment.SetFragment;

import android.support.v4.app.Fragment;

public class SetActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		return new SetFragment();
	}

}
