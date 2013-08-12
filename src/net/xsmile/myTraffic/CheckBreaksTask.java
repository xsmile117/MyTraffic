package net.xsmile.myTraffic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;



//AsyncTask<>的参数类型由用户设定，这里设为
class CheckBreaksTask extends AsyncTask<Object, Void, Integer> {
	
	private Context myContext;
	private TextView myView;
	private LinearLayout myProgress;
	private ListView myListView;
	private Button myCheckButton;
	private Vehicle checkVehicle;
	
	
	public CheckBreaksTask(Context c,TextView v,LinearLayout p,ListView l,Button b){
		this.myContext=c;
		this.myView=v;
		this.myProgress=p;
		this.myListView=l;
		this.myCheckButton=b;

		
	}
	
	//HttpClient myHttpClient=MyApplication.getInstance().getMyHttpClient();
	//VehicleBreak checkVehicleBreak;
	List<VehicleBreak> myVehicleBreaks=new ArrayList<VehicleBreak>();
	ArrayList<HashMap<String, String>> myBreakList = new ArrayList<HashMap<String, String>>(); 
	HandleHtml handleHtml=new HandleHtml();
	String info="";
	
    // 后台执行的耗时任务，接收参数并返回结果
    // 当onPostExecute()执行完，在后台线程中被系统调用
	/*
	 *返回值0表示无违章
	 *      1表示成功
	 *      -1表示失败
	 */
    @Override
    protected Integer doInBackground(Object... params) {
        // TODO Auto-generated method stub
    	//获取NetOperation.excute(String xx)中的参数    
    	NetOperation operation=new NetOperation();
    	checkVehicle=(Vehicle)params[0];
    	
    	if(!isCancelled()&&(operation.netReady())){
    		int number=operation.getBreaks(checkVehicle);
    		if((!isCancelled())&&(number>0)){
    			for(int i=0;i<number;i++){
    				int code=operation.getBreaksDetail(i);
    				if((!isCancelled())&&(code==1)){
    					continue;
    				}else{
    					info=MyApplication.getInstance().getInfomation();
    					return -1;
    				}
    			}
    			//Log.i("breaks","size"+operation.getHandleHtml().getBreaks().size());
    			checkVehicle.setVehiclebreaks(operation.getHandleHtml().getBreaks());
    			handleHtml=operation.getHandleHtml();
    			return 1;
    		}else if((!isCancelled())&&(number==0)){
    			info=MyApplication.getInstance().getInfomation();
    			return 0;
    		}else{
    			info=MyApplication.getInstance().getInfomation();
    			//Log.i("wrong",info);
    			return -1;
    		}
    	}else{
    		info="对方服务器错误，请稍后重试！";
    		//Log.i("wrongwww",info);
    		return -1;
    		
    	}
    }
    
    

    @Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		Log.i("task","Task is Cancelled");
	}



	// 任务执行结束后，在UI线程中被系统调用
    // 一般用来显示任务已经执行结束
    @Override
    protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            //System.out.println("onPostExecute name --------> " + Thread.currentThread().getName());
            //System.out.println("onPostExecute id --------> " + Thread.currentThread().getName());
            super.onPostExecute(result);
            //ListView lv=(ListView)MyApplication.getInstance().getApplicationContext().getResources().getClass().;
            //(Activity)context.con
            //Toast.makeText(testAsync.this, result, Toast.LENGTH_SHORT).show();
            //(SimpleAdapter)myListView.getAdapter()re
            myProgress.setVisibility(View.GONE);
            myCheckButton.setEnabled(true);
            myListView.setEnabled(true);
            switch(result){
            case 0:
            	myView.setText(info);
            	if(MyApplication.getInstance().isCheckCorrect()){
            		MyApplication.getInstance().setLastCheckDate(Calendar.getInstance().get(Calendar.YEAR)+"-"+ 
    						(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+
    						Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            	}
            	break;
            case 1:
            	myVehicleBreaks=checkVehicle.getVehiclebreaks();
            	String breakresult="您累计违章"+myVehicleBreaks.size()+
								"次，共罚款"+handleHtml.getAllMoney()+
								"元，扣"+handleHtml.getAllScore()+"分!";
            	myView.setText(breakresult);
            	setBreakList();
            	myListView.setAdapter(new SimpleAdapter(myContext, 
            			myBreakList, R.layout.breaklistview_item,
            			new String[] {"monitortype", "breakmoney","breakscore","breaksummary","breaktime"},
            			new int[] {R.id.monitortype,R.id.breakmoney,R.id.breakscore,R.id.breaksummary,R.id.breaktime}));
            	String temp1=myVehicleBreaks.get(0).getBreakTime();
            	String temp2=MyApplication.getInstance().getLastBreakDate();
            	String temp=(new DateUtil().isCheckDateNew(temp2, temp1)?temp1:temp2);	
            	MyApplication.getInstance().setLastBreakDate(temp);
            	MyApplication.getInstance().setLastCheckDate(Calendar.getInstance().get(Calendar.YEAR)+"-"+ 
						(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+
						Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            	break;
            case -1:
            	myView.setText(info);
            	break;
            }
    }
        
    public void setBreakList(){
    	for(VehicleBreak i:myVehicleBreaks){
        	HashMap<String, String> map = new HashMap<String, String>();  
        	map.put("monitortype", i.getMonitorType());  
        	map.put("breakmoney", i.getBreakMoney()); 
        	map.put("breakscore", i.getBreakScore());
        	map.put("breaksummary", i.getBreakPlace()+i.getBreakType());
        	map.put("breaktime", i.getBreakTime());
        	myBreakList.add(map);  
        }
        	
    }
        
       
       

    // 最先执行，在UI线程中被系统调用
    // 一般用来在UI中产生一个进度条
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
       // System.out.println("onPreExecute id -------> " + Thread.currentThread().getId());
        //System.out.println("onPreExecute name -------> " + Thread.currentThread().getName() );
    	super.onPreExecute();
        myCheckButton.setEnabled(false);
        myProgress.setVisibility(View.VISIBLE);
        myListView.setEnabled(false);
        //myProgress.VISIBLE;
        //CheckVehicleActivity
                
    }

    // 更新界面操作，在收到更新消息后，在UI线程中被系统调用
    @Override
    protected void onProgressUpdate(Void... values) {
        // TODO Auto-generated method stub
       // System.out.println("onProgressUpdate id ---------> " + Thread.currentThread().getId());
        //System.out.println("onProgressUpdate name -------> " + Thread.currentThread().getName());
        super.onProgressUpdate(values);       
    }
        
}