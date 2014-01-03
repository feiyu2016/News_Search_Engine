package com.bitmechanic.listlib;

import javax.servlet.jsp.JspException;

public class HasNextTag extends ListBaseTag {

    public int doStartTag() throws JspException {
        ListContainer lc = getList();
        if (lc.hasNext())
            return EVAL_BODY_INCLUDE;
        else
            return SKIP_BODY;
    }
}
