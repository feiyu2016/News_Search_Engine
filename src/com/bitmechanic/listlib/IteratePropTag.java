package com.bitmechanic.listlib;

import org.apache.commons.beanutils.BeanUtils;
import java.util.Map;
import javax.servlet.jsp.JspException;

public class IteratePropTag extends ListBaseTag {

    private String _prop;

    public void setProperty(String prop) {
    	//System.out.println("prop:"+prop);
        _prop = prop;
    }

    public int doStartTag() throws JspException {
        IterateTag tag = (IterateTag)findAncestorWithClass(this, IterateTag.class);
        if (tag == null) {
            throw new JspException("Unable to find parent IterateTag");
        }

        Object obj = tag.getObj();
        String val;
        if (obj instanceof Map) {
            val = String.valueOf(((Map)obj).get(_prop));
        }
        else {
            try {
                val = BeanUtils.getProperty(obj, _prop);
            }
            catch(Exception e) {
                throw new JspException("Unable to get property: " + _prop, e);
            }
        }

        print(val);

        return SKIP_BODY;
    }
    
    public void release() {
        super.release();
        _prop = null;
    }

}
