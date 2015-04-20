package com.bocs.dodemo.print;

import com.bocs.dodemo.util.WriteLog;

import android.graphics.Bitmap;

public class PrinterCommunicationImpl implements PrinterCommunication {
	private PrinterCommunication printerDriver;

	// 无参构造
	public PrinterCommunicationImpl() {
		printerDriver = new JQPrinterFirm();
		WriteLog.WriteLogCatInfo("---JQPrinterFirm---");

	}
	

	@Override
	public void PrinterConnect(String sPrinterAddr) {
		// TODO Auto-generated method stub
		printerDriver.PrinterConnect(sPrinterAddr);
	}

	@Override
	public void PrinterWrite() {
		// TODO Auto-generated method stub
		printerDriver.PrinterWrite();
	}

	@Override
	public void PrinterDisConnect() {
		// TODO Auto-generated method stub
		printerDriver.PrinterDisConnect();
	}

	@Override
	public boolean PrinterConnectionStatus() {
		// TODO Auto-generated method stub
		return printerDriver.PrinterConnectionStatus();
	}

	@Override
	public void PrinterInit() {
		// TODO Auto-generated method stub
		printerDriver.PrinterInit();
	}

	@Override
	public void PrinterText(String sPrinterStr) {
		// TODO Auto-generated method stub
		printerDriver.PrinterText(sPrinterStr);
	}

	@Override
	public void PrinterWrap() {
		// TODO Auto-generated method stub
		printerDriver.PrinterWrap();
	}

	@Override
	public void PrinterBitmap(Bitmap bmp, int PaperWidth, int nGrayThreshold) {
		// TODO Auto-generated method stub
		printerDriver.PrinterBitmap(bmp, PaperWidth, nGrayThreshold);
	}

	@Override
	public void PrinterFontSize(int iFontSize) {
		// TODO Auto-generated method stub
		printerDriver.PrinterFontSize(iFontSize);
	}

	@Override
	public void PrinterFont(int iFont) {
		// TODO Auto-generated method stub
		printerDriver.PrinterFont(iFont);
	}

	@Override
	public void PrinterAlignment(int iAlign) {
		// TODO Auto-generated method stub
		printerDriver.PrinterAlignment(iAlign);
	}

}
