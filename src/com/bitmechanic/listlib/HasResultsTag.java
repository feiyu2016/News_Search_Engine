package com.bitmechanic.listlib;

import javax.servlet.jsp.JspException;

public class HasResultsTag extends ListBaseTag {

    public int doStartTag() throws JspException {
        ListContainer lc = getList();

        if (lc==null)
            return SKIP_BODY;
        else if (lc.getSize() < 1)
            return SKIP_BODY;
        else
            return EVAL_BODY_INCLUDE;
    }
    
}
