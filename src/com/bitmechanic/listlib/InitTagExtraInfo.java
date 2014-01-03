/*
 * Created on 2004-8-3
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.bitmechanic.listlib;

import javax.servlet.jsp.tagext.*;
/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InitTagExtraInfo extends TagExtraInfo{

	public VariableInfo[] getVariableInfo(TagData data) {
		return new VariableInfo[] {
			// The use of NESTED scope means that these scripting variables
			// will only be available inside the VarHelloTag.
			new VariableInfo("listSize", "java.lang.String", true, VariableInfo.NESTED)
		};
	}
}
