package net.xsmile.myTraffic;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class MyHttpClientCheck implements MyHttpClientAPI {
	
	private static final String TAG = "NetCheck";
	
	
	protected DefaultHttpClient myHttpClient;
	
	public MyHttpClientCheck(){
		myHttpClient=MyHttpClient.getHttpClient();
	}

	@Override
	public HttpPost createHttpPost(String url,
			List<NameValuePair> params) {
		HttpPost post=new HttpPost(url);
		//post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, new Integer(CONNECTION_TIMEOUT));
		//post.setHeader("Connection", "close");
		if(params!=null&&!params.equals("")){
			try {
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, e.getMessage(), e);
				e.printStackTrace();
			}
		}
		return post;
	}

	@Override
	public HttpGet createHttpGet(String url) {
		HttpGet get=new HttpGet(url);
		//get.setHeader("Connection", "close");
		return get;
	}

	@Override
	public String excuteHttpRequestForContent(HttpRequestBase httpRequest,Cookie myCookie,Boolean isCheck) {
		HttpResponse hp=null;
		String content=null;
		if(myCookie!=null){
			myHttpClient.getCookieStore().addCookie(myCookie); 
		}
        try {
        	myHttpClient.getConnectionManager().closeExpiredConnections();
			hp=myHttpClient.execute(httpRequest);
			if(hp.getStatusLine().getStatusCode() == 200)  
	    	{ 
	    		if(!isCheck){
	    			/*¶Á·µ»ØÊý¾Ý*/
		    		content = EntityUtils.toString(hp.getEntity(),"utf-8");;
					return content;
	    		}else{
	    			return "succes";
	    		}
				
	    	}
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		}
        finally{
        	httpRequest.abort();
        }
        return content;
		
	}
	
	
	@Override
	public Bitmap excuteHttpRequestForPic(HttpRequestBase httpRequest,Cookie myCookie){
		HttpResponse hp=null;
		Bitmap bm=null;
		if(myCookie!=null){
			myHttpClient.getCookieStore().addCookie(myCookie); 
		}
        try {
        	myHttpClient.getConnectionManager().closeExpiredConnections();
			hp=myHttpClient.execute(httpRequest);
			if(hp.getStatusLine().getStatusCode() == 200)  
	    	{ 
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(hp.getEntity()); 
				InputStream is = bufHttpEntity.getContent();
				bm = BitmapFactory.decodeStream(is);
				return bm;	
					
			}

		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		}
        finally{
        	httpRequest.abort();
        }
        return bm;
		
	}


}
