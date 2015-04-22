package com.example.cardmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.example.adapter.CardListAdapter;
import com.example.assistclass.ValueUtil;
import com.example.dbhelper.CardInfoDBHelper;
import com.example.dbhelper.CardManager;
import com.example.unit.CardInfoUnit;
import com.example.unit.SimpleCardInfoUnit;
import com.example.views.DatePickerDialog;
import com.example.views.NoScrollListView;
import com.example.views.NoScrollListView.OnNoScrollItemClickListener;
import com.example.views.NoScrollListView.OnNoScrollItemLongClcikListener;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class CardListActivity extends BaseActivity {

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private int mode = 0;
	public static int MODE_NORMAL = 0;
	public static int MODE_NEW = 1;
	public static int MODE_GROUPON = 2;
	public static int MODE_EXPIRING = 3;
	public static String CARD_LIST_MODE = "mode";
	private GridView categoryGridView;
	private NoScrollListView cardListView;
	private CardInfoDBHelper cardInfoDBHelper;
	private CardListAdapter cardListAdapter;
	private CategoryGridViewAdapter categoryGridViewAdapter;
	private int categoryId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null) {
			mode = intent.getIntExtra(CARD_LIST_MODE, MODE_NORMAL);
		} else {
			Toast.makeText(CardListActivity.this, "出现错误，请重试！",
					Toast.LENGTH_SHORT).show();
			finish();
		}
		setBaseContentView(R.layout.activity_card_list);
		cardInfoDBHelper = new CardInfoDBHelper(getApplicationContext());
		categoryGridView = (GridView) findViewById(R.id.activity_category_detail_grid);
		categoryGridViewAdapter = new CategoryGridViewAdapter(
				CardListActivity.this, getResources().getIntArray(
						R.array.category_colors));
		categoryGridView.setAdapter(categoryGridViewAdapter);
		categoryGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				categoryGridViewAdapter.setChecked(arg2);
				query(arg2);
			}
		});
		cardListView = (NoScrollListView) findViewById(R.id.activity_category_detail_list);
		cardListAdapter = new CardListAdapter(CardListActivity.this);
		cardListView.setAdapter(cardListAdapter);
		cardListView.setOnItemClickListener(new OnNoScrollItemClickListener() {

			@Override
			public void onItemClick(View v, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CardListActivity.this,
						CardDetailActivity.class);
				intent.putExtra(CardDetailActivity.CARD_DETAIL_STATU,
						CardDetailActivity.STATU_SHOW);
				intent.putExtra(CardDetailActivity.CARD_DETAIL_ROW_ID, (int) id);
				startActivity(intent);
			}
		});
		cardListView
				.setOnItemLongClickListener(new OnNoScrollItemLongClcikListener() {

					@Override
					public void onItemClick(View v, int position, long id) {
						// TODO Auto-generated method stub
						showDialog((SimpleCardInfoUnit) cardListAdapter
								.getItem(position));
					}
				});
		setRightButton(R.drawable.base_title_icon_add, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CardListActivity.this,
						CardDetailActivity.class);
				intent.putExtra(CardDetailActivity.CARD_DETAIL_STATU,
						CardDetailActivity.STATU_ADD);
				startActivity(intent);
			}
		});
		setBackButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				back();
			}
		});
	}

	private void showDialog(final SimpleCardInfoUnit simpleCardInfoUnit) {
		final boolean isFrequent = simpleCardInfoUnit.isFrequent();
		final int cardStatu = simpleCardInfoUnit.getStatu();
		String frequentTip = isFrequent ? "取消常用卡标记" : "标记为常用卡";
		String statuTip = cardStatu == CardInfoUnit.STATU_USED ? "标记为未使用"
				: "标记为已使用";
		final String[] optionString = new String[] { "删除", "分享", frequentTip,
				statuTip };
		Builder dialogBuilder = new AlertDialog.Builder(CardListActivity.this);
		dialogBuilder.setTitle("选择需要进行的操作");
		dialogBuilder.setItems(optionString,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog1, int which) {
						if (which == 0) {
							new AlertDialog.Builder(CardListActivity.this)
									.setTitle("确认删除这张卡片？")
									.setPositiveButton(
											"确认",
											new android.content.DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													CardManager
															.deleteCard(simpleCardInfoUnit
																	.getRowId());
													refresh();
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
						} else if (which == 1) {
							dialog.dismiss();
							Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
							intent.setType("text/plain"); // 分享发送的数据类型
							String msg = simpleCardInfoUnit.getName();
							intent.putExtra(Intent.EXTRA_TEXT, msg); // 分享的内容
							startActivity(Intent.createChooser(intent, "选择分享"));// 目标应用选择对话框的标题
						} else if (which == 2) {
							dialog.dismiss();
							simpleCardInfoUnit.setFrequent(!isFrequent);
							ContentValues value = new ContentValues();
							value.put(CardInfoDBHelper.CARD_IS_FREQUENT,
									ValueUtil.boolean2int(!isFrequent));
							CardManager.updateCard(value,
									simpleCardInfoUnit.getRowId());
							refresh();
						} else if (which == 3) {
							dialog.dismiss();
							if (cardStatu == CardInfoUnit.STATU_USED) {
								Date vadility = null;
								try {
									SimpleDateFormat df = new SimpleDateFormat(
											"yyyy"
													+ DatePickerDialog.DATE_SEPERATOR
													+ "MM"
													+ DatePickerDialog.DATE_SEPERATOR
													+ "dd");
									vadility = df.parse(simpleCardInfoUnit
											.getValidityEnd());
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (vadility == null) {
									simpleCardInfoUnit
											.setStatu(CardInfoUnit.STATU_NONE);
								} else {
									Calendar calendar = Calendar.getInstance();
									Date now = calendar.getTime();
									long dayOffset = (vadility.getTime() - now
											.getTime());
									if (dayOffset < 0) {
										Toast.makeText(CardListActivity.this,
												"当前卡已过有效期，不能标记为未使用！",
												Toast.LENGTH_LONG).show();
									} else if (dayOffset >= 0
											&& dayOffset <= ((long) MainActivity.EXPIRING_OFFSET)
													* 24 * 60 * 60 * 1000) {
										simpleCardInfoUnit
												.setStatu(CardInfoUnit.STATU_EXPIRING);
									} else {
										simpleCardInfoUnit
												.setStatu(CardInfoUnit.STATU_NONE);
									}
								}
							} else {
								simpleCardInfoUnit
										.setStatu(CardInfoUnit.STATU_USED);
							}
							ContentValues value = new ContentValues();
							value.put(CardInfoDBHelper.CARD_STATUS,
									simpleCardInfoUnit.getStatu());
							CardManager.updateCard(value,
									simpleCardInfoUnit.getRowId());
							refresh();
						}
					}
				});
		dialog = dialogBuilder.create();
		dialog.show();
	}

	private AlertDialog dialog;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refresh();
	}

	private void refresh() {
		categoryGridViewAdapter.setChecked(-1);
		if (mode == MODE_NORMAL) {
			List<String> categorys = new LinkedList<String>(
					Arrays.asList(CardListActivity.this.getResources()
							.getStringArray(R.array.category_name_default)));
			SharedPreferences sp = CardListActivity.this.getSharedPreferences(
					MainActivity.SHARE_PREFERENCE_CATEGORYS, 0);
			Map<String, String> map = (Map<String, String>) sp.getAll();
			for (String value : map.values()) {
				categorys.add(value);
			}
			categoryGridViewAdapter.setCategorys(categorys);
			int height = MainActivity.TEXT_SIZE_MEDIUM + 2
					* MainActivity.OFFSET_LESS + 2 * MainActivity.OFFSET_MEDIUM;
			int line = (categorys.size() - 1) / 3 + 1;
			categoryGridView.setLayoutParams(new LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, line * height));
			query(-1);
		} else if (mode == MODE_NEW) {
			categoryGridView.setVisibility(View.GONE);
			query(-1);
		} else if (mode == MODE_GROUPON) {
			categoryGridView.setVisibility(View.GONE);
			query("团购券");
		} else if (mode == MODE_EXPIRING) {
			categoryGridView.setVisibility(View.GONE);
			query(-1);
		}
	}

	private void query(String category) {
		SQLiteDatabase dbReader = cardInfoDBHelper.getReadableDatabase();
		Cursor c = null;
		if (TextUtils.isEmpty(category)) {
			if (mode == MODE_NORMAL) {
				setTitle("总卡");
				c = dbReader.query(CardInfoDBHelper.TBL_NAME_CARD,
						SimpleCardInfoUnit.columns, null, null, null, null,
						CardInfoDBHelper.CARD_CREATE_TIME);
			} else if (mode == MODE_NEW) {
				setTitle("新增卡");
				long nowTime = Calendar.getInstance().getTimeInMillis();
				long newTime = nowTime
						- MainActivity.EXPIRING_OFFSET * 24 * 60 * 60 * 1000;
				c = dbReader.query(CardInfoDBHelper.TBL_NAME_CARD,
						SimpleCardInfoUnit.columns,
						CardInfoDBHelper.CARD_CREATE_TIME + "> ?",
						new String[] { newTime + "" }, null, null,
						CardInfoDBHelper.CARD_CREATE_TIME);
			} else if (mode == MODE_EXPIRING) {
				setTitle("即将过期卡");
				c = dbReader.query(CardInfoDBHelper.TBL_NAME_CARD,
						SimpleCardInfoUnit.columns,
						CardInfoDBHelper.CARD_STATUS + "= ?",
						new String[] { CardInfoUnit.STATU_EXPIRING + "" },
						null, null, CardInfoDBHelper.CARD_CREATE_TIME);
			}
		} else {
			setTitle(category);
			c = dbReader.query(CardInfoDBHelper.TBL_NAME_CARD,
					SimpleCardInfoUnit.columns, CardInfoDBHelper.CARD_CATEGORYS
							+ " LIKE ?", new String[] { "%" + category + "%" },
					null, null, CardInfoDBHelper.CARD_CREATE_TIME);
		}
		if (c.moveToFirst()) {
			List<SimpleCardInfoUnit> simpleCardInfoUnits = new ArrayList<SimpleCardInfoUnit>();
			do {
				simpleCardInfoUnits.add(new SimpleCardInfoUnit(c));
			} while (c.moveToNext());
			cardListAdapter.setSimpleCardInfoUnitList(simpleCardInfoUnits);
		} else {
			cardListAdapter.setSimpleCardInfoUnitList(null);
			Toast.makeText(CardListActivity.this, "未能找到任何匹配结果！",
					Toast.LENGTH_LONG).show();
		}
	}

	private void query(int categoryId) {
		this.categoryId = categoryId;
		if (categoryId == -1) {
			query(null);
		} else {
			query(categoryGridViewAdapter.getItem(categoryId).toString());
		}

	}

	private void back() {
		if (categoryId == -1)
			finish();
		else {
			categoryGridViewAdapter.setChecked(-1);
			query(-1);
		}
	}

}
