package com.bocs.dodemo.activity;

import com.bocs.dodemo.application.ApplicationVariable;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.fragment.TableOrderFragment;
import com.bocs.dodemo.util.ShowToast;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class TableOrderActivity extends SingleFragmentActivity {

	private ShowToast st = new ShowToast();
	private ApplicationVariable av;

	@Override
	public Fragment createFragment() {
		String where = getIntent().getStringExtra(Constants.ARGS_WHERE);
		return TableOrderFragment.newInstance(where);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TableOrderFragment.REQUEST_FROM_TABLE_ORDER) {
			if (resultCode == Activity.RESULT_OK) {
				st.showToastCENTERAndLONG(this,
						getResources().getString(R.string.oMessage2));
			}
		}
		av = (ApplicationVariable) getApplication();
		if(av.isSUCCESS_ORDER()){
			st.showToastCENTERAndLONG(this,
					getResources().getString(R.string.oMessage2));
			av.setSUCCESS_ORDER(false);
		}
		finish();
	}
}
