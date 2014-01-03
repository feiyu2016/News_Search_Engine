package com.bitmechanic.listlib;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.ServletContext;

public class InitTag extends TagSupport {

    public static final String TAG_KEY = "listlib_tag_";
    public static final String OFFSET_KEY = "pager.offset";
    public static final String CAT_KEY = "cat";
    public static final String SORT_KEY = "s";

    private String _name, _class;
    private int _max;
    private ListContainer _lc;
    private ListCreator _creator;

    public static String getTagKey(String name) {
        return TAG_KEY + name;
    }

    public static InitTag getListTag(TagSupport child, PageContext ctx, 
        String name) throws JspException {

        InitTag tag;
        if (name == null) {
            tag = (InitTag)findAncestorWithClass(child, InitTag.class);
            if (tag == null) {
                throw new JspException("No InitTag found on this page");
            }
        }
        else {
            tag = (InitTag)ctx.getAttribute(getTagKey(name));
            if (tag == null) {
                throw new JspException("No InitTag found with name: " + name);
            }
        }

        return tag;
    }

    public void setName(String name) 
    {
    	System.out.print(name);
        _name = name;
    }

    public void setClass(String clazz) {
    	System.out.print(clazz);
        _class = clazz;
    }

    public void setListCreator(String creator) {
        //_creator = (ListCreator)pageContext.getAttribute(creator);
        
        ServletContext sc =
        	pageContext.getServletConfig().getServletContext();
        _creator = (ListCreator)sc.getAttribute(creator);

    }

    public void setMax(int max) {
        _max = max;
    }

    // throws JspException
    public ListContainer getList() {
        return _lc;
    }
    
    // throws JspException
    public int doStartTag() {
        if (_creator == null) {
            try {
            	_class="com.bitmechanic.searchpage.Search";
            	System.out.println("class is :"+_class);
            	System.out.println("max is :"+_max);
            	System.out.println("name is :"+_name);
                Class c = Class.forName(_class);
                _creator = (ListCreator)c.newInstance();
            }
            catch(Exception e) {
                //throw new JspException("Unable to instantiate: " + _class, e);
            	_creator = null;
            	System.out.print("eeeee");
            	e.printStackTrace();
            	return EVAL_BODY_INCLUDE;
            }
        }

        int offset = 0;
        String offsetStr = pageContext.getRequest().getParameter(OFFSET_KEY);
        if (offsetStr != null) {
            try {
                offset = Integer.parseInt(offsetStr);
            }
            catch(Exception e) {
            	_creator = null;
            	e.printStackTrace();
            	return EVAL_BODY_INCLUDE;
                //throw new JspException("Unable to get OFFSET_KEY: " + _class, e);
                // squelch.. use starting offset by default.
            }
        }
        
        int category = -1;
        
        offsetStr = pageContext.getRequest().getParameter(CAT_KEY);
        if (offsetStr != null && ! "".equals(offsetStr)) {
            try {
            	category = Integer.parseInt(offsetStr);
            }
            catch(Exception e) {
            	_creator = null;
            	e.printStackTrace();
            	return EVAL_BODY_INCLUDE;
                //throw new JspException("Unable to get INDEX_KEY: " + _class, e);
                // use index kind by default.
            }
        }

        boolean dateSort = false;
        
        offsetStr = pageContext.getRequest().getParameter(SORT_KEY);
        if (offsetStr != null && ! "".equals(offsetStr)) {
            try {
            	dateSort = (Integer.parseInt(offsetStr) == 1);
            }
            catch(Exception e) {
            	_creator = null;
            	e.printStackTrace();
            	return EVAL_BODY_INCLUDE;
                //throw new JspException("Unable to get INDEX_KEY: " + _class, e);
                // use index kind by default.
            }
        }
        
        try {
        	
            _lc = _creator.execute(pageContext,category, offset, _max,dateSort);
//            System.out.println("鏈�ぇ鏁伴噺鏄� 锛�+_lc.getSize());
        }
        catch(org.apache.lucene.queryParser.ParseException e)
		{
        	_creator = null;
        	e.printStackTrace();
        	return EVAL_BODY_INCLUDE;
            //throw new JspException("query syntax error:" + e.getMessage());
		}
        catch(Exception e) {
            //_creator = null;
        	_creator = null;
        	e.printStackTrace();
        	return EVAL_BODY_INCLUDE;
            //throw new JspException("Unable to load list: " + e.getMessage(),e);
        }

        _lc.setMax(_max);
        _lc.setOffset(offset);
        

        if (_name != null) {
            pageContext.setAttribute(getTagKey(_name), this);
            pageContext.setAttribute(_name, getList());
            System.out.print(_lc.getSize());
            pageContext.setAttribute("listSize", Integer.toString(_lc.getSize()));
        }
        
        pageContext.setAttribute("listSize", Integer.toString(_lc.getSize()));

        _creator = null;

        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
    public void release() {
        super.release();

        _name = _class = null;
        _lc = null;
        _max = 20;
        _creator = null;
    }
    
}
