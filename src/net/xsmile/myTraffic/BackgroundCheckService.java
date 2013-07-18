package net.xsmile.myTraffic;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;


public class BackgroundCheckService extends Service {
	


	private ArrayList<Vehicle> myVehicles;
	private int index;
	private Vehicle checkVehicle;
	private int num=0;
	//boolean flag=false;//标志位，阻止一次重复的查询
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
		//int repeatTime=2000;
		myVehicles=new ArrayList<Vehicle>();
		index=-1;
		
		
		//flag=true;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("SERVICE","Destory");
		
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.i("SERVICE","Start");
		
		
		//if(!flag){
			ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
	    	NetworkInfo info = cwjManager.getActiveNetworkInfo(); 
	    	if (info != null && info.isAvailable() && info.getExtraInfo()!="cmwap"){ 
				myVehicles.clear();
				readVehicle();
				NetOperation operation=new NetOperation();
				if(operation.netReady()){
					for(Vehicle i:myVehicles){
						index++;
						num=operation.getBreaks(i);
						Log.i("num",""+num);
						if(MyApplication.getInstance().isCheckCorrect()&&num>0){
							checkVehicle=i;
							showNotification(checkVehicle,index,num);
							num=0;
						}
					}
				}
				index=-1;
	    	}
		//}
		//flag=false;
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
    private void readVehicle(){
    	SharedPreferences myVehicleData =getSharedPreferences("myVehicles",Context.MODE_PRIVATE);
    	//SharedPreferences.Editor myVehiclesEditor=myVehicleData.edit();
    	String vehicles=myVehicleData.getString("vehicles", "");
    	//Log.i("vehicles",vehicles);
    	if(vehicles!=""){
    		readMyVehicle(vehicles);
    	//showVehicle();
    	}
    	
    }
    
    private void readMyVehicle(String vehicles){
    	String[] temps;	
    	//List<Vehicle> tempVehicles=new ArrayList<Vehicle>();
    		//((MyApplication)getApplication()).getMyVehicles();
    	
    	temps=vehicles.split("\\|");
    	for (int i = 0 ; i <temps.length ; i++ ) {
    		
    		String[] temp;
    		Vehicle tempVehicle=new Vehicle();
    		temp=temps[i].split("\\+");
    		tempVehicle.setVehicleType(temp[0]);
    		tempVehicle.setVehicleNum(temp[1]);
    		tempVehicle.setVehicleId(temp[2]);
    		tempVehicle.setLastBreakDate(temp[3]);
    		tempVehicle.setLastCheckDate(temp[4]);
    		tempVehicle.setStartTime(new DateUtil().afterLastBreakDate(temp[3]));
    		tempVehicle.setEndTime(new DateUtil().today());
    		myVehicles.add(tempVehicle);
    	}
    	
    	//Log.i("size","is+"+myVehicles.size()+myVehicles.get(0).getVehicleNum());
    	//((MyApplication)getApplication()).setMyVehicles(tempVehicles);
    }
    
    private void showNotification(Vehicle v,int index,int nums){
    	Log.i("nums",""+nums);
    	String notice="苏K"+v.getVehicleNum()+"有"+nums+"条新违章信息!";
    	NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);				
    	Notification n = new Notification(R.drawable.notify, notice, System.currentTimeMillis());				
    	n.flags = Notification.FLAG_AUTO_CANCEL;
    	n.defaults=Notification.DEFAULT_ALL;
    	Intent i = new Intent(getApplicationContext(), CheckVehicleActivity.class);
    	i.putExtra("checkVehicle", v);
    	i.putExtra("checkVehicleIndex", index);
    	i.putExtra("source",0);
    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);			
    	//PendingIntent
    	PendingIntent contentIntent = PendingIntent.getActivity(
    			getApplicationContext(), 
    			index, 
    			i, 
    			PendingIntent.FLAG_UPDATE_CURRENT);
    					
    	n.setLatestEventInfo(
    			getApplicationContext(),
    			notice, 
    			"点击查看", 
    			contentIntent);
    	nm.notify(index, n);
    	//nm.notify(2, n);
    }

}
