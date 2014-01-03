package com.bitmechanic.listlib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public abstract class ListBaseBodyTag extends BodyTagSupport {

    protected String _name;

    public void setName(String name) {
        _name = name;
    }

    protected ListContainer getList() throws JspException {
        return InitTag.getListTag(this, pageContext, _name).getList();
    }

    protected void print(String str) throws JspException {
        try {
            pageContext.getOut().print(str);
        }
        catch(java.io.IOException e) {
            throw new JspException(e.getMessage());
        }
    }

    public void release() {
        super.release();
        _name = null;
    }
}
