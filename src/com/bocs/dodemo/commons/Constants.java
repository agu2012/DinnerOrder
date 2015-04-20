package com.bocs.dodemo.commons;

public class Constants {

	// 分隔符
	public static final String DATA_SPLIT_F = "|";
	public static final String DATA_SPLIT_S = ".";
	public static final String DATA_SPLIT_T = ",";
	public static final String DATA_SPLIT_ENTER = "\r\n";
	public static final String ES_SPLIT = "--------------------------------\n";

	// SharedPreferences
	public static final String SP_FILE_NAME = "orderDinner";
	public static final String SP_ITEM_ORDER_MAIN_NO = "orderMainNo";
	public static final String SP_LOCALE = "locale";
	public static final String SP_PWD_DISCOUNT = "discount_pwd";

	// args
	public static final String ARGS_ORDER_TABLE_ID = "ORDER_TABLE_ID";
	public static final String ARGS_ORDER_CHOOSE = "ORDER_CHOOSE";
	public static final String ARGS_MENU_CLASS = "MENU_CLASS";
	public static final String ARGS_MENU_CHOOSE = "MENU_CHOOSE";
	public static final String ARGS_ORDER_ID = "ORDER_ID";
	public static final String ARGS_WHERE = "WHERE";
	public static final String ARGS_IS_SETTLED = "CAN_CHANGE";
	public static final String ARGS_IS_DRINK = "IS_DRINK";

	// intent
	public static final String INTENT_ORDER = "ODER";
	public static final String INTENT_ORDER_MANAGEMENT = "MANAGEMENT";

	// bluetooth print firm
	public static final String PRINT_BLUEBOOTH_VMP = "VMP"; // JQ
	public static final String PRINTER_ADDRESS = "PRINTER_ADDRESS";

	// print font
	public static final int FONT_GBK_16x16 = 1;
	public static final int FONT_GBK_24x24 = 2;
	public static final int FONT_ASCII_8x16 = 11;
	public static final int FONT_ASCII_12x24 = 12;
	// print font size
	public static final int FONT_NORMAL = 1;
	public static final int FONT_HEIGHT_DOUBLE = 2;
	// print Alignment
	public static final int ALIGN_CENTER = 1;
	public static final int ALIGN_LEFT = 2;
	public static final int ALIGN_RIGHT = 3;

	/**
	 * debug mode
	 */
	public static final String DEBUG_TAG = "mpos debug";
	public static final boolean DEBUG = false;
}
