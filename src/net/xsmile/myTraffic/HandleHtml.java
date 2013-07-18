package net.xsmile.myTraffic;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;


public class HandleHtml {
	private String info;
	private ArrayList<VehicleBreak> breaks;
	private int allMoney;
	private int allScore;
	private static final String ERROR_HTML="网页解析失败！";
	private static final String ERROR_INFO="解析异常！";
	
	public HandleHtml(){
		
		this.info="";
		this.allMoney=0;
		this.allScore=0;
	}
	
	public ArrayList<VehicleBreak> getBreaks() {
		return breaks;
	}

	public String getInfo() {
		return info;
	}
	
	public int getAllMoney() {
		return allMoney;
	}

	public int getAllScore() {
		return allScore;
	}

	/*获取违章清单
	 *返回值0表示无违章
	 *      1表示有违章
	 *      2表示网页解析失败
	 *      -1表示网页关键字“交”获取失败
	 * 
	 */
	public int getBreaksFromHtml(String html){
		//ArrayList<VehicleBreak> breaks=new ArrayList<VehicleBreak>();
		VehicleBreak tempVehicleBreak=new VehicleBreak();
		breaks=new ArrayList<VehicleBreak>();
		
		int i=0;//标志位
    	int j=0;//标志位
    	String[] temp;
    	Document doc = Jsoup.parse(html);
    	try{
    		
    		Element content = doc.getElementById("mainT");
    		//System.out.println(doc.toString());
    		if(content!=null){
    			
    			Elements links = content.getElementsByTag("td"); 
    			for (Element link : links) {
    				switch(i){
    				case 0:
    					tempVehicleBreak.setMonitorType(link.text());
    					i++;
    					break;
    				case 1:
    					tempVehicleBreak.setBreakTime(link.text());
    					i++;
    					break;
    				case 2:
    					tempVehicleBreak.setBreakPlace(link.text());
    					i++;
    					break;
    				case 3:
    					tempVehicleBreak.setBreakType(link.text());
    					i++;
    					break;
    				case 4:
    					i=0;
    					breaks.add(tempVehicleBreak);
    					tempVehicleBreak=new VehicleBreak();
    					break;
    				default:
    					break;    			
    				}
    			}
    			Elements morelinks = content.select("a[href]");
    			for (Element link : morelinks) { 
    				temp=link.attr("href").split("'");
    		//System.out.println(temp[1]);
    				switch(i){
    				case 0:
    					breaks.get(j).setBreakTypeId(temp[1]);
    					i++;
    					break;
    				case 1:
    					breaks.get(j).setBreakPicId(temp[1]);
    					i=0;
    					j++;
    					break;
    				default:
    					break;
    			
    				}
    	
    			}
    			
    			return 1;
    		}else{
    			info=doc.select("[height=100%]").first().text();
    			if(info!=null){
    				//判断提示语
    				String word=info.substring(9);//取提示语第10个字母
    				if(word.startsWith("交")){
    					return 0;//无违章
    				}else{
    					return -1;//信息有误
    				}
    				
    			}else{
    				info=ERROR_HTML;
    				return 2;
    			}
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		info=ERROR_INFO;
    		return 2;
    	}
	
	}
	
	/*获取违章详细内容
	 *返回值0表示网页解析失败
	 *      1表示网页解析成功
	 *      2表示异常
	 * 
	 */
	public int getDetailFromHtml(String html,int i){
		String tmoney;
    	String tscore;
    	String temp1;
    	String temp2;
    	//Log.i("html",html);
    	Document doc = Jsoup.parse(html);
    	try{
    		
    		Elements links = doc.select("u");
    		if(links.size()!=0){
	    		temp1=Jsoup.clean(links.get(0).text(), Whitelist.none()).replace("&nbsp;", "");
	    		temp2=Jsoup.clean(links.get(1).text(), Whitelist.none()).replace("&nbsp;", "");
	    		tmoney=Jsoup.clean(links.get(2).text(), Whitelist.none()).replace("&nbsp;", "");
	    		tscore=Jsoup.clean(links.get(3).text(), Whitelist.none()).replace("&nbsp;", "");
	    		breaks.get(i).setBreakTypeDetail(temp1+"\n根据"+
	    				temp2+"，给予罚款"+tmoney+"元、扣"+
	    				tscore+"分的处罚");
	    		breaks.get(i).setBreakMoney(tmoney);
	    		breaks.get(i).setBreakScore(tscore);
	    		allMoney+=Integer.parseInt(tmoney);
	    		allScore+=Integer.parseInt(tscore);
	    		return 1;
    		}else{
    			info=ERROR_HTML;
    			return 0;
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		info=ERROR_INFO;
    		return 2;
    	}
	}
	
	
	/*获取违章照片数量
	 *返回值0表示无照片
	 *      2表示网页解析失败
	 * 
	 */
	public int getPicNumsFromHtml(String html){
		Document doc = Jsoup.parse(html);
		try{
			Element link = doc.select("[color=red]").first();
			if(link!=null){
				int i=Integer.parseInt(link.text());
				return i;
			}else{
				//info="未查询到您的违章图片，实际情况以交管部门窗口数据为准!";
				return 0;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			info=ERROR_INFO;
			return -2;
		}
	}

	

}
