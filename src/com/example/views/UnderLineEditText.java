package com.example.views;

import com.example.cardmanager.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class UnderLineEditText extends EditText {
	private Paint mPaint;
	private Rect mRect;
	private float add = 1.5f;

	public UnderLineEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public UnderLineEditText(Context context) {
		super(context);
		init(context, null);
	}

	private void init(Context context, AttributeSet attrs) {
		setBackgroundColor(0x00000000);
		mRect = new Rect();
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs,
					R.styleable.UnderLineEditText);
			int color = typedArray.getColor(
					R.styleable.UnderLineEditText_underLineColor, 0xff969696);
			typedArray.recycle();
			mPaint.setColor(color);
		} else
			mPaint.setColor(0xff969696);
		mPaint.setAntiAlias(true);
		setLineSpacing(add, 1.0f);
		setIncludeFontPadding(false);
	}

	@Override
	public void onDraw(Canvas canvas) {
		int count = getLineCount();
		for (int i = 0; i < count; i++) {
			getLineBounds(i, mRect);
			int baseline = (i + 1) * getLineHeight()+12;
			canvas.drawLine(mRect.left, baseline, mRect.right, baseline, mPaint);
		}
		super.onDraw(canvas);
	}

}