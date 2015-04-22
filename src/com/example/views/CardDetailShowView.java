package com.example.views;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.assistclass.BitmapUtil;
import com.example.cardmanager.CardDetailActivity;
import com.example.cardmanager.MainActivity;
import com.example.cardmanager.R;
import com.example.unit.CardInfoUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CardDetailShowView extends LinearLayout implements
		CardDetailViewInterface {
	private Context context;
	private TextView nameText;
	private ImageView photoButton;
	private CardDetailModuleBarView infoBarView;
	private LinearLayout infoLayout;
	private GridView categoryGridView;
	private TextView idText;
	private TextView vadilityBeginText;
	private TextView vadilityEndText;
	private TextView noteText;
	private CardDetailModuleBarView merchantBarView;
	private LinearLayout merchantLayout;
	private TextView merchantNameText;
	private TextView merchantAddressText;
	private TextView merchantPhoneText;
	private TextView merchantWebsiteText;
	private Button merchantPhoneButton;
	private CardDetailModuleBarView messageBarView;
	private CardDetailModuleBarView aroundBarView;
	private CategoryGridAdapter categoryGridAdapter;

	private CardInfoUnit cardInfoUnit;
	private Bitmap iconBitmap;

	public CardDetailShowView(Context context) {
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
		inflater.inflate(R.layout.card_detail_show, this);
		nameText = (TextView) findViewById(R.id.card_detail_show_title);
		photoButton = (ImageView) findViewById(R.id.card_detaiL_show_button_photo);
		infoBarView = (CardDetailModuleBarView) findViewById(R.id.card_detail_show_bar_info);
		infoLayout = (LinearLayout) findViewById(R.id.card_detail_show_module_info);
		categoryGridView = (GridView) findViewById(R.id.card_detail_show_grid_category);
		idText = (TextView) findViewById(R.id.card_detail_show_id);
		vadilityBeginText = (TextView) findViewById(R.id.card_detail_show_validity_begin);
		vadilityEndText = (TextView) findViewById(R.id.card_detail_show_validity_end);
		noteText = (TextView) findViewById(R.id.card_detail_show_note);
		merchantBarView = (CardDetailModuleBarView) findViewById(R.id.card_detail_show_bar_merchant);
		merchantLayout = (LinearLayout) findViewById(R.id.card_detail_show_module_merchant);
		merchantNameText = (TextView) findViewById(R.id.card_detail_show_merchant_name);
		merchantAddressText = (TextView) findViewById(R.id.card_detail_show_address);
		merchantPhoneText = (TextView) findViewById(R.id.card_detail_show_merchant_phone);
		merchantWebsiteText = (TextView) findViewById(R.id.card_detail_show_merchant_website);
		merchantPhoneButton = (Button) findViewById(R.id.card_detail_show_buttom_merchant_phone);
		messageBarView = (CardDetailModuleBarView) findViewById(R.id.card_detail_show_bar_message);
		aroundBarView = (CardDetailModuleBarView) findViewById(R.id.card_detail_show_bar_around);
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
		categoryGridAdapter = new CategoryGridAdapter(context, null);
		categoryGridView.setAdapter(categoryGridAdapter);
		merchantPhoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(cardInfoUnit.getMerchantPhone())) {
					String mobile = cardInfoUnit.getMerchantPhone();
					// 使用系统的电话拨号服务，必须去声明权限，在AndroidManifest.xml中进行声明
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + mobile));
					context.startActivity(intent);
				} else {
					Toast.makeText(context, "电话号码不符合要求", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		aroundBarView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(cardInfoUnit.getMerchantAddress())) {
					Toast.makeText(context, "商家地址为空，无法查询！", Toast.LENGTH_LONG)
							.show();
					return;
				}

				LocationManager locManger = (LocationManager) context
						.getSystemService(Context.LOCATION_SERVICE);
				Location loc = locManger
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (loc == null) {
					loc = locManger
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
				final GeoPoint ptStart = new GeoPoint(
						(int) (loc.getLatitude() * 1e6), (int) (loc
								.getLongitude() * 1e6));
				MKSearch mSearch = new MKSearch();
				mSearch.init(CardDetailActivity.mBMapManager,
						new MKSearchListener() {
							@Override
							public void onGetPoiDetailSearchResult(int type,
									int error) {
							}

							@Override
							public void onGetAddrResult(MKAddrInfo res,
									int error) {
								if (error != 0) {
									String str = String.format("错误号：%d", error);
									Toast.makeText(context, str,
											Toast.LENGTH_LONG).show();
									return;
								}
								String city = res.addressComponents.city;
								try {
									Intent intent = Intent.getIntent("intent://map/line?coordtype=&zoom=&region="
											+ city
											+ "&name="
											+ cardInfoUnit.getMerchantAddress()
											+ "&src=cardManager#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
									if (new File("/data/data/"
											+ "com.baidu.BaiduMap").exists()) {
										context.startActivity(intent); // 启动调用
									} else {
										Toast.makeText(context, "没有安装百度地图客户端",
												Toast.LENGTH_LONG).show();
									}
								} catch (URISyntaxException e) {
									e.printStackTrace();
								}

							}

							@Override
							public void onGetSuggestionResult(
									MKSuggestionResult res, int arg1) {
							}

							@Override
							public void onGetShareUrlResult(
									MKShareUrlResult result, int type, int error) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onGetBusDetailResult(
									MKBusLineResult arg0, int arg1) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onGetDrivingRouteResult(
									MKDrivingRouteResult arg0, int arg1) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onGetPoiResult(MKPoiResult arg0,
									int arg1, int arg2) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onGetTransitRouteResult(
									MKTransitRouteResult arg0, int arg1) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onGetWalkingRouteResult(
									MKWalkingRouteResult arg0, int arg1) {
								// TODO Auto-generated method stub

							}

						});
				mSearch.reverseGeocode(ptStart);
			}
		});
	}

	public void setContent(final CardInfoUnit cardInfoUnit) {
		if (cardInfoUnit != null) {
			this.cardInfoUnit = cardInfoUnit;
			nameText.setText(cardInfoUnit.getName());
			idText.setText(TextUtils.isEmpty(cardInfoUnit.getCardId()) ? "暂无"
					: cardInfoUnit.getCardId());
			vadilityBeginText.setText(TextUtils.isEmpty(cardInfoUnit
					.getVadilityBegin()) ? "暂无" : cardInfoUnit
					.getVadilityBegin());
			vadilityEndText.setText(TextUtils.isEmpty(cardInfoUnit
					.getVadilityEnd()) ? "暂无" : cardInfoUnit.getVadilityEnd());
			noteText.setText(TextUtils.isEmpty(cardInfoUnit.getNote()) ? "暂无"
					: cardInfoUnit.getNote());
			merchantNameText
					.setText(TextUtils.isEmpty(cardInfoUnit.getMerchantName()) ? "暂无"
							: cardInfoUnit.getMerchantName());
			merchantAddressText.setText(TextUtils.isEmpty(cardInfoUnit
					.getMerchantAddress()) ? "暂无" : cardInfoUnit
					.getMerchantAddress());
			merchantPhoneText.setText(TextUtils.isEmpty(cardInfoUnit
					.getMerchantPhone()) ? "暂无" : cardInfoUnit
					.getMerchantPhone());
			merchantWebsiteText.setText(TextUtils.isEmpty(cardInfoUnit
					.getMerchantWebsite()) ? "暂无" : cardInfoUnit
					.getMerchantWebsite());
			if (!TextUtils.isEmpty(cardInfoUnit.getMerchantWebsite())) {
				merchantWebsiteText.setText(cardInfoUnit.getMerchantWebsite());
				merchantWebsiteText.setTextColor(0xff3695ff);
				merchantWebsiteText.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						String website = cardInfoUnit.getMerchantWebsite();
						if (!website.contains("http://")) {
							website = "http://" + website;
						}
						Uri uri = Uri.parse(website);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						context.startActivity(intent);
					}
				});
			}else{
				merchantWebsiteText.setText("暂无");
				merchantWebsiteText.setTextColor(context.getResources().getColor(R.color.text_color));
			}
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
			setUsed(cardInfoUnit.getStatu() == CardInfoUnit.STATU_USED);
		}
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

	public void setOnAroundButtonClickListener(OnClickListener l) {
		aroundBarView.setOnClickListener(l);
	}

	class CategoryGridAdapter extends BaseAdapter {
		public List<String> getCategorys() {
			return categorys;
		}

		public void setCategorys(List<String> categorys) {
			this.categorys = categorys;
			LinearLayout.LayoutParams gridLP = new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					(((categorys.size() - 1) / 4) + 1)
							* (MainActivity.TEXT_SIZE_MEDIUM + 2 * MainActivity.OFFSET_MEDIUM),
					1.0f);
			categoryGridView.setLayoutParams(gridLP);
			notifyDataSetChanged();
		}

		public CategoryGridAdapter(Context context, List<String> categorys) {
			super();
			this.context = context;
			colors = context.getResources()
					.getIntArray(R.array.category_colors);
			if (categorys == null)
				this.categorys = new ArrayList<String>();
			else
				this.categorys = categorys;
		}

		private List<String> categorys;
		private int[] colors;
		private Context context;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return categorys.size();
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
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if (arg1 == null)
				arg1 = new TextView(context);
			((TextView) arg1).setLayoutParams(new AbsListView.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			((TextView) arg1).setGravity(Gravity.CENTER);
			((TextView) arg1).setBackgroundColor(colors[arg0]);
			((TextView) arg1).setText(categorys.get(arg0));
			((TextView) arg1).setOnClickListener(null);
			((TextView) arg1).setPadding(MainActivity.OFFSET_SMALL,
					MainActivity.OFFSET_LESS, MainActivity.OFFSET_SMALL,
					MainActivity.OFFSET_LESS);
			((TextView) arg1).setTextColor(0xffffffff);
			((TextView) arg1).setTextSize(
					TypedValue.COMPLEX_UNIT_PX,
					getResources().getDimensionPixelSize(
							R.dimen.text_size_medium));
			return arg1;
		}

	}

	@Override
	public void setUsed(boolean isUsed) {
		// TODO Auto-generated method stub
		infoBarView.setUsed(isUsed);
	}
}
