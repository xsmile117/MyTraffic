package net.xsmile.myTraffic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.net.ParseException;


public class DateUtil {
	public DateUtil(){
		
	}
	
	public Date changeToCheckDate(String dateString){
		Date date=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			try {
				date=sdf.parse(dateString);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public boolean isCheckDateNew(String oldCheckDateString,String newCheckDateString){
		Date newCheckDate=null;
		Date oldCheckDate=null;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			try {
				newCheckDate=sdf.parse(newCheckDateString);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				oldCheckDate=sdf.parse(oldCheckDateString);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(newCheckDate!=null){
			return(newCheckDate.after(oldCheckDate));
		}else{
			return false;
		}
	}
	
	public String afterLastBreakDate(String breakDate){
		String dateAfter;
		String lastBreakDate=breakDate;
		Date d=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			try {
				d=sdf.parse(lastBreakDate);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//oldCheckDate=sdf.parse(oldCheckDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DAY_OF_MONTH, 1);
		dateAfter=c.get(Calendar.YEAR)+"-"+ 
				(c.get(Calendar.MONTH)+1)+"-"+
				c.get(Calendar.DAY_OF_MONTH);
		return dateAfter;
		
	}
	
	public String today(){
		Calendar c=Calendar.getInstance();
		return c.get(Calendar.YEAR)+"-"+ 
		(c.get(Calendar.MONTH)+1)+"-"+
		c.get(Calendar.DAY_OF_MONTH);
	}
	


}
