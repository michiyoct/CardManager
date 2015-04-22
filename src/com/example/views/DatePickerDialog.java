package com.example.views;

import com.example.cardmanager.R;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class DatePickerDialog extends Dialog {
	public static String DATE_SEPERATOR = "/";
	private Context context;
	private int style;
	private OnDateSetListener listener;
	private NumberPicker np1, np2, np3;
	private int str1 = 1999;
	private int str2 = 1;
	private int str3 = 1;

	public DatePickerDialog(Context context, OnDateSetListener listener,
			int year, int month, int day, int style) {
		super(context);
		this.context = context;
		this.listener = listener;
		str1 = year;
		str2 = month;
		str3 = day;
		this.style = style;
	}

	public DatePickerDialog(Context context, int style) {
		super(context);
		this.context = context;
		this.style = style;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_date_picker);
		np1 = (NumberPicker) findViewById(R.id.dialog_date_picker_np1);
		np2 = (NumberPicker) findViewById(R.id.dialog_date_picker_np2);
		np3 = (NumberPicker) findViewById(R.id.dialog_date_picker_np3);

		np1.setMaxValue(2299);
		np1.setMinValue(1970);
		np2.setMaxValue(12);
		np2.setMinValue(1);
		np3.setMaxValue(31);
		np3.setMinValue(1);
		np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				str1 = np1.getValue();
				if (str1 % 4 == 0 && str1 % 100 != 0 || str1 % 400 == 0) {
					if (str2 == 1 || str2 == 3 || str2 == 5 || str2 == 7
							|| str2 == 8 || str2 == 10 || str2 == 12) {
						np3.setMaxValue(31);
						np3.setMinValue(1);
					} else if (str2 == 4 || str2 == 6 || str2 == 9
							|| str2 == 11) {
						np3.setMaxValue(30);
						np3.setMinValue(1);
					} else {
						np3.setMaxValue(29);
						np3.setMinValue(1);
					}

				} else {
					if (str2 == 1 || str2 == 3 || str2 == 5 || str2 == 7
							|| str2 == 8 || str2 == 10 || str2 == 12) {
						np3.setMaxValue(31);
						np3.setMinValue(1);
					} else if (str2 == 4 || str2 == 6 || str2 == 9
							|| str2 == 11) {
						np3.setMaxValue(30);
						np3.setMinValue(1);
					} else {
						np3.setMaxValue(28);
						np3.setMinValue(1);
					}
				}

			}
		});
		np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				str2 = np2.getValue();
				if (str2 == 1 || str2 == 3 || str2 == 5 || str2 == 7
						|| str2 == 8 || str2 == 10 || str2 == 12) {
					np3.setMaxValue(31);
					np3.setMinValue(1);
				} else if (str2 == 4 || str2 == 6 || str2 == 9 || str2 == 11) {
					np3.setMaxValue(30);
					np3.setMinValue(1);
				} else {
					if (str1 % 4 == 0 && str1 % 100 != 0 || str1 % 400 == 0) {
						np3.setMaxValue(29);
						np3.setMinValue(1);
					} else {
						np3.setMaxValue(28);
						np3.setMinValue(1);
					}
				}
			}
		});
		np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				str3 = np3.getValue();
			}
		});
		np1.setValue(str1);
		np2.setValue(str2);
		np3.setValue(str3);
		Button bckButton = ((Button) findViewById(R.id.dialog_date_picker_back));

		bckButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		Button cfmButton = ((Button) findViewById(R.id.dialog_date_picker_confirm));

		if (listener != null) {
			cfmButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onDateSet(null, str1, str2, str3);
					dismiss();
				}
			});
		}
	}

	public String getDate() {
		return str1 + DATE_SEPERATOR + str2 + DATE_SEPERATOR + str3;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}
}
