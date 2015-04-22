/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.views;

import com.example.cardmanager.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 */
public final class TakePhotoLayerView extends View {
	/**
	 * �ĸ���ɫ�߽Ƕ�Ӧ�ĳ���
	 */
	private int ScreenRate;

	/**
	 * �ĸ���ɫ�߽Ƕ�Ӧ�Ŀ��
	 */
	private static final int CORNER_WIDTH = 10;

	/**
	 * �ֻ�����Ļ�ܶ�
	 */
	private static float density;
	/**
	 * �����С
	 */
	private static final int TEXT_SIZE = 16;
	/**
	 * �������ɨ�������ľ���
	 */
	private static final int TEXT_PADDING_TOP = 30;

	/**
	 * ���ʶ��������
	 */
	private Paint paint;

	private final int maskColor;
	private final int resultColor;

	public TakePhotoLayerView(Context context, AttributeSet attrs) {
		super(context, attrs);

		density = context.getResources().getDisplayMetrics().density;
		// ������ת����dp
		ScreenRate = (int) (20 * density);
		WIDTH = context.getResources().getDisplayMetrics().widthPixels;
		HEIGHT = context.getResources().getDisplayMetrics().heightPixels;
		imageWidth = context.getResources().getDimensionPixelSize(
				R.dimen.card_image_width);
		imageHeight = context.getResources().getDimensionPixelSize(
				R.dimen.card_image_height);
		paint = new Paint();
		Resources resources = getResources();
		maskColor = 0x60000000;
		resultColor = 0xb0000000;
	}

	private int WIDTH;
	private int HEIGHT;
	private int imageWidth;
	private int imageHeight;

	@Override
	public void onDraw(Canvas canvas) {
		// �м��ɨ�����Ҫ�޸�ɨ���Ĵ�С��ȥCameraManager�����޸�
		int leftOffset = (WIDTH - imageWidth) / 2;
		int topOffset = (HEIGHT - imageHeight) / 2;
		Rect frame = new Rect(leftOffset, topOffset, leftOffset + imageWidth,
				topOffset + imageHeight);
		// ��ȡ��Ļ�Ŀ�͸�
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		paint.setColor(maskColor);

		// ����ɨ����������Ӱ���֣����ĸ����֣�ɨ�������浽��Ļ���棬ɨ�������浽��Ļ����
		// ɨ��������浽��Ļ��ߣ�ɨ�����ұߵ���Ļ�ұ�
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		// ��ɨ�����ϵĽǣ��ܹ�8������
		paint.setColor(Color.GREEN);
		canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
				frame.top + CORNER_WIDTH, paint);
		canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH,
				frame.top + ScreenRate, paint);
		canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
				frame.top + CORNER_WIDTH, paint);
		canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right,
				frame.top + ScreenRate, paint);
		canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
				+ ScreenRate, frame.bottom, paint);
		canvas.drawRect(frame.left, frame.bottom - ScreenRate, frame.left
				+ CORNER_WIDTH, frame.bottom, paint);
		canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
				frame.right, frame.bottom, paint);
		canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
				frame.right, frame.bottom, paint);

		// ��ɨ����������
		paint.setColor(Color.WHITE);
		paint.setTextSize(TEXT_SIZE * density);
		paint.setAlpha(0x40);
		paint.setTypeface(Typeface.create("System", Typeface.BOLD));
		canvas.drawText("�뽫��Ƭ�����ڿ���", frame.left,
				frame.bottom + TEXT_PADDING_TOP * density,
				paint);

	}

	public void drawViewfinder() {
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		invalidate();
	}

}
