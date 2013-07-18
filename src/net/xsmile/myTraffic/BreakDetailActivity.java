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
	private float cw,ch,pw,ph;   //��������ͼƬ����
	
	
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
        // ����ImageSwitcher�����ʾͼ��Ķ���Ч��
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
        	menu.add(0, 0, 0, "���浱ǰΥ��ͼƬ");
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
                 	Toast.makeText(getApplicationContext(), "ͼƬ����ɹ���",
                 		     Toast.LENGTH_SHORT).show();
                 }else{
                 	Toast.makeText(getApplicationContext(), "ͼƬ����ʧ�ܣ�",
                 		     Toast.LENGTH_SHORT).show();
                 }
                 break;
        }
        return true;
     }
    
    
    
    
    //ImageView switcher����Ҫʵ�ֵ�
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
		
		
		//�������ű���
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
	//�����Ĳ˵���������ͨ��������Ŀ���������Ĳ˵�
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, 
            ContextMenuInfo menuInfo) {
    	if(picNum!=-1){
        menu.setHeaderTitle("ѡ��");
        //��Ӳ˵���
        menu.add(0, Menu.FIRST, 0, "���浱ǰΥ��ͼƬ");
    	}
    }
    */
    
    //�˵�������Ӧ
    @Override
    public boolean onContextItemSelected(MenuItem item){
        //��ȡ��ǰ��ѡ��Ĳ˵������Ϣ
        switch(item.getItemId()){
        case Menu.FIRST:
            //��������Ӵ������
            if(savePic(picNum)){
            	Toast.makeText(getApplicationContext(), "ͼƬ����ɹ���",
            		     Toast.LENGTH_SHORT).show();
            }else{
            	Toast.makeText(getApplicationContext(), "ͼƬ����ʧ�ܣ�",
            		     Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return true;
    }
    
    private boolean savePic(int picId) {
    	Bitmap pic=((MyApplication)getApplication()).getTempPics()[picId];
    	if (isHasSdcard()) {
    		// ����һ���ļ��ж��󣬸�ֵΪ�ⲿ�洢����Ŀ¼
            File sdcardDir =Environment.getExternalStorageDirectory();
            //�õ�һ��·����������sdcard���ļ���·��������
            String newPath=sdcardDir.getPath()+"/myTrafficImages/";//newPath�ڳ�����Ҫ����
            File picPath = new File(newPath);
            if (!picPath.exists()) {
	            //�������ڣ�����Ŀ¼��������Ӧ��������ʱ�򴴽�
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
    		Toast.makeText(getApplicationContext(), "δ�ҵ�SD����",
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
				//textView.setText("����˰�ť:which="+which+" ��ť��");
				
			}
		}).show();
		
		
	}
	*/
	
    
    

}
