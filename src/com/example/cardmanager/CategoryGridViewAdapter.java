package com.example.cardmanager;

import java.util.List;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class CategoryGridViewAdapter extends BaseAdapter {
	public CategoryGridViewAdapter(Context context, int[] colors) {
		super();
		this.context = context;
		this.colors = colors;
	}

	public void setChecked(int index) {
		this.index = index;
		notifyDataSetChanged();
	}

	public void setCategorys(List<String> categorys) {
		this.categorys = categorys;
		notifyDataSetChanged();
	}

	private Context context;
	private List<String> categorys;
	private int[] colors;
	private int index = -1;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (categorys == null)
			return 0;
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Viewholder viewholder;
		if (convertView == null) {
			convertView = new FrameLayout(context);
			((FrameLayout) convertView)
					.setLayoutParams(new AbsListView.LayoutParams(
							LayoutParams.MATCH_PARENT,
							MainActivity.TEXT_SIZE_MEDIUM + 2 * MainActivity.OFFSET_MEDIUM));
			TextView textView = new TextView(context);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					MainActivity.TEXT_SIZE_MEDIUM);
			textView.setGravity(Gravity.CENTER);
			textView.setPadding(MainActivity.OFFSET_MEDIUM,
					MainActivity.OFFSET_LESS, MainActivity.OFFSET_MEDIUM,
					MainActivity.OFFSET_LESS);
			textView.setTextColor(0xffffffff);
			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(new FrameLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView
					.setImageResource(R.drawable.card_detail_category_selected);
			((FrameLayout) convertView).addView(textView);
			((FrameLayout) convertView).addView(imageView);
			viewholder = new Viewholder();
			viewholder.imageView = imageView;
			viewholder.textView = textView;
			convertView.setTag(viewholder);
		} else {
			viewholder = (Viewholder) convertView.getTag();
		}
		viewholder.textView.setText(categorys.get(position));
		if (position < colors.length)
			viewholder.textView.setBackgroundColor(colors[position]);
		else
			viewholder.textView.setBackgroundColor(colors[position
					- colors.length]);
		if (position != index) {
			viewholder.imageView.setVisibility(View.INVISIBLE);
		}else{
			viewholder.imageView.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	static class Viewholder {
		ImageView imageView;
		TextView textView;
	}

}