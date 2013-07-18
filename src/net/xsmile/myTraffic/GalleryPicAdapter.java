package net.xsmile.myTraffic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;



public class GalleryPicAdapter extends BaseAdapter
{
	
	int mGalleryItemBackground;
	private Context myContext;
	private Bitmap[] pics;
	 
	 
	public GalleryPicAdapter(Context C,Bitmap[] p) {
		this.myContext = C;
		this.pics=p;
	    TypedArray a = myContext.obtainStyledAttributes(R.styleable.Gallery);  
	    mGalleryItemBackground = a.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
	    a.recycle();
	}
	
	  
	@Override
	public int getCount() {
		return pics.length;
	}
	
	@Override
	public Object getItem(int arg0) {
		return arg0;
	}
	
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// 创建一个 ImageView 对象
		ImageView view = new ImageView(myContext);
		view.setDrawingCacheEnabled(true);
		view.setImageBitmap(pics[arg0]);	
		view.setScaleType(ImageView.ScaleType.FIT_XY);
		view.setBackgroundResource(mGalleryItemBackground);
		
		int itemHeight = myContext.getResources().getDimensionPixelSize(R.dimen.Item_Height);
		int itemWidth = myContext.getResources().getDimensionPixelSize(R.dimen.Item_Width);
		
		// 设置这个 ImageView 对象的宽高
		view.setLayoutParams(new Gallery.LayoutParams(itemWidth, itemHeight));    
		return view;
	}
	    
}