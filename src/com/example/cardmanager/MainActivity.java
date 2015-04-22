package com.example.cardmanager;

import com.example.adapter.FrequentCardListAdapter;
import com.example.dbhelper.CardInfoDBHelper;
import com.example.dbhelper.CardManager;
import com.example.service.NoticeService;
import com.example.views.NoScrollListView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {

	public static int WIDTH;
	public static int HEIGHT;
	public static int OFFSET_SMALL;
	public static int OFFSET_LESS;
	public static int OFFSET_MEDIUM;
	public static int OFFSET_LARGER;
	public static int OFFSET_LARGE;
	public static int ICON_SIZE_MEDIUM;
	public static int ICON_SIZE_LESS;
	public static int TEXT_SIZE_LARGE;
	public static int TEXT_SIZE_MEDIUM;
	public static int TEXT_SIZE_SMALL;
	public static int CARD_IMAGE_WIDTH;
	public static int CARD_IMAGE_HEIGHT;
	public static int COUNT_FREQUENT_CARD = 5;
	public static int EXPIRING_OFFSET = 7;
	public final static String SHARE_PREFERENCE = "sharepreference";
	public final static String SHARE_PREFERENCE_NOTICE = "notice";
	public final static String SHARE_PREFERENCE_NOTICE_TIME = "noticeTime";
	public final static String SHARE_PREFERENCE_CATEGORYS = "categorys";
	private Button triangleButton1;
	private Button triangleButton2;
	private Button triangleButton3;
	private Button addButton;
	private TextView tipTextView;
	private NoScrollListView frequentCardListView;
	public static float rate = 1.0f;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		startService(new Intent(MainActivity.this, NoticeService.class));
		CardInfoDBHelper.initCardInfoDBHelper(getApplicationContext());
		setContentView(R.layout.activity_main);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		WIDTH = metric.widthPixels;
		HEIGHT = metric.heightPixels;
		Resources r = getResources();
		OFFSET_MEDIUM = r.getDimensionPixelOffset(R.dimen.offset_medium);
		OFFSET_SMALL = r.getDimensionPixelOffset(R.dimen.offset_small);
		OFFSET_LARGE = r.getDimensionPixelOffset(R.dimen.offset_large);
		OFFSET_LARGER = r.getDimensionPixelOffset(R.dimen.offset_larger);
		OFFSET_LESS = r.getDimensionPixelOffset(R.dimen.offset_less);
		ICON_SIZE_MEDIUM = r.getDimensionPixelOffset(R.dimen.icon_size_medium);
		ICON_SIZE_LESS = r.getDimensionPixelSize(R.dimen.icon_size_less);
		TEXT_SIZE_LARGE = r.getDimensionPixelSize(R.dimen.text_size_large);
		TEXT_SIZE_MEDIUM = r.getDimensionPixelSize(R.dimen.text_size_medium);
		TEXT_SIZE_SMALL = r.getDimensionPixelSize(R.dimen.text_size_small);
		CARD_IMAGE_WIDTH = r.getDimensionPixelOffset(R.dimen.card_image_width);
		CARD_IMAGE_HEIGHT = r
				.getDimensionPixelOffset(R.dimen.card_image_height);
		rate = (WIDTH / 2 - OFFSET_LESS) / 355.0f;
		triangleButton1 = (Button) findViewById(R.id.activity_main_triangle_button_1);
		triangleButton2 = (Button) findViewById(R.id.activity_main_triangle_button_2);
		triangleButton3 = (Button) findViewById(R.id.activity_main_triangle_button_3);
		addButton = (Button) findViewById(R.id.activity_main_button_add);
		tipTextView = (TextView) findViewById(R.id.activity_main_text_tip);
		frequentCardListView = (NoScrollListView) findViewById(R.id.activity_main_list_frequent_card);
		initPosition();
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						CardDetailActivity.class);
				intent.putExtra(CardDetailActivity.CARD_DETAIL_STATU,
						CardDetailActivity.STATU_ADD);
				startActivity(intent);
			}
		});
		triangleButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						CardListActivity.class);
				startActivity(intent);
			}
		});
		triangleButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						CardListActivity.class);
				intent.putExtra(CardListActivity.CARD_LIST_MODE,
						CardListActivity.MODE_GROUPON);
				startActivity(intent);
			}
		});
		triangleButton3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						CardListActivity.class);
				intent.putExtra(CardListActivity.CARD_LIST_MODE,
						CardListActivity.MODE_NEW);
				startActivity(intent);
			}
		});
		Button searchButton = (Button) findViewById(R.id.activity_main_top_button_search);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						SearchActivity.class);
				startActivity(intent);
			}
		});
		Button importButton = (Button) findViewById(R.id.activity_main_top_button_inport);
		importButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						ImportMessageActivity.class);
				startActivity(intent);
			}
		});
		Button settingButton = (Button) findViewById(R.id.activity_main_top_button_set);
		settingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						SettingActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initPosition() {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_main_layout_top);
		rl.setLayoutParams(new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, MainActivity.OFFSET_MEDIUM * 2
						+ MainActivity.ICON_SIZE_MEDIUM));
		RelativeLayout.LayoutParams triangleButtonLP1 = new RelativeLayout.LayoutParams(
				(int) (221 * rate), (int) (169 * rate));
		triangleButtonLP1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		triangleButtonLP1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		RelativeLayout.LayoutParams triangleButtonLP2 = new RelativeLayout.LayoutParams(
				(int) (226 * rate), (int) (180 * rate));
		triangleButtonLP2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		triangleButtonLP2.addRule(RelativeLayout.BELOW,
				R.id.activity_main_triangle_button_1);
		triangleButtonLP2.setMargins(0, (int) (12 * rate), 0, 0);
		RelativeLayout.LayoutParams triangleButtonLP3 = new RelativeLayout.LayoutParams(
				(int) (161 * rate), (int) (258 * rate));
		triangleButtonLP3.addRule(RelativeLayout.CENTER_VERTICAL);
		triangleButtonLP3.addRule(RelativeLayout.RIGHT_OF,
				R.id.activity_main_triangle_button_1);
		triangleButtonLP3.setMargins((int) (-32 * rate), 0, 0, 0);
		triangleButton1.setLayoutParams(triangleButtonLP1);
		triangleButton2.setLayoutParams(triangleButtonLP2);
		triangleButton3.setLayoutParams(triangleButtonLP3);
		RelativeLayout.LayoutParams addButtonLP = new RelativeLayout.LayoutParams(
				(int) (136 * rate), (int) (136 * rate));
		addButtonLP.addRule(RelativeLayout.ALIGN_TOP,
				R.id.activity_main_triangle_button_3);
		addButtonLP.addRule(RelativeLayout.LEFT_OF,
				R.id.activity_main_triangle_button_3);
		addButtonLP.setMargins(0, (int) (108 * rate), 0, 0);
		addButton.setLayoutParams(addButtonLP);
		RelativeLayout.LayoutParams tipTextLP = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, (int) (101 * rate));
		tipTextLP.addRule(RelativeLayout.RIGHT_OF,
				R.id.activity_main_triangle_button_3);
		tipTextLP.addRule(RelativeLayout.CENTER_VERTICAL);
		tipTextLP.setMargins((int) (-10 * rate), 0, 0, 0);
		tipTextView.setLayoutParams(tipTextLP);
		tipTextView.setPadding((int) (30 * rate), 0, (int) (20 * rate), 0);
		tipTextView.setText("暂时没有任何提示信息");
		tipTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						CardListActivity.class);
				intent.putExtra(CardListActivity.CARD_LIST_MODE,
						CardListActivity.MODE_EXPIRING);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initFrequentCard();
		refreshNotice();
	}

	private void initFrequentCard() {
		frequentCardListView.setAdapter(new FrequentCardListAdapter(
				MainActivity.this, CardManager.getFrequentCard()));
	}

	private void refreshNotice() {
		int count = CardManager.getExpiringCard().size();
		if (count > 0)
			tipTextView.setText("您有" + count + "张卡片即将过期");
		else
			tipTextView.setText("暂时没有任何提示信息");
	}
}
