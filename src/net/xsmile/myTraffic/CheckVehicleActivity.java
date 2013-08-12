package net.xsmile.myTraffic;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



public class CheckVehicleActivity extends Activity implements OnDateSetListener{
	
	final int FROM_MAIN=1;
	final int FROM_NOTIFICATION=0;
	final int CHANGE=0;
    final int CHANGE_STARTTIME=1;
    final int CHANGE_ENDTIME=2;
    
    Calendar calendar;
    Calendar sCalendar;
    Calendar eCalendar;
    LinearLayout waitProgress;
    TextView vn;
    Button checkButton;
    TextView noteText;
    ListView lv;
    Button startButton;
    Button endButton;
    
    CheckBreaksTask netCheck;
    Vehicle checkVehicle;
    String vLastBreakDate;
   // VehicleBreak checkVehicleBreak;
	

    int flag;
    int source;//开启本Activity来源
    int vehicleIndex;
    
    
    
    String sTime;
    String eTime;
    Bundle bun;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkvehicle);
        setViews();
        setListeners();
        
        init();
        initDate();
        //source=FROM_MAIN;
   
    }
    
    
    
    @Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		Log.i("NewIntent","newIntent");
		init();
        initDate();
        //source=FROM_NOTIFICATION;
		
		
	}
    
    
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		//全局变量复位
		MyApplication.getInstance().setCheckCorrect(false);
		MyApplication.getInstance().setLastBreakDate("1900-1-1 00:00:00");
		MyApplication.getInstance().setLastCheckDate("1900-1-1");
	}



	private void setViews(){
    	//pb=(ProgressBar)findViewById(R.id.wait);
    	waitProgress=(LinearLayout)findViewById(R.id.checking);
    	vn=(TextView)findViewById(R.id.vehiclenum);
    	checkButton=(Button)findViewById(R.id.check);
    	noteText=(TextView)findViewById(R.id.note);
    	lv=(ListView)findViewById(R.id.trafficlistview);
    	startButton=(Button)findViewById(R.id.starttime);
    	endButton=(Button)findViewById(R.id.endtime);

    }
    
    private void setListeners(){
		checkButton.setOnClickListener(buttonListener);
		startButton.setOnClickListener(buttonListener);
		endButton.setOnClickListener(buttonListener);
		lv.setOnItemClickListener(selectBreak);
		lv.setOnItemLongClickListener(shareBreak);
		//lv.setAdapter(breaksAdapter);
		
    }
    
    
	private void init(){
		//全局变量复位
		MyApplication.getInstance().setCheckCorrect(false);
		MyApplication.getInstance().setLastBreakDate("1900-1-1 00:00:00");
		MyApplication.getInstance().setLastCheckDate("1900-1-1");
		
    	vehicleIndex=getIntent().getIntExtra("checkVehicleIndex",-1);
    	source=getIntent().getIntExtra("source", -1);
    	checkVehicle=getIntent().getParcelableExtra("checkVehicle");
    	vLastBreakDate=checkVehicle.getLastBreakDate();
    	sTime=checkVehicle.getStartTime();
    	eTime=checkVehicle.getEndTime();
    	Log.i("source","is +"+source);
    }
   
    
    private Button.OnClickListener buttonListener = new Button.OnClickListener()
    {
        @Override
		public void onClick(View v)
        {
        	switch (v.getId()){
        	case R.id.check:
        		verifyDateAndCheck();
        		break;
        	case R.id.starttime:
        		//Log.i("checkdate",checkVehicle.getLastCheckDate());
        		//bun.putInt("change", 0);
        		flag=0;//标志位
        		dateDialog().show();
        		break;
        	case R.id.endtime:
        		//Log.i("ff","+"+checkVehicle.getVehiclebreaks().size());
        		flag=1;//标志位
        		dateDialog().show();
        		break;
        	default:
        		break;
        	}
        }
    };
    
    private ListView.OnItemClickListener selectBreak=new  ListView.OnItemClickListener()  
    {	  
        @Override  
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
        {  

        	//tb=((MyApplication)getApplication()).getTempVehicle().getVehiclebreaks().get(arg2);
        	//((MyApplication)getApplication()).setTempBreak(tb);
        	//System.out.println(tb.getBreakPicId());
        	Intent myintent = new Intent(getApplicationContext(),BreakDetailActivity.class);
        	myintent.putExtra("checkBreak",checkVehicle.getVehiclebreaks().get(arg2));
        	//Log.i("break","+"+checkVehicle.getVehiclebreaks().get(arg2).getBreakMoney());
        	startActivity(myintent);
        	
        }  
    };
    
 

    public Dialog dateDialog() {
    	if(flag==0){
			String[] ss=sTime.split("-");
			return new DatePickerDialog(this, 
	    			this, Integer.parseInt(ss[0]), 
	    			Integer.parseInt(ss[1])-1, Integer.parseInt(ss[2]));
	    	
		}else{
			String[] ee=eTime.split("-");
			return new DatePickerDialog(this, 
	    			this, Integer.parseInt(ee[0]), 
	    			Integer.parseInt(ee[1])-1, Integer.parseInt(ee[2]));
		}
	
	}
    

    private void initDate(){
    	calendar=Calendar.getInstance();
    	eCalendar=Calendar.getInstance();
    	sCalendar=Calendar.getInstance();
    	
    	if(eTime.equals("0")){
	    	eTime=calendar.get(Calendar.YEAR)+"-"+ 
							(calendar.get(Calendar.MONTH)+1)+"-"+
							calendar.get(Calendar.DAY_OF_MONTH);
	    	//eCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
	    	
	    	if(!checkVehicle.getLastCheckDate().equals("1900-1-1")){
	    		calendar.add(calendar.MONTH,-3);
	    		sTime=(calendar.get(Calendar.YEAR))+"-"+ 
	    			(calendar.get(Calendar.MONTH)+1)+"-"+ calendar.get(Calendar.DAY_OF_MONTH);		
	    	}else{
	    		
	    		sTime=(calendar.get(Calendar.YEAR)-1)+"-1-1";
	    		//sCalendar.set(calendar.get(Calendar.YEAR), 01, 01);
	    		checkVehicle.setStartTime(sTime);
	    		checkVehicle.setEndTime(eTime);
	    		netCheckBreak();
	    	}
    	}else{
    		netCheckBreak();
    	}
    	
    	//Log.i("time","is+"+sTime+eTime);
	
    	startButton.setText("起始时间："+sTime);
    	endButton.setText("结束时间："+eTime);
    	vn.setText("苏K"+checkVehicle.getVehicleNum());
    	eCalendar.setTime(new DateUtil().changeToCheckDate(eTime));
    	sCalendar.setTime(new DateUtil().changeToCheckDate(sTime));
    	
    	
    }
    
    /*
    public static String MonthAdd(String currentDate,int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(currentDate));
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        calendar.add(calendar.MONTH,day);//把日期往后增加一天.整数往后推,负数往前移动
        return sdf.format(calendar.getTime());  //这个时间就是日期往后推一天的结果
    }
    */
    
    
    

	@Override
	public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		String tempTime=arg1+"-"+(arg2+1)+"-"+arg3;
		if(flag==0){
			sCalendar.set(arg1, arg2, arg3);
			sTime=tempTime;
    		startButton.setText("起始时间："+sTime);
    		
		}else{
			eCalendar.set(arg1, arg2, arg3);
			eTime=tempTime;
			endButton.setText("结束时间："+eTime);
		}
	}
    
	private void verifyDateAndCheck(){
		if(eCalendar.before(sCalendar)){
			showDialog(this,"结束时间不能早于起始时间");
		}else{
			checkVehicle.setStartTime(sTime);
			checkVehicle.setEndTime(eTime);
			netCheckBreak();
		}
	}
	
	 private void showDialog(Context context,String what) {   
	        AlertDialog.Builder builder = new AlertDialog.Builder(context);   
	        builder.setIcon(R.drawable.dialog);   
	        builder.setTitle("错误");   
	        builder.setMessage(what); 
	        builder.show();
	 }

	
	
	 
	@Override
	public void onBackPressed() {
		stopTask();
		setLastDate();
		
		//Log.i("source","is "+source);
		if(source==FROM_MAIN){
			if(!checkVehicle.getLastCheckDate().equals("1900-1-1")){
				setBackResult();
				//Log.i("INFO", "set back");
			}
		}else{
			goMyTraffic();
		}
		finish();
	}
	
	private void stopTask(){
		if(netCheck!=null){
			if(!netCheck.getStatus().equals("FINISHED")){
				netCheck.cancel(true);
			}
		}
	}
	
	private void setLastDate(){
		String lastCheckDate=MyApplication.getInstance().getLastCheckDate();
		String lastBreakDate=MyApplication.getInstance().getLastBreakDate();
		String temp=(new DateUtil().isCheckDateNew(vLastBreakDate, lastBreakDate)?lastBreakDate:vLastBreakDate);	
		checkVehicle.setLastBreakDate(temp);
		checkVehicle.setLastCheckDate(lastCheckDate);
	}
	
	private void setBackResult(){
		Intent back=new Intent();
		back.putExtra("checkVehicle", checkVehicle);
		back.putExtra("checkVehicleIndex", vehicleIndex);
		setResult(RESULT_OK,back);
	}
	
	private void goMyTraffic(){
		Intent go=new Intent(getApplicationContext(),MyTrafficActivity.class);
		go.putExtra("checkVehicle", checkVehicle);
		go.putExtra("checkVehicleIndex", vehicleIndex);
		go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(go);
	}
	
	private void netCheckBreak(){
		
		netCheck=new CheckBreaksTask(this,noteText,waitProgress,lv,checkButton);
		netCheck.execute(checkVehicle);
	}
	
	private ListView.OnItemLongClickListener shareBreak=new  ListView.OnItemLongClickListener()
    {
    	@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
         {
    		
    		String text_title="苏K"+checkVehicle.getVehicleNum()+checkVehicle.getVehiclebreaks().get(arg2).getBreakPlace()+"交通违章";
    		String text_content="扬州交违查询小助手提醒您,"+
    							"苏K"+checkVehicle.getVehicleNum()+
    							"于"+checkVehicle.getVehiclebreaks().get(arg2).getBreakTime()+
    							"在"+checkVehicle.getVehiclebreaks().get(arg2).getBreakPlace()+
    							checkVehicle.getVehiclebreaks().get(arg2).getBreakType()+","+
    							"罚款"+checkVehicle.getVehiclebreaks().get(arg2).getBreakMoney()+"元,"+
    							"扣"+checkVehicle.getVehiclebreaks().get(arg2).getBreakScore()+"分。";
    							
    		Intent intent = new Intent(Intent.ACTION_SEND);
    		intent.setType("text/plain");
    		intent.putExtra(Intent.EXTRA_SUBJECT,text_title);//Gmail的邮件主题，可选
    		intent.putExtra(Intent.EXTRA_TEXT,text_content);//文本内容
    		startActivity(Intent.createChooser(intent,"发送于"));//弹出的选择程序处理的文字标题
    	    return true;
         }
    	
    };
	 
	
}
