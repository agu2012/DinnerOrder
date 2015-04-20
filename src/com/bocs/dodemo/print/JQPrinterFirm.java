package com.bocs.dodemo.print;

import com.bocs.dodemo.commons.Constants;
import com.zh.jqtek.JQ_BT_CTL;
import com.zh.jqtek.JQ_ESC_CMD;
import com.zh.jqtek.JQ_ESC_METHOD;
import com.zh.jqtek.JQ_TYPE.FONT_ID;
import com.zh.jqtek.JQ_TYPE.MODE_ALIGN;
import com.zh.jqtek.JQ_TYPE.MODE_ENLARGE;

import android.graphics.Bitmap;

public class JQPrinterFirm implements PrinterCommunication{
	private JQ_BT_CTL mBtManagement = new JQ_BT_CTL(); // 控制类
	JQ_ESC_CMD EscCmd = new JQ_ESC_CMD(); // 打印类
	
	@Override
	public void PrinterConnect(String sPrinterAddr) {
		// TODO Auto-generated method stub
		mBtManagement.bt_connect(sPrinterAddr);
	}

	@Override
	public void PrinterWrite() {
		// TODO Auto-generated method stub
		mBtManagement.bt_write(EscCmd.esc_data_get(),
				EscCmd.esc_length_get());
	}

	@Override
	public void PrinterDisConnect() {
		// TODO Auto-generated method stub
		mBtManagement.bt_disconnect();
	}

	@Override
	public boolean PrinterConnectionStatus() {
		// TODO Auto-generated method stub
		return JQ_BT_CTL.BT_STATUS_CONNECTED == mBtManagement.bt_status_get();
	}

	@Override
	public void PrinterInit() {
		// TODO Auto-generated method stub
		EscCmd.esc_init_printer();
	}

	@Override
	public void PrinterText(String sPrinterStr) {
		// TODO Auto-generated method stub
		EscCmd.esc_text_print(sPrinterStr);
	}

	@Override
	public void PrinterWrap() {
		// TODO Auto-generated method stub
		EscCmd.esc_enter();
	}

	@Override
	public void PrinterBitmap(Bitmap bmp, int PaperWidth, int nGrayThreshold) {
		// TODO Auto-generated method stub
		JQ_ESC_METHOD.draw_bitmap(EscCmd, bmp, PaperWidth, nGrayThreshold);
	}

	@Override
	public void PrinterFontSize(int iFontSize) {
		// TODO Auto-generated method stub
		if (Constants.FONT_HEIGHT_DOUBLE == iFontSize)
			EscCmd.esc_char_enlarge(MODE_ENLARGE.HEIGHT_DOUBLE);
		else if (Constants.FONT_NORMAL == iFontSize)
			EscCmd.esc_char_enlarge(MODE_ENLARGE.NORMAL);
	}

	@Override
	public void PrinterFont(int iFont) {
		// TODO Auto-generated method stub
		if (Constants.FONT_GBK_16x16 == iFont)
			EscCmd.esc_char_font(FONT_ID.FONT_GBK_16x16);
		else if (Constants.FONT_GBK_24x24 == iFont)
			EscCmd.esc_char_font(FONT_ID.FONT_GBK_24x24);
		else if (Constants.FONT_ASCII_8x16 == iFont)
			EscCmd.esc_char_font(FONT_ID.FONT_ASCII_8x16);
		else if (Constants.FONT_ASCII_12x24 == iFont)
			EscCmd.esc_char_font(FONT_ID.FONT_ASCII_12x24);
	}

	@Override
	public void PrinterAlignment(int iAlign) {
		// TODO Auto-generated method stub
		if (Constants.ALIGN_CENTER == iAlign)
			EscCmd.esc_align_set(MODE_ALIGN.ALIGN_CENTER);
		else if (Constants.ALIGN_LEFT == iAlign)
			EscCmd.esc_align_set(MODE_ALIGN.ALIGN_LEFT);
		else if (Constants.ALIGN_RIGHT == iAlign)
			EscCmd.esc_align_set(MODE_ALIGN.ALIGN_RIGHT);
	}



}
