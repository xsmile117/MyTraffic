package net.xsmile.myTraffic;

import android.app.Application;
import android.graphics.Bitmap;



public class MyApplication extends Application {
	
	
	
	/**
	 * ����ȫ�ֱ���
	 * ȫ�ֱ���һ�㶼�Ƚ������ڴ���һ���������������ļ�����ʹ��static��̬����
	 * 
	 * ����ʹ������Application��������ݵķ���ʵ��ȫ�ֱ���
	 * ע����AndroidManifest.xml�е�Application�ڵ����android:name=".MyApplication"����
	 * 
	 */

	public MyApplication(){
		infomation="�����쳣";
		checkCorrect=false;
		lastBreakDate="1900-1-1 00:00:00";
		lastCheckDate="1900-1-1";
	}
	
	//����������ʹ��MyApplication.getInstance()�Ϳ��Ի�ȡӦ�ó���Context��
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