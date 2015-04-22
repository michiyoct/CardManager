package com.example.receiver;

import com.example.service.NoticeService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NoticeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		arg0.startService(new Intent(arg0, NoticeService.class));
	}

}
