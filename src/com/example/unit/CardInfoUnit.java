package com.example.unit;

import java.io.Serializable;
import java.util.List;

import com.example.assistclass.ValueUtil;
import com.example.dbhelper.CardInfoDBHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

public class CardInfoUnit implements Serializable {
	public String getShareInfo() {
		String shareInfo = "";
		if (TextUtils.isEmpty(cardId))
			shareInfo += (name + "\n");
		else
			shareInfo += (name + "　" + cardId + "\n");
		if (TextUtils.isEmpty(validityEnd))
			shareInfo += ("生效:" + validityEnd + "　" + "失效:" + "\n");
		else
			shareInfo += ("生效:" + validityEnd + "　" + "失效:" + validityEnd + "\n");
		if (TextUtils.isEmpty(merchantAddress))
			shareInfo += ("地址:暂无\n");
		else
			shareInfo += ("地址:" + merchantAddress + "\n");
		if (TextUtils.isEmpty(merchantPhone))
			shareInfo += ("电话:暂无");
		else
			shareInfo += ("电话:" + merchantPhone);
		return shareInfo;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public CardInfoUnit(int rowId, String name, String cardId,
			String validityBegin, String validityEnd, String note,
			String merchantName, String merchantAddress, String merchantPhone,
			String merchantWebsite, List<String> categorys,
			List<String> imagePaths, String iconPath,
			List<String> messageNumbers, int statu, long createTime,
			boolean isFrequent) {
		super();
		this.rowId = rowId;
		this.name = name;
		this.cardId = cardId;
		this.validityBegin = validityBegin;
		this.validityEnd = validityEnd;
		this.note = note;
		this.merchantName = merchantName;
		this.merchantAddress = merchantAddress;
		this.merchantPhone = merchantPhone;
		this.merchantWebsite = merchantWebsite;
		this.categorys = categorys;
		this.imagePaths = imagePaths;
		this.iconPath = iconPath;
		this.messageNumbers = messageNumbers;
		this.statu = statu;
		this.createTime = createTime;
		this.isFrequent = isFrequent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getVadilityBegin() {
		return validityBegin;
	}

	public void setVadilityBegin(String validityBegin) {
		this.validityBegin = validityBegin;
	}

	public String getVadilityEnd() {
		return validityEnd;
	}

	public void setVadilityEnd(String validityEnd) {
		this.validityEnd = validityEnd;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}

	public String getMerchantPhone() {
		return merchantPhone;
	}

	public void setMerchantPhone(String merchantPhone) {
		this.merchantPhone = merchantPhone;
	}

	public String getMerchantWebsite() {
		return merchantWebsite;
	}

	public void setMerchantWebsite(String merchantWebsite) {
		this.merchantWebsite = merchantWebsite;
	}

	public List<String> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<String> categorys) {
		this.categorys = categorys;
	}

	public List<String> getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(List<String> imagePaths) {
		this.imagePaths = imagePaths;
	}

	public List<String> getMessageNumbers() {
		return messageNumbers;
	}

	public void setMessageNumbers(List<String> messageNumbers) {
		this.messageNumbers = messageNumbers;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public boolean isFrequent() {
		return isFrequent;
	}

	public void setFrequent(boolean isFrequent) {
		this.isFrequent = isFrequent;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public CardInfoUnit(Cursor c) {
		this.rowId = c.getInt(c.getColumnIndex(CardInfoDBHelper.CARD_ROW_ID));
		this.name = c.getString(c.getColumnIndex(CardInfoDBHelper.CARD_NAME));
		this.cardId = c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_CARD_ID));
		this.validityBegin = c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_VALIDITY_BEGIN));
		this.validityEnd = c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_VALIDITY_END));
		this.note = c.getString(c.getColumnIndex(CardInfoDBHelper.CARD_NOTE));
		this.merchantName = c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_MERCHANT_NAME));
		this.merchantAddress = c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_MERCHANT_ADDRESS));
		this.merchantPhone = c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_MERCHANT_PHONE));
		this.merchantWebsite = c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_MERCHANT_WEBSITE));
		this.imagePaths = ValueUtil.string2list(c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_PHOTOS)));
		this.messageNumbers = ValueUtil.string2list(c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_MESSAGE_NUMBERS)));
		this.statu = c.getInt(c.getColumnIndex(CardInfoDBHelper.CARD_STATUS));
		this.createTime = c.getLong(c
				.getColumnIndex(CardInfoDBHelper.CARD_CREATE_TIME));
		this.categorys = ValueUtil.string2list(c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_CATEGORYS)));
		this.isFrequent = ValueUtil.int2boolean(c.getInt(c
				.getColumnIndex(CardInfoDBHelper.CARD_IS_FREQUENT)));
		this.iconPath = c.getString(c
				.getColumnIndex(CardInfoDBHelper.CARD_PHOTOS_ICON));
	}

	public ContentValues getDBValue() {
		ContentValues values = new ContentValues();
		values.put(CardInfoDBHelper.CARD_NAME, name);
		values.put(CardInfoDBHelper.CARD_CARD_ID, cardId);
		values.put(CardInfoDBHelper.CARD_VALIDITY_BEGIN, validityBegin);
		values.put(CardInfoDBHelper.CARD_VALIDITY_END, validityEnd);
		values.put(CardInfoDBHelper.CARD_NOTE, note);
		values.put(CardInfoDBHelper.CARD_MERCHANT_NAME, merchantName);
		values.put(CardInfoDBHelper.CARD_MERCHANT_ADDRESS, merchantAddress);
		values.put(CardInfoDBHelper.CARD_MERCHANT_PHONE, merchantPhone);
		values.put(CardInfoDBHelper.CARD_MERCHANT_WEBSITE, merchantWebsite);
		values.put(CardInfoDBHelper.CARD_CATEGORYS,
				ValueUtil.list2string(categorys));
		values.put(CardInfoDBHelper.CARD_PHOTOS,
				ValueUtil.list2string(imagePaths));
		values.put(CardInfoDBHelper.CARD_MESSAGE_NUMBERS,
				ValueUtil.list2string(messageNumbers));
		values.put(CardInfoDBHelper.CARD_STATUS, statu);
		values.put(CardInfoDBHelper.CARD_CREATE_TIME, createTime);
		values.put(CardInfoDBHelper.CARD_IS_FREQUENT,
				ValueUtil.boolean2int(isFrequent));
		values.put(CardInfoDBHelper.CARD_PHOTOS_ICON, iconPath);
		return values;
	}

	public final static int STATU_NONE = 0;
	public final static int STATU_EXPIRING = 1;
	public final static int STATU_USED = 2;

	private int rowId = -1;
	private String name;
	private String cardId;
	private String validityBegin;
	private String validityEnd;
	private String note;
	private String merchantName;
	private String merchantAddress;
	private String merchantPhone;
	private String merchantWebsite;
	private List<String> categorys;
	private List<String> imagePaths;
	private String iconPath;
	private List<String> messageNumbers;
	private int statu = STATU_NONE;
	private long createTime = 0;
	private boolean isFrequent = false;;
}
