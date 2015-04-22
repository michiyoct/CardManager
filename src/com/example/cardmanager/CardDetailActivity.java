package com.example.cardmanager;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.baidu.mapapi.BMapManager;
import com.example.assistclass.BitmapUtil;
import com.example.assistclass.DensityUtil;
import com.example.assistclass.ValueUtil;
import com.example.dbhelper.CardInfoDBHelper;
import com.example.dbhelper.CardManager;
import com.example.unit.CardInfoUnit;
import com.example.views.CardDetailEditView;
import com.example.views.CardDetailShowView;
import com.example.views.CardDetailViewInterface;
import com.example.views.DatePickerDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CardDetailActivity extends BaseActivity {

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			showOptionDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public final static String CARD_DETAIL_STATU = "cardDetailStatu";
	public final static String CARD_DETAIL_ROW_ID = "rowId";
	public final static String CARD_DETAIL_IMAGE_PATHS = "imagePaths";
	public final static String CARD_DETAIL_ICON_POSITION = "iconPosition";
	public final static String CARD_DETAIL_MESSAGE_NUMBERS = "messageNumbers";
	public final static String CARD_DETAIL_MERCHANT_NUMBER = "merchantNumber";
	public final static String CARD_DETAIL_CARD_UNIT = "cardUnit";
	public final static int STATU_ADD = 0;
	public final static int STATU_EDIT = 1;
	public final static int STATU_SHOW = 2;
	public final static int STATU_ADD_FROM_MESSAGE = 3;
	private CardInfoUnit cardInfoUnit;
	private List<String> imagePaths;
	private int iconPosition = -1;
	private List<String> messageNumbers;
	private int statu = -1;
	private int rowId = -1;
	private CardDetailViewInterface mCardDetailViewInterface;
	public static BMapManager mBMapManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mBMapManager = new BMapManager(getApplicationContext());
		mBMapManager.init(null);
		super.onCreate(savedInstanceState);
		int statu = -1;
		Intent intent = getIntent();
		if (intent != null) {
			statu = intent.getIntExtra(CARD_DETAIL_STATU, -1);
			if (statu == -1) {
				Toast.makeText(CardDetailActivity.this, "出现错误，请重试！",
						Toast.LENGTH_SHORT).show();
				finish();
			} else if (statu == STATU_SHOW) {
				rowId = intent.getIntExtra(CARD_DETAIL_ROW_ID, -1);
				if (rowId == -1) {
					Toast.makeText(CardDetailActivity.this, "出现错误，请重试！",
							Toast.LENGTH_SHORT).show();
					finish();
				}
			} else if (statu == STATU_ADD_FROM_MESSAGE) {
				cardInfoUnit = (CardInfoUnit) intent
						.getSerializableExtra(CARD_DETAIL_CARD_UNIT);
				statu = STATU_ADD;
			}
		} else {
			Toast.makeText(CardDetailActivity.this, "出现错误，请重试！",
					Toast.LENGTH_SHORT).show();
			finish();
		}
		setBackButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				back();
			}
		});
		changeStatu(statu);
	}

	private void changeStatu(int statu) {
		if (statu == -1) {
			Toast.makeText(CardDetailActivity.this, "出现错误，请重试！",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		this.statu = statu;
		if (statu == STATU_ADD) {
			final CardDetailEditView cardDetailEditView = new CardDetailEditView(
					CardDetailActivity.this);
			cardDetailEditView.setContent(cardInfoUnit);
			cardDetailEditView
					.setOnPhotoButtonClickListener(new OnPhotoButtonClickListener());
			cardDetailEditView
					.setOnMessageButtonClickListener(new OnMessageBarClickListener());
			setBaseContentView(cardDetailEditView);
			setRightButton(R.drawable.base_title_icon_save,
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (cardDetailEditView.isValid()) {
								cardInfoUnit = cardDetailEditView
										.getCardInfoUnit();
								cardInfoUnit.setImagePaths(imagePaths);
								if (iconPosition != -1)
									cardInfoUnit.setIconPath(imagePaths
											.get(iconPosition));
								cardInfoUnit.setMessageNumbers(messageNumbers);
								rowId = CardManager.insertCard(cardInfoUnit);
								cardInfoUnit.setRowId(rowId);
								changeStatu(STATU_SHOW);
							} else {
								Toast.makeText(CardDetailActivity.this,
										"请至少输入卡片名称并且选择至少一个标签！",
										Toast.LENGTH_LONG).show();
							}
						}
					});
			mCardDetailViewInterface = cardDetailEditView;
		} else if (statu == STATU_SHOW) {
			if (rowId == -1) {
				Toast.makeText(CardDetailActivity.this, "出现错误，请重试！",
						Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			if (cardInfoUnit == null) {
				cardInfoUnit = CardManager.getCard(rowId);
				if (cardInfoUnit == null) {
					Toast.makeText(CardDetailActivity.this, "找不到该数据，请重试！",
							Toast.LENGTH_LONG).show();
					finish();
					return;
				}
			}
			imagePaths = cardInfoUnit.getImagePaths();
			messageNumbers = cardInfoUnit.getMessageNumbers();
			if (imagePaths == null)
				iconPosition = -1;
			else
				iconPosition = imagePaths.indexOf(cardInfoUnit.getIconPath());
			final CardDetailShowView cardDetailShowView = new CardDetailShowView(
					CardDetailActivity.this);
			cardDetailShowView.setContent(cardInfoUnit);
			cardDetailShowView
					.setOnPhotoButtonClickListener(new OnPhotoButtonClickListener());
			cardDetailShowView
					.setOnMessageButtonClickListener(new OnMessageBarClickListener());
			setBaseContentView(cardDetailShowView);
			setRightButton(R.drawable.top_button_set, new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showOptionDialog();
				}
			});
			mCardDetailViewInterface = cardDetailShowView;
		} else if (statu == STATU_EDIT) {
			final CardDetailEditView cardDetailEditView = new CardDetailEditView(
					CardDetailActivity.this);
			cardDetailEditView.setContent(cardInfoUnit);
			cardDetailEditView
					.setOnPhotoButtonClickListener(new OnPhotoButtonClickListener());
			cardDetailEditView
					.setOnMessageButtonClickListener(new OnMessageBarClickListener());
			setBaseContentView(cardDetailEditView);
			setRightButton(R.drawable.base_title_icon_save,
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (cardDetailEditView.isValid()) {
								cardInfoUnit = cardDetailEditView
										.getCardInfoUnit();
								cardInfoUnit.setImagePaths(imagePaths);
								if (iconPosition != -1)
									cardInfoUnit.setIconPath(imagePaths
											.get(iconPosition));
								cardInfoUnit.setMessageNumbers(messageNumbers);
								CardManager.updateCard(
										cardInfoUnit.getDBValue(), rowId);
								changeStatu(STATU_SHOW);
							} else {
								Toast.makeText(CardDetailActivity.this,
										"请至少输入卡片名称并且选择至少一个标签！",
										Toast.LENGTH_LONG).show();
							}
						}
					});
			mCardDetailViewInterface = cardDetailEditView;
		}
	}

	private void back() {
		if (statu == STATU_SHOW) {
			finish();
		} else if (statu == STATU_EDIT) {
			new AlertDialog.Builder(CardDetailActivity.this)
					.setTitle("确认退出？已更改信息将会丢失")
					.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									changeStatu(STATU_SHOW);
								}
							}).setNegativeButton("取消", null).show();
		} else if (statu == STATU_ADD) {
			new AlertDialog.Builder(CardDetailActivity.this)
					.setTitle("确认退出？已输入信息将会丢失")
					.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									if (imagePaths != null) {
										for (String imagePath : imagePaths) {
											File file = new File(imagePath);
											if (file.exists())
												file.delete();
										}
									}
									finish();
								}
							}).setNegativeButton("取消", null).show();
		}
	}

	private void showOptionDialog() {
		final boolean isFrequent = cardInfoUnit.isFrequent();
		final int cardStatu = cardInfoUnit.getStatu();
		String frequentTip = isFrequent ? "取消常用卡标记" : "标记为常用卡";
		String statuTip = cardStatu == CardInfoUnit.STATU_USED ? "标记为未使用"
				: "标记为已使用";
		final String[] optionString = new String[] { "编辑", "删除", "分享",
				frequentTip, statuTip };
		final Dialog dialog = new Dialog(CardDetailActivity.this,
				R.style.Dialog);
		ListView view = new ListView(CardDetailActivity.this);
		view.setDivider(new ColorDrawable(0xffc9c9c9));
		view.setDividerHeight(DensityUtil.dip2px(getApplicationContext(), 1));
		view.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				if (arg1 == null) {
					arg1 = new LinearLayout(CardDetailActivity.this);
					((LinearLayout) arg1)
							.setOrientation(LinearLayout.HORIZONTAL);
					arg1.setPadding(MainActivity.OFFSET_MEDIUM,
							MainActivity.OFFSET_MEDIUM,
							MainActivity.OFFSET_MEDIUM,
							MainActivity.OFFSET_MEDIUM);
					ImageView imageView = new ImageView(CardDetailActivity.this);
					LayoutParams imageLP = new LayoutParams(
							MainActivity.ICON_SIZE_LESS,
							MainActivity.ICON_SIZE_LESS);
					imageLP.gravity = Gravity.CENTER_VERTICAL;
					imageView.setLayoutParams(imageLP);
					imageView.setTag("ImageView");
					TextView textView = new TextView(CardDetailActivity.this);
					LayoutParams textLP = new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					textLP.gravity = Gravity.CENTER_VERTICAL;
					textLP.setMargins(MainActivity.OFFSET_MEDIUM, 0, 0, 0);
					textView.setLayoutParams(textLP);
					textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							MainActivity.TEXT_SIZE_MEDIUM);
					textView.setTextColor(0xff717c7d);
					textView.setSingleLine();
					textView.setTag("TextView");
					((LinearLayout) arg1).addView(imageView);
					((LinearLayout) arg1).addView(textView);
				}
				((ImageView) arg1.findViewWithTag("ImageView"))
						.setImageResource(R.drawable.card_detail_setting_option_1
								+ arg0);
				((TextView) arg1.findViewWithTag("TextView"))
						.setText(optionString[arg0]);
				return arg1;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return optionString[arg0];
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return optionString.length;
			}
		});
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0: {
					changeStatu(STATU_EDIT);
					dialog.dismiss();
					break;
				}
				case 1: {
					dialog.dismiss();
					new AlertDialog.Builder(CardDetailActivity.this)
							.setTitle("确认删除？")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											CardManager.deleteCard(rowId);
											CardDetailActivity.this.finish();
										}
									}).setNegativeButton("取消", null).create()
							.show();
					break;
				}
				case 2: {
					dialog.dismiss();
					final EditText inputServer = new EditText(
							CardDetailActivity.this);
					inputServer
							.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
					inputServer.setSingleLine(false);
					inputServer.setGravity(Gravity.TOP);
					inputServer.setFocusable(true);
					inputServer.setText(cardInfoUnit.getShareInfo());
					AlertDialog.Builder builder = new AlertDialog.Builder(
							CardDetailActivity.this);
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
				case 3: {
					dialog.dismiss();
					cardInfoUnit.setFrequent(!isFrequent);
					ContentValues value = new ContentValues();
					value.put(CardInfoDBHelper.CARD_IS_FREQUENT,
							ValueUtil.boolean2int(!isFrequent));
					CardManager.updateCard(value, rowId);
					break;
				}
				case 4: {
					dialog.dismiss();
					if (cardStatu == CardInfoUnit.STATU_USED) {
						Date vadility = null;
						try {
							SimpleDateFormat df = new SimpleDateFormat("yyyy"
									+ DatePickerDialog.DATE_SEPERATOR + "MM"
									+ DatePickerDialog.DATE_SEPERATOR + "dd");
							vadility = df.parse(cardInfoUnit.getVadilityEnd());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (vadility == null) {
							cardInfoUnit.setStatu(CardInfoUnit.STATU_NONE);
						} else {
							Calendar calendar = Calendar.getInstance();
							Date now = calendar.getTime();
							long dayOffset = (vadility.getTime() - now
									.getTime());
							if (dayOffset < 0) {
								Toast.makeText(CardDetailActivity.this,
										"当前卡已过有效期，不能标记为未使用！", Toast.LENGTH_LONG)
										.show();
							} else if (dayOffset >= 0
									&& dayOffset <= ((long) MainActivity.EXPIRING_OFFSET)
											* 24 * 60 * 60 * 1000) {
								cardInfoUnit
										.setStatu(CardInfoUnit.STATU_EXPIRING);
							} else {
								cardInfoUnit.setStatu(CardInfoUnit.STATU_NONE);
							}
						}
					} else {
						cardInfoUnit.setStatu(CardInfoUnit.STATU_USED);
					}
					mCardDetailViewInterface.setUsed(cardInfoUnit.getStatu() == CardInfoUnit.STATU_USED);
					ContentValues value = new ContentValues();
					value.put(CardInfoDBHelper.CARD_STATUS,
							cardInfoUnit.getStatu());
					CardManager.updateCard(value, rowId);
					break;
				}
				}
			}
		});
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = getWindowManager().getDefaultDisplay().getWidth();
		wl.y = (int) ((-(MainActivity.HEIGHT - (MainActivity.OFFSET_MEDIUM * 2 + MainActivity.ICON_SIZE_LESS)
				* optionString.length) / 2 + (MainActivity.OFFSET_MEDIUM * 2 + MainActivity.ICON_SIZE_MEDIUM)) * 0.89);
		wl.width = MainActivity.ICON_SIZE_LESS + MainActivity.TEXT_SIZE_MEDIUM
				* 9 + 3 * MainActivity.OFFSET_MEDIUM;
		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围消散
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	private final static int REQUEST_CODE_PHOTO = 0;
	private final static int REQUEST_CODE_MESSAGES = 1;

	class OnPhotoButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(CardDetailActivity.this,
					ShowImageActivity.class);
			intent.putExtra(CARD_DETAIL_IMAGE_PATHS,
					ValueUtil.list2string(imagePaths));
			intent.putExtra(CARD_DETAIL_ICON_POSITION, iconPosition);
			startActivityForResult(intent, REQUEST_CODE_PHOTO);
		}
	}

	class OnMessageBarClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(CardDetailActivity.this,
					CardShowMessageActivity.class);
			intent.putExtra(CARD_DETAIL_MESSAGE_NUMBERS,
					ValueUtil.list2string(messageNumbers));
			if (cardInfoUnit != null)
				intent.putExtra(CARD_DETAIL_MERCHANT_NUMBER,
						cardInfoUnit.getMerchantPhone());
			startActivityForResult(intent, REQUEST_CODE_MESSAGES);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_PHOTO) {
				imagePaths = ValueUtil.string2list(data
						.getStringExtra(CARD_DETAIL_IMAGE_PATHS));
				iconPosition = data.getIntExtra(CARD_DETAIL_ICON_POSITION, -1);
				if (iconPosition != -1) {
					mCardDetailViewInterface.setPhotoButtonImage(BitmapUtil
							.decodeSampledBitmapFromFile(
									imagePaths.get(iconPosition),
									MainActivity.ICON_SIZE_MEDIUM
											* MainActivity.CARD_IMAGE_WIDTH
											/ MainActivity.CARD_IMAGE_HEIGHT,
									MainActivity.ICON_SIZE_MEDIUM));
				} else {
					mCardDetailViewInterface.setPhotoButtonImage(null);
				}
				if (rowId != -1) {
					ContentValues value = new ContentValues();
					value.put(CardInfoDBHelper.CARD_PHOTOS,
							ValueUtil.list2string(imagePaths));
					if (iconPosition != -1 && iconPosition < imagePaths.size())
						value.put(CardInfoDBHelper.CARD_PHOTOS_ICON,
								imagePaths.get(iconPosition));
					else
						value.put(CardInfoDBHelper.CARD_PHOTOS_ICON, "");
					CardManager.updateCard(value, rowId);
				}
			} else if (requestCode == REQUEST_CODE_MESSAGES) {
				messageNumbers = ValueUtil.string2list(data
						.getStringExtra(CARD_DETAIL_MESSAGE_NUMBERS));
				if (rowId != -1) {
					ContentValues value = new ContentValues();
					value.put(CardInfoDBHelper.CARD_MESSAGE_NUMBERS,
							ValueUtil.list2string(messageNumbers));
					CardManager.updateCard(value, rowId);
				}
			}
		}
	}
}
