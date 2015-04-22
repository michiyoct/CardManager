package com.example.cardmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.assistclass.BitmapUtil;
import com.example.assistclass.ValueUtil;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.Toast;

public class ShowImageActivity extends BaseActivity {

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(CardDetailActivity.CARD_DETAIL_IMAGE_PATHS,
				ValueUtil.list2string(imagePathList));
		intent.putExtra(CardDetailActivity.CARD_DETAIL_ICON_POSITION,
				iconPosition);
		ShowImageActivity.this.setResult(RESULT_OK, intent);
		super.finish();
	}

	private ListView listView;
	private ShowImagePagerAdapter adapter;
	private List<String> imagePathList;
	private List<Bitmap> bitmapList = new ArrayList<Bitmap>();
	private int iconPosition = -1;
	private static int REQUEST_CAMERA = 0;
	private static int REQUEST_PHOTO = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("图册");
		setRightButton(R.drawable.base_title_icon_save, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		setBaseContentView(R.layout.activity_show_image);
		Intent intent = getIntent();
		if (intent != null) {
			imagePathList = ValueUtil.string2list(intent.getExtras().getString(
					CardDetailActivity.CARD_DETAIL_IMAGE_PATHS));
			iconPosition = intent.getIntExtra(
					CardDetailActivity.CARD_DETAIL_ICON_POSITION, -1);
			for (int i = 0; i < imagePathList.size(); i++) {
				String imagePath = imagePathList.get(i);
				Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(
						imagePath,
						getResources().getDimensionPixelSize(
								R.dimen.card_image_width),
						getResources().getDimensionPixelSize(
								R.dimen.card_image_height));

				if (bitmap != null) {
					bitmapList.add(bitmap);
				} else {
					imagePathList.remove(i);
					i--;
				}
			}
		} else {
			Toast.makeText(ShowImageActivity.this, "出现错误，请重试！",
					Toast.LENGTH_SHORT).show();
			finish();
		}
		listView = (ListView) findViewById(R.id.activity_show_image_list);
		adapter = new ShowImagePagerAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 0) {
					CharSequence[] items = { "相机", "相册" };
					new AlertDialog.Builder(ShowImageActivity.this)
							.setTitle("选择图片来源")
							.setItems(items,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (which == 1) {
												Intent intent = new Intent(
														Intent.ACTION_GET_CONTENT);
												intent.addCategory(Intent.CATEGORY_OPENABLE);
												intent.setType("image/*");
												startActivityForResult(Intent
														.createChooser(intent,
																"选择图片"),
														REQUEST_PHOTO);
											} else {
												Intent intent = new Intent(
														ShowImageActivity.this,
														TakePhotoActivity.class);
												startActivityForResult(intent,
														REQUEST_CAMERA);
											}
										}
									}).create().show();
				} else {
					final int index = (int) adapter.getItemId(arg2);
					CharSequence[] items = { "删除", "设为图标" };
					Builder dialogBuilder = new AlertDialog.Builder(
							ShowImageActivity.this);
					dialogBuilder.setTitle("选择需要进行的操作");
					dialogBuilder.setItems(items,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog1,
										int which) {
									if (which == 0) {
										new AlertDialog.Builder(
												ShowImageActivity.this)
												.setTitle("确认删除这张照片？")
												.setPositiveButton(
														"确认",
														new android.content.DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface arg0,
																	int arg1) {
																// TODO
																// Auto-generated
																// method stub
																deleteImage(index);
																dialog.dismiss();
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
																// Auto-generated
																// method stub
																dialog.dismiss();
															}
														}).create().show();
									} else {
										iconPosition = index;
									}
								}
							});
					dialog = dialogBuilder.create();
					dialog.show();
				}
			}
		});
	}

	private AlertDialog dialog;

	private void deleteImage(final int index) {
		Bitmap bmp = bitmapList.get(index);
		if (bmp != null)
			bmp.recycle();
		bitmapList.remove(index);
		File f = new File(imagePathList.get(index));
		if (f.exists()) {
			f.delete();
		}
		if (iconPosition == index)
			iconPosition = -1;
		imagePathList.remove(index);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				String imagePath = data.getStringExtra("imagePath");
				addImage(imagePath);
			} else if (requestCode == REQUEST_PHOTO) {
				// 选择图片
				Uri uri = data.getData();
				String filePath;
				String[] filePathColumn = { MediaColumns.DATA };

				Cursor cursor = ShowImageActivity.this.managedQuery(uri,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				filePath = cursor.getString(columnIndex);
				cursor.close();
				File imgFileDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyyMMddhhmmss");
				String strDate = dateFormat.format(new Date());
				String imgFileName = "img_" + strDate + ".jpg";
				// 图像路径
				String imgFilePath = imgFileDir.getPath() + File.separator
						+ imgFileName;
				Copy(filePath, imgFilePath);
				addImage(imgFilePath);
			}
		} else {
			Toast.makeText(ShowImageActivity.this, "请重新选择图片",
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void Copy(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addImage(String imagePath) {
		if (TextUtils.isEmpty(imagePath)) {
			Toast.makeText(ShowImageActivity.this, "请重新选择图片",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Bitmap bmp = null;
		bmp = BitmapUtil.decodeSampledBitmapFromFile(imagePath,
				MainActivity.CARD_IMAGE_WIDTH, MainActivity.CARD_IMAGE_HEIGHT);
		if (bmp != null) {
			imagePathList.add(imagePath);
			bitmapList.add(bmp);
			adapter.notifyDataSetChanged();
		} else {
			Toast.makeText(ShowImageActivity.this, "请重新选择图片",
					Toast.LENGTH_SHORT).show();
		}
	}

	class ShowImagePagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			int count = bitmapList.size();
			if (count == 0) {
				iconPosition = -1;
			} else {
				if (iconPosition == -1) {
					iconPosition = 0;
				}
			}
			count++;
			return count;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return bitmapList.size() - arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if (arg1 == null) {
				arg1 = new ImageView(ShowImageActivity.this);
				arg1.setLayoutParams(new AbsListView.LayoutParams(
						LayoutParams.MATCH_PARENT,
						(MainActivity.WIDTH - 2 * MainActivity.OFFSET_MEDIUM)
								* MainActivity.CARD_IMAGE_HEIGHT
								/ MainActivity.CARD_IMAGE_WIDTH));
			}
			if (arg0 == 0) {
				((ImageView) arg1).setScaleType(ScaleType.CENTER);
				((ImageView) arg1).setBackgroundColor(0xffdcdcdc);
				((ImageView) arg1)
						.setImageResource(R.drawable.show_image_icon_camera);
			} else {
				int index = bitmapList.size() - arg0;
				((ImageView) arg1).setScaleType(ScaleType.CENTER_CROP);
				((ImageView) arg1).setImageBitmap(bitmapList.get(index));
			}
			return arg1;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return bitmapList.get(arg0 - 1);
		}
	};
}
