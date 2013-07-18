package net.xsmile.myTraffic;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		boolean isStart=intent.getBooleanExtra("isStart", false);
		if(isStart){
			setBackService(context);
		}else{
			unsetBackService(context);
		}
		//Log.i("Receive","is"+isStart);


	}
	
	private void setBackService(Context context){
		long repeatTime=24 * 60 * 60 * 1000;
		PendingIntent mAlarmSender;
		mAlarmSender = PendingIntent.getService(context, 0, new Intent(context,
				BackgroundCheckService.class), 0);
		
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ repeatTime, 2*repeatTime, mAlarmSender);    
        
	}
	
	private void unsetBackService(Context context){
		PendingIntent mAlarmSender;
		mAlarmSender = PendingIntent.getService(context, 0, new Intent(context,
				BackgroundCheckService.class), 0);
		
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.cancel(mAlarmSender);
	}
	
	

}
