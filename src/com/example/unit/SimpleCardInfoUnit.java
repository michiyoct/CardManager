package com.example.unit;

import android.database.Cursor;

import com.example.assistclass.ValueUtil;
import com.example.dbhelper.CardInfoDBHelper;

public class SimpleCardInfoUnit {
	public void setFrequent(boolean isFrequent) {
		this.isFrequent = isFrequent;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public boolean isFrequent() {
		return isFrequent;
	}

	public int getStatu() {
		return statu;
	}

	public static String[] columns = new String[] { CardInfoDBHelper.CARD_NAME,
			CardInfoDBHelper.CARD_VALIDITY_END, CardInfoDBHelper.CARD_NOTE,
			CardInfoDBHelper.CARD_ROW_ID, CardInfoDBHelper.CARD_PHOTOS_ICON,
			CardInfoDBHelper.CARD_STATUS, CardInfoDBHelper.CARD_IS_FREQUENT };

	public String getName() {
		return name;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getValidityEnd() {
		return validityEnd;
	}

	public String getNote() {
		return note;
	}

	public int getRowId() {
		return rowId;
	}

	public SimpleCardInfoUnit(Cursor c) {
		this.rowId = c.getInt(c.getColumnIndex(CardInfoDBHelper.CARD_ROW_ID));
		this.name = c.getString(c.getColumnIndex(CardInfoDBHelper.CARD_NAME));
		this.validityEnd = c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_VALIDITY_END));
		this.note = c.getString(c.getColumnIndex(CardInfoDBHelper.CARD_NOTE));
		this.imagePath = c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_PHOTOS_ICON));
		this.statu = c.getInt(c.getColumnIndex(CardInfoDBHelper.CARD_STATUS));
		this.isFrequent = ValueUtil.int2boolean(c.getInt(c
				.getColumnIndex(CardInfoDBHelper.CARD_IS_FREQUENT)));
	}

	private boolean isFrequent;
	private String name;
	private String imagePath;
	private String validityEnd;
	private String note;
	private int statu = CardInfoUnit.STATU_NONE;
	private int rowId;
}
