package net.xsmile.myTraffic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;



public class BreakDetailActivity extends Activity implements OnItemSelectedListener,
ViewFactory {
	
	private Gallery myGallery;
	private ImageSwitcher myImageSwitcher;
	private TextView myTextView;
	private LinearLayout myProgress;
	private LinearLayout picshow;
	private TextView title;
	private ImageView imageView;
	private TextView breakTimeView;
	
	
	private int picNum;
	private VehicleBreak checkBreak;
	
	
	private CheckBreakDetailTask netCheck;
	private boolean isCheckRunning;
	private float cw,ch,pw,ph;   //容器长宽；图片长宽
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakdetail);
        setViews();
        setListeners();
        init();
        
 
        netCheck=new CheckBreakDetailTask(this,myGallery,myProgress,picshow,myImageSwitcher,imageView);
        netCheck.execute(checkBreak.getBreakPicId());
        isCheckRunning=false;

       
    }
    
    
 
    private void setViews(){
    	myProgress=(LinearLayout)findViewById(R.id.picwait);
        picshow=(LinearLayout)findViewById(R.id.picshow);
        myTextView = (TextView)findViewById(R.id.breakdetail);
        breakTimeView = (TextView)findViewById(R.id.breaktime);
        title=(TextView)findViewById(R.id.breaktext);
        myGallery = (Gallery) findViewById(R.id.mygallery);
        myImageSwitcher=(ImageSwitcher)findViewById(R.id.imageSwitcher1);
    }
    
    private void setListeners(){     
    	myImageSwitcher.setFactory(this);
        myGallery.setOnItemSelectedListener(this);
        // 设置ImageSwitcher组件显示图像的动画效果
        myImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        myImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
    }
    
    
    private void init(){
    	isCheckRunning=false;
    	checkBreak=getIntent().getParcelableExtra("checkBreak");
    	title.setText(checkBreak.getBreakPlace());
    	breakTimeView.setText(checkBreak.getBreakTime());
        myTextView.setText(checkBreak.getBreakTypeDetail());
        picNum=-1;
       
    }
    


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(picNum!=-1){
        	menu.add(0, 0, 0, "保存当前违章图片");
        }
        return true;
    }
    
     @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
             case 0:
            	 if(savePic(picNum)){
                 	Toast.makeText(getApplicationContext(), "图片保存成功！",
                 		     Toast.LENGTH_SHORT).show();
                 }else{
                 	Toast.makeText(getApplicationContext(), "图片保存失败！",
                 		     Toast.LENGTH_SHORT).show();
                 }
                 break;
        }
        return true;
     }
    
    
    
    
    //ImageView switcher必须要实现的
	@Override
	public View makeView() {
		//Log.i("makeview","done");
		// TODO Auto-generated method stub
		imageView = new ImageView(this);
		imageView.setBackgroundColor(0xFFededed);
		imageView.setScaleType(ImageView.ScaleType.MATRIX);
		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
		         android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT));
		 
		//registerForContextMenu(imageView);
		
		imageView.setOnTouchListener(new MulitPointTouchListener());
		//imageView.setOnLongClickListener(imageclick);
		return imageView;
	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Bitmap b=((MyApplication)getApplication()).getTempPics()[arg2];
		setPicCenter(b);
		picNum=arg2;
		 
		
	}


	/*
	private ImageView.OnLongClickListener imageclick=
    	new ImageView.OnLongClickListener(){
    	

		@Override
		public boolean onLongClick(View arg0) {
			// TODO Auto-generated method stub
			showAlertDialog2();
			return true;
		}
    };
    */

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Log.i("isRunning","+"+isCheckRunning);
		if(isCheckRunning){
			netCheck.cancel(true);
			isCheckRunning=false;
		}
	}
	
	
	private void setPicCenter(Bitmap b){
		Matrix mMatrix = new Matrix();
		float pl=0,pt=0;//padding left,padding top
		// TODO Auto-generated method stub
		float scale=1.0f;
		pw=b.getWidth()/(getResources().getDisplayMetrics().density)+0.5f;
		//Log.i("dd","pw"+pw);
		ph=b.getHeight()/(getResources().getDisplayMetrics().density)+0.5f;
		BitmapDrawable bd=new BitmapDrawable(b);
		
		myImageSwitcher.setImageDrawable(bd);
		cw=myImageSwitcher.getWidth();
		ch=myImageSwitcher.getHeight();
		
		
		//计算缩放比例
		if(pw>=ph){
			scale=cw/pw;
		}else{
			scale=ch/ph;
		}
		mMatrix.postScale(scale, scale);
		pl=cw/2-pw*scale/2;
		pt=ch/2-ph*scale/2;
		mMatrix.postTranslate(pl, pt);
		imageView.setImageMatrix(mMatrix);
	    ((ImageView)myImageSwitcher.getNextView()).setImageMatrix(mMatrix);
	    
	    // imageView.setPadding(pl, pt, 0, 0);
	    Log.i("arg","arg+"+cw+pw+ch+ph);
	}

	
	/*
	//上下文菜单，本例会通过长按条目激活上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, 
            ContextMenuInfo menuInfo) {
    	if(picNum!=-1){
        menu.setHeaderTitle("选项");
        //添加菜单项
        menu.add(0, Menu.FIRST, 0, "保存当前违章图片");
    	}
    }
    */
    
    //菜单单击响应
    @Override
    public boolean onContextItemSelected(MenuItem item){
        //获取当前被选择的菜单项的信息
        switch(item.getItemId()){
        case Menu.FIRST:
            //在这里添加处理代码
            if(savePic(picNum)){
            	Toast.makeText(getApplicationContext(), "图片保存成功！",
            		     Toast.LENGTH_SHORT).show();
            }else{
            	Toast.makeText(getApplicationContext(), "图片保存失败！",
            		     Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return true;
    }
    
    private boolean savePic(int picId) {
    	Bitmap pic=((MyApplication)getApplication()).getTempPics()[picId];
    	if (isHasSdcard()) {
    		// 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir =Environment.getExternalStorageDirectory();
            //得到一个路径，内容是sdcard的文件夹路径和名字
            String newPath=sdcardDir.getPath()+"/myTrafficImages/";//newPath在程序中要声明
            File picPath = new File(newPath);
            if (!picPath.exists()) {
	            //若不存在，创建目录，可以在应用启动的时候创建
	            picPath.mkdirs();
            }
	        File f = new File(newPath + checkBreak.getBreakPicId()+ "_" + picNum + ".png");
			try {
				if(f.exists()){
					f.delete();
				}
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(f);        
			} catch (FileNotFoundException e) {        
				e.printStackTrace();
				return false;
			}
			pic.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			try {
				fOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			try {
				fOut.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
    	
    	}else{
    		Toast.makeText(getApplicationContext(), "未找到SD卡！",
          		     Toast.LENGTH_SHORT).show();
       		return false;
    	}
    }
    

    private boolean isHasSdcard(){
    	String status = Environment.getExternalStorageState();
    	  if (status.equals(Environment.MEDIA_MOUNTED)) {
    	   return true;
    	  } else {
    	   return false;
    	  }
    }
    
   /*
    private void showAlertDialog2(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		//dialog.setTitle(R.string.hello);
		String [] dataArray = new String[]{"hello","baby","word"};
		dialog.setItems(dataArray, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//textView.setText("点击了按钮:which="+which+" 按钮：");
				
			}
		}).show();
		
		
	}
	*/
	
    
    

}
