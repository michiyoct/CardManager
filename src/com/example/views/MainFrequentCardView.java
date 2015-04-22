package com.example.views;

import com.example.assistclass.BitmapUtil;
import com.example.cardmanager.MainActivity;
import com.example.cardmanager.R;
import com.example.unit.BasicCardInfoUnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainFrequentCardView extends RelativeLayout {

	public MainFrequentCardView(Context context, int index) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
		setIndex(index);
	}

	private Context context;
	private ImageView backgroundImageView;
	private RelativeLayout infoLayout;
	private ImageView iconImageView;
	private TextView nameTextView;
	private int index = 0;

	private void init() {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.main_frequent_card_view, this);
		backgroundImageView = (ImageView) findViewById(R.id.view_main_frequent_card_image_background);
		infoLayout = (RelativeLayout) findViewById(R.id.view_main_frequent_card_layout_info);
		iconImageView = (ImageView) findViewById(R.id.view_main_frequent_card_image_icon);
		nameTextView = (TextView) findViewById(R.id.view_main_frequent_card_text_name);
	}

	private void setIndex(int index) {
		this.index = index;
		Animation rotateAnimation = null;
		if (index == 0) {
			rotateAnimation = new RotateAnimation(0, 2.7f);
			backgroundImageView
					.setImageResource(R.drawable.main_frequent_card_1);
		} else if (index == 1 || index == 3) {
			rotateAnimation = new RotateAnimation(0, 0);
			backgroundImageView
					.setImageResource(R.drawable.main_frequent_card_2);
		} else if (index == 2 || index == 4) {
			rotateAnimation = new RotateAnimation(0, 6.4f);
			backgroundImageView
					.setImageResource(R.drawable.main_frequent_card_3);
		}
		rotateAnimation.setFillAfter(true);
		rotateAnimation.setDuration(0);
		infoLayout.setAnimation(rotateAnimation);
		rotateAnimation.start();
	}

	public void setContent(BasicCardInfoUnit basicCardInfoUnit) {
		if (basicCardInfoUnit != null) {
			nameTextView.setText(basicCardInfoUnit.getName());
			String imagePath = basicCardInfoUnit.getImagePath();
			if (TextUtils.isEmpty(imagePath))
				iconImageView.setImageResource(R.drawable.logo_default);
			else {
				Bitmap bmp = BitmapUtil.decodeSampledBitmapFromFile(imagePath,
						MainActivity.CARD_IMAGE_WIDTH
								* MainActivity.ICON_SIZE_LESS
								/ MainActivity.CARD_IMAGE_HEIGHT,
						MainActivity.ICON_SIZE_LESS);
				if (bmp != null)
					iconImageView.setImageBitmap(bmp);
				else
					iconImageView.setImageResource(R.drawable.logo_default);
			}
		}
	}
}
