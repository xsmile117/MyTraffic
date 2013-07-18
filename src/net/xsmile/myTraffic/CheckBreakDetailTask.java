package net.xsmile.myTraffic;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class CheckBreakDetailTask extends AsyncTask<Object, Void, Integer> {
	
	private Context myContext;
	private Gallery myGallery;
	private LinearLayout myProgress;
	private LinearLayout picshow;
	private ImageSwitcher myImageSwitcher;
	private ImageView myImageView;
	private Bitmap[] pics;
	private String picId;
	
	
	//private ProgressBar myProgress;
	//private ListView myListView;
	public CheckBreakDetailTask(Context c,Gallery g,LinearLayout p,LinearLayout pic,ImageSwitcher ims,ImageView iv){
		this.myGallery=g;
		this.myContext=c;
		this.myProgress=p;
		this.picshow=pic;
		this.myImageSwitcher=ims;
		this.myImageView=iv;
	}
	

	@Override
	protected Integer doInBackground(Object... params) {
		// TODO Auto-generated method stub
		picId=(String)params[0];
		
		NetOperation operation=new NetOperation();
    	if(!isCancelled()&&(operation.netReady())){
    		int number=operation.getBreakPicNums(picId);
    		if((!isCancelled())&&(number>0)){
    			pics=operation.getBreakPicture(picId, number, myContext);
    			MyApplication.getInstance().setTempPics(pics);
    			return 1;
    		}else if((!isCancelled())&&(number==0)){
    			//info=operation.getInfo();
    			return 0;
    		}else{
    			return -1;
    		}
    	}else{
    		return -1;
    	}
		
	}

	


	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		myProgress.setVisibility(View.GONE);
		picshow.setVisibility(View.VISIBLE);
		switch(result){
        case 0:
        	//Log.i("code","0");
        	myImageSwitcher.setImageResource(R.drawable.nopic);
        	myImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        	break;
        case 1:
        	myGallery.setAdapter(new GalleryPicAdapter(myContext,pics));
        	break;
        case -1:
        	myImageSwitcher.setImageResource(R.drawable.netpicerror);
        	myImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        	break;
        }
		
		
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	

}
