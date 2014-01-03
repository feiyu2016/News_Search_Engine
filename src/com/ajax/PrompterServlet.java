/*
 * 处理页面下来框的servlet
*/
package com.ajax;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajax.SuggestDic.TSTItem;
import com.bitmechanic.listlib.*;
import com.bitmechanic.searchpage.*;

import java.io.PrintWriter;
import java.io.IOException;
//继承HttpServlet
public class PrompterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static SuggestDic dic = SuggestDic.getInstance();
//覆盖doGet方法，覆盖doPost方法
	public void doGet(HttpServletRequest request,
		      		  HttpServletResponse response)throws IOException {
				doPost(request,response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response)throws IOException {

		String queryTerm = request.getParameter("query");
		System.out.print("++++++++++++"+queryTerm);
		//在这里获得词典的前缀匹配
		TSTItem[] t=dic.matchPrefix(queryTerm, 10);
		if(t!=null){
		System.out.print("!!!!!!!!!!!!"+t.length+"fffffff");
		StringBuilder message = new StringBuilder("<ul>");
		try {
			for(int i=0;i<t.length;i++)
			{
	            message.append("<li style=\"font-size:12px;padding:0px;height:15px;line-height:15px;overflow:hidden\"><div style=\"text-align:left;float:left\">");
	           //message.append(i);
	            message.append("</div><div style=\"text-align:right;float:right\"><span class=\"informal\">");
	           //message.append(i);
	           // message.append("<font color=\"#009900\">结果</font></span>ffffffffffff</div></li>");
	            message.append("<font color=\"#009900\"></font></span>");
	            message.append(t[i].key);
	            message.append("</div></li>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		message.append("</ul>");
		//在使用servlet的时候经常使用response
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out = response.getWriter();

		System.out.print(out.toString());
		//Servlet的结果必须要out出去
		System.out.print(out.toString());
		out.println(message.toString());
		}
    }
}