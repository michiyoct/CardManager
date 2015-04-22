package com.example.dbhelper;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.assistclass.ValueUtil;
import com.example.cardmanager.MainActivity;
import com.example.unit.BasicCardInfoUnit;
import com.example.unit.CardInfoUnit;
import com.example.unit.SimpleCardInfoUnit;
import com.example.views.DatePickerDialog;

public class CardManager {
	public static List<BasicCardInfoUnit> getFrequentCard() {
		SQLiteDatabase dbReader = CardInfoDBHelper.getCardInfoDBHelper()
				.getReadableDatabase();
		Cursor c = dbReader.query(CardInfoDBHelper.TBL_NAME_CARD,
				BasicCardInfoUnit.columns, CardInfoDBHelper.CARD_IS_FREQUENT
						+ "=?",
				new String[] { ValueUtil.boolean2int(true) + "" }, null, null,
				null);
		List<BasicCardInfoUnit> basicCardInfoUnits = new ArrayList<BasicCardInfoUnit>();
		if (c.moveToFirst()) {
			do {
				basicCardInfoUnits.add(new BasicCardInfoUnit(c));
			} while (c.moveToNext());
		}
		c.close();
		dbReader.close();
		return basicCardInfoUnits;
	}

	public static void deleteCard(int rowId) {
		CardInfoUnit cardInfoUnit = CardInfoDBHelper.getCardInfoDBHelper()
				.query(rowId);
		if (cardInfoUnit != null) {
			List<String> imagePaths = cardInfoUnit.getImagePaths();
			if (imagePaths != null) {
				for (String imagePath : imagePaths) {
					File file = new File(imagePath);
					if (file.exists())
						file.delete();
				}
			}
			if (rowId != -1) {
				CardInfoDBHelper.getCardInfoDBHelper().del(
						CardInfoDBHelper.TBL_NAME_CARD, rowId);
			}
		}
	}

	public static int insertCard(CardInfoUnit cardInfoUnit) {
		int row = CardInfoDBHelper.getCardInfoDBHelper().insert(
				cardInfoUnit.getDBValue(), CardInfoDBHelper.TBL_NAME_CARD);
		SQLiteDatabase dbReader = CardInfoDBHelper.getCardInfoDBHelper()
				.getReadableDatabase();
		Cursor c = dbReader.query(CardInfoDBHelper.TBL_NAME_CARD,
				new String[] { CardInfoDBHelper.CARD_ROW_ID }, null, null,
				null, null, CardInfoDBHelper.CARD_ROW_ID);
		c.moveToFirst();
		while (row != 0 && !c.isLast()) {
			c.moveToNext();
			row--;
		}
		int rowId = c.getInt(c.getColumnIndex(CardInfoDBHelper.CARD_ROW_ID));
		return rowId;
	}

	public static CardInfoUnit getCard(int rowId) {
		SQLiteDatabase dbReader = CardInfoDBHelper.getCardInfoDBHelper()
				.getReadableDatabase();
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

	public static void updateCard(ContentValues value, int rowId) {
		CardInfoDBHelper.getCardInfoDBHelper().update(value,
				CardInfoDBHelper.TBL_NAME_CARD, rowId + "");
	}

	public static List<SimpleCardInfoUnit> getExpiringCard() {
		List<SimpleCardInfoUnit> cardInfoUnits = new ArrayList<SimpleCardInfoUnit>();
		SQLiteDatabase dbReader = CardInfoDBHelper.getCardInfoDBHelper()
				.getReadableDatabase();
		Cursor c = dbReader.query(CardInfoDBHelper.TBL_NAME_CARD,
				SimpleCardInfoUnit.columns,
				CardInfoDBHelper.CARD_STATUS + "=?",
				new String[] { CardInfoUnit.STATU_EXPIRING + "" }, null, null,
				null);
		if (c.moveToFirst()) {
			do {
				cardInfoUnits.add(new SimpleCardInfoUnit(c));
			} while (c.moveToNext());
		}
		return cardInfoUnits;
	}

	public static void refreshExpiringCard() {
		SQLiteDatabase dbReader = CardInfoDBHelper.getCardInfoDBHelper()
				.getReadableDatabase();
		Cursor c = dbReader.query(CardInfoDBHelper.TBL_NAME_CARD, new String[] {
				CardInfoDBHelper.CARD_VALIDITY_END,
				CardInfoDBHelper.CARD_ROW_ID, CardInfoDBHelper.CARD_STATUS },
				null, null, null, null, null);
		if (c.moveToFirst()) {
			do {
				String vadilityEnd = c.getString(c
						.getColumnIndex(CardInfoDBHelper.CARD_VALIDITY_END));
				int statu = c.getInt(c
						.getColumnIndex(CardInfoDBHelper.CARD_STATUS));
				int rowId = c.getInt(c
						.getColumnIndex(CardInfoDBHelper.CARD_ROW_ID));
				if (statu != CardInfoUnit.STATU_USED) {
					Date vadility = null;
					try {
						SimpleDateFormat df = new SimpleDateFormat("yyyy"
								+ DatePickerDialog.DATE_SEPERATOR + "MM"
								+ DatePickerDialog.DATE_SEPERATOR + "dd");
						vadility = df.parse(vadilityEnd);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (vadility != null) {
						Calendar calendar = Calendar.getInstance();
						Date now = calendar.getTime();
						long dayOffset = (vadility.getTime() - now.getTime());
						if (dayOffset < 0) {
							statu = CardInfoUnit.STATU_USED;
							ContentValues values = new ContentValues();
							values.put(CardInfoDBHelper.CARD_STATUS, statu);
							updateCard(values, rowId);
						} else if (dayOffset >= 0
								&& dayOffset <= ((long) MainActivity.EXPIRING_OFFSET)
										* 24 * 60 * 60 * 1000) {
							statu = CardInfoUnit.STATU_EXPIRING;
							ContentValues values = new ContentValues();
							values.put(CardInfoDBHelper.CARD_STATUS, statu);
							updateCard(values, rowId);
						}
					}
				}
			} while (c.moveToNext());
		}
	}
}
