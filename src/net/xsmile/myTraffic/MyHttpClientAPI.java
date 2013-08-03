package net.xsmile.myTraffic;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;

import android.graphics.Bitmap;


public interface MyHttpClientAPI {
	
	abstract public HttpPost createHttpPost(String url,List<NameValuePair> params);
	
	abstract public HttpGet createHttpGet(String url);
	
	abstract public String excuteHttpRequestForContent(HttpRequestBase httpRequest,Cookie cookie,Boolean isCheck);
	
	abstract public Bitmap excuteHttpRequestForPic(HttpRequestBase httpRequest,Cookie cookie);

	

}
