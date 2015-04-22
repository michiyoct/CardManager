package com.example.assistclass;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;

public class BitmapUtil {

	/**
	 * ����ͼƬ
	 * 
	 * @param orgBitmap
	 * @param f
	 * @return
	 */
	public static Bitmap zoom(Bitmap orgBitmap, float zf) {
		Matrix matrix = new Matrix();
		matrix.postScale(zf, zf);
		Bitmap resizedBitmap = Bitmap.createBitmap(orgBitmap, 0, 0,
				orgBitmap.getWidth(), orgBitmap.getHeight(), matrix, true);
		if (orgBitmap != null && resizedBitmap != orgBitmap)
			orgBitmap.recycle();
		return resizedBitmap;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param bitmap
	 * @param f
	 * @return
	 */
	public static Bitmap zoom(Bitmap orgBitmap, float wf, float hf) {
		Matrix matrix = new Matrix();
		matrix.postScale(wf, hf);
		Bitmap resizedBitmap = Bitmap.createBitmap(orgBitmap, 0, 0,
				orgBitmap.getWidth(), orgBitmap.getHeight(), matrix, true);
		if (orgBitmap != null && resizedBitmap != orgBitmap)
			orgBitmap.recycle();
		return resizedBitmap;
	}

	/**
	 * ͼƬԲ�Ǵ���
	 * 
	 * @param bitmap
	 * @param roundPX
	 * @return
	 */
	public static Bitmap getRCB(Bitmap bitmap, float roundPX) {
		// RCB means
		// Rounded
		// Corner Bitmap
		Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(dstbmp);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		if (bitmap != null && dstbmp != bitmap)
			bitmap.recycle();
		return dstbmp;
	}

	public static int calculateRatio(int orgWidth, int orgHeight, int reqWidth,
			int reqHeight) {
		// ԴͼƬ�ĸ߶ȺͿ��
		int ratio = 1;
		if (orgHeight > reqHeight || orgWidth > reqWidth) {
			// �����ʵ�ʿ�ߺ�Ŀ���ߵı���
			final int heightRatio = Math.round((float) orgHeight
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) orgWidth
					/ (float) reqWidth);
			// ѡ���͸�����С�ı�����ΪinSampleSize��ֵ���������Ա�֤����ͼƬ�Ŀ�͸�
			// һ��������ڵ���Ŀ��Ŀ�͸ߡ�
			ratio = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return ratio;
	}

	public static Bitmap narrowBitmap(Bitmap orgBitmap, int reqWidth,
			int reqHeight) {
		float ratio = 1.0f;
		if (orgBitmap.getHeight() > reqHeight
				|| orgBitmap.getWidth() > reqWidth) {
			// �����ʵ�ʿ�ߺ�Ŀ���ߵı���
			float heightRatio = (float) reqHeight
					/ (float) orgBitmap.getHeight();
			float widthRatio = (float) reqWidth / (float) orgBitmap.getWidth();
			// ѡ���͸�����С�ı�����ΪinSampleSize��ֵ���������Ա�֤����ͼƬ�Ŀ�͸�
			// һ��������ڵ���Ŀ��Ŀ�͸ߡ�
			ratio = heightRatio > widthRatio ? heightRatio : widthRatio;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(ratio, ratio);
		Bitmap resizedBitmap = Bitmap.createBitmap(orgBitmap, 0, 0,
				orgBitmap.getWidth(), orgBitmap.getHeight(), matrix, true);
		if (orgBitmap != null && resizedBitmap != orgBitmap)
			orgBitmap.recycle();
		return resizedBitmap;

	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		// ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// �������涨��ķ�������inSampleSizeֵ
		options.inSampleSize = calculateRatio(options.outWidth,
				options.outHeight, reqWidth, reqHeight);
		// ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap decodeSampledBitmapFromUri(Context context, Uri uri,
			int reqWidth, int reqHeight) throws FileNotFoundException {
		// ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С
		ContentResolver cr = context.getContentResolver();
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		BitmapFactory.decodeStream(cr.openInputStream(uri), null, options);

		// �������涨��ķ�������inSampleSizeֵ
		options.inSampleSize = calculateRatio(options.outWidth,
				options.outHeight, reqWidth, reqHeight);
		// ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ
		options.inJustDecodeBounds = false;

		return narrowBitmap(BitmapFactory.decodeStream(cr.openInputStream(uri),
				null, options), reqWidth, reqHeight);
	}

	public static Bitmap byteToBitmap(byte[] b) {
		return (b == null || b.length == 0) ? null : BitmapFactory
				.decodeByteArray(b, 0, b.length);
	}

	public static Bitmap decodeSampledBitmapFromUri(Context context, Uri uri)
			throws FileNotFoundException {
		ContentResolver cr = context.getContentResolver();
		return BitmapFactory.decodeStream(cr.openInputStream(uri), null, null);
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
			int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		BitmapFactory.decodeFile(path, options);
		// �������涨��ķ�������inSampleSizeֵ
		options.inSampleSize = calculateRatio(options.outWidth,
				options.outHeight, reqWidth, reqHeight);
		// ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		if(bitmap == null)
			return null;
		return narrowBitmap(bitmap, reqWidth,
				reqHeight);
	}

	public static Bitmap decodeSampledBitmapFromUrl(String Url, int reqWidth,
			int reqHeight) throws Exception {

		URL url = new URL(Url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(6 * 1000);
		// �𳬹�10�롣
		if (conn.getResponseCode() == 200) {
			InputStream inputStream = conn.getInputStream();
			byte[] data = readStream(inputStream);
			// ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(data, 0, data.length, options);
			// �������涨��ķ�������inSampleSizeֵ
			options.inSampleSize = calculateRatio(options.outWidth,
					options.outHeight, reqWidth, reqHeight);
			// ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeByteArray(data, 0, data.length, options);
		} else {
			throw new Exception("err: responseCode " + conn.getResponseCode());
		}
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		outstream.close();
		inStream.close();

		return outstream.toByteArray();
	}

}
