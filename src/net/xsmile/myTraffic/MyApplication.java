package net.xsmile.myTraffic;

import android.app.Application;
import android.graphics.Bitmap;



public class MyApplication extends Application {
	
	
	
	/**
	 * 创建全局变量
	 * 全局变量一般都比较倾向于创建一个单独的数据类文件，并使用static静态变量
	 * 
	 * 这里使用了在Application中添加数据的方法实现全局变量
	 * 注意在AndroidManifest.xml中的Application节点添加android:name=".MyApplication"属性
	 * 
	 */

	public MyApplication(){
		infomation="数据异常";
		checkCorrect=false;
		lastBreakDate="1900-1-1 00:00:00";
		lastCheckDate="1900-1-1";
	}
	
	//在任意类中使用MyApplication.getInstance()就可以获取应用程序Context了
	 private static MyApplication instance;

	    public static MyApplication getInstance() {
	        return instance;
	    }
	    
	    @Override
	    public void onCreate() {
	        // TODO Auto-generated method stub
	        super.onCreate();
	        instance = this;
	    }
	    
	    
	
	




	private Bitmap[] tempPics;
	private boolean checkCorrect;
	private String lastBreakDate;
	private String lastCheckDate;
	private String infomation;
	
	

	public String getLastCheckDate() {
		return lastCheckDate;
	}

	public void setLastCheckDate(String lastCheckDate) {
		this.lastCheckDate = lastCheckDate;
	}

	public String getLastBreakDate() {
		return lastBreakDate;
	}

	public void setLastBreakDate(String lastBreakDate) {
		this.lastBreakDate = lastBreakDate;
	}

	public boolean isCheckCorrect() {
		return checkCorrect;
	}

	public void setCheckCorrect(boolean isCheckCorrect) {
		this.checkCorrect = isCheckCorrect;
	}

	public Bitmap[] getTempPics() {
		return tempPics;
	}

	public void setTempPics(Bitmap[] tempPics) {
		this.tempPics = tempPics;
	}

	public String getInfomation() {
		return infomation;
	}

	public void setInfomation(String infomation) {
		this.infomation = infomation;
	}


}