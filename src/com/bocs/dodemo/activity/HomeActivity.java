package com.bocs.dodemo.activity;

import com.bocs.dodemo.fragment.HomeFragment;

import android.support.v4.app.Fragment;

public class HomeActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		return new HomeFragment();
	}

}
