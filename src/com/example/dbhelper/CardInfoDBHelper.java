package com.example.dbhelper;

import com.example.unit.CardInfoUnit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CardInfoDBHelper extends SQLiteOpenHelper {
	private final static String DB_NAME = "coll.db";
	private final static int DB_VERSION = 1;
	public final static String TBL_NAME_CARD = "CARD";
	public final static String CARD_ROW_ID = "_ID";
	public final static String CARD_NAME = "NAME";
	public final static String CARD_CARD_ID = "CARDID";
	public final static String CARD_VALIDITY_BEGIN = "VALIDITY_BEGIN";
	public final static String CARD_VALIDITY_END = "VALIDITY_END";
	public final static String CARD_NOTE = "NOTE";
	public final static String CARD_CATEGORYS = "CATEGORYS";
	public final static String CARD_MERCHANT_NAME = "MERCHANT_NAME";
	public final static String CARD_MERCHANT_ADDRESS = "MERCHANT_ADDRESS";
	public final static String CARD_MERCHANT_PHONE = "MERCHANT_PHONE";
	public final static String CARD_MERCHANT_WEBSITE = "MERCHANT_WEBSITE";
	public final static String CARD_MESSAGE_NUMBERS = "MESSAGE_NUMBERS";
	public final static String CARD_PHOTOS = "PHOTOS";
	public final static String CARD_PHOTOS_ICON = "ICON";
	public final static String CARD_STATUS = "STATUS";
	public final static String CARD_CREATE_TIME = "CREATE_TIME";
	public final static String CARD_IS_FREQUENT = "FREQUENT";

	private SQLiteDatabase db;

	public CardInfoDBHelper(Context c) {
		super(c, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		db.execSQL(" CREATE TABLE " + TBL_NAME_CARD + "(" + CARD_ROW_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + CARD_NAME
				+ " TEXT, " + CARD_CARD_ID + " TEXT," + CARD_CATEGORYS
				+ " TEXT," + CARD_VALIDITY_BEGIN + " TEXT," + CARD_VALIDITY_END
				+ " TEXT," + CARD_NOTE + " TEXT," + CARD_MERCHANT_NAME
				+ " TEXT, " + CARD_MERCHANT_ADDRESS + " TEXT, "
				+ CARD_MERCHANT_PHONE + " TEXT, " + CARD_MERCHANT_WEBSITE
				+ " TEXT, " + CARD_MESSAGE_NUMBERS + " TEXT," + CARD_PHOTOS
				+ " TEXT, " + CARD_PHOTOS_ICON + " INTEGER," + CARD_STATUS
				+ " INTEGER, " + CARD_CREATE_TIME + " LONG, "
				+ CARD_IS_FREQUENT + " INTEGER)");
	}

	public int insert(ContentValues values, String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		int row = (int) db.insert(tableName, null, values);
		db.close();
		return row;
	}

	public void update(ContentValues values, String tableName, String rowId) {
		SQLiteDatabase db = getWritableDatabase();
		db.update(tableName, values, CARD_ROW_ID + " = ?",
				new String[] { rowId });
		db.close();
	}

	public void del(String tableName, int id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(tableName, CARD_ROW_ID + "=?", new String[] { id + "" });
		db.close();
	}

	@Override
	public void close() {
		if (db != null)
			db.close();
	}

	public CardInfoUnit query(int rowId) {
		SQLiteDatabase dbReader = getReadableDatabase();
		Cursor c = dbReader.query(CardInfoDBHelper.TBL_NAME_CARD, null,
				CardInfoDBHelper.CARD_ROW_ID + "=?",
				new String[] { rowId + "" }, null, null, null);
		CardInfoUnit cardInfoUnit = null;
		if (c.moveToFirst()) {
			cardInfoUnit = new CardInfoUnit(c);
		}
		c.close();
		dbReader.close();
		return cardInfoUnit;
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	private static CardInfoDBHelper cardInfoDBHelper;

	public static void initCardInfoDBHelper(Context context) {
		cardInfoDBHelper = new CardInfoDBHelper(context);
	}

	public static CardInfoDBHelper getCardInfoDBHelper() {
		return cardInfoDBHelper;
	}
}
