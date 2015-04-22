package com.example.service;

import java.util.Calendar;

import com.example.cardmanager.CardListActivity;
import com.example.cardmanager.MainActivity;
import com.example.cardmanager.R;
import com.example.dbhelper.CardInfoDBHelper;
import com.example.dbhelper.CardManager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;

public class NoticeService extends Service {
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		AlarmManager am = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		Intent broadcastIntent = new Intent("WAKEUP");
		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
				0, broadcastIntent, 0);
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.HOUR, 24);// 设置启动时间为30秒后
		am.set(AlarmManager.RTC_WAKEUP, cl.getTimeInMillis() + 1000, pi);
		if (needNotice()) {
			CardInfoDBHelper.initCardInfoDBHelper(getApplicationContext());
			CardManager.refreshExpiringCard();
			int count = CardManager.getExpiringCard().size();
			if (count > 0) {
				sendNotice(count);
			}
			SharedPreferences sp = getSharedPreferences(
					MainActivity.SHARE_PREFERENCE, 0);
			Editor editor = sp.edit();
			editor.putLong(MainActivity.SHARE_PREFERENCE_NOTICE_TIME, Calendar
					.getInstance().getTimeInMillis());
			editor.commit();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public static final String WAKE_UP_TAG = "WAKE_UP";

	private void sendNotice(int count) {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(this, CardListActivity.class);
		intent.putExtra(CardListActivity.CARD_LIST_MODE,
				CardListActivity.MODE_EXPIRING);
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
				0, intent, 0);
		Notification n = new Notification();
		n.icon = R.drawable.ic_launcher;
		n.tickerText = "您有" + count + "张卡片即将过期，请点击查看";
		n.when = System.currentTimeMillis();
		n.setLatestEventInfo(getApplicationContext(), "卡片到期提醒", "您有" + count
				+ "张卡片即将过期，请点击查看", pi);
		nm.notify(1, n);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean needNotice() {
		SharedPreferences sp = getSharedPreferences(
				MainActivity.SHARE_PREFERENCE, 0);
		long noticeTime = sp.getLong(MainActivity.SHARE_PREFERENCE_NOTICE_TIME,
				0);
		boolean isNotice = sp.getBoolean(MainActivity.SHARE_PREFERENCE_NOTICE,
				false);
		System.out.println("time:" + noticeTime);
		if (isNotice) {
			long currentTime = Calendar.getInstance().getTimeInMillis();
			if ((currentTime - noticeTime) / 1000 / 60 / 60 / 24 > 0) {
				System.out.println("not notice");
				return true;
			} else {
				System.out.println("noticed");
				return false;
			}
		} else {
			return false;
		}
	}
}
