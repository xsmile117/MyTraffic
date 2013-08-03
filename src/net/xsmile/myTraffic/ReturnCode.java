/**
 * 
 */
package net.xsmile.myTraffic;

/**
 * @author xsmile
 *
 */
public class ReturnCode {
	
	//NetOperation and HandHtml
	public static final int CHECK_NO_RECORD=0;      //查询无违章
	public static final int CHECK_HAS_RECORD=1;     //存在违章信息
	public static final int CHECK_HTML_ERROR=2;     //网页解析失败
	public static final int CHECK_SERVER_ERROR=3;   //服务器错误
	public static final int CHECK_NET_ERROR=4;      //网络错误
	public static final int CHECK_UNKNOWN_ERROR=5;  //未知错误
	public static final int CHECK_KEY_ERROR=-1;     //网页关键字“交”获取失败
	
	
	
	
	
	

}
