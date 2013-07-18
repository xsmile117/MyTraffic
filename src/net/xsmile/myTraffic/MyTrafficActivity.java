package net.xsmile.myTraffic;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MyTrafficActivity extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setViews();
        setDialog();
        setListeners();
        init();
        readVehicle();
		checkUpdate();
		stopBackService(); 
    }
    
   
    static final int ADD_VEHICLE=2;
    static final int CHECK_VEHICLE=1;
    
    private ListView vehicleList;
    private Button addButton;
   
	private ArrayAdapter<String> myVehiclesAdapter;
	private ArrayList<Vehicle> myVehicles;
	private ArrayList<String> myVehicleNames;
    private Boolean isNetWork;
    private Boolean isNeedSave;
    
    private Vehicle myVehicle;
    
    private int mListPos;
    
    
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
        initNet();
		//Log.i("TAG","start");
		
	}
    

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startBackService();
	}


	private void checkUpdate(){
    	Intent intent=getIntent();
    	int index=intent.getIntExtra("checkVehicleIndex", -1);
    	if(index!=-1){
    		updateVehicle(intent);
    	}
    }
    
    private void updateVehicle(Intent intent){
    	Vehicle checkVehicle=intent.getParcelableExtra("checkVehicle");
    	if(checkVehicle!=null){
    	checkVehicle.setEndTime("0");
    	checkVehicle.setStartTime("0");
		int vIndex=intent.getIntExtra("checkVehicleIndex", -1);
    	myVehicles.set(vIndex, checkVehicle);//更新
    	isNeedSave=true;
    	}
    }
    
    private void stopBackService(){
    	//Log.i("TAG","stopservice");
    	Intent intent = new Intent();
	    intent.setAction("net.xsmile.startservice");
		   
	    //要发送的内容
	    intent.putExtra("isStart", false);
	    //发送 一个无序广播
	    this.sendBroadcast(intent);
    }
    
    private void startBackService(){
    	//Log.i("TAG","startservice");
    	Intent intent = new Intent();
	    intent.setAction("net.xsmile.startservice");
		   
	    //要发送的内容
	    intent.putExtra("isStart", true);
	    //发送 一个无序广播
	    this.sendBroadcast(intent);
    }


	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		updateVehicle(intent);
		//Log.i("TAG","newIntent");
	}




	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//initNet();
		//Log.i("TAG","restart");
	}




	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showVehicle();	 
		//Log.i("TAG","resume");
		
	}
    
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(isNeedSave){
			saveVehicles();
			isNeedSave=false;
		}
		//Log.i("TAG","pause");
	}



	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.exit(0);
	}
	
	private Dialog myDialog;
	private TextView dialogContent;
	private ImageView dialogImage;
	
	private void setDialog(){
		myDialog = new Dialog(this);
    	myDialog.setContentView(R.layout.mydialog);
    	dialogContent = (TextView)myDialog.findViewById(R.id.dialogText);
    	dialogImage = (ImageView)myDialog.findViewById(R.id.dialogImage);
    	
	}



	private void setViews(){
		vehicleList=(ListView)findViewById(R.id.vehicle_listview);
		addButton=(Button)findViewById(R.id.add_button);
		
	}
		
	private void setListeners(){
		addButton.setOnClickListener(addVehicle);
		vehicleList.setOnItemClickListener(selectVehicle);
		this.registerForContextMenu(vehicleList);
		//vehicleList.setOnItemLongClickListener(deleteVehicle);
		
	}
	
	
	private void init(){
		//showMessage();
		myVehicles=new ArrayList<Vehicle>();
		myVehicleNames=new ArrayList<String>();
	    isNetWork=false;
	    myVehiclesAdapter=new ArrayAdapter<String>(this, R.layout.vehiclelistview_item,myVehicleNames);
	    vehicleList.setAdapter(myVehiclesAdapter);
		isNeedSave=false;
		
	}
	
	
	/*
	private void showMessage(){
		String myMessage="这款软件纯粹是本人出于对Android的兴趣爱好，利用挤出来的业余时间设计完成。选择交通违章查询这个方面，也是本着于人于己使用方便而已。\n" +
				"随着大家的使用和建议，这款软件更新到了1.7版本。但是昨天，对方服务器端不知何故做了些许改动，导致本软件不能正常使用，现已被紧急修复。\n" +
				"因本人不是神仙，精力实在有限，从1.8版本开始，将不保证此软件的更新或维护，实在抱歉！\n" +
				"最后对某些蛋疼的人说几句，多读读书，做点实事，有那闲工夫把网站完善完善，至少弄个像样的移动版网页出来。大家用的舒服，也就没人会用我这个破东西了。";
		SharedPreferences message =getSharedPreferences("message",Context.MODE_PRIVATE);
    	//SharedPreferences.Editor myVehiclesEditor=myVehicleData.edit();
    	String isShown=message.getString("isShow", "F");
		if(!isShown.equals("T")){
			SharedPreferences.Editor messageEditor=message.edit();
			messageEditor.putString("isShow", "T");
			messageEditor.commit();
			showDialog(this,myMessage);
		}	
	}
	
	
	
	private void showDialog(Context context,String what) {   
        AlertDialog.Builder builder = new AlertDialog.Builder(context);   
       // builder.setIcon(R.drawable.dialog);   
        builder.setTitle("朋友们");   
        builder.setMessage(what); 
        builder.show();
    }
    
    */
	
	private Button.OnClickListener addVehicle = new Button.OnClickListener()
    {
        @Override
		public void onClick(View v)
        {
        	if(isNetWork){
        	Intent myintent = new Intent(getApplicationContext(),AddVehicleActivity.class);
        	startActivityForResult(myintent,ADD_VEHICLE);
        	}
        }
    };
    
    
    
    /*
     * onActivityResult函数后续将执行onRestart/onResume
     * 
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode){
        case ADD_VEHICLE:
    	    if(resultCode==RESULT_OK){
    	    	Vehicle newVehicle=intent.getParcelableExtra("newVehicle");
    	    	myVehicles.add(newVehicle);
    	    	isNeedSave=true;
    	    	//Log.i("add",newVehicle.getLastBreakDate());
            }
            break;
        case CHECK_VEHICLE:
        	if(resultCode==RESULT_OK){
        		Vehicle checkVehicle=intent.getParcelableExtra("checkVehicle");
        		int vIndex=intent.getIntExtra("checkVehicleIndex", -1);
    	    	myVehicles.set(vIndex, checkVehicle);//更新
    	    	isNeedSave=true;
    	    	//Log.i("update",checkVehicle.getVehicleNum());
            }
        	break;
        }
    }
    
    private ListView.OnItemClickListener selectVehicle=new  ListView.OnItemClickListener()  
    {	  
        @Override  
        public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) 
        {  
        	if(isNetWork){
        		myVehicle=myVehicles.get(index);
        		Intent myintent = new Intent(getApplicationContext(),CheckVehicleActivity.class);
        		myintent.putExtra("checkVehicle", myVehicle);
        		myintent.putExtra("checkVehicleIndex", index);
        		myintent.putExtra("source", 1);
        		startActivityForResult(myintent,CHECK_VEHICLE);
        	}
        	
        }  
    };  
    
    /*
    private ListView.OnItemLongClickListener deleteVehicle=new  ListView.OnItemLongClickListener()
    {
    	@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
         { 
    		 myVehicles.remove(arg2);
    		 showVehicle();	
    		 isNeedSave=true;
    	     return true;
         }
    	
    };
    */
    
    
    
  //添加上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	//此方法在每次调用上下文菜单时都会被调用一次
    	//menu.setHeaderIcon(R.drawable.car);
    	final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    	mListPos = info.position;
    	menu.setHeaderTitle("确认");
		menu.add(0, 1, 0, "删除该车辆信息");
		
    }
    
    //响应上下文菜单
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case 1:
			myVehicles.remove(mListPos);
   		 	showVehicle();	
   		 	isNeedSave=true;
   		 	mListPos=-1;
   		 	break;
    	}	
    	return true;
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "说明").setIcon(R.drawable.help);
        menu.add(0, 1, 0, "关于").setIcon(R.drawable.info);
        menu.add(0, 2, 0, "退出").setIcon(R.drawable.exit);
        return true;
    }
    
     @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
             case 0:
            	 myDialog.setTitle("说明");
            	 dialogContent.setText(getString(R.string.info_content));
            	 dialogImage.setImageResource(R.drawable.mydialog);
            	 myDialog.show();
                 break;
             case 1:
            	 myDialog.setTitle("关于");
            	 dialogContent.setAutoLinkMask(Linkify.ALL);
            	 dialogContent.setText(getString(R.string.about_content));
            	 
            	 dialogImage.setImageResource(R.drawable.mydialog);
            	 myDialog.show();
                 break;
             case 2:
            	 finish();
            	 break;
           }
        return true;
     }
    
    private void readVehicle(){
    	SharedPreferences myVehicleData =getSharedPreferences("myVehicles",Context.MODE_PRIVATE);
    	//SharedPreferences.Editor myVehiclesEditor=myVehicleData.edit();
    	String vehicles=myVehicleData.getString("vehicles", "");
    	//Log.i("vehicles",vehicles);
    	if(vehicles!=""){
    	readMyVehicle(vehicles);
    	}
    	
    }
    
    private void readMyVehicle(String vehicles){
    	String[] temps;	
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
    		myVehicles.add(tempVehicle);
    	}
    }
    
    
    private void showVehicle(){
    	myVehicleNames.clear();
    	for(Vehicle i:myVehicles){
			myVehicleNames.add("苏K"+i.getVehicleNum());
		}
    	myVehiclesAdapter.notifyDataSetChanged();
    	
    }
    
    
    private void saveVehicles(){
    	SharedPreferences myVehicleData =getSharedPreferences("myVehicles",Context.MODE_PRIVATE);
    	SharedPreferences.Editor myVehiclesEditor=myVehicleData.edit();
    	String temp="";
    	
    	for(Vehicle i:myVehicles){
    		temp+=i.getVehicleType()+"+"+i.getVehicleNum()+"+"+i.getVehicleId()+"+"+i.getLastBreakDate()+"+"+i.getLastCheckDate()+"|";
    		
		}
    	myVehiclesEditor.putString("vehicles", temp);
    	myVehiclesEditor.commit();
    	
    }
    
    private void initNet(){

    	ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
    	NetworkInfo info = cwjManager.getActiveNetworkInfo(); 
    	if (info != null && info.isAvailable() && info.getExtraInfo()!="cmwap"){ 
    		isNetWork=true;
    	} 
    	else if(info != null && info.getExtraInfo()=="cmwap"){
			Toast.makeText(this,"不支持cmwap网络，请使用其他网络连接！",Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this,"未连接网络，不能查询违章信息！",Toast.LENGTH_LONG).show();
    	}
    	
    }
    
	
}