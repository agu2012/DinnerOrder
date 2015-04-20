package com.bocs.dodemo.util;

import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.Order;
import com.bocs.dodemo.print.PrintOrderList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

@SuppressLint("HandlerLeak")
public class DialogUtiles {
	private String[] listStr1;
	private String[] listStr2;

	private int checkedNum;
	private String title;
	private String message;
	private int index;
	private Activity activity;
	private ProgressDialog progressDialog;
	private PrintOrderList pol = null;// 打印
	int returnInt;

	public DialogUtiles(Activity a, String s, String[] t1, String[] t2, int num) {
		listStr1 = t1;
		listStr2 = t2;
		checkedNum = num;
		title = s;
		activity = a;
	}

	public DialogUtiles(Activity _activity, String _title, String msg) {
		title = _title;
		message = msg;
		activity = _activity;
	}

	public int getIndex() {
		return index;
	}

	public boolean ShowBlueTooth() {
		// TODO 初始化界面-蓝牙选择对话框
		/**
		 * 修改蓝牙默认选中无效问题
		 */
		index = checkedNum;

		new Thread() {
			@SuppressWarnings("unused")
			AlertDialog a = new AlertDialog.Builder(activity)
					.setTitle(title)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(listStr1, checkedNum,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									index = which;
								}
							})
					.setNegativeButton(R.string.confirm,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (listStr2.length == 0) {
										return;
									}

									SharedPreferences sp = activity
											.getSharedPreferences(
													Constants.SP_FILE_NAME, 0);
									sp.edit()
											.putString(
													Constants.PRINTER_ADDRESS,
													listStr2[index]).commit();

									progressDialog = ProgressDialog.show(
											activity,
											activity.getString(R.string.dialog_title),
											activity.getString(R.string.dialog_bluetooth_setting));
									dialog.dismiss();
									// 新建线程
									new Thread() {

										@Override
										public void run() {
											Message message = new Message();
											message.what = 0;
											handler.sendMessage(message);
										}
									}.start();
									return;
								}
							}).setPositiveButton(R.string.cancel, null).show();

		}.start();

		return true;

	}

	/**
	 * 位置：主界面 功能：跳转积分功能面板
	 * 
	 * @return
	 */

	/**
	 * 用Handler来更新UI
	 */
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message message) {
			// 关闭ProgressDialog
			progressDialog.dismiss();
			ShowToast st = new ShowToast();
			switch (message.what) {
			case 0:
				st.showToastCENTERAndSHORT(activity,
						activity.getString(R.string.prompt_bluetooth_set_ok));
				break;

			default:
				break;
			}

		}
	};

	/*
	 * 打印机成功打印一联，然后再打印另一联
	 */
	public void showPrintDialog(final Order order, final boolean isFinalPrint) {
		// 第一联打印成功--弹出对话框，再打一联
		new AlertDialog.Builder(activity)
				.setTitle(R.string.sc_sucess)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(R.string.prompt_print_again)
				.setNegativeButton(R.string.confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 打印完再退出
								pol = new PrintOrderList(activity);
								if (!isFinalPrint) {
									returnInt = pol.printOrderedList(activity,
											order);
								} else {
									returnInt = pol.printOrder(activity, order);
								}

								new AlertDialog.Builder(activity)
										.setTitle(R.string.sc_sucess)
										.setIcon(
												android.R.drawable.ic_dialog_alert)
										.setMessage(R.string.prompt_print_over)
										.setNegativeButton(
												R.string.confirm,
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														if (isFinalPrint) {
															DinnerOrderManager dom = new DinnerOrderManagerImpl(
																	activity);
															dom.removeAllInfoByTabeleID(order
																	.getTableNo());
															activity.setResult(Activity.RESULT_OK);
															activity.finish();
														}
														dialog.dismiss();
													}
												}).show();

							}
						})
				.setPositiveButton(R.string.dCancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog0,
									int which) {
							}
						}).show();
	}

	public void showAlertDialog() {
		new AlertDialog.Builder(activity)
				.setTitle(R.string.sc_sucess)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(title)
				.setNegativeButton(R.string.confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
	}
}
