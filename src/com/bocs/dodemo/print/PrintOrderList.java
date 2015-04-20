package com.bocs.dodemo.print;

import java.text.SimpleDateFormat;

import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.commons.Constants;
import com.bocs.dodemo.entity.Order;
import com.bocs.dodemo.entity.OrderItem;
import com.bocs.dodemo.util.StringUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PrintOrderList {

	PrinterCommunication pc = new PrinterCommunicationImpl();
	ProgressDialog progressDialog;
	Activity activity = new Activity();
	Resources r;
	SharedPreferences sp;

	public PrintOrderList(Activity activity) {
		this.activity = activity;
		r = activity.getResources();
		sp = activity.getSharedPreferences(Constants.SP_FILE_NAME, 0);
	}

	/**
	 * 打印签购单
	 * 
	 * @param transInfo
	 * @return 0--成功；1--蓝牙连接失败
	 */
	public int printOrderedList(Activity activity, Order order) {

		pc.PrinterConnect(sp.getString(Constants.PRINTER_ADDRESS, ""));
		if (!pc.PrinterConnectionStatus()) {
			return 2;
		} else {
			/**
			 * 连接蓝牙打印机
			 */
			pc.PrinterInit();

			// 打印logo
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 1;
			Bitmap bmp = BitmapFactory.decodeResource(r, R.drawable.print_logo,
					opts);
			for (int i = 0; i < 2; i++) {
				pc.PrinterBitmap(bmp, 204, 225);
				pc.PrinterWrap();
				// 信息
				pc.PrinterAlignment(Constants.ALIGN_CENTER);
				pc.PrinterText("结账单 RECKONING\n");
				pc.PrinterText(Constants.ES_SPLIT);

				pc.PrinterAlignment(Constants.ALIGN_LEFT);

				pc.PrinterText("订单号：" + order.getOrderID() + "\n");
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:MM:ss");
				pc.PrinterText("桌台号：" + order.getTableNo() + " 人数："
						+ order.getCnt() + "\n");
				pc.PrinterText("开单日期：" + df.format(order.getOrderDate()) + "\n");
				pc.PrinterText("操作员：" + order.getWaiter() + "\n");
				pc.PrinterText(Constants.ES_SPLIT);

				pc.PrinterText(String.format("%-14s", "消费项目")
						+ String.format("%-4s", "数量")
						+ String.format("%6s", "单价") + "\n");
				for (OrderItem oi : order.getItems()) {
					String name = oi.getMenuItem().getItemName();
					pc.PrinterText((name + "                    ").substring(0,
							21 - name.length())
							+ (oi.getItemCnt() + "   ").substring(0, 4)
							+ (oi.getMenuItem().getItemPrice() + ".00" + "     ")
									.substring(0, 6));
					pc.PrinterText("\n");
				}

				pc.PrinterWrap();

				pc.PrinterAlignment(Constants.ALIGN_RIGHT);
				pc.PrinterText("总计：" + order.getTotalAmount() + ".00" + "\n");

				pc.PrinterText(Constants.ES_SPLIT);
				pc.PrinterText("另加收服务费10%\n");
				pc.PrinterAlignment(Constants.ALIGN_LEFT);

				pc.PrinterWrap();
				pc.PrinterWrap();
				pc.PrinterWrap();
			}
			pc.PrinterWrite();
			// 如果是蓝牙打印机，就try,不然打印不完
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {

			}

			pc.PrinterDisConnect();
			return 0; // Success
		}

	}

	public int printOrder(Activity activity, Order order) {
		String addr = sp.getString(Constants.PRINTER_ADDRESS, "");
		pc.PrinterConnect(addr);
		if (!pc.PrinterConnectionStatus()) {
			return 2;
		} else {
			/**
			 * 连接蓝牙打印机
			 */
			pc.PrinterInit();
			// 打印logo
			// BitmapFactory.Options opts = new BitmapFactory.Options();
			// opts.inSampleSize = 3;// 将图片设为原来宽高的1/2，防止内存溢出
			Bitmap bmp = BitmapFactory.decodeResource(r, R.drawable.print_logo,
					null);
			for (int i = 0; i < 2; i++) {
				pc.PrinterBitmap(bmp, 300, 225);
				pc.PrinterWrap();

				// 信息
				pc.PrinterAlignment(Constants.ALIGN_CENTER);
				pc.PrinterText("结账单 RECKONING\n");
				pc.PrinterText(Constants.ES_SPLIT);

				pc.PrinterAlignment(Constants.ALIGN_LEFT);
				pc.PrinterText("账单号：" + order.getOrderMainNo() + "\n");

				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:MM:ss");
				pc.PrinterText("桌台号：" + order.getTableNo() + " 人数："
						+ order.getCnt() + "\n");
				pc.PrinterText("开单日期：" + df.format(order.getOrderDate()) + "\n");
				pc.PrinterText("操作员：" + order.getWaiter() + "\n");
				pc.PrinterText("结算方式：" + (order.getSettlementType()==1?"现金":"mops") + "\n");
				pc.PrinterText(Constants.ES_SPLIT);

				pc.PrinterText(String.format("%-14s", "消费项目")
						+ String.format("%-4s", "数量")
						+ String.format("%6s", "单价") + "\n");
				for (OrderItem oi : order.getItems()) {
					String name = oi.getMenuItem().getItemName();
					pc.PrinterText((name + "                    ").substring(0,
							21 - name.length())
							+ (oi.getItemCnt() + "   ").substring(0, 4)
							+ (oi.getMenuItem().getItemPrice() + ".00" + "     ")
									.substring(0, 6));
					pc.PrinterText("\n");
				}

				pc.PrinterWrap();
				pc.PrinterWrap();
				pc.PrinterText(Constants.ES_SPLIT);
				pc.PrinterText("消费总计：￥" + order.getTotalAmount() + ".00" + "\n");
				pc.PrinterText("服务费(service charge)：￥"
						+ StringUtils.getAmount(order.getTotalAmount() * 10
								+ "") + "\n");
				long total = order.getTotalAmount() * 110;
				if (order.getDiscount() != 0) {
					pc.PrinterText("折扣(discount)：￥"
							+ StringUtils.getAmount(order.getDiscount() + "")
							+ "\n");
					total = total - order.getDiscount();
				}

				pc.PrinterWrap();
				pc.PrinterText("总金额：￥" + StringUtils.getAmount(total + "")
						+ "\n");

				pc.PrinterWrap();
				pc.PrinterWrap();
				pc.PrinterWrap();
				pc.PrinterWrap();

				pc.PrinterAlignment(Constants.ALIGN_RIGHT);
				pc.PrinterText("客人签字____________\n");

				pc.PrinterText(Constants.ES_SPLIT);
				pc.PrinterText("另加收服务费10%\n");
				pc.PrinterWrap();
				pc.PrinterWrap();
				pc.PrinterWrap();
				pc.PrinterAlignment(Constants.ALIGN_LEFT);
			}
			pc.PrinterWrite();
			// 如果是蓝牙打印机，就try,不然打印不完
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			pc.PrinterDisConnect();
			return 0; 
		}

	}
}
