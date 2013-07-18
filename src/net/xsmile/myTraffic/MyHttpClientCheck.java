package net.xsmile.myTraffic;

import java.io.IOException;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

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
		post.setHeader("Connection", "close");
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
		get.setHeader("Connection", "close");
		return get;
	}

	@Override
	public HttpResponse excuteHttpRequest(HttpRequestBase httpRequest,Cookie myCookie) {
		
		if(myCookie!=null){
			myHttpClient.getCookieStore().addCookie(myCookie); 
		}
        try {
        	myHttpClient.getConnectionManager().closeExpiredConnections();
			return myHttpClient.execute(httpRequest);
		} catch (ClientProtocolException e) {
			httpRequest.abort(); 
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			httpRequest.abort(); 
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		}
        return null;
		
	}


}
