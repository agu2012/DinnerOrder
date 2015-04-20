package com.bocs.dodemo.sqlite;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.entity.Employee;
import com.bocs.dodemo.entity.MenuItem;
import com.bocs.dodemo.entity.Order;
import com.bocs.dodemo.entity.OrderItem;

public class DinnerOrderSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "dinner.sqlite";
	private static final int VERSION = 1;

	private static final String TABLE_MENU_ITEM = "menu_item";
	private static final String TABLE_ORDER = "tb_order";
	private static final String TABLE_ORDER_LIST = "order_list";
	private static final String TABLE_EMPLOYEE = "employee";

	// private static final String TABLE_MENU_ITEM = "menu_item";
	private static DinnerOrderSQLiteOpenHelper mInstance = null;

	public synchronized static DinnerOrderSQLiteOpenHelper getInstance(
			Context context) {
		if (mInstance == null) {
			mInstance = new DinnerOrderSQLiteOpenHelper(context);
		}
		return mInstance;
	};

	public DinnerOrderSQLiteOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/**
		 * TODO 建表 1-TABLE_MENU_ITEM:菜品; 2-TABLE_ORDER：订单;
		 * 3-TABLE_ORDER_LIST：点单表;
		 */
		String sql_create_menu_item = "create table "
				+ TABLE_MENU_ITEM
				+ "( _id INTEGER PRIMARY KEY AUTOINCREMENT,item_name VARCHAR"
				+ ",item_name_en VARCHAR,item_desc VARCHAR,item_desc_en VARCHAR,item_unit VARCHAR"
				+ ",item_class INTEGER,item_price INTEGER,item_desc2 VARCHAR,item_desc2_en VARCHAR, item_pic INTEGER)";
		String sql_create_order = "create table "
				+ TABLE_ORDER
				+ "( _id INTEGER PRIMARY KEY AUTOINCREMENT,order_id VARCHAR NOT NULL,table_id VARCHAR,cnt INTEGER"
				+ ",order_date Date,order_main_no VARCHAR,waiter VARCHAR"
				+ ",settlement_type INTEGER,is_settled int,is_submitted int,discount real,total_amount real)";
		String sql_create_order_list = "create table "
				+ TABLE_ORDER_LIST
				+ "( _id INTEGER PRIMARY KEY AUTOINCREMENT,table_id VARCHAR,order_id VARCHAR REFERENCES "
				+ TABLE_ORDER
				+ "(order_id),item_id INTEGER REFERENCES "
				+ TABLE_MENU_ITEM
				+ "(_id),item_cnt INTEGER,item_remark VARCHAR,item_class INTEGER)";
		String sql_create_employee = "create table "
				+ TABLE_EMPLOYEE
				+ "( _id INTEGER PRIMARY KEY AUTOINCREMENT,employee_name VARCHAR)";

		db.execSQL(sql_create_menu_item);
		db.execSQL(sql_create_order);
		db.execSQL(sql_create_order_list);
		db.execSQL(sql_create_employee);
		// _id INTEGER PRIMARY KEY AUTOINCREMENT,item_name VARCHAR"+
		// ",item_name_en VARCHAR,item_desc VARCHAR,item_desc_en VARCHAR,item_unit VARCHAR"
		// + ",item_class INTEGER,item_price INTEGER,item_desc2
		// VARCHAR,item_desc2_en
		// 0 - Autipasti Appetizers
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('蜜瓜火腿','Parma ham with honey melon','份',0,40,"
				+ R.drawable.dish_01 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('烟熏三文鱼','Smoken salmon with lemon and tomato dressing','份',0,48,"
				+ R.drawable.dish_02 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('番茄奶酪配紫苏少司','Tomato and buffalo mozzarella salad','份',0,48,"
				+ R.drawable.dish_03 + ")");
		// 1 - salad
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('凯撒沙拉','Caesar salad','份',1,46,"
				+ R.drawable.dish_04 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('厨师沙拉','Chef'' s salad','份',1,48,"
				+ R.drawable.dish_05 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('泰式辣牛肉沙拉','Spicy thai style beef salad','份',1,48,"
				+ R.drawable.dish + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc,item_desc_en,item_unit,item_class,item_price, item_pic)"
				+ " values('什锦蔬菜沙拉','Tossed mixed salad','千岛汁、油醋汁、法汁','Thousand/&Island、Oil vinegar、Franch dressing','份',1,38,"
				+ R.drawable.dish_07 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc,item_desc_en,item_unit,item_class,item_price, item_pic)"
				+ " values('芒果海鲜沙拉','Mango seafood salad','虾仁、鱼、扇贝、洋葱、番茄、鸡尾汁','shrimps、fish 、scallop 、onion 、tomato with cocktail sauce','份',1,52,"
				+ R.drawable.dish + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc,item_desc_en,item_unit,item_class,item_price, item_pic)"
				+ " values('首都沙拉','Capital salad','土豆、鸡蛋、鸡肉、蛋黄酱','potatoes 、eggs 、chicken 、mayaonalse','份',1,42,"
				+ R.drawable.dish_09 + ")");
		// 2-soup
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('泰国冬阴功汤','Tom yam goong soup','份',2,48,"
				+ R.drawable.dish_10 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('俄式红菜汤','Russian bortsh soup','份',2,36,"
				+ R.drawable.dish_11 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('奶油蘑菇汤','Gream of mushroom soup','份',2,36,"
				+ R.drawable.dish_12 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('匈牙利牛肉汤','Hungary goulash soup','份',2,36,"
				+ R.drawable.dish_13 + ")");
		// 3 - Sandwich & Burger
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc,item_desc_en,item_unit,item_class,item_price, item_pic)"
				+ " values('光明俱乐部三明治','Guang Ming club sandwich','咸肉、鸡肉煎鸡蛋、番茄配菜丝沙拉及炸薯条'"
				+ ",'Triple decker with crispy bacon, grill chicken, fried egg, sticed tomato on toast served with coleslaw and french fries','份',3,60,"
				+ R.drawable.dish_14 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('火腿奶酪三明治','Ham and cheese sandwich','份',3,48,"
				+ R.drawable.dish + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('金枪鱼三明治','Tuna fish sandwich','份',3,48,"
				+ R.drawable.dish + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('传统牛肉汉堡肉','The traditional beef burger','份',3,68,"
				+ R.drawable.dish_17 + ")");
		// 4 - Main courses
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('凯珍扒鸡腿配咸肉烩蘑菇','Cajun chicken leg with mushroom risotto','份',4,52,"
				+ R.drawable.dish + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('咖喱鸡配鸡蛋米饭','Curried chicken with egg rice','份',4,48,"
				+ R.drawable.dish + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('煎鲈鱼配香橙茴香土豆饼','Pan-fried sea bass with orange braised fennel served on a potato cake','份',4,70,"
				+ R.drawable.dish + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('三文鱼扒配香草汁','Pan-fried salmon filled with pesto sauce','份',4,118,"
				+ R.drawable.dish_21 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('炸鱼排配它它汁','Deep-fried cold fish filled','份',4,50,"
				+ R.drawable.dish + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('扒新西兰羊排配炒素菜百里香蒜汁','Frilled lamb chops with potato and thyme galic sauce','份',4,138,"
				+ R.drawable.dish_23 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('扒大虾配柠檬黄油少司','Grilled king prawns with butter lemon sause','份',4,80,"
				+ R.drawable.dish_24 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('米兰煎猪排','Grilled king prawns with butter lemon sause','份',4,68,"
				+ R.drawable.dish_25 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('新西兰牛扒跟奶油松露汁及鹅肝','Grilled tendertoin of new zealand grass fed beef served with creamy truffle sauce','份',4,138,"
				+ R.drawable.dish + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('煎牛扒配炸薯条跟汁或蘑菇汁','Steak and fries beef steak with french fies a choice of black pepper or mushroom sauce','份',4,160,"
				+ R.drawable.dish_25 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('烤笋鸡配炸薯条','Roasted baby chicken with french fries','份',4,48,"
				+ R.drawable.dish_28 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('铁扒杂拌','Mix grill/(chicken, lamb, pork, beef/)','份',4,78,"
				+ R.drawable.dish_29 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('安格斯肉眼扒跟黑胡椒汁或蘑菇汁','Angus ribeye steak with black pepper or mushroom sauce','份',4,180,"
				+ R.drawable.dish_30 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('红烩牛尾','Braised oxtaile','份',4,80,"
				+ R.drawable.dish_31 + ")");
		// 5 - Asian favourites
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('印尼炒饭','Nasi goring','份',5,50," + R.drawable.dish
				+ ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('光明特式炒饭','Guang Ming Cafe'' s fried rice','份',5,50,"
				+ R.drawable.dish_33 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('牛肉炒乌冬面','Stirfried beef with Udon noodles','份',5,48,"
				+ R.drawable.dish + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('云吞汤面','Wonton noodles soup','份',5,48,"
				+ R.drawable.dish_35 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('日式乌冬汤面','Japanese Udon soup','份',5,48,"
				+ R.drawable.dish_35 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('炸春卷','Spring rolls','份',5,38," + R.drawable.dish
				+ ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('法式炸薯条','French fries','份',5,38," + R.drawable.dish
				+ ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('意大利肉酱面','Spaghetti Bologness','份',5,48,"
				+ R.drawable.dish_39 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('培根炒意面','Spaghetti with bacon','份',5,48,"
				+ R.drawable.dish_40 + ")");
		// 6 - Pizza Promotion
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc,item_desc_en,item_unit,item_class,item_price, item_pic)"
				+ " values('蔬菜比萨','Vegetable pizza','蘑菇、洋葱、番茄酱、彩椒、橄榄'"
				+ ",'mushroom、onion、tomato sauce、bell pepper、olives','份',6,60,"
				+ R.drawable.dish_41 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc,item_desc_en,item_unit,item_class,item_price, item_pic)"
				+ " values('海鲜比萨','Seafood pizza','虾仁、鱼、鱿鱼、青口、西兰花、洋葱、青椒'"
				+ ",'shrimps、fish、squid、mussels、brroccoli、onion、pepper','份',6,68,"
				+ R.drawable.dish_41 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc,item_desc_en,item_unit,item_class,item_price, item_pic)"
				+ " values('夏威夷比萨','Hawaii pizza','菠萝、火腿、洋葱、青椒、奶酪'"
				+ ",'pineapple、ham、onion、pepper、cheese','份',6,68,"
				+ R.drawable.dish_41 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('培根蘑菇比萨','Bacon and mushroom pizza','份',6,55,"
				+ R.drawable.dish_41 + ")");
		// 7 - Desserts
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('香蕉船','Banana split','份',7,40," + R.drawable.dish_45
				+ ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('热苹果派配冰淇淋','Warm Apple pie with Ice creram','份',7,46,"
				+ R.drawable.dish + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('光明奶酪蛋糕','Guang Ming cheese cake','份',7,30,"
				+ R.drawable.dish + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('黑森林蛋糕','Black forest','份',7,30,"
				+ R.drawable.dish_48 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('时令果盘（小）','Seasonal fresh fruit platter','份',7,48,"
				+ R.drawable.dish_49 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price, item_pic)"
				+ " values('时令果盘（大）','Seasonal fresh fruit platter','份',7,68,"
				+ R.drawable.dish_49 + ")");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc,item_desc_en,item_unit,item_class,item_price, item_pic)"
				+ " values('各式冰淇淋','Ice cream','香草、草莓、巧克力、栏目、绿茶等'"
				+ ",'Vanilla, stawberry, chocolate, Rum, green tea...','球',7,16,"
				+ R.drawable.dish_50 + ")");
		// TODO---------------------Drinks------------------------
		// 8
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc2,item_desc2_en,item_unit,item_class,item_price)"
				+ " values('百利达斯长相思白葡萄酒','Torreon de paredes sauvignon blanc','瑞格，智利'"
				+ ",'Reng, Chile','瓶Bot',8,388)");

		// 9
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc2,item_desc2_en,item_unit,item_class,item_price)"
				+ " values('百利达斯特级赤霞珠红葡萄酒','Torreon de paredes reserve cabernet sauvignon','瑞格，智利'"
				+ ",'Reng, Chile','瓶Bot',9,468)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc2,item_desc2_en,item_unit,item_class,item_price)"
				+ " values('猴王赤霞珠红葡萄酒','Monkey puzzle classic cabernet sauvignon','隆康米拉谷，智利'"
				+ ",'Loncomilla valley, Chile','瓶Bot',9,288)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc2,item_desc2_en,item_unit,item_class,item_price)"
				+ " values('蒙塞佩城堡红葡萄酒','Chateau Haut Mont Saint Pey Rouge','波尔多AOC'"
				+ ",'AOC Bordeaux','瓶Bot',9,398)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc2,item_desc2_en,item_unit,item_class,item_price)"
				+ " values('兰多妮城堡红葡萄酒','Chateau landonnet','波尔多AOC'"
				+ ",'AOC Bordeaux','瓶Bot',9,498)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc2,item_desc2_en,item_unit,item_class,item_price)"
				+ " values('拉贵奥城堡红葡萄酒','Chateau la grave d''Arzac rouge','格拉芙AOC'"
				+ ",'AOC Graves','瓶Bot',9,538)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc2,item_desc2_en,item_unit,item_class,item_price)"
				+ " values('拉瑞城堡红葡萄酒','Chateau la rame rouge 2010','波尔多AOC'"
				+ ",'AOC Bordeaux','瓶Bot',9,560)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc2,item_desc2_en,item_unit,item_class,item_price)"
				+ " values('澳可迷白标系列—解百纳美乐红葡萄酒','Alkoomi white label cabernet merlot','法兰克兰河区，西澳大利亚'"
				+ ",'Frankland river, western Australia','瓶Bot',9,638)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc2,item_desc2_en,item_unit,item_class,item_price)"
				+ " values('宝歌赤霞珠葡萄酒','Bogle cabernet sauvignon 2008/ 09','克拉斯伯格，加利福尼亚'"
				+ ",'Clarksburg, California','瓶Bot',9,798)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc2,item_desc2_en,item_unit,item_class,item_price)"
				+ " values('德拉斯尼玛赛兰红葡萄酒','Di luccio Domenico Negroamaro Salento Rosso IGT','威尼斯/&萨伦托，意大利'"
				+ ",'Veneto/& Salento, Italy','瓶Bot',9,458)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_desc2,item_desc2_en,item_unit,item_class,item_price)"
				+ " values('高达城堡设拉子红葡萄酒 ','Casa Gualda Shiraz','拉曼恰，西班牙'"
				+ ",'Casa mancha, Spain','瓶Bot',9,418)");
		// 10
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('国产光明特选红葡萄酒','Local House Red Wine','Glass/杯',10,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('国产光明特选红葡萄酒','Local House Red Wine','瓶Bot',10,198)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('进口光明特选红葡萄酒 ','Imported House Red Wine','Glass/杯',10,48)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('进口光明特选红葡萄酒 ','Imported House Red Wine','瓶Bot',10,238)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('国产光明特选白葡萄酒','Local House White Wine','Glass/杯',10,38)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('国产光明特选白葡萄酒','Local House White Wine','瓶Bot',10,198)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('进口光明特选白葡萄酒 ','Imported House White Wine','Glass/杯',10,48)");
		db.execSQL("insert into "
				+ TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('进口光明特选白葡萄酒 ','Imported House White Wine','瓶Bot',10,238)");

		// 11
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('金花XO','Camus X. O','OZ/份',11,128)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('轩尼诗XO','Hennessy X. O','OZ/份',11,128)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('人头马XO','Reny martin X. O','OZ/份',11,128)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('马爹利XO','Martell X. O','OZ/份',11,128)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('蓝带马爹利','Martell Cordon Blue X. O','OZ/份',11,88)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('人头马俱乐部','Reny martin Club','OZ/份',11,88)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('轩尼诗VSOP','Hennessy VSOP','OZ/份',11,68)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('人头马VSOP','Reny martin VSOP','OZ/份',11,68)");

		// 12
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('芝华士12年','Chivas Regal 12 years','OZ/份',12,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('芝华士12年','Chivas Regal 12 years','Bot/瓶',12,880)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('杰克丹尼','Jack Daniel''s','OZ/份',12,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('杰克丹尼','Jack Daniel''s','Bot/瓶',12,880)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('格兰特','Grant''s','OZ/份',12,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('格兰特','Grant''s','Bot/瓶',12,880)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('黑方','Black Label','OZ/份',12,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('黑方','Black Label','Bot/瓶',12,880)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('红方','Red Label','OZ/份',12,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('红方','Red Label','Bot/瓶',12,880)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('百龄坛','Ballantine''s','OZ/份',12,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('百龄坛','Ballantine''s','Bot/瓶',12,880)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('四玫瑰','Four Rose','OZ/份',12,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('四玫瑰','Four Rose','Bot/瓶',12,668)");

		// 13
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('孟买蓝宝石','Bombay','OZ/份',13,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('哥顿金酒','Gordon''s','OZ/份',13,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('必法达','Beefeater','OZ/份',13,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('金汤力','Gin tonic','OZ/份',13,58)");

		// 14
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('喜力','Heineken','Bot/瓶',14,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('科罗娜','Corona','Bot/瓶',14,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('嘉士伯','Garlsberg','Bot/瓶',14,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('朝日','Asahi Dry','Bot/瓶',14,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('青岛','Tsing Tao','Bot/瓶',14,30)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('燕京','Yan Jing','Bot/瓶',14,30)");

		// 15
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('可口可乐','Coca Cola','CAN/听',15,28)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('健怡可乐','Diet Coke','CAN/听',15,28)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('苏打水','Soda Water','CAN/听',15,28)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('汤力水','Tonic Water','CAN/听',15,28)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('干姜水','Ginger Water','CAN/听',15,28)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('雪碧','Sprite','CAN/听',15,28)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('椰汁','Coconut Juice','CAN/听',15,28)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('杏仁露','Almond Milk','CAN/听',15,28)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('橙汁','Orange Juice','CAN/听',15,28)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('菠萝汁','Pineapple Juice','CAN/听',15,28)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('苹果汁','Apple Juice','CAN/听',15,28)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('西柚汁','Grapefruit Juice','CAN/听',15,28)");

		// 16
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('橙子','Orange Juice','Glass/杯',16,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('西瓜','Watermelon Juice','Glass/杯',16,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('哈密瓜','Hami melon Juice','Glass/杯',16,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('苹果','Apple Juice','Glass/杯',16,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('猕猴桃','Kiwi Fruit Juice','Glass/杯',16,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('胡萝卜','Carrot Juice','Glass/杯',16,48)");

		// 17
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('香草奶昔','Venilla Milk Shake','Glass/杯',17,32)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('草莓奶昔','Strawberry Milk Shake','Glass/杯',17,32)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('芒果奶昔','Mango Milk Shake','Glass/杯',17,32)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('香芋奶昔','Taro Milk Shake','Glass/杯',17,32)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('巧克力奶昔','Chocolate Milk Shake','Glass/杯',17,32)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('酸奶','Yoghourt','Glass/杯',17,32)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('牛奶','Milk','Glass/杯',17,28)");

		// 18
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('崂山','Laoshan Water','Bot/瓶',18,25)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('蓝涧','Lorgin Water','Bot/瓶',18,25)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('依云','Evian Water','Bot/瓶',18,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('巴黎水','Paris water','Bot/瓶',18,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('柠檬蜂蜜水','Lemon honey water','Bot/瓶',18,28)");

		// 19
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('红茶','Black tea','OZ/份',19,30)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('红茶','Black tea','POT/壶',19,120)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('绿茶','Green tea','OZ/份',19,30)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('绿茶','Green tea','POT/壶',19,120)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('菊花茶','Chrysanthemum tea','OZ/份',19,30)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('菊花茶','Chrysanthemum tea','POT/壶',19,120)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('茉莉花茶','Jasmine tea','OZ/份',19,30)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('茉莉花茶','Jasmine tea','POT/壶',19,120)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('柠檬茶（冰/热）','Lemon/( Ice // Hot /)','OZ/份',19,30)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('柠檬茶（冰/热）','Lemon/( Ice // Hot /)','POT/壶',19,120)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('乌龙茶','Oolong (tea)','OZ/份',19,35)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('乌龙茶','Oolong (tea)','POT/壶',19,150)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('普洱茶','Pu''er tea','OZ/份',19,35)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('普洱茶','Pu''er tea','POT/壶',19,150)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('铁观音茶','Tieguanyin tea','OZ/份',19,35)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('铁观音茶','Tieguanyin tea','POT/壶',19,150)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('昆山雪菊','Kunshan Chrysanthemum','OZ/份',19,35)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('昆山雪菊','Kunshan Chrysanthemum','POT/壶',19,150)");

		// 20
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('热巧克力','Hot chocolate','OZ/份',20,32)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('现磨咖啡','Fresh Coffee','OZ/份',20,32)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('特浓咖啡','Espresso Coffee','OZ/份',20,32)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('双杯特浓','Double Espresso Coffee','OZ/份',20,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('拿铁','Coffee Latte','OZ/份',20,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('卡布基诺','Cappuccino','OZ/份',20,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('焦糖基诺','Caramel Keno','OZ/份',20,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('摩卡基诺','Mocha Keno','OZ/份',20,48)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('蓝山','Blue Mountain Coffee','OZ/份',20,58)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('冰咖啡','Ice Coffee','OZ/份',20,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('冰拿铁','Ice Coffee Latte','OZ/份',20,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('冰卡布基诺','Ice Cappuccino','OZ/份',20,38)");
		db.execSQL("insert into " + TABLE_MENU_ITEM
				+ "(item_name,item_name_en,item_unit,item_class,item_price)"
				+ " values('冰摩卡基诺','Ice Mocha Keno','OZ/份',20,48)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO

	}

	/**
	 * TODO 菜谱管理 1-新增；2-查询所有；
	 */
	public long insertMenuItem(MenuItem mi) {
		ContentValues cv = new ContentValues();
		cv.put("item_name", mi.getItemName());
		cv.put("item_name_en", mi.getItemNameEn());
		cv.put("item_unit", mi.getItemUnit());
		cv.put("item_desc", mi.getItemDesc());
		cv.put("item_desc_en", mi.getItemDescEn());
		cv.put("item_class", mi.getItemClass());
		cv.put("item_price", mi.getItemPrice());
		cv.put("item_desc2", mi.getItemDesc2());
		cv.put("item_desc2_en", mi.getItemDescEn2());
		cv.put("item_pic", mi.getItemPic());
		return getWritableDatabase().insert(TABLE_MENU_ITEM, null, cv);
	}

	public MenuItemCursor queryMenuItems() {
		String orderBy = "_id asc";
		// query (String table, String[] columns, String selection,
		// String[] selectionArgs, String groupBy, String having, String
		// orderBy)
		String[] selectionArgs = { "8" };
		String whereClause = "item_class < ?";
		Cursor wapped = getReadableDatabase().query(TABLE_MENU_ITEM, null,
				whereClause, selectionArgs, null, null, orderBy);
		return new MenuItemCursor(wapped);

	}

	public MenuItemCursor queryDrinkItems() {
		String orderBy = "_id asc";
		// query (String table, String[] columns, String selection,
		// String[] selectionArgs, String groupBy, String having, String
		// orderBy)
		String[] selectionArgs = { "7" };
		String whereClause = "item_class > ?";
		Cursor wapped = getReadableDatabase().query(TABLE_MENU_ITEM, null,
				whereClause, selectionArgs, null, null, orderBy);
		return new MenuItemCursor(wapped);

	}

	public static class MenuItemCursor extends CursorWrapper {
		public MenuItemCursor(Cursor cursor) {
			super(cursor);
		}

		public MenuItem getMenuItem() {
			MenuItem mi = new MenuItem();
			mi.set_id(getInt(getColumnIndex("_id")));
			mi.setItemName(getString(getColumnIndex("item_name")));
			mi.setItemNameEn(getString(getColumnIndex("item_name_en")));
			mi.setItemDesc(getString(getColumnIndex("item_desc")));
			mi.setItemUnit(getString(getColumnIndex("item_unit")));
			mi.setItemDescEn(getString(getColumnIndex("item_desc_en")));
			mi.setItemClass(getInt(getColumnIndex("item_class")));
			mi.setItemPrice(getInt(getColumnIndex("item_price")));
			mi.setItemDesc2(getString(getColumnIndex("item_desc2")));
			mi.setItemDescEn2(getString(getColumnIndex("item_desc2_en")));
			mi.setItemPic(getInt(getColumnIndex("item_pic")));
			return mi;
		}
	}

	/**
	 * TODO 订单管理： 1-新增； 2-删除； 3-查询所有未结账的桌号； 4-更新订单
	 */

	public boolean insertOrder(Order o) {
		ContentValues cv = new ContentValues();
		cv.put("order_id", o.getOrderID());
		cv.put("table_id", o.getTableNo());
		cv.put("cnt", o.getCnt());
		cv.put("order_date", o.getOrderDate().getTime());// TODO Date
		cv.put("order_main_no", o.getOrderMainNo());
		cv.put("waiter", o.getWaiter());
		cv.put("settlement_type", o.getSettlementType());
		cv.put("is_settled", o.isSettled() ? 1 : 0);
		cv.put("is_submitted", o.isSubmitted() ? 1 : 0);
		cv.put("discount", o.getDiscount());
		cv.put("total_amount", o.getTotalAmount());
		return getWritableDatabase().insert(TABLE_ORDER, null, cv) != -1;
	}

	public boolean deleteOrderByTableID(String tableID) {
		String whereClause = "table_id = ?";
		String[] whereArgs = new String[] { tableID };
		int delRow = 0;
		delRow = getWritableDatabase().delete(TABLE_ORDER, whereClause,
				whereArgs);
		deleteOrderListByTableID(tableID);// 删除所有订单
		return delRow == 0;
	}

	public boolean deleteOrderByOrderID(String orderID) {
		String whereClause = "order_id = ?";
		String[] whereArgs = new String[] { orderID };
		int delRow = 0;
		delRow = getWritableDatabase().delete(TABLE_ORDER, whereClause,
				whereArgs);
		deleteOrderListByOrderID(orderID);// 删除所有订单
		return delRow == 0;
	}

	public OrderCursor queryOrder() {
		// query (String table, String[] columns, String selection,
		// String[] selectionArgs, String groupBy, String having, String
		// orderBy)
		// String[] columns = { "table_id" };
		// String selection = "is_settled = ?";
		// String[] selectionArgs = { "0" }; // unSettled Table == Available
		String orderBy = "_id asc";

		// Cursor c = getReadableDatabase().query(TABLE_ORDER, null, selection,
		// selectionArgs, null, null, orderBy);
		Cursor c = getReadableDatabase().query(TABLE_ORDER, null, null, null,
				null, null, orderBy);
		return new OrderCursor(c);
	}

	public OrderCursor queryOrderByTableId(String table_id) {
		// query (String table, String[] columns, String selection,
		// String[] selectionArgs, String groupBy, String having, String
		// orderBy)
		String selection = "table_id = ?";
		String[] selectionArgs = { table_id }; // unSettled Table == Available
		String orderBy = "order_date desc";

		Cursor c = getReadableDatabase().query(TABLE_ORDER, null, selection,
				selectionArgs, null, null, orderBy);
		return new OrderCursor(c);
	}

	public OrderCursor queryOrderByOrderID(String orderID) {
		String selection = "order_id = ?";
		String[] selectionArgs = { orderID }; // unSettled Table ==
												// Available
		Cursor c = getReadableDatabase().query(TABLE_ORDER, null, selection,
				selectionArgs, null, null, null);
		return new OrderCursor(c);
	}

	public boolean updateOrderByOrderID(Order o) {
		ContentValues cv = new ContentValues();
		cv.put("order_id", o.getOrderID());
		cv.put("table_id", o.getTableNo());
		cv.put("cnt", o.getCnt());
		cv.put("order_date", o.getOrderDate().getTime());// TODO Date
		cv.put("waiter", o.getWaiter());
		cv.put("settlement_type", o.getSettlementType());
		cv.put("is_settled", o.isSettled() ? 1 : 0);
		cv.put("is_submitted", o.isSubmitted() ? 1 : 0);
		cv.put("discount", o.getDiscount());
		cv.put("total_amount", o.getTotalAmount());
		// update (String table, ContentValues values, String whereClause,
		// String[] whereArgs)
		String whereClause = "order_id = ?";
		String[] whereArgs = { o.getOrderID() };

		return getWritableDatabase().update(TABLE_ORDER, cv, whereClause,
				whereArgs) > 0;
	}

	public boolean updateOrderByTableID(Order o) {
		ContentValues cv = new ContentValues();
		cv.put("order_id", o.getOrderID());
		cv.put("table_id", o.getTableNo());
		cv.put("cnt", o.getCnt());
		cv.put("order_date", o.getOrderDate().getTime());// TODO Date
		cv.put("waiter", o.getWaiter());
		cv.put("settlement_type", o.getSettlementType());
		cv.put("is_settled", o.isSettled() ? 1 : 0);
		cv.put("is_submitted", o.isSubmitted() ? 1 : 0);
		cv.put("discount", o.getDiscount());
		cv.put("total_amount", o.getTotalAmount());
		// update (String table, ContentValues values, String whereClause,
		// String[] whereArgs)
		String whereClause = "table_id = ?";
		String[] whereArgs = { o.getTableNo() };

		return getWritableDatabase().update(TABLE_ORDER, cv, whereClause,
				whereArgs) > 0;
	}

	public boolean updateOrderByTableId(String newTableId, String tableIdOrg) {
		ContentValues cv = new ContentValues();
		cv.put("table_id", newTableId);
		String whereClause = "table_id = ?";
		String[] whereArgs = { tableIdOrg };

		return getWritableDatabase().update(TABLE_ORDER, cv, whereClause,
				whereArgs) > 0;
	}

	public static class OrderCursor extends CursorWrapper {
		public OrderCursor(Cursor cursor) {
			super(cursor);
		}

		public Order getOrder() {

			Order o = new Order();
			o.setOrderID(getString(getColumnIndex("order_id")));
			o.setTableNo(getString(getColumnIndex("table_id")));
			o.setCnt(getInt(getColumnIndex("cnt")));
			o.setOrderDate(new Date(getLong(getColumnIndex("order_date"))));
			o.setOrderMainNo(getString(getColumnIndex("order_main_no")));
			o.setWaiter(getString(getColumnIndex("waiter")));
			o.setSettlementType(getInt(getColumnIndex("settlement_type")));
			o.setSettled(getInt(getColumnIndex("is_settled")) == 1);
			o.setSubmitted(getInt(getColumnIndex("is_submitted")) == 1);
			o.setDiscount(getLong(getColumnIndex("discount")));
			o.setTotalAmount(getLong(getColumnIndex("total_amount")));
			return o;
		}

	}

	/**
	 * TODO 点菜管理 1-新增；2-删除；
	 */
	public boolean insertOrderList(OrderItem oi) {
		ContentValues cv = new ContentValues();
		cv.put("table_id", oi.getTableID());
		cv.put("order_id", oi.getOrderID());
		cv.put("item_id", oi.getMenuItem().get_id());
		cv.put("item_cnt", oi.getItemCnt());
		cv.put("item_class", oi.getMenuItem().getItemClass());
		return getWritableDatabase().insert(TABLE_ORDER_LIST, null, cv) != -1;
	}

	public boolean updateOrderListByOrderIDMenuID(OrderItem oi) {
		ContentValues cv = new ContentValues();
		cv.put("table_id", oi.getTableID());
		cv.put("order_id", oi.getOrderID());
		cv.put("item_id", oi.getMenuItem().get_id());
		cv.put("item_cnt", oi.getItemCnt());
		cv.put("item_class", oi.getMenuItem().getItemClass());
		// update (String table, ContentValues values, String whereClause,
		// String[] whereArgs)
		String whereClause = "order_id = ? and item_id = ?";
		String[] whereArgs = { oi.getOrderID(), oi.getMenuItem().get_id() + "" };

		return getWritableDatabase().update(TABLE_ORDER_LIST, cv, whereClause,
				whereArgs) > 0;
	}

	public boolean deleteOrderListByTableID(String tableID) {
		String whereClause = "table_id = ?";
		String[] whereArgs = new String[] { tableID };
		int delRow = 0;
		delRow = getWritableDatabase().delete(TABLE_ORDER_LIST, whereClause,
				whereArgs);
		return delRow == 0;
	}

	public boolean deleteOrderListByOrderID(String orderID) {
		String whereClause = "order_id = ?";
		String[] whereArgs = new String[] { orderID };
		int delRow = 0;
		delRow = getWritableDatabase().delete(TABLE_ORDER_LIST, whereClause,
				whereArgs);
		return delRow == 0;
	}

	public boolean deleteOrderItem(OrderItem oi) {
		String whereClause = "table_id = ? and order_id = ? and item_id = ?";
		String[] whereArgs = new String[] { oi.getTableID(), oi.getOrderID(),
				oi.getMenuItem().get_id() + "" };
		int delRow = 0;
		delRow = getWritableDatabase().delete(TABLE_ORDER_LIST, whereClause,
				whereArgs);
		return delRow == 0;
	}

	// 一个客户可产生多个订单1：n
	public OrderItemCursor queryOrderListByOrderID(String orderID) {
		// 查询未提交的订单
		String orderBy = "_id asc";
		String selection = "order_id = ? ";
		String[] selectionArgs = new String[] { orderID };
		Cursor c = getReadableDatabase().query(TABLE_ORDER_LIST, null,
				selection, selectionArgs, null, null, orderBy);
		return new OrderItemCursor(c);
	}

	public OrderItemCursor queryOrderListAll() {

		String orderBy = "_id asc";
		Cursor c = getReadableDatabase().query(TABLE_ORDER_LIST, null, null,
				null, null, null, orderBy);
		return new OrderItemCursor(c);
	}

	// orderMainNo客户最终开票编号1:1
	public OrderItemCursor queryOrderListByTableId(String table_id) {

		String orderBy = "_id asc";
		String selection = "table_id = ?";
		String[] selectionArgs = new String[] { table_id };
		Cursor c = getReadableDatabase().query(TABLE_ORDER_LIST, null,
				selection, selectionArgs, null, null, orderBy);
		return new OrderItemCursor(c);
	}

	public static class OrderItemCursor extends CursorWrapper {
		public OrderItemCursor(Cursor cursor) {
			super(cursor);
		}

		public OrderItem getOrderItem() {
			OrderItem oi = new OrderItem();
			oi.setMenuItem(new MenuItem());
			oi.set_id(getInt(getColumnIndex("item_cnt")));
			oi.getMenuItem().set_id(getInt(getColumnIndex("item_id")));
			oi.setItemCnt(getInt(getColumnIndex("item_cnt")));
			oi.setTableID(getString(getColumnIndex("table_id")));
			oi.setOrderID(getString(getColumnIndex("order_id")));
			oi.getMenuItem().setItemClass(getInt(getColumnIndex("item_class")));
			// oi.setOrderRemark(getString(getColumnIndex("item_remark")));

			return oi;
		}
	}

	/**
	 * TODO 员工表管理
	 * 
	 */
	public boolean insertEmployee(String employeeName) {
		ContentValues cv = new ContentValues();
		cv.put("employee_name", employeeName);
		return getWritableDatabase().insert(TABLE_EMPLOYEE, null, cv) != -1;
	}

	// 查询所有员工
	public EmployeeCursor queryEmployees() {
		String orderBy = "_id asc";
		Cursor wapped = getReadableDatabase().query(TABLE_EMPLOYEE, null, null,
				null, null, null, orderBy);
		return new EmployeeCursor(wapped);
	}

	public boolean deleteEmployee(String employeeName) {
		String whereClause = "employee_name = ?";
		String[] whereArgs = new String[] { employeeName };
		int delRow = 0;
		delRow = getWritableDatabase().delete(TABLE_EMPLOYEE, whereClause,
				whereArgs);
		return delRow == 0;
	}

	public static class EmployeeCursor extends CursorWrapper {
		public EmployeeCursor(Cursor cursor) {
			super(cursor);
		}

		public Employee getEmployee() {
			if (isBeforeFirst() || isAfterLast())
				return null;// 检查是否越界

			Employee e = new Employee();
			e.setEmployeeName(getString(getColumnIndex("employee_name")));
			return e;
		}
	}
}
