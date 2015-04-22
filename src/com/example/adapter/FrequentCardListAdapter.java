package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

import com.example.cardmanager.CardDetailActivity;
import com.example.cardmanager.MainActivity;
import com.example.unit.BasicCardInfoUnit;
import com.example.views.MainFrequentCardView;

public class FrequentCardListAdapter extends BaseAdapter {

	public FrequentCardListAdapter(Context context,
			List<BasicCardInfoUnit> basicCardInfoUnits) {
		super();
		this.context = context;
		this.basicCardInfoUnits = basicCardInfoUnits;
	}

	private List<BasicCardInfoUnit> basicCardInfoUnits;
	private Context context;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MainActivity.COUNT_FREQUENT_CARD;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if (basicCardInfoUnits == null)
			return null;
		else if (basicCardInfoUnits.size() <= arg0)
			return null;
		else
			return basicCardInfoUnits.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		if (basicCardInfoUnits == null)
			return 0;
		else if (basicCardInfoUnits.size() <= arg0)
			return 0;
		else
			return basicCardInfoUnits.get(arg0).getRowId();
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (arg1 == null)
			arg1 = new MainFrequentCardView(context, arg0);
		ViewGroup.LayoutParams viewLP = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				(int) ((MainActivity.HEIGHT - 465 * MainActivity.rate
						- MainActivity.OFFSET_LESS
						- MainActivity.ICON_SIZE_MEDIUM - 4 * MainActivity.OFFSET_MEDIUM) / 5));
		arg1.setLayoutParams(viewLP);
		if (basicCardInfoUnits != null) {
			int index = 0;
			if (basicCardInfoUnits.size() >= 5)
				index = arg0;
			else
				index = basicCardInfoUnits.size()
						- MainActivity.COUNT_FREQUENT_CARD + arg0;
			if (index >= 0 && index < basicCardInfoUnits.size()) {
				final BasicCardInfoUnit basicCardInfoUnit = basicCardInfoUnits
						.get(index);
				((MainFrequentCardView) arg1).setContent(basicCardInfoUnit);
				((MainFrequentCardView) arg1)
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context,
										CardDetailActivity.class);
								intent.putExtra(
										CardDetailActivity.CARD_DETAIL_STATU,
										CardDetailActivity.STATU_SHOW);
								intent.putExtra(
										CardDetailActivity.CARD_DETAIL_ROW_ID,
										basicCardInfoUnit.getRowId());
								context.startActivity(intent);
							}
						});
			}
		}
		return arg1;
	}
}
