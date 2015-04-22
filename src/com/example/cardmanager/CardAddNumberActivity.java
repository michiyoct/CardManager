package com.example.cardmanager;

import java.util.ArrayList;
import java.util.List;

import com.example.assistclass.ValueUtil;
import com.example.views.NoScrollListView;
import com.example.views.NoScrollListView.OnNoScrollItemClickListener;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class CardAddNumberActivity extends BaseActivity {
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(CardDetailActivity.CARD_DETAIL_MESSAGE_NUMBERS,
				ValueUtil.list2string(messageNumbers));
		setResult(RESULT_OK, intent);
		super.finish();
	}

	private NoScrollListView listView;
	private TextView addTextView;
	private LinearLayout editLayout;
	private TextView tipTextView;
	private AutoCompleteTextView inputEditText;
	private List<String> messageNumbers;
	private MessageNumberAdapter messageNumberAdapter = new MessageNumberAdapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_card_add_number);
		listView = (NoScrollListView) findViewById(R.id.activity_card_add_number_list);
		listView.setAdapter(messageNumberAdapter);
		Intent intent = getIntent();
		if (intent != null) {
			messageNumbers = ValueUtil
					.string2list(intent
							.getStringExtra(CardDetailActivity.CARD_DETAIL_MESSAGE_NUMBERS));
			messageNumberAdapter.setMessageNumbers(messageNumbers);
		} else {
			Toast.makeText(CardAddNumberActivity.this, "出现错误，请重试！",
					Toast.LENGTH_SHORT).show();
			finish();
		}
		addTextView = (TextView) findViewById(R.id.activity_card_add_number_text_add);
		editLayout = (LinearLayout) findViewById(R.id.activity_card_add_number_layout_edit);
		tipTextView = (TextView) findViewById(R.id.activity_card_add_number_edit_text);
		inputEditText = (AutoCompleteTextView) findViewById(R.id.activity_card_add_number_edit_edit);
		initAutoComplete();
		addTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addTextView.setVisibility(View.GONE);
				editLayout.setVisibility(View.VISIBLE);
				tipTextView.setText("短信抓取号码"
						+ (messageNumberAdapter.getCount() + 1) + ":");
			}
		});
		inputEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg1 == EditorInfo.IME_ACTION_DONE) {
					inputFinished(arg0.getText().toString());
				}
				return false;
			}
		});
		setBackButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		setTitle("短信抓取号码");
		setRightButton(R.drawable.base_title_icon_save, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (editLayout.getVisibility() == View.VISIBLE) {
					inputFinished(inputEditText.getText().toString());
				} else {
					finish();
				}
			}
		});
		listView.setOnItemClickListener(new OnNoScrollItemClickListener() {

			@Override
			public void onItemClick(View v, final int position, long id) {
				// TODO Auto-generated method stub
				CharSequence[] items = { "删除" };
				Builder dialogBuilder = new AlertDialog.Builder(
						CardAddNumberActivity.this);
				dialogBuilder.setTitle("选择需要进行的操作");
				dialogBuilder.setItems(items,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog1,
									int which) {
								if (which == 0) {
									new AlertDialog.Builder(
											CardAddNumberActivity.this)
											.setTitle("确认删除这条号码？")
											.setPositiveButton(
													"确认",
													new android.content.DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface arg0,
																int arg1) {
															// TODO
															// Auto-generated
															// method stub
															deleteMessageNumber(position);
															dialog.dismiss();
														}
													})
											.setNegativeButton(
													"取消",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface arg0,
																int arg1) {
															// TODO
															// Auto-generated
															// method stub
															dialog.dismiss();
														}
													}).create().show();
								}
							}
						});
				dialog = dialogBuilder.create();
				dialog.show();
			}
		});
	}

	private void initAutoComplete() {
		List<String> messageNumbers = new ArrayList<String>();
		final String SMS_URI_INBOX = "content://sms/inbox";

		try {
			ContentResolver cr = getContentResolver();
			String[] projection = new String[] { "address" };
			Uri uri = Uri.parse(SMS_URI_INBOX);

			Cursor cur = cr.query(uri, projection, null, null, "date desc");
			if (cur.moveToFirst()) {
				do {
					messageNumbers.add(cur.getString(cur
							.getColumnIndex("address")));
				} while (cur.moveToNext());
			}
		} catch (SQLiteException ex) {
			ex.printStackTrace();
		}
		inputEditText.setAdapter(new ArrayAdapter<String>(
				CardAddNumberActivity.this,
				android.R.layout.simple_dropdown_item_1line, messageNumbers));
	}

	private AlertDialog dialog;

	private void deleteMessageNumber(int position) {
		messageNumbers.remove(position);
		messageNumberAdapter.setMessageNumbers(messageNumbers);
	}

	private void inputFinished(String input) {
		if (!TextUtils.isEmpty(input)) {
			messageNumbers.add(input);
			messageNumberAdapter.setMessageNumbers(messageNumbers);
		}
		inputEditText.setText("");
		editLayout.setVisibility(View.GONE);
		addTextView.setVisibility(View.VISIBLE);
	}

	class MessageNumberAdapter extends BaseAdapter {
		public void setMessageNumbers(List<String> messageNumbers) {
			this.messageNumbers = messageNumbers;
			notifyDataSetChanged();
		}

		private List<String> messageNumbers;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (messageNumbers == null)
				return 0;
			return messageNumbers.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return messageNumbers.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			String TAG_TIP = "tip";
			String TAG_NUMBER = "number";
			if (arg1 == null) {
				arg1 = new LinearLayout(CardAddNumberActivity.this);
				arg1.setBackgroundResource(R.drawable.background_corner_radius_all);
				TextView tipTextView = new TextView(CardAddNumberActivity.this);
				LayoutParams textLP = new LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				tipTextView.setLayoutParams(textLP);
				tipTextView.setTextColor(0xff7d7d7d);
				tipTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						MainActivity.TEXT_SIZE_MEDIUM);
				tipTextView.setTag(TAG_TIP);
				TextView numberTextView = new TextView(
						CardAddNumberActivity.this);
				LayoutParams numberTextLP = new LayoutParams(
						android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				numberTextView.setLayoutParams(numberTextLP);
				numberTextView.setTextColor(0xff4a4a4a);
				numberTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						MainActivity.TEXT_SIZE_MEDIUM);
				numberTextView.setTag(TAG_NUMBER);
				((LinearLayout) arg1).addView(tipTextView);
				((LinearLayout) arg1).addView(numberTextView);
				((LinearLayout) arg1).setOrientation(LinearLayout.HORIZONTAL);
				((LinearLayout) arg1).setPadding(MainActivity.OFFSET_LARGER,
						MainActivity.OFFSET_MEDIUM, MainActivity.OFFSET_LARGER,
						MainActivity.OFFSET_MEDIUM);
			}
			TextView tipTextView = (TextView) arg1.findViewWithTag(TAG_TIP);
			tipTextView.setText("短信抓取号码" + (arg0 + 1) + ":");
			TextView numberTextView = (TextView) arg1
					.findViewWithTag(TAG_NUMBER);
			numberTextView.setText(messageNumbers.get(arg0));
			return arg1;
		}

	}
}
