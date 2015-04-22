package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.unit.SimpleCardInfoUnit;
import com.example.views.CardListSimpleCardView;

public class CardListAdapter extends BaseAdapter {
	public CardListAdapter(Context context) {
		super();
		this.context = context;
	}

	public void setSimpleCardInfoUnitList(
			List<SimpleCardInfoUnit> simpleCardInfoUnitList) {
		this.simpleCardInfoUnitList = simpleCardInfoUnitList;
		notifyDataSetChanged();
	}

	private List<SimpleCardInfoUnit> simpleCardInfoUnitList;
	private Context context;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (simpleCardInfoUnitList == null)
			return 0;
		return simpleCardInfoUnitList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return simpleCardInfoUnitList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return simpleCardInfoUnitList.get(arg0).getRowId();
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (arg1 == null)
			arg1 = new CardListSimpleCardView(context);
		((CardListSimpleCardView) arg1).setContent(simpleCardInfoUnitList
				.get(arg0));
		return arg1;

	}
}