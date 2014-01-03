package com.bitmechanic.listlib;

import javax.servlet.jsp.JspException;

public class NextLinkTag extends PrevLinkTag {

    public int doStartTag() throws JspException {
        ListContainer lc = getList();

        int offset = lc.getEnd();
            
        print(getURL(offset));

        return SKIP_BODY;
    }
    
}
