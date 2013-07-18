package net.xsmile.myTraffic;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;


public interface MyHttpClientAPI {
	
	abstract public HttpPost createHttpPost(String url,List<NameValuePair> params);
	
	abstract public HttpGet createHttpGet(String url);
	
	abstract public HttpResponse excuteHttpRequest(HttpRequestBase httpRequest,Cookie cookie);

	

}
