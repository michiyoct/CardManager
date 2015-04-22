package com.example.views;

import com.example.cardmanager.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardDetailModuleBarView extends LinearLayout {

	private Context context;
	private TextView titleTextView;
	private ImageView iconImageView;
	private boolean statuIsOpen = false;
	private boolean isUsed = false;
	public CardDetailModuleBarView(Context context) {
		super(context);
		init(context, null);
	}

	public CardDetailModuleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.card_detail_module_bar_view, this);
		titleTextView = (TextView) findViewById(R.id.view_card_detail_module_bar_text_title);
		iconImageView = (ImageView) findViewById(R.id.view_card_detail_module_bar_image_icon);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CardDetailModuleBarView);
		setText(typedArray.getString(R.styleable.CardDetailModuleBarView_text));
		typedArray.recycle();
	}

	public void setText(String title) {
		if (title != null)
			titleTextView.setText(title);
		else
			titleTextView.setText("");
	}

	public void setState(boolean isOpen) {
		if (!isEqual(statuIsOpen, isOpen)) {
			statuIsOpen = isOpen;
			if (statuIsOpen) {
				iconImageView
						.setImageResource(R.drawable.card_detail_module_bar_icon_open);
				setBackgroundResource(R.drawable.background_corner_radius_top);
			} else {
				iconImageView
						.setImageResource(R.drawable.card_detail_module_bar_icon_close);
				setBackgroundResource(R.drawable.background_corner_radius_all);
			}
		}
	}
	public void setUsed(boolean isUsed){
		if(!isEqual(this.isUsed, isUsed)){
			this.isUsed = isUsed;
			if(isUsed){
				((ImageView) findViewById(R.id.view_card_detail_module_bar_image_used)).setVisibility(View.VISIBLE);
			}else{
				((ImageView) findViewById(R.id.view_card_detail_module_bar_image_used)).setVisibility(View.INVISIBLE);
			}
		}
	}
	private boolean isEqual(boolean arg0, boolean arg1) {
		if (arg0)
			return arg1;
		else
			return !arg1;
	}
}
