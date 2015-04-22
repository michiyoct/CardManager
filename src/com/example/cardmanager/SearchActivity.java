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
import com.example.views.NoScrollListView.OnNoScrollItemLongClcikListener;
import com.example.views.UnderLineEditText;
import com.example.views.NoScrollListView.OnNoScrollItemClickListener;

import android.app.Activity;
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
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class SearchActivity extends Activity {
	private GridView categoryGridView;
	private NoScrollListView cardListView;
	private CardInfoDBHelper cardInfoDBHelper;
	private CardListAdapter cardListAdapter;
	private CategoryGridViewAdapter categoryGridViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		cardInfoDBHelper = new CardInfoDBHelper(getApplicationContext());
		Button backButton = (Button) findViewById(R.id.activity_search_button_title_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		final UnderLineEditText searchText = (UnderLineEditText) findViewById(R.id.activity_search_text);
		Button searchButton = (Button) findViewById(R.id.activity_search_button_title_search);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String text = searchText.getText().toString();
				if (!TextUtils.isEmpty(text)) {
					query(text);
				} else {
					Toast.makeText(SearchActivity.this, "请输入要搜索的卡片名称！",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		categoryGridView = (GridView) findViewById(R.id.activity_search_grid);
		categoryGridViewAdapter = new CategoryGridViewAdapter(
				SearchActivity.this, getResources().getIntArray(
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
		cardListView = (NoScrollListView) findViewById(R.id.activity_search_list);
		cardListAdapter = new CardListAdapter(SearchActivity.this);
		cardListView.setAdapter(cardListAdapter);
		cardListView.setOnItemClickListener(new OnNoScrollItemClickListener() {

			@Override
			public void onItemClick(View v, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SearchActivity.this,
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

	}

	private void showDialog(final SimpleCardInfoUnit simpleCardInfoUnit) {
		final boolean isFrequent = simpleCardInfoUnit.isFrequent();
		final int cardStatu = simpleCardInfoUnit.getStatu();
		String frequentTip = isFrequent ? "取消常用卡标记" : "标记为常用卡";
		String statuTip = cardStatu == CardInfoUnit.STATU_USED ? "标记为未使用"
				: "标记为已使用";
		final String[] optionString = new String[] { "删除", "分享", frequentTip,
				statuTip };
		Builder dialogBuilder = new AlertDialog.Builder(SearchActivity.this);
		dialogBuilder.setTitle("选择需要进行的操作");
		dialogBuilder.setItems(optionString,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog1, int which) {
						if (which == 0) {
							new AlertDialog.Builder(SearchActivity.this)
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
										Toast.makeText(SearchActivity.this,
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
		List<String> categorys = new LinkedList<String>(
				Arrays.asList(SearchActivity.this.getResources()
						.getStringArray(R.array.category_name_default)));
		SharedPreferences sp = SearchActivity.this.getSharedPreferences(
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
	}

	private void query(int categoryId) {
		SQLiteDatabase dbReader = cardInfoDBHelper.getReadableDatabase();
		Cursor c;
		if (categoryId == -1) {
			setTitle("总卡");
			c = dbReader.query(CardInfoDBHelper.TBL_NAME_CARD,
					SimpleCardInfoUnit.columns, null, null, null, null,
					CardInfoDBHelper.CARD_CREATE_TIME);
		} else {
			setTitle(categoryGridViewAdapter.getItem(categoryId).toString());
			c = dbReader
					.query(CardInfoDBHelper.TBL_NAME_CARD,
							SimpleCardInfoUnit.columns,
							CardInfoDBHelper.CARD_CATEGORYS + " LIKE ?",
							new String[] { "%"
									+ categoryGridViewAdapter
											.getItem(categoryId) + "%" }, null,
							null, CardInfoDBHelper.CARD_CREATE_TIME);
		}
		if (c.moveToFirst()) {
			List<SimpleCardInfoUnit> simpleCardInfoUnits = new ArrayList<SimpleCardInfoUnit>();
			do {
				simpleCardInfoUnits.add(new SimpleCardInfoUnit(c));
			} while (c.moveToNext());
			cardListAdapter.setSimpleCardInfoUnitList(simpleCardInfoUnits);
		} else {
			cardListAdapter.setSimpleCardInfoUnitList(null);
			Toast.makeText(SearchActivity.this, "未能找到任何匹配结果！",
					Toast.LENGTH_LONG).show();
		}

	}

	private void query(String name) {
		categoryGridViewAdapter.setChecked(-1);
		SQLiteDatabase dbReader = cardInfoDBHelper.getReadableDatabase();
		Cursor c;
		c = dbReader.query(CardInfoDBHelper.TBL_NAME_CARD,
				SimpleCardInfoUnit.columns, CardInfoDBHelper.CARD_NAME
						+ " LIKE ?", new String[] { "%" + name + "%" }, null,
				null, CardInfoDBHelper.CARD_CREATE_TIME);

		if (c.moveToFirst()) {
			List<SimpleCardInfoUnit> simpleCardInfoUnits = new ArrayList<SimpleCardInfoUnit>();
			do {
				simpleCardInfoUnits.add(new SimpleCardInfoUnit(c));
			} while (c.moveToNext());
			cardListAdapter.setSimpleCardInfoUnitList(simpleCardInfoUnits);
		} else {
			cardListAdapter.setSimpleCardInfoUnitList(null);
			Toast.makeText(SearchActivity.this, "未能找到任何匹配结果！",
					Toast.LENGTH_LONG).show();
		}

	}
}
