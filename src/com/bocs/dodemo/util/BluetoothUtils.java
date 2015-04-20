package com.bocs.dodemo.util;

import java.util.Set;

import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.Order;
import com.bocs.dodemo.print.PrintOrderList;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class BluetoothUtils {
	private static BluetoothAdapter adapter;
	private static SharedPreferences sp;

	// public static BluetoothUtils newInstance(Activity _activity){
	// activity = _activity;
	// sp = activity.getSharedPreferences(
	// Constants.SP_FILE_NAME, 0);
	// BluetoothUtils bu = new BluetoothUtils();
	// return bu;
	// }

	/**
	 * 蓝牙选择
	 * 
	 * @param activity
	 * @param bBluetoothFilter
	 *            是否需要筛选当前唯一记录的蓝牙设备
	 * @param sCheckString
	 *            bBluetoothFilter=FALSE时，根据此参数进行筛选
	 * @param iFirmType
	 *            2--Printer
	 */
	public static void bluetoothSetting(Activity activity) {

		adapter = BluetoothAdapter.getDefaultAdapter();
		// 得到所有已经配对蓝牙设备地址
		Set<BluetoothDevice> devices = adapter.getBondedDevices();
		// adapter.startDiscovery();
		String sCheckString = "VMP";

		int i = 0;
		for (BluetoothDevice device : devices) {
			if (device.getName().startsWith(sCheckString)) {
				i++;
			}
		}
		String[] deviceNameList = null;
		String[] addressList = null;
		deviceNameList = new String[i];
		addressList = new String[i];

		int num = 0;
		String sBluetoothAddr;
		sp = activity.getSharedPreferences(Constants.SP_FILE_NAME, 0);
		sBluetoothAddr = sp.getString(Constants.PRINTER_ADDRESS, "");

		i = 0;
		for (BluetoothDevice device : devices) {
			if (device.getName().startsWith(sCheckString)) {
				deviceNameList[i] = device.getName();
				addressList[i] = device.getAddress();
				if (addressList[i].equals(sBluetoothAddr)) {
					num = i;
				}
				i++;
			}
		}

		DialogUtiles dialog = new DialogUtiles(activity, activity
				.getResources().getString(R.string.prompt_choose_bluetooth),
				deviceNameList, addressList, num);
		dialog.ShowBlueTooth();

	}

	public static boolean BluetoothEnableChecking(Activity activity) {
		adapter = BluetoothAdapter.getDefaultAdapter();
		if (!adapter.isEnabled()) {// 判断蓝牙是否打开和打开蓝牙
			return false;
		}
		return true;
	}

	/**
	 * 打印2种单据，返回打印结果:0-成功
	 * 
	 * @param activity
	 * @param order
	 *            -订单
	 * @param isFinalPrint
	 *            -打印订单的种类（false-普通点菜单据；true-总单据）
	 */
	public static int checkPrintSlip(Activity activity, Order order,
			boolean isFinalPrint) {
		int result = -1;
		// 判断打印设备时候预先设置
		sp = activity.getSharedPreferences(Constants.SP_FILE_NAME, 0);
		if (StringUtils.isEmpty(sp.getString(Constants.PRINTER_ADDRESS, ""))) {// 没有设置打印
			// @提示错误message对话框
			DialogUtiles du = new DialogUtiles(activity, "打印失败，请检查打印机的设置或状态",
					"");
			du.showAlertDialog();
		} else {// 设置了打印

			result = printList(activity, order, isFinalPrint);

			if (result == 0) {
				if (isFinalPrint) {// 打最终单成功后需提示是否重新打印
					showPrintDialog(activity, order);
				} else {
					// 打印点单需关闭当前页面
					activity.setResult(Activity.RESULT_OK);
					activity.finish();
				}

			} else if (result == 2) {// 如果打印机没有开启，return 2
				// @提示错误message对话框
				DialogUtiles du = new DialogUtiles(activity,
						"打印失败，请检查打印机的设置或状态", "");
				du.showAlertDialog();
			}
		}

		return result;
	}

	private static int printList(Activity activity, Order order,
			boolean isFinalPrint) {
		PrintOrderList pol = new PrintOrderList(activity);
		if (!isFinalPrint) {

			return pol.printOrderedList(activity, order);
		} else {
			return pol.printOrder(activity, order);
		}
	}

	public static void showPrintDialog(final Activity activity,
			final Order order) {
		// 第一联打印成功--弹出对话框，再打一联
		new AlertDialog.Builder(activity)
				.setTitle(R.string.sc_sucess)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage("交易成功，是否重新打印结账单？")
				.setNegativeButton(R.string.confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								checkPrintSlip(activity, order, true);
							}
						})
				.setPositiveButton(R.string.dCancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog0,
									int which) {
								// 放弃重新打印则清除该笔交易，返回主界面
								DinnerOrderManager dom = new DinnerOrderManagerImpl(
										activity);
								dom.removeAllInfoByTabeleID(order.getTableNo());
								dialog0.dismiss();
								activity.setResult(Activity.RESULT_OK);
								activity.finish();
							}
						}).show();
	}

}
