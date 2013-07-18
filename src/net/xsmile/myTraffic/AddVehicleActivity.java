package net.xsmile.myTraffic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;


public class AddVehicleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addvehicle);
		
		setViews();
		setListeners();
		
	}
	
	static final int CHECK_BREAK=1;
	private Spinner spin;
	private EditText cp;
	private EditText id;
	private CheckBox save;
	private Button checkButton;
	//private List<Vehicle> tempVehicles;
	private Vehicle newVehicleInfo;
	Boolean isSave;
	
	
	
	private void setViews(){
		spin=(Spinner)findViewById(R.id.Spinner02); 
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource ( this, R.array.vehicleType_arry , android.R.layout.simple_spinner_item); 
		adapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item); 
		spin.setAdapter (adapter);
		spin.setSelection(1, true);//设置默认选项
		
		cp=(EditText)findViewById(R.id.editText1);
		id=(EditText)findViewById(R.id.editText2);
		save=(CheckBox)findViewById(R.id.checkBox1);
		checkButton=(Button)findViewById(R.id.newcheck_button);
	}
	
	private void setListeners(){
		checkButton.setOnClickListener(newVehicle);
		//spin.setOnItemSelectedListener(selectVehicleType);
	}
	
	
	
	
	
	
	
	
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("TAG","Destroy");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("TAG","Pause");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("TAG","Restart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("TAG","Resume");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("TAG","Start");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("TAG","Stop");
	}

	private Button.OnClickListener newVehicle = new Button.OnClickListener()
    {
        @Override
		public void onClick(View v)
        {
        	if(verifyVehicle()){
        		readVehicleAndCheck();
        	}
        }
    };
    

    
    private Boolean verifyVehicle(){
    	if(cp.getText().length()<5){
    		
    		showDialog(this,"车牌号码不能少于5位");
    		return false;
    	}else if(id.getText().length()<6){
    		
    		showDialog(this,"验证码不能少于6位");
    		return false;
    	}else{
    		return true;
    	}
    	
    }
    
    private void readVehicleAndCheck(){
    	String vehicleType;
    	//List<Vehicle> tempVehicles=new ArrayList<Vehicle>();
    	int temp=spin.getSelectedItemPosition()+1;
    	if(temp<10){
    		vehicleType="0"+temp;
    	}else{
    		vehicleType=String.valueOf(temp);
    	}
    	String vehicleNum=cp.getText().toString().toUpperCase();
    	String vehicleId=id.getText().toString();
    	isSave=save.isChecked();
    
    	newVehicleInfo=new Vehicle();
    	newVehicleInfo.setVehicleId(vehicleId);
    	newVehicleInfo.setVehicleNum(vehicleNum);
    	newVehicleInfo.setVehicleType(vehicleType);
    	//Log.i("newVehicleInfo","is "+newVehicleInfo.getEndTime());
    	Intent myintent = new Intent(getApplicationContext(),CheckVehicleActivity.class);
    	myintent.putExtra("checkVehicle", newVehicleInfo);
    	myintent.putExtra("source", 1);
    	startActivityForResult(myintent,CHECK_BREAK);

    	
    	
    }
    
    private void showDialog(Context context,String what) {   
        AlertDialog.Builder builder = new AlertDialog.Builder(context);   
        builder.setIcon(R.drawable.dialog);   
        builder.setTitle("错误");   
        builder.setMessage(what); 
        builder.show();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //Log.i("Tag", "ActivityResult");
        switch(requestCode){
        case CHECK_BREAK:
    	    if(resultCode==RESULT_OK){
    	    	Vehicle newVehicle=intent.getParcelableExtra("checkVehicle");
    	    	//Log.i("addBack","true");
    	    	Intent back=new Intent();
    	    	//String lastCheckDate=intent.getStringExtra("lastcheckdate");
    	    	//String lastBreakDate=intent.getStringExtra("lastbreakdate");
    	    	//newVehicleInfo.setLastBreakDate(lastBreakDate);
    	    	//newVehicleInfo.setLastCheckDate(lastCheckDate);
    	    	back.putExtra("newVehicle",newVehicle);
    	    	setResult(RESULT_OK,back);
    	    	finish();
            }
            break;
        }
        
        
    }
    

}
