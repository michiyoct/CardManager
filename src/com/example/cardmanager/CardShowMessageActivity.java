package com.example.cardmanager;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.MessageListAdapter;
import com.example.assistclass.ValueUtil;
import com.example.views.NoScrollListView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CardShowMessageActivity extends BaseActivity {
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data != null) {
				messageNumbers = ValueUtil
						.string2list(data
								.getStringExtra(CardDetailActivity.CARD_DETAIL_MESSAGE_NUMBERS));
				getSmsInPhone();
			}
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(CardDetailActivity.CARD_DETAIL_MESSAGE_NUMBERS,
				ValueUtil.list2string(messageNumbers));
		setResult(RESULT_OK, intent);
		super.finish();
	}

	private Button addMessageNumberButton;
	private NoScrollListView messageListView;
	private List<String> messageNumbers;
	private String merchantNumber;
	private MessageListAdapter messageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_card_show_message);
		Intent intent = getIntent();
		if (intent != null) {
			messageNumbers = ValueUtil
					.string2list(intent
							.getStringExtra(CardDetailActivity.CARD_DETAIL_MESSAGE_NUMBERS));
			merchantNumber = intent
					.getStringExtra(CardDetailActivity.CARD_DETAIL_MERCHANT_NUMBER);
		} else {
			Toast.makeText(CardShowMessageActivity.this, "出现错误，请重试！",
					Toast.LENGTH_SHORT).show();
			finish();
		}
		addMessageNumberButton = (Button) findViewById(R.id.activity_card_show_message_button_add);
		addMessageNumberButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CardShowMessageActivity.this,
						CardAddNumberActivity.class);
				intent.putExtra(CardDetailActivity.CARD_DETAIL_MESSAGE_NUMBERS,
						ValueUtil.list2string(messageNumbers));
				startActivityForResult(intent, 0);
			}
		});
		messageListView = (NoScrollListView) findViewById(R.id.activity_card_show_message_list);
		messageAdapter = new MessageListAdapter(CardShowMessageActivity.this);
		messageListView.setAdapter(messageAdapter);
		setTitle("短信列表");
		setBackButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		setRightButton(R.drawable.base_title_icon_delete,
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

					}
				});
		getSmsInPhone();
	}

	private void getSmsInPhone() {
		List<String> tempMessageNumbers = new ArrayList<String>();
		if (!TextUtils.isEmpty(merchantNumber)) {
			tempMessageNumbers.add(merchantNumber);
		}
		for (int i = 0; i < messageNumbers.size(); i++) {
			String messageNumber = messageNumbers.get(i);
			if (!TextUtils.isEmpty(messageNumber)) {
				tempMessageNumbers.add(messageNumber);
			} else {
				messageNumbers.remove(i);
				i--;
			}
		}
		if (tempMessageNumbers.size() == 0) {
			messageAdapter.setMessageNumber(new ArrayList<String>(),
					new ArrayList<Long>());
			return;
		}
		final String SMS_URI_INBOX = "content://sms/inbox";

		try {
			ContentResolver cr = getContentResolver();
			String[] projection = new String[] { "_id", "body", "address",
					"thread_id" };
			Uri uri = Uri.parse(SMS_URI_INBOX);

			String selections = "";
			String[] selectionArgs = new String[tempMessageNumbers.size()];
			for (int i = 0; i < tempMessageNumbers.size(); i++) {
				if (i == 0) {
					selections = "address LIKE ?";
					selectionArgs[i] = "%" + tempMessageNumbers.get(i) + "%";
				} else {
					selections += " OR address LIKE ?";
					selectionArgs[i] = "%" + tempMessageNumbers.get(i) + "%";
				}
			}
			Cursor cur = cr.query(uri, projection, selections, selectionArgs,
					"date desc");
			List<String> messages = new ArrayList<String>();
			List<Long> threadIds = new ArrayList<Long>();
			if (cur.moveToFirst()) {
				long threadId;
				String smsbody;
				do {
					threadId = cur.getLong(cur.getColumnIndex("thread_id"));
					smsbody = cur.getString(cur.getColumnIndex("body"));
					messages.add(smsbody);
					threadIds.add(threadId);
				} while (cur.moveToNext());
			} else {
				Toast.makeText(CardShowMessageActivity.this, "没有找到任何短信！",
						Toast.LENGTH_LONG).show();
			}
			messageAdapter.setMessageNumber(messages, threadIds);
		} catch (SQLiteException ex) {
			ex.printStackTrace();
		}
	}
}
