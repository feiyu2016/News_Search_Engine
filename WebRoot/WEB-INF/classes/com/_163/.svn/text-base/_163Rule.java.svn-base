package com._163;

import java.util.Calendar;
import java.util.Date;

import com.RuleStrategy;

public class _163Rule implements RuleStrategy{
	/*
	 *  输入实例：http://sports.163.com/yy/mmdd/[\\d]+/[\\w]+.html
	 * @see com.RuleStrategy#urlFilterStrategy(java.lang.String)
	 */
	public String urlFilterStrategy(String urlFilter) {
		Calendar calendar = Calendar.getInstance();
		String year = Integer.toString(calendar.get(calendar.YEAR)).substring(2);
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
		_163Rule _163Rule = new _163Rule();
		String temp = _163Rule.urlFilterStrategy("http://sports.163.com/yyyy/mmdd/[\\d]+/[\\w]+.html");
		System.out.println(temp);
	}
}