package net.xsmile.myTraffic;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootReceiver extends BroadcastReceiver {
	
	private PendingIntent mAlarmSender;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// 在这里干你想干的事（启动一个Service，Activity等），本例是启动一个定时调度程序，每5天启动一个Service去更新数据
		long repeatTime=24 * 60 * 60 * 1000;
		mAlarmSender = PendingIntent.getService(context, 0, new Intent(context,
				BackgroundCheckService.class), 0);
		
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		//am.cancel(mAlarmSender);
		//long firstTime = SystemClock.elapsedRealtime();
		//am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5*60*1000 , mAlarmSender);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5*60*1000, 2*repeatTime, mAlarmSender);    
        
	}
}