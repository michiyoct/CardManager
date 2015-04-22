package com.example.cardmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends BaseActivity {
	private EditText contentEditText;
	private String email;
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_feedback);
		setTitle("意见反馈");
		contentEditText = (EditText) findViewById(R.id.activity_feedback_edit_content);
		TextView emailTextView = (TextView) findViewById(R.id.activity_feedback_text_mailbox);
		emailTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final EditText inputServer = new EditText(FeedbackActivity.this);
				inputServer
						.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				inputServer.setFocusable(true);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						FeedbackActivity.this);
				builder.setTitle("请输入您的邮箱").setView(inputServer)
						.setNegativeButton("取消", null);
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								email = inputServer.getText().toString();
							}
						});
				builder.show();
			}
		});
		TextView phoneTextView = (TextView) findViewById(R.id.activity_feedback_text_phone);
		phoneTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final EditText inputServer = new EditText(FeedbackActivity.this);
				inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
				inputServer.setFocusable(true);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						FeedbackActivity.this);
				builder.setTitle("请输入您的手机号码").setView(inputServer)
						.setNegativeButton("取消", null);
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								phone = inputServer.getText().toString();
							}
						});
				builder.show();
			}
		});
		setRightButton(R.drawable.base_title_icon_save, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String content = contentEditText.getText().toString();
				if (TextUtils.isEmpty(content)) {
					Toast.makeText(FeedbackActivity.this, "请给出您的宝贵意见，谢谢！",
							Toast.LENGTH_LONG).show();
				} else {
					sendFeedBack(content, phone, email);
					Toast.makeText(FeedbackActivity.this, "反馈成功，谢谢您的评价！",
							Toast.LENGTH_LONG).show();
					finish();
				}
			}

		});
	}

	private void sendFeedBack(String content, String phone, String email) {
		// TODO Auto-generated method stub

	}
}
