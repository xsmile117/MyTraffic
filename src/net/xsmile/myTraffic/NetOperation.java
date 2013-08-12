package net.xsmile.myTraffic;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class NetOperation extends MyHttpClientCheck {
	
	
	private static final String TAG = "NetOperation";
	private static final boolean NET_IS_READY=true;
	private static final boolean NET_NOT_READY=false;
	

	private static final String URL_REFERER="http://www.yzjxw.com/";
	private static final String URL_HOME="http://218.106.97.174:8080/cx/";
	private static final String URL_BREAKS="http://218.106.97.174:8080/cx/queryVehInfoGM.do";
	private static final String URL_BREAKTYPE_DETAIL=URL_BREAKS+"?method=selectVehInfoMingxi&xh=";
	private static final String URL_BREAKPICS=URL_BREAKS+"?method=selectPicNum&xh=";
	private static final String URL_BREAKPIC_DETAIL=URL_BREAKS+"?method=selectPic&xh=";//+picId+"&number=";

	private HandleHtml handleHtml;
	//private String info;
	private Context myContext;
	
	public NetOperation(){
		this.handleHtml=new HandleHtml();
	}
	
	
	
	//public String getInfo() {
	//	return info;
	//}


	public HandleHtml getHandleHtml(){
		return this.handleHtml;
	}
	
	/*
	 * 准备session
	 * 
	 */

    public boolean netReady(){
    	HttpGet get=null;
    	//myHttpClient=new DefaultHttpClient();
    	get=createHttpGet(URL_HOME);
        get.setHeader("referer", URL_REFERER);
       // result=EntityUtils.toString(excuteHttpRequest(get,null),"utf-8"); 
        if(excuteHttpRequestForContent(get,null,true)=="succes"){
        	//myHttpClient.getCookieStore().getCookies().get(0);
        	get=new HttpGet(URL_HOME+"/module/outside/viewVehInfo.jsp");
        	if(excuteHttpRequestForContent(get,null,true)=="succes")  
            	{ 
        			return NET_IS_READY;
            	}
        } 
        return NET_NOT_READY;
    }
    
    
	/*获取违章清单
	 *返回值0表示无违章
	 *      1表示有违章
	 *      2表示网页解析失败
	 *      3表示服务器异常
	 *      4表示网络异常
	 *      5表示未知错误
	 * 
	 */
        
    public int getBreaks(Vehicle v){
    	Vehicle checkVehicle=v;
    	HttpPost post = null;
    	String result=null;
    	//HttpEntity he=null;
    	
        /*
         * NameValuePair实现请求参数的封装
        */
        List <NameValuePair> checkParams = new ArrayList <NameValuePair>(); 
        checkParams.add(new BasicNameValuePair("method", "selectVehInfo")); 
        checkParams.add(new BasicNameValuePair("hpzl", checkVehicle.getVehicleType())); 
        checkParams.add(new BasicNameValuePair("cphm", "苏K"+checkVehicle.getVehicleNum())); 
        checkParams.add(new BasicNameValuePair("clsbdh", checkVehicle.getVehicleId())); 
        checkParams.add(new BasicNameValuePair("startTime", checkVehicle.getStartTime())); 
        checkParams.add(new BasicNameValuePair("endTime", checkVehicle.getEndTime())); 
        
        //Log.i("time",checkParams.toString());
        
        post=createHttpPost(URL_BREAKS,checkParams);
        result=excuteHttpRequestForContent(post,null,false);
        

    	if(result!=null)  
    	{ 
				switch(handleHtml.getBreaksFromHtml(result)){
	    		case 0:
	    			//MyApplication.getInstance().setInfomation(handleHtml.getInfo());
	    			MyApplication.getInstance().setCheckCorrect(true);
	    			return 0;
	    			//break;
	    		case 1:
	    			Log.i("size","---"+handleHtml.getBreaks().size());
	    			MyApplication.getInstance().setCheckCorrect(true);
	    			return handleHtml.getBreaks().size();
	    		case 2:
	    			MyApplication.getInstance().setCheckCorrect(false);
	    			return -2;
	    		case -1:
	    			//MyApplication.getInstance().setInfomation(handleHtml.getInfo());
	    			MyApplication.getInstance().setCheckCorrect(false);
	    			return 0;
	    			
	    		}
			
    		
    	} 
    	else 
    	{ 
    		Log.i(TAG, "Status"+result);
    		return -3;
    		
    	} 
        return -5;
    }
    
    public int getBreaksDetail(int id){
    	String url=URL_BREAKTYPE_DETAIL+handleHtml.getBreaks().get(id).getBreakTypeId();
    	//myHttpClient=new DefaultHttpClient();
    	HttpGet get=createHttpGet(url);
		//((AbstractHttpClient) myHttpClient).getCookieStore().addCookie(myCookie);
    	String result=null;
    	result=excuteHttpRequestForContent(get,null,false);
    	
    	if(result!=null){
    		handleHtml.getDetailFromHtml(result,id); 
		    return 1;
			
    	}
    	return -4;
		
    }
    
    
    public int getBreakPicNums(String picId){
    	String url=URL_BREAKPICS+picId;
    	//myHttpClient=new DefaultHttpClient();
    	HttpGet get=createHttpGet(url);
    	String result=null;
    	//HttpEntity he=null;
    	result=excuteHttpRequestForContent(get,null,false);
    	if(result!=null){
    		int i=handleHtml.getPicNumsFromHtml(result);
	            switch(i){
	            case 0:
	           	 	return 0;
	            case 2:
	           	 	return -2;
	            default:
	            	return i;
	            }
			
    	}
    	return -4;
		
    }
    
    public Bitmap[] getBreakPicture(String picId,int picNums,Context c){
    	myContext=c;
    	//Bitmap picError=BitmapFactory.decodeResource(myContext
				//.getResources(), R.drawable.picerror);
    	Bitmap netPicError=BitmapFactory.decodeResource(myContext
				.getResources(), R.drawable.netpicerror);
    	Bitmap[] pics=new Bitmap[picNums];
    	String url=URL_BREAKPIC_DETAIL+picId+"&number=";
    	
    	Bitmap bitmap=null;
		//myHttpClient=new DefaultHttpClient();
    	//((AbstractHttpClient) myHttpClient).getCookieStore().addCookie(myCookie);
    	for(int i=1;i<picNums+1;i++){
			HttpGet get=createHttpGet(url+i);
			bitmap=excuteHttpRequestForPic(get,null);
			if(bitmap!=null){
				pics[i-1]=bitmap;
			}else{
				pics[i-1]=netPicError;
			}
			
		}
		
		return pics;
    }
    
    


}
