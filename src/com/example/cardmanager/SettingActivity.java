package com.example.cardmanager;

import com.example.views.NoScrollListView;
import com.example.views.NoScrollListView.OnNoScrollItemClickListener;
import com.example.views.SlideSwitch;
import com.example.views.SlideSwitch.OnChangedListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SettingActivity extends BaseActivity {
	private final static String[] options = new String[] { "清除缓存", "设置锁屏界面",
			"数据云同步", "评价一下吧", "检查更新", "软件分享" };
	private SlideSwitch noticeBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_setting);
		setTitle("设置");
		setRightButton(-1, null);
		NoScrollListView listView = (NoScrollListView) findViewById(R.id.activity_setting_listview);
		listView.setOnItemClickListener(new OnNoScrollItemClickListener() {

			@Override
			public void onItemClick(View v, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0: {
					break;
				}
				case 1: {
					break;
				}
				case 2: {
					break;
				}
				case 3: {
					Intent intent = new Intent(SettingActivity.this,
							FeedbackActivity.class);
					startActivity(intent);
					break;
				}
				case 4: {
					Toast.makeText(SettingActivity.this, "已经是最新版本",
							Toast.LENGTH_LONG).show();
					break;
				}
				case 5: {
					final EditText inputServer = new EditText(
							SettingActivity.this);
					inputServer.setSingleLine(false);
					inputServer.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE
							| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
					inputServer.setFocusable(true);
					inputServer.setText("对数量繁多，种类五花八门的卡的管理");
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SettingActivity.this);
					builder.setTitle("请输入分享信息:").setView(inputServer)
							.setNegativeButton("取消", null);
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String shareInfo = inputServer.getText()
											.toString();
									if (TextUtils.isEmpty(shareInfo)) {
										Toast.makeText(getApplicationContext(),
												"分享内容不能为空！", Toast.LENGTH_SHORT)
												.show();
									} else {
										Intent intent = new Intent(
												Intent.ACTION_SEND); // 启动分享发送的属性
										intent.setType("text/plain"); // 分享发送的数据类型
										intent.putExtra(Intent.EXTRA_TEXT,
												shareInfo); // 分享的内容
										startActivity(Intent.createChooser(
												intent, "选择分享"));// 目标应用选择对话框的标题
									}
								}
							});
					builder.show();
					break;
				}

				}
			}
		});
		listView.setAdapter(new SettingListAdapter());
		final SharedPreferences sp = getSharedPreferences(
				MainActivity.SHARE_PREFERENCE, 0);
		boolean isNotice = sp.getBoolean(MainActivity.SHARE_PREFERENCE_NOTICE,
				false);
		noticeBox = (SlideSwitch) findViewById(R.id.activity_setting_slideswitch);
		noticeBox.setLayoutParams(new LayoutParams(
				MainActivity.ICON_SIZE_MEDIUM * 2,
				MainActivity.ICON_SIZE_MEDIUM));
		noticeBox.setChecked(isNotice);
		noticeBox.setOnChangedListener(new OnChangedListener() {

			@Override
			public void OnChanged(SlideSwitch wiperSwitch, boolean checkState) {
				// TODO Auto-generated method stub
				Editor editor = sp.edit();
				editor.putBoolean(MainActivity.SHARE_PREFERENCE_NOTICE,
						checkState);
				editor.putLong(MainActivity.SHARE_PREFERENCE_NOTICE_TIME, 0);
				editor.commit();
			}
		});
	}

	class SettingListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return options.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return options[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			Viewholder viewholder = null;
			if (arg1 == null) {
				arg1 = new LinearLayout(SettingActivity.this);
				arg1.setBackgroundResource(R.drawable.background_corner_radius_all_ffffffff);
				((LinearLayout) arg1).setGravity(Gravity.CENTER_VERTICAL);
				((LinearLayout) arg1).setPadding(MainActivity.OFFSET_MEDIUM,
						MainActivity.OFFSET_MEDIUM, MainActivity.OFFSET_MEDIUM,
						MainActivity.OFFSET_MEDIUM);
				TextView textView = new TextView(SettingActivity.this);
				textView.setLayoutParams(new LinearLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));
				textView.setTextColor(0xff002E34);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						MainActivity.TEXT_SIZE_MEDIUM);
				ImageView imageView = new ImageView(SettingActivity.this);
				imageView.setImageResource(R.drawable.activity_setting_arrow);
				((LinearLayout) arg1).addView(textView);
				((LinearLayout) arg1).addView(imageView);
				viewholder = new Viewholder();
				viewholder.textView = textView;
				arg1.setTag(viewholder);
			} else {
				viewholder = (Viewholder) arg1.getTag();
			}
			viewholder.textView.setText(options[arg0]);
			return arg1;
		}
	}

	static class Viewholder {

		TextView textView;

	}
}
