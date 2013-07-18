package net.xsmile.myTraffic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.util.Log;


public class NetOperation extends MyHttpClientCheck {
	
	
	private static final String TAG = "NetCheck";
	private static final boolean NET_IS_READY=true;
	private static final boolean NET_NOT_READY=false;
	

	private static final String URL_REFERER="http://www.yzjxw.com/";
	private static final String URL_HOME="http://218.106.97.174:8080/cx/";
	private static final String URL_BREAKS="http://218.106.97.174:8080/cx/queryVehInfoGM.do";
	private static final String URL_BREAKTYPE_DETAIL=URL_BREAKS+"?method=selectVehInfoMingxi&xh=";
	private static final String URL_BREAKPICS=URL_BREAKS+"?method=selectPicNum&xh=";
	private static final String URL_BREAKPIC_DETAIL=URL_BREAKS+"?method=selectPic&xh=";//+picId+"&number=";

	private Cookie myCookie;
	private HandleHtml handleHtml;
	private String info;
	private Context myContext;
	//private String jessionid;
	
	public NetOperation(){
		this.handleHtml=new HandleHtml();
	}
	
	
	
	public String getInfo() {
		return info;
	}


	public HandleHtml getHandleHtml(){
		return this.handleHtml;
	}
	
	/*
	 * 准备session
	 * 
	 */

    public boolean netReady(){
    	HttpGet get=null;
    	HttpResponse httpResponse=null;
    	//myHttpClient=new DefaultHttpClient();
    	get=createHttpGet(URL_HOME);
        get.setHeader("referer", URL_REFERER);
        try{
        httpResponse=excuteHttpRequest(get,null);
        if(httpResponse.getStatusLine().getStatusCode() == 200){
        	get.abort();
        	myCookie=myHttpClient.getCookieStore().getCookies().get(0);
        	get=new HttpGet(URL_HOME+"/module/outside/viewVehInfo.jsp");
        	httpResponse=excuteHttpRequest(get,null);
        	if(httpResponse.getStatusLine().getStatusCode() == 200)  
            { 
        		
        		return NET_IS_READY;
            }
        }
        }
        catch(Exception e){
        	Log.e(TAG, e.getMessage(), e);
        	e.printStackTrace();
        	return NET_NOT_READY;
        }
        finally{
        	get.abort();
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
        HttpResponse httpResponse=excuteHttpRequest(post,myCookie);
        
        try {
    	/*若状态码为200 ok*/
    	if(httpResponse.getStatusLine().getStatusCode() == 200)  
    	{ 
    		/*读返回数据*/
    		//httpRequest.re
			
				result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
				switch(handleHtml.getBreaksFromHtml(result)){
	    		case 0:
	    			this.info=handleHtml.getInfo();
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
	    			this.info=handleHtml.getInfo();
	    			MyApplication.getInstance().setCheckCorrect(false);
	    			return 0;
	    			
	    		}
			
    		
    	} 
    	else 
    	{ 
    		Log.i(TAG, "Status"+httpResponse.getStatusLine().getStatusCode());
    		return -3;
    		
    	} 
        } catch (ParseException e) {
        	
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
			return -5;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
			return -5;
		} finally{
			post.abort();
		}
        return -5;
    }
    
    public int getBreaksDetail(int id){
    	String url=URL_BREAKTYPE_DETAIL+handleHtml.getBreaks().get(id).getBreakTypeId();
    	//myHttpClient=new DefaultHttpClient();
    	HttpGet get=createHttpGet(url);
		//((AbstractHttpClient) myHttpClient).getCookieStore().addCookie(myCookie);
    	
    	try {
    	HttpResponse httpResponse=excuteHttpRequest(get,myCookie);
    	if(httpResponse.getStatusLine().getStatusCode() == 200){
    		String result;
			
				result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
				 handleHtml.getDetailFromHtml(result,id); 
				    return 1;
			
    	}else
    	{
    		Log.i(TAG, "Status"+httpResponse.getStatusLine().getStatusCode());
    		return -3;
    	}
    	} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
			return -4;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
			return -4;
		}finally{
			get.abort();
		}
		
    }
    
    
    public int getBreakPicNums(String picId){
    	String url=URL_BREAKPICS+picId;
    	//myHttpClient=new DefaultHttpClient();
    	HttpGet get=createHttpGet(url);
    	
    	try {
    	HttpResponse httpResponse=excuteHttpRequest(get,myCookie);
		//((AbstractHttpClient) myHttpClient).getCookieStore().addCookie(myCookie);
    	if(httpResponse.getStatusLine().getStatusCode() == 200){
    		String strResult;
			
				strResult = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
				int i=handleHtml.getPicNumsFromHtml(strResult);
	            switch(i){
	            case 0:
	           	 	return 0;
	            case 2:
	           	 	return -2;
	            default:
	            	return i;
	            }
			
    	}else{
    		Log.i(TAG, "Status"+httpResponse.getStatusLine().getStatusCode());
    		return -3;
        }
    	} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		} finally{
			get.abort();
		}
    	return -4;
		
    }
    
    public Bitmap[] getBreakPicture(String picId,int picNums,Context c){
    	myContext=c;
    	Bitmap picError=BitmapFactory.decodeResource(myContext
				.getResources(), R.drawable.picerror);
    	Bitmap netPicError=BitmapFactory.decodeResource(myContext
				.getResources(), R.drawable.netpicerror);
    	Bitmap[] pics=new Bitmap[picNums];
    	String url=URL_BREAKPIC_DETAIL+picId+"&number=";
		//myHttpClient=new DefaultHttpClient();
    	//((AbstractHttpClient) myHttpClient).getCookieStore().addCookie(myCookie);
    	
		for(int i=1;i<picNums+1;i++){
			HttpGet get=createHttpGet(url+i);
			try{
			HttpResponse httpResponse=excuteHttpRequest(get,null);
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = httpResponse.getEntity();
				
				    BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity); 
				    InputStream is = bufHttpEntity.getContent();
				    Bitmap bitmap = BitmapFactory.decodeStream(is);
				    pics[i-1]=bitmap;
				
				 
			}else{
				pics[i-1]=picError;
			}
			} catch (Exception e) {
				pics[i-1]=netPicError;
				Log.e(TAG, e.getMessage(), e);
				e.printStackTrace();
			} finally{
				get.abort();
			}
			
		}
		//myHttpClient.getConnectionManager().shutdown();
		return pics;
    }
    
    


}
