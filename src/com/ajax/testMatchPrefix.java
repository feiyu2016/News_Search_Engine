package com.ajax;

import com.ajax.SuggestDic.TSTItem;

public class testMatchPrefix {
	public static void main(String[] args){
		SuggestDic dic = SuggestDic.getInstance();
		TSTItem[] t=dic.matchPrefix("q", 10);
		for(int i=0;i<t.length;i++)
		{
           System.out.println(t[i].toString());
		}
		
	}
}
