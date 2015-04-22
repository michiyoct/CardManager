package com.example.views;

import com.example.assistclass.BitmapUtil;
import com.example.cardmanager.MainActivity;
import com.example.cardmanager.R;
import com.example.unit.CardInfoUnit;
import com.example.unit.SimpleCardInfoUnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CardListSimpleCardView extends RelativeLayout {
	private ImageView iconImageView;
	private TextView nameTextView;
	private TextView validityEndTextView;
	private TextView noteTextView;
	private SimpleCardInfoUnit simpleCardInfoUnit;
	private Context context;

	public CardListSimpleCardView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.card_list_simple_card_view, this);
		iconImageView = (ImageView) findViewById(R.id.card_list_simple_card_image_icon);
		int imageWidth = context.getResources().getDimensionPixelSize(
				R.dimen.card_image_width);
		int imageHeight = context.getResources().getDimensionPixelSize(
				R.dimen.card_image_height);
		reqHeight = (int) ((2 * MainActivity.TEXT_SIZE_MEDIUM
				+ MainActivity.TEXT_SIZE_SMALL + MainActivity.OFFSET_LESS) * 1.2f);
		reqWidth = imageWidth * reqHeight / imageHeight;
		LayoutParams lp = new LayoutParams(reqWidth, reqHeight);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		iconImageView.setLayoutParams(lp);
		nameTextView = (TextView) findViewById(R.id.card_list_simple_card_text_name);
		validityEndTextView = (TextView) findViewById(R.id.card_list_simple_card_text_validity_end);
		noteTextView = (TextView) findViewById(R.id.card_list_simple_card_text_note);
	}

	int reqHeight;
	int reqWidth;

	public void setContent(SimpleCardInfoUnit simpleCardInfoUnit) {
		this.simpleCardInfoUnit = simpleCardInfoUnit;
		Bitmap iconBitmap = null;
		iconBitmap = BitmapUtil.decodeSampledBitmapFromFile(
				simpleCardInfoUnit.getImagePath(), reqWidth, reqHeight);
		if (iconBitmap != null)
			iconImageView.setImageBitmap(iconBitmap);
		nameTextView.setText(simpleCardInfoUnit.getName());
		String validityEnd = simpleCardInfoUnit.getValidityEnd();
		if (TextUtils.isEmpty(validityEnd)) {
			validityEndTextView.setText("此卡为永久卡");
		} else {
			validityEndTextView.setText("截止日期:" + validityEnd);
		}
		noteTextView.setText(simpleCardInfoUnit.getNote());
		((ImageView) findViewById(R.id.card_list_simple_card_image_used))
				.setVisibility(simpleCardInfoUnit.getStatu() == CardInfoUnit.STATU_USED ? View.VISIBLE
						: View.INVISIBLE);
	}

	public int getRowId() {
		if (simpleCardInfoUnit != null)
			return simpleCardInfoUnit.getRowId();
		return -1;
	}
}
