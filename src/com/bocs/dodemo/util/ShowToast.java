package com.bocs.dodemo.util;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowToast {
	public void showToastCENTERAndSHORT(Context context, String showMessage) {
		Toast toast = Toast.makeText(context.getApplicationContext(), showMessage, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, toast.getXOffset()/2, toast.getYOffset()/2);
        toast.show();
	}
	

	public void showToastCENTERAndSHORT(Context context, String showMessage,Activity activity) {
		Toast toast = Toast.makeText(context.getApplicationContext(), showMessage, Toast.LENGTH_SHORT);

		TextView tv = new TextView(activity); 
        tv.setText(showMessage); 
        tv.setTextSize(20);
        View toastView = toast.getView(); 
        LinearLayout linearLayout = new LinearLayout(activity); 
        linearLayout.setOrientation(LinearLayout.HORIZONTAL); 
        linearLayout.addView(tv); 
        linearLayout.addView(toastView); 
        toast.setView(linearLayout); 
		toast.setGravity(Gravity.CENTER, toast.getXOffset()/2, toast.getYOffset()/2);
        toast.show();
	}
	
	public void showToastTOPAndSHORT(Context context, String showMessage) {
		Toast toast = Toast.makeText(context.getApplicationContext(), showMessage, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, toast.getXOffset()/2, toast.getYOffset()/2);
        toast.show();
	}
	
	public void showToastBOTTOMAndSHORT(Context context, String showMessage) {
		Toast toast = Toast.makeText(context.getApplicationContext(), showMessage, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, toast.getXOffset()/2, toast.getYOffset()/2);
        toast.show();
	}
	
	public void showToastCENTERAndLONG(Context context, String showMessage) {
		Toast toast = Toast.makeText(context.getApplicationContext(), showMessage, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, toast.getXOffset()/2, toast.getYOffset()/2);
        toast.show();
	}
	
	public void showToastTOPAndLONG(Context context, String showMessage) {
		Toast toast = Toast.makeText(context.getApplicationContext(), showMessage, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP, toast.getXOffset()/2, toast.getYOffset()/2);
        toast.show();
	}
	
	public void showToastBOTTOMAndLONG(Context context, String showMessage) {
		Toast toast = Toast.makeText(context.getApplicationContext(), showMessage, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM, toast.getXOffset()/2, toast.getYOffset()/2);
        toast.show();
	}
}
