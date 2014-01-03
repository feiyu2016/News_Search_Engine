package com.bitmechanic.listlib;

import javax.servlet.jsp.PageContext;

public interface ListCreator {

    public ListContainer execute(PageContext context,
    		int category , 
			int offset, 
			int max,
			boolean dateSort)
        throws Exception;
}
