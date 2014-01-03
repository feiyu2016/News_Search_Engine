package com.bitmechanic.listlib;

import java.util.Iterator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

public class IterateTag extends ListBaseBodyTag {

    private Iterator _iter;
    private Object _obj;

    public Object getObj() {
        return _obj;
    }

    public int doStartTag() throws JspException {
        _iter = getList().getIterator();
        
        if (_iter.hasNext()) {
            _obj = _iter.next();
            return EVAL_BODY_BUFFERED;
        }
        else {
            return SKIP_BODY;
        }
    }

    public int doAfterBody() throws JspException {
        BodyContent body = getBodyContent();
        if (body != null) {
            try {
                body.writeOut(getPreviousOut());
            }
            catch(Exception e) {
                throw new JspException("Unable to write body content", e);
            }
            body.clearBody();
        }

        if (_iter.hasNext()) {
            _obj = _iter.next();
            return EVAL_BODY_AGAIN;
        }
        else {
            return SKIP_BODY;
        }
    }

    public void release () {
        super.release();
        _iter = null;
    }

}
