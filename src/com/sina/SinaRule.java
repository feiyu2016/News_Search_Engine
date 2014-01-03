package com.sina;

import java.util.Calendar;

import com.RuleStrategy;
 
public class SinaRule implements RuleStrategy{

	/*
	 *  输入实例：http://sports.163.com/yy/mmdd/[\\d]+/[\\w]+.html
	 * @see com.RuleStrategy#urlFilterStrategy(java.lang.String)
	 */
	public String urlFilterStrategy(String urlFilter) {
		Calendar calendar = Calendar.getInstance();
		String year = Integer.toString(calendar.get(calendar.YEAR));
		String month = "0"+Integer.toString(calendar.get(calendar.MONTH)+1);
		month = month.substring(month.length()-2);
		String day = "0"+Integer.toString(calendar.get(calendar.DAY_OF_MONTH));
		day = day.substring(day.length()-2);
		
		urlFilter = urlFilter.replaceAll("yyyy", year );
		urlFilter = urlFilter.replaceAll("mm", month );
		urlFilter = urlFilter.replaceAll("dd", day );
		return urlFilter;
	}
	
	public static void main(String[] args){
		SinaRule sinaRule = new SinaRule();
		String temp = sinaRule.urlFilterStrategy("http://mil.news.sina.com.cn/yyyy-mm-dd/[\\d]+.html");
		System.out.println(temp);
	}
}