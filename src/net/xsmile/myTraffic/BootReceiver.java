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
		// �����������ɵ��£�����һ��Service��Activity�ȣ�������������һ����ʱ���ȳ���ÿ5������һ��Serviceȥ��������
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