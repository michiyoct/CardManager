package com.example.cardmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class TakePhotoActivity extends Activity implements
		Camera.PictureCallback {
	@Override
	public boolean onTouchEvent(MotionEvent event) { 
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			mCamera.takePicture(null, null, null, getPictureCallback);
		return super.onTouchEvent(event);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			initCamera();
		}

	}

	/** Called when the activity is first created. */
	private SurfaceView svCamera = null;
	protected SurfaceHolder mSurfaceHolder;

	private Camera mCamera; // �����hardware��Camera����
	private String imgPath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hideStatusBar();
		setContentView(R.layout.activity_take_photo);

		svCamera = (SurfaceView) findViewById(R.id.svCamera);
		mSurfaceHolder = svCamera.getHolder();
		// ������ΪSurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS��Ͳ��ܻ�ͼ��
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	/**
	 * Jpeg��ʽѹ��
	 */
	PictureCallback getPictureCallback = new PictureCallback() {
		@Override
		// ȡ������ͼƬ
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			// ����ǰ�ر�Ԥ��
			mCamera.stopPreview();
			// ȡ��ͼ��·��
			imgPath = saveFile2(data);
			Intent intent = new Intent();
			intent.putExtra("imagePath", imgPath);
			setResult(RESULT_OK, intent);
			finish();
		}

	};

	/**
	 * save bmp as jpg by path
	 * 
	 * @param bmpPath
	 * @param bmp
	 * @throws IOException
	 */
	public void saveBmp(Bitmap bmp, String fileName) throws IOException {

		File f = new File(fileName);
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int degree;

	/**
	 * return imgFilePath
	 * 
	 * @param data
	 * @return
	 */
	private String saveFile2(byte[] data) {
		File imgFileDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		if (!imgFileDir.exists() && !imgFileDir.mkdirs()) {
			return null;
		}
		// ͼ������
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String strDate = dateFormat.format(new Date());
		String imgFileName = "img_" + strDate + ".jpg";
		// ͼ��·��
		String imgFilePath = imgFileDir.getPath() + File.separator
				+ imgFileName;
		File imgFile = new File(imgFilePath);
		Bitmap orgBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		Bitmap resultBitmap = getCenterImage(orgBitmap);
		try {
			FileOutputStream out = new FileOutputStream(imgFile);
			resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		resultBitmap.recycle();
		return imgFilePath;
	}

	private Bitmap getCenterImage(Bitmap orgBitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		int imageWidth = getResources().getDimensionPixelSize(
				R.dimen.card_image_width);
		int imageHeight = getResources().getDimensionPixelSize(
				R.dimen.card_image_height);
		int topOffset;
		int leftOffset;
		Bitmap rotatedBitmap;
		if (degree == 90 || degree == 270) {
			topOffset = (MainActivity.WIDTH - imageWidth) / 2;
			leftOffset = (MainActivity.HEIGHT - imageHeight) / 2;
			rotatedBitmap = Bitmap.createBitmap(orgBitmap, leftOffset,
					topOffset, imageHeight, imageWidth, matrix, true);
		} else {
			leftOffset = (MainActivity.HEIGHT - imageWidth) / 2;
			topOffset = (MainActivity.WIDTH - imageHeight) / 2;
			rotatedBitmap = Bitmap.createBitmap(orgBitmap, leftOffset,
					topOffset, imageWidth, imageHeight, matrix, true);
		}

		if (orgBitmap != null && orgBitmap != rotatedBitmap)
			orgBitmap.recycle();
		return rotatedBitmap;
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		// data��һ��ԭʼ��JPEGͼ�����ݣ�
		// ���������ǿ��Դ洢ͼƬ������Ȼ���Բ���MediaStore
		// ע�Ᵽ��ͼƬ���ٴε���stopPreview()ֹͣԤ�����ȴ�����
		Uri imageUri = this.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new ContentValues());
		try {
			OutputStream os = this.getContentResolver().openOutputStream(
					imageUri);
			os.write(data);
			os.flush();
			os.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// ���պ�ֹͣԤ��
		mCamera.stopPreview();
	}

	/**
	 * �ر����
	 */
	public void closeCamera() {
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

	/**
	 * ��ʼ�����
	 */
	public void initCamera() {
		mCamera = Camera.open(0);
		try {
			int WIDTH = getResources().getDisplayMetrics().widthPixels;
			int HEIGHT = getResources().getDisplayMetrics().heightPixels;
			Camera.Parameters mParameters = mCamera.getParameters();
			mParameters.setPictureFormat(ImageFormat.JPEG); // ������Ƭ��ʽ
			List<Size> sizes = mParameters.getSupportedPreviewSizes();
			Size optimalSize = getOptimalPreviewSize(sizes, WIDTH, HEIGHT);
			mParameters.setPreviewSize(optimalSize.width, optimalSize.height); // ��С
			mParameters.setPictureSize(optimalSize.width, optimalSize.height);

			mParameters.set("jpeg-quality", 100);// ��Ƭ����
			mCamera.setParameters(mParameters);
			mCamera.setPreviewDisplay(mSurfaceHolder);
			degree = getDisplayOritation(getDispalyRotation(), 0);
			mCamera.setDisplayOrientation(degree);
			mCamera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int getDisplayOritation(int degrees, int cameraId) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;
		} else {
			result = (info.orientation - degrees + 360) % 360;
		}
		return result;
	}

	private int getDispalyRotation() {
		int i = getWindowManager().getDefaultDisplay().getRotation();
		switch (i) {
		case Surface.ROTATION_0:
			return 0;
		case Surface.ROTATION_90:
			return 90;
		case Surface.ROTATION_180:
			return 180;
		case Surface.ROTATION_270:
			return 270;
		}
		return 0;
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.05;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		closeCamera();
	}

	// �� Activity.setCurrentView()֮ǰ����
	public void hideStatusBar() {
		// ���ر���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����ȫ������
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		// ��ô��ڶ���
		Window curWindow = this.getWindow();
		// ����Flag��ʾ
		curWindow.setFlags(flag, flag);
	}
}