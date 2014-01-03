package com.bitmechanic.listlib;

import javax.servlet.jsp.JspException;

public class HasNoResultsTag extends ListBaseTag {

    public int doStartTag() throws JspException {
        ListContainer lc = getList();
        if (lc.getSize() > 0)
            return SKIP_BODY;
        else
            return EVAL_BODY_INCLUDE;
    }
    
}
