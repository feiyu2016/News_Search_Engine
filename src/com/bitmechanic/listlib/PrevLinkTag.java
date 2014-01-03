package com.bitmechanic.listlib;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

//import org.htmlparser.util.ParserException;

public class PrevLinkTag extends ListBaseTag {

    protected String getURL(int offset) {
        HttpServletRequest req = (HttpServletRequest)pageContext.getRequest();
        StringBuffer url = new StringBuffer(req.getRequestURI());
        boolean first = true;
        for (Enumeration e = req.getParameterNames(); e.hasMoreElements();) {
            String name = (String)e.nextElement();
            if (!name.equals(InitTag.OFFSET_KEY)) {
                if (first) {
                    url.append("?");
                    first = false;
                }
                else url.append("&");
                
                url.append(name).append("=");
                try{
                	String parameter=new String(req.getParameter(name).getBytes("UTF-8"),"GBK"); //req.getParameter(name);
                   	//url.append(java.net.URLEncoder.encode(parameter,"UTF-8"));
                	url.append(parameter);
                }
                catch (Exception ue)
				{}
            }
        }

        if (first)
            url.append("?");
        else
            url.append("&");

        url.append(InitTag.OFFSET_KEY).append("=").append(offset);

        return url.toString();
    }

    public int doStartTag() throws JspException {
        ListContainer lc = getList();

        int offset = Math.max(0, lc.getStart() - lc.getMax() - 1);
        print(getURL(offset));

        return SKIP_BODY;
    }
    
}
