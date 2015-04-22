package com.example.cardmanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseActivity extends Activity {
	private Button backButton;
	private Button rightButton;
	private TextView titleText;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_base);
		RelativeLayout rl = (RelativeLayout)findViewById(R.id.activity_base_layout_background);
		rl.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, MainActivity.OFFSET_MEDIUM*2+MainActivity.ICON_SIZE_MEDIUM));
		backButton = (Button) findViewById(R.id.activity_base_button_title_back);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		rightButton = (Button) findViewById(R.id.activity_base_button_title_right);
		titleText = (TextView) findViewById(R.id.activity_base_text_title);
	}

	public void setBaseContentView(int layoutID) {
		LinearLayout contentLayout = (LinearLayout) findViewById(R.id.activity_base_layout_content);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(layoutID, null);
		contentLayout.removeAllViews();
		contentLayout.addView(view);
	}

	public void setBaseContentView(View view) {
		LinearLayout contentLayout = (LinearLayout) findViewById(R.id.activity_base_layout_content);
		contentLayout.removeAllViews();
		contentLayout.addView(view);
	}

	public void setBackButtonOnClickListener(OnClickListener onClickListener) {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(onClickListener);
	}

	public void setRightButtonOnClickListener(OnClickListener l) {
		rightButton.setOnClickListener(l);
	}

	public void setRightButton(int resId, OnClickListener l) {
		if (l == null) {
			rightButton.setVisibility(View.INVISIBLE);
		} else {
			rightButton.setBackgroundResource(resId);
			rightButton.setOnClickListener(l);
		}
	}

	public void setTitle(String title) {
		titleText.setText(title);
	}
}
