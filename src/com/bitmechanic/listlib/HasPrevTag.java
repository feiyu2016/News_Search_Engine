package com.bitmechanic.listlib;

import javax.servlet.jsp.JspException;

public class HasPrevTag extends ListBaseTag {

    public int doStartTag() throws JspException {
        ListContainer lc = getList();
        if (lc.hasPrev())
            return EVAL_BODY_INCLUDE;
        else
            return SKIP_BODY;
    }
    
}
