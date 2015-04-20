package com.bocs.dodemo.print;

import android.graphics.Bitmap;

public interface PrinterCommunication {
	
	/**
	 * 连接蓝牙打印机
	 * @param sPrinterAddr
	 */
	public void PrinterConnect(String sPrinterAddr);
	
	/**
	 * 打印
	 */
	public void PrinterWrite();
	
	/**
	 * 断开打印机连接
	 */
	public void PrinterDisConnect();
	
	/**
	 * 判断打印机连接状态
	 * @return
	 */
	public boolean PrinterConnectionStatus();
	
	/**
	 * 初始化打印机设备
	 */
	public void PrinterInit();
	
	/**
	 * 打印文本内容
	 * @param sPrinterStr
	 */
	public void PrinterText(String sPrinterStr);
	
	/**
	 * 打印换行
	 */
	public void PrinterWrap();
	
	/**
	 * 打印图片内容
	 * @param bmp
	 * @param PaperWidth
	 * @param nGrayThreshold
	 */
	public void PrinterBitmap(Bitmap bmp, int PaperWidth, int nGrayThreshold);
	
	/**
	 * 设置打印字体大小
	 * @param o
	 */
	public void PrinterFontSize(int iFontSize);
	
	/**
	 * 设置打印字体
	 * @param o
	 */
	public void PrinterFont(int iFont);
	
	/**
	 * 对齐方式
	 * @param iAlign
	 */
	public void PrinterAlignment(int iAlign);
}
