package com.bitmechanic.listlib;

import javax.servlet.jsp.JspException;

public class PropTag extends ListBaseTag {

    private String _prop;

    public void setProperty(String prop) {
        _prop = prop;
    }

    public int doStartTag() throws JspException {
        ListContainer lc = getList();
        if (_prop.equals("size")) print(String.valueOf(lc.getSize()));
        else if (_prop.equals("max")) print(String.valueOf(lc.getSize()));
        else if (_prop.equals("start")) print(String.valueOf(lc.getStart()));
        else if (_prop.equals("end")) print(String.valueOf(lc.getEnd()));
        else {
            throw new JspException("Undefined property: " + _prop);
        }

        return SKIP_BODY;
    }
    
    public void release() {
        super.release();
        _prop = null;
    }

}
