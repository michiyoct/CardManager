package com.example.unit;

import android.database.Cursor;

import com.example.dbhelper.CardInfoDBHelper;

public class BasicCardInfoUnit {
	public static String[] columns = new String[] { CardInfoDBHelper.CARD_NAME,
			CardInfoDBHelper.CARD_ROW_ID, CardInfoDBHelper.CARD_PHOTOS_ICON };

	public String getName() {
		return name;
	}

	public String getImagePath() {
		return imagePath;
	}

	public int getRowId() {
		return rowId;
	}

	public BasicCardInfoUnit(Cursor c) {
		this.rowId = c.getInt(c.getColumnIndex(CardInfoDBHelper.CARD_ROW_ID));
		this.name = c.getString(c.getColumnIndex(CardInfoDBHelper.CARD_NAME));
		this.imagePath = c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_PHOTOS_ICON));
	}

	public BasicCardInfoUnit(String name, String imagePath, int rowId) {
		super();
		this.name = name;
		this.imagePath = imagePath;
		this.rowId = rowId;
	}

	private String name;
	private String imagePath;
	private int rowId;
}
