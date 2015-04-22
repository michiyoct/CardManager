package com.example.views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.assistclass.BitmapUtil;
import com.example.assistclass.ValueUtil;
import com.example.cardmanager.CardListActivity;
import com.example.cardmanager.MainActivity;
import com.example.cardmanager.R;
import com.example.dbhelper.CardInfoDBHelper;
import com.example.dbhelper.CardManager;
import com.example.unit.CardInfoUnit;
import com.example.unit.SimpleCardInfoUnit;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CardDetailEditView extends LinearLayout implements
		CardDetailViewInterface {
	private Context context;
	private UnderLineEditText nameEditText;
	private ImageView photoButton;
	private CardDetailModuleBarView infoBarView;
	private LinearLayout infoLayout;
	private GridView categoryGridView;
	private UnderLineEditText idEditText;
	private UnderLineEditText validityBeginEditText;
	private UnderLineEditText validityEndEditText;
	private UnderLineEditText noteEditText;
	private CardDetailModuleBarView merchantBarView;
	private LinearLayout merchantLayout;
	private UnderLineEditText merchantNameEditText;
	private UnderLineEditText merchantAddressEditText;
	private UnderLineEditText merchantPhoneEditText;
	private UnderLineEditText merchantWebsiteEditText;
	private CardDetailModuleBarView messageBarView;
	private CategoryGridAdapter categoryGridAdapter;

	private CardInfoUnit cardInfoUnit;
	private Bitmap iconBitmap;

	public CardDetailEditView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.card_detail_edit, this);
		nameEditText = (UnderLineEditText) findViewById(R.id.card_detail_edit_title);
		photoButton = (ImageView) findViewById(R.id.card_detaiL_edit_button_photo);
		infoBarView = (CardDetailModuleBarView) findViewById(R.id.card_detail_edit_bar_info);
		infoLayout = (LinearLayout) findViewById(R.id.card_detail_edit_module_info);
		categoryGridView = (GridView) findViewById(R.id.card_detail_edit_grid_category);
		idEditText = (UnderLineEditText) findViewById(R.id.card_detail_edit_id);
		validityBeginEditText = (UnderLineEditText) findViewById(R.id.card_detail_edit_validity_begin);
		validityEndEditText = (UnderLineEditText) findViewById(R.id.card_detail_edit_validity_end);
		validityBeginEditText.setFocusable(false);
		validityEndEditText.setFocusable(false);
		validityBeginEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(context, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						String validityEnd = validityEndEditText.getText()
								.toString();
						boolean isValid = true;
						if (!TextUtils.isEmpty(validityEnd)) {
							SimpleDateFormat df = new SimpleDateFormat("yyyy"
									+ DatePickerDialog.DATE_SEPERATOR + "MM"
									+ DatePickerDialog.DATE_SEPERATOR + "dd");
							try {
								Date validityEndDate = df.parse(validityEnd);
								Date validityBeginDate = df.parse(year
										+ DatePickerDialog.DATE_SEPERATOR
										+ (monthOfYear)
										+ DatePickerDialog.DATE_SEPERATOR
										+ dayOfMonth);
								long dayOffset = ((validityBeginDate.getTime() - validityEndDate
										.getTime())) / 1000 / 60 / 60 / 24;
								if (dayOffset > 0) {
									isValid = false;
								}
							} catch (ParseException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							}
						}

						if (isValid)
							validityBeginEditText.setText(year
									+ DatePickerDialog.DATE_SEPERATOR
									+ (monthOfYear)
									+ DatePickerDialog.DATE_SEPERATOR
									+ dayOfMonth);
						else
							Toast.makeText(context, "生效日期不能晚于失效日期！",
									Toast.LENGTH_LONG).show();
					}
				}, c.get(Calendar.YEAR), // 传入年份
						c.get(Calendar.MONTH) + 1, // 传入月份
						c.get(Calendar.DAY_OF_MONTH), R.style.Dialog).show(); // 传入天数
			}
		});
		validityEndEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(context, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						String validityBegin = validityBeginEditText.getText()
								.toString();
						boolean isValid = true;
						if (!TextUtils.isEmpty(validityBegin)) {
							SimpleDateFormat df = new SimpleDateFormat("yyyy"
									+ DatePickerDialog.DATE_SEPERATOR + "MM"
									+ DatePickerDialog.DATE_SEPERATOR + "dd");
							try {
								Date validityBeginDate = df
										.parse(validityBegin);
								Date validityEndDate = df.parse(year
										+ DatePickerDialog.DATE_SEPERATOR
										+ (monthOfYear)
										+ DatePickerDialog.DATE_SEPERATOR
										+ dayOfMonth);
								long dayOffset = ((validityBeginDate.getTime() - validityEndDate
										.getTime())) / 1000 / 60 / 60 / 24;
								if (dayOffset > 0) {
									isValid = false;
								}
							} catch (ParseException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							}
						}
						if (isValid)
							validityEndEditText.setText(year
									+ DatePickerDialog.DATE_SEPERATOR
									+ (monthOfYear)
									+ DatePickerDialog.DATE_SEPERATOR
									+ dayOfMonth);
						else
							Toast.makeText(context, "失效日期不能早于生效日期！",
									Toast.LENGTH_LONG).show();
					}
				}, c.get(Calendar.YEAR), // 传入年份
						c.get(Calendar.MONTH) + 1, // 传入月份
						c.get(Calendar.DAY_OF_MONTH), R.style.Dialog).show(); // 传入天数
			}
		});
		noteEditText = (UnderLineEditText) findViewById(R.id.card_detail_edit_note);
		merchantBarView = (CardDetailModuleBarView) findViewById(R.id.card_detail_edit_bar_merchant);
		merchantLayout = (LinearLayout) findViewById(R.id.card_detail_edit_module_merchant);
		merchantNameEditText = (UnderLineEditText) findViewById(R.id.card_detail_edit_merchant_name);
		merchantAddressEditText = (UnderLineEditText) findViewById(R.id.card_detail_edit_address);
		merchantPhoneEditText = (UnderLineEditText) findViewById(R.id.card_detail_edit_merchant_phone);
		merchantWebsiteEditText = (UnderLineEditText) findViewById(R.id.card_detail_edit_merchant_website);
		messageBarView = (CardDetailModuleBarView) findViewById(R.id.card_detail_edit_bar_message);
		infoBarView.setState(true);
		infoBarView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (infoLayout.getVisibility() == View.VISIBLE) {
					infoLayout.setVisibility(View.GONE);
					infoBarView.setState(false);
				} else {
					infoLayout.setVisibility(View.VISIBLE);
					infoBarView.setState(true);
				}
			}
		});
		merchantBarView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (merchantLayout.getVisibility() == View.VISIBLE) {
					merchantLayout.setVisibility(View.GONE);
					merchantBarView.setState(false);
				} else {
					merchantLayout.setVisibility(View.VISIBLE);
					merchantBarView.setState(true);

				}
			}
		});
		categoryGridAdapter = new CategoryGridAdapter(context);
		categoryGridView.setAdapter(categoryGridAdapter);
	}

	public void setContent(CardInfoUnit cardInfoUnit) {
		if (cardInfoUnit != null) {
			this.cardInfoUnit = cardInfoUnit;
			nameEditText.setText(cardInfoUnit.getName());
			idEditText.setText(cardInfoUnit.getCardId());
			validityBeginEditText.setText(cardInfoUnit.getVadilityBegin());
			validityEndEditText.setText(cardInfoUnit.getVadilityEnd());
			noteEditText.setText(cardInfoUnit.getNote());
			merchantNameEditText.setText(cardInfoUnit.getMerchantName());
			merchantAddressEditText.setText(cardInfoUnit.getMerchantAddress());
			merchantPhoneEditText.setText(cardInfoUnit.getMerchantPhone());
			merchantWebsiteEditText.setText(cardInfoUnit.getMerchantWebsite());
			categoryGridAdapter.setCategorys(cardInfoUnit.getCategorys());
			if (!TextUtils.isEmpty(cardInfoUnit.getIconPath())) {
				if (iconBitmap != null) {
					iconBitmap.recycle();
				}
				iconBitmap = BitmapUtil.decodeSampledBitmapFromFile(
						cardInfoUnit.getIconPath(),
						MainActivity.ICON_SIZE_MEDIUM
								* MainActivity.CARD_IMAGE_WIDTH
								/ MainActivity.CARD_IMAGE_HEIGHT,
						MainActivity.ICON_SIZE_MEDIUM);
				if (iconBitmap != null) {
					photoButton.setImageBitmap(iconBitmap);
				} else {
					photoButton
							.setImageResource(R.drawable.card_detail_icon_photo);
				}
			}

		}
		if (TextUtils.isEmpty(validityBeginEditText.getText())) {
			Calendar c = Calendar.getInstance();
			validityBeginEditText.setText(c.get(Calendar.YEAR)
					+ DatePickerDialog.DATE_SEPERATOR
					+ (c.get(Calendar.MONTH) + 1)
					+ DatePickerDialog.DATE_SEPERATOR
					+ c.get(Calendar.DAY_OF_MONTH));
		}
	}

	public boolean isValid() {
		String name = nameEditText.getText().toString();
		List<String> categorys = categoryGridAdapter.getCategorys();
		return !TextUtils.isEmpty(name) && categorys.size() > 0;
	}

	public CardInfoUnit getCardInfoUnit() {
		String name = nameEditText.getText().toString();
		String cardId = idEditText.getText().toString();
		String validityBegin = validityBeginEditText.getText().toString();
		String validityEnd = validityEndEditText.getText().toString();
		String note = noteEditText.getText().toString();
		String merchantName = merchantNameEditText.getText().toString();
		String merchantAddress = merchantAddressEditText.getText().toString();
		String merchantPhone = merchantPhoneEditText.getText().toString();
		String merchantWebsite = merchantWebsiteEditText.getText().toString();
		List<String> categorys = categoryGridAdapter.getCategorys();
		int statu = CardInfoUnit.STATU_NONE;
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		long createTime;
		if (cardInfoUnit == null) {
			createTime = now.getTime();
		} else {
			createTime = cardInfoUnit.getCreateTime();
		}
		if (!TextUtils.isEmpty(validityEnd)) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			try {
				Date validityEndDate = df.parse(validityEnd);
				long dayOffset = ((validityEndDate.getTime() - now.getTime()))
						/ 1000 / 60 / 60 / 24;
				if (dayOffset < 0) {
					statu = CardInfoUnit.STATU_USED;
				} else if (dayOffset >= 0
						&& dayOffset <= MainActivity.EXPIRING_OFFSET) {
					statu = CardInfoUnit.STATU_EXPIRING;
				} else {
					statu = CardInfoUnit.STATU_NONE;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch
				// block
				e.printStackTrace();
			}
		}
		return new CardInfoUnit(-1, name, cardId, validityBegin, validityEnd,
				note, merchantName, merchantAddress, merchantPhone,
				merchantWebsite, categorys, null, null, null, statu,
				createTime, false);
	}

	@Override
	public void setPhotoButtonImage(Bitmap bitmap) {
		if (iconBitmap != null)
			iconBitmap.recycle();
		iconBitmap = bitmap;
		if (bitmap == null) {
			photoButton.setImageResource(R.drawable.card_detail_icon_photo);
		} else {
			photoButton.setImageBitmap(bitmap);
		}
	}

	public void setOnPhotoButtonClickListener(OnClickListener l) {
		photoButton.setOnClickListener(l);
	}

	public void setOnMessageButtonClickListener(OnClickListener l) {
		messageBarView.setOnClickListener(l);
	}

	class CategoryGridAdapter extends BaseAdapter {
		public List<String> getCategorys() {
			List<String> resultCategorys = new ArrayList<String>();
			for (int i = 0; i < categorys.size(); i++) {
				if (isChecked[i])
					resultCategorys.add(categorys.get(i));
			}
			return resultCategorys;
		}

		public void setCategorys(List<String> cardCategorys) {
			// TODO Auto-generated method stub
			if (cardCategorys == null) {
				cardCategorys = new ArrayList<String>();
			}
			for (int i = 0; i < this.categorys.size(); i++) {
				if (cardCategorys.contains(this.categorys.get(i))) {
					isChecked[i] = true;
				} else {
					isChecked[i] = false;
				}
			}
			notifyDataSetChanged();
		}

		public void updateCategorys() {
			this.categorys = new LinkedList<String>(Arrays.asList(context
					.getResources().getStringArray(
							R.array.category_name_default)));
			SharedPreferences sp = context.getSharedPreferences(
					MainActivity.SHARE_PREFERENCE_CATEGORYS, 0);
			Map<String, String> map = (Map<String, String>) sp.getAll();
			for (String value : map.values()) {
				categorys.add(value);
			}
			notifyDataSetChanged();
		}

		public CategoryGridAdapter(Context context) {
			super();
			this.context = context;
			colors = context.getResources()
					.getIntArray(R.array.category_colors);
			MAX_COUNT = colors.length;
			isChecked = new boolean[MAX_COUNT];
			for (int i = 0; i < MAX_COUNT; i++) {
				isChecked[i] = false;
			}
			updateCategorys();
		}

		private List<String> categorys;
		private int MAX_COUNT;
		private int[] colors;
		private boolean[] isChecked;
		private Context context;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			int count = 0;
			if (categorys == null)
				count = 1;
			if (categorys.size() == colors.length)
				count = MAX_COUNT;
			else
				count = categorys.size() + 1;
			LinearLayout.LayoutParams gridLP = new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					(((count - 1) / 4) + 1)
							* (MainActivity.TEXT_SIZE_MEDIUM + 2 * MainActivity.OFFSET_MEDIUM),
					1.0f);
			categoryGridView.setLayoutParams(gridLP);
			return count;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return categorys.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			Viewholder viewholder;
			if (convertView == null) {
				convertView = new FrameLayout(context);
				((FrameLayout) convertView)
						.setLayoutParams(new AbsListView.LayoutParams(
								android.view.ViewGroup.LayoutParams.MATCH_PARENT,
								(int) (MainActivity.TEXT_SIZE_MEDIUM + 1.8 * MainActivity.OFFSET_MEDIUM)));
				TextView textView = new TextView(context);
				textView.setLayoutParams(new FrameLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT));
				textView.setGravity(Gravity.CENTER);
				textView.setBackgroundColor(colors[position]);
				textView.setPadding(MainActivity.OFFSET_SMALL,
						MainActivity.OFFSET_LESS, MainActivity.OFFSET_SMALL,
						MainActivity.OFFSET_LESS);
				textView.setTextColor(0xffffffff);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						MainActivity.TEXT_SIZE_MEDIUM);
				ImageView imageView = new ImageView(context);
				imageView.setLayoutParams(new FrameLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT));
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView
						.setImageResource(R.drawable.card_detail_category_selected);
				((FrameLayout) convertView).addView(textView);
				((FrameLayout) convertView).addView(imageView);
				viewholder = new Viewholder();
				viewholder.imageView = imageView;
				viewholder.textView = textView;
				convertView.setTag(viewholder);
			} else {
				viewholder = (Viewholder) convertView.getTag();
			}
			final ImageView finalImageView = viewholder.imageView;
			if (categorys == null || position == categorys.size()) {
				viewholder.textView.setText("+");
				viewholder.imageView.setVisibility(View.INVISIBLE);
				((FrameLayout) convertView)
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								showInputDialog(position);
							}
						});
				((FrameLayout) convertView).setOnLongClickListener(null);
			} else {
				viewholder.textView.setText(categorys.get(position));
				if (isChecked[position]) {
					finalImageView.setVisibility(View.VISIBLE);
				} else {
					finalImageView.setVisibility(View.INVISIBLE);
				}
				((FrameLayout) convertView)
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								if (finalImageView.getVisibility() == View.VISIBLE) {
									finalImageView
											.setVisibility(View.INVISIBLE);
									isChecked[position] = false;
								} else {
									int count = 0;
									for (int i = 0; i < isChecked.length; i++) {
										if (isChecked[i])
											count++;
									}
									if (count >= 3) {
										Toast.makeText(context, "最多选择3个标签！",
												Toast.LENGTH_LONG).show();
									} else {
										isChecked[position] = true;
										finalImageView
												.setVisibility(View.VISIBLE);
									}
								}
							}
						});
				((FrameLayout) convertView)
						.setOnLongClickListener(new OnLongClickListener() {

							@Override
							public boolean onLongClick(View arg0) {
								// TODO Auto-generated method stub
								if (position >= context.getResources()
										.getStringArray(
												R.array.category_name_default).length) {
									showDeleteDialog(position);
									return true;
								}
								return false;
							}
						});
			}
			return convertView;
		}

	}

	static class Viewholder {
		ImageView imageView;
		TextView textView;
	}

	private void showDeleteDialog(final int index) {
		final String[] optionString = new String[] { "删除" };
		Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setTitle("选择需要进行的操作");
		dialogBuilder.setItems(optionString,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog1, int which) {
						if (which == 0) {
							new AlertDialog.Builder(context)
									.setTitle("确认删除这张标签？")
									.setPositiveButton(
											"确认",
											new android.content.DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													SharedPreferences sp = context
															.getSharedPreferences(
																	MainActivity.SHARE_PREFERENCE_CATEGORYS,
																	0);
													Editor editor = sp.edit();
													editor.remove("category"
															+ index);
													editor.commit();
													editor.apply();
													Toast.makeText(context,
															"删除成功！",
															Toast.LENGTH_LONG)
															.show();
													categoryGridAdapter
															.updateCategorys();
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
													dialog.dismiss();
												}
											}).create().show();
						}
					}
				});
		dialog = dialogBuilder.create();
		dialog.show();
	}

	private AlertDialog dialog;

	public void showInputDialog(final int index) {
		final EditText inputServer = new EditText(context);
		inputServer.setFocusable(true);
		inputServer.setMaxEms(3);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("请输入想要增加的标签名称").setView(inputServer)
				.setNegativeButton("取消", null);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String category = inputServer.getText().toString();
				if (TextUtils.isEmpty(category)) {
					Toast.makeText(context, "输入不能为空", Toast.LENGTH_LONG).show();
				} else {
					SharedPreferences sp = context.getSharedPreferences(
							MainActivity.SHARE_PREFERENCE_CATEGORYS, 0);
					Editor editor = sp.edit();
					editor.putString("category" + index, category);
					editor.commit();
					editor.apply();
					Toast.makeText(context, "添加成功！", Toast.LENGTH_LONG).show();
					categoryGridAdapter.updateCategorys();
				}
			}
		});
		builder.show();
	}

	@Override
	public void setUsed(boolean isUsed) {
		// TODO Auto-generated method stub
		infoBarView.setUsed(isUsed);
	}
}
