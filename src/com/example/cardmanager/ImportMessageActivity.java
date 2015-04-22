package com.example.cardmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.adapter.MessageListAdapter;
import com.example.unit.CardInfoUnit;
import com.hmammon.xcfhsms.smsutils.BankSMS;
import com.hmammon.xcfhsms.smsutils.SMSUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ImportMessageActivity extends BaseActivity {
	private ListView listView;
	private MessageListAdapter messageListAdapter;
	private List<String> messageNumbers;
	private List<String> cardIds;
	private List<String> merchantNames;
	private List<String> cardName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_message_import);
		listView = (ListView) findViewById(R.id.activity_message_import_list);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ImportMessageActivity.this,
						CardDetailActivity.class);
				intent.putExtra(CardDetailActivity.CARD_DETAIL_STATU,
						CardDetailActivity.STATU_ADD_FROM_MESSAGE);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(
						CardDetailActivity.CARD_DETAIL_CARD_UNIT,
						new CardInfoUnit(-1, cardName.get(position), cardIds
								.get(position), null, null, null, merchantNames
								.get(position), null, messageNumbers
								.get(position), null, null, null, null, null,
								CardInfoUnit.STATU_NONE, Calendar.getInstance()
										.getTimeInMillis(), false));
				intent.putExtras(mBundle);
				startActivity(intent);
			}
		});
		messageListAdapter = new MessageListAdapter(ImportMessageActivity.this);
		listView.setAdapter(messageListAdapter);
		setTitle("导入短信");
		setRightButton(-1, null);
		setBackButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		findMessage();
	}

	private void findMessage() {
		// TODO Auto-generated method stub
		Map<String, String> importMessageNumbers = getImportMessageNumbers(ImportMessageActivity.this);
		final String SMS_URI_INBOX = "content://sms/inbox";
		List<String> messages = new ArrayList<String>();
		messageNumbers = new ArrayList<String>();
		cardIds = new ArrayList<String>();
		merchantNames = new ArrayList<String>();
		cardName = new ArrayList<String>();
		try {
			ContentResolver cr = getContentResolver();
			String[] projection = new String[] { "_id", "body", "address" };
			Uri uri = Uri.parse(SMS_URI_INBOX);
			Cursor cur = cr.query(uri, projection, null, null, "date desc");

			if (cur.moveToFirst()) {
				String smsbody;
				String address;
				do {
					address = cur.getString(cur.getColumnIndex("address"));
					smsbody = cur.getString(cur.getColumnIndex("body"));
					String name = importMessageNumbers.get(address);
					if (!TextUtils.isEmpty(name)) {
						messages.add(name + "\n\t" + smsbody);
						messageNumbers.add(address);
						cardIds.add(null);
						merchantNames.add(name);
						cardName.add(name);
					}
				} while (cur.moveToNext());
			}
		} catch (SQLiteException ex) {
			ex.printStackTrace();
		}
		SMSUtils utils = SMSUtils.getInstance(this);
		List<BankSMS> list = utils
				.analyseSMS(utils.getReadyList(), true, false);
		for (BankSMS tempBankSMS : list) {
			messages.add(tempBankSMS.getBankName() + "\n\t"
					+ tempBankSMS.getBody());
			messageNumbers.add(tempBankSMS.getAddress());
			cardIds.add(tempBankSMS.getCardNumber());
			merchantNames.add(tempBankSMS.getBankName());
			cardName.add(tempBankSMS.getBankName() + tempBankSMS.getCardType());
		}
		messageListAdapter.setMessageNumber(messages, null);
	}

	public static Map<String, String> getImportMessageNumbers(Context context) {
		Map<String, String> importMessageNumbers = new HashMap<String, String>();
		XmlResourceParser xrp = context.getResources().getXml(R.xml.number);
		try {
			// 直到文档的结尾处
			while (xrp.getEventType() != XmlPullParser.END_DOCUMENT) {
				// 如果遇到了开始标签
				if (xrp.getEventType() == XmlPullParser.START_TAG) {
					String tagName = xrp.getName();// 获取标签的名字
					if (tagName.equals("item")) {
						String name = xrp.getAttributeValue(null, "name");// 通过属性名来获取属性值
						String number = xrp.nextText();
						importMessageNumbers.put(number, name);
					}
				}
				xrp.next();// 获取解析下一个事件
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return importMessageNumbers;
	}
}
