package com.example.adapter;

import java.util.List;

import com.example.cardmanager.MainActivity;
import com.example.cardmanager.R;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageListAdapter extends BaseAdapter {
	public MessageListAdapter(Context context) {
		super();
		this.context = context;
	}

	public void setMessageNumber(List<String> messages, List<Long> threadIds) {
		this.messages = messages;
		this.threadIds = threadIds;
		notifyDataSetChanged();
	}

	private Context context;
	private List<String> messages;
	private List<Long> threadIds;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (messages == null)
			return 0;
		return messages.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return messages.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		if (threadIds == null)
			return arg0;
		return threadIds.get(arg0);
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (arg1 == null) {
			arg1 = new TextView(context);
			arg1.setBackgroundResource(R.drawable.background_corner_radius_all_99000000);
			((TextView) arg1).setTextColor(0xffffffff);
			((TextView) arg1).setTextSize(TypedValue.COMPLEX_UNIT_PX,
					MainActivity.TEXT_SIZE_MEDIUM);
			((TextView) arg1).setPadding(MainActivity.OFFSET_MEDIUM,
					MainActivity.OFFSET_MEDIUM, MainActivity.OFFSET_MEDIUM,
					MainActivity.OFFSET_MEDIUM);
		}
		((TextView) arg1).setText(messages.get(arg0));
		return arg1;
	}
}
