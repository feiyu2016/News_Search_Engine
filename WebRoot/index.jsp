<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" import="java.net.*;" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<%@ page session="false"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="pg"%><!-- 注意这两个taglib的使用-->
<%@ taglib uri="/WEB-INF/listlib.tld" prefix="list"%><!-- 注意这两个taglib的使用 -->
<%
	String query = request.getParameter("query");
	//URLDecoder.encode(request.getParameter("query"),"UTF-8");  
	if (query == null)
		query = "";
	out.println("Character Encoding: " + request.getCharacterEncoding()
			+ "<br>");
	out.println("responseCharacter Encoding: "
			+ response.getCharacterEncoding() + "<br>");
%>


<STYLE>
.SELECT {
	BORDER-RIGHT: #cccccc 1px solid;
	BORDER-TOP: #cccccc 1px solid;
	BORDER-LEFT: #cccccc 1px solid;
	BORDER-BOTTOM: #cccccc 1px solid
}

.form-button {
	BORDER-RIGHT: #6699cc 1px solid;
	BORDER-TOP: #6699cc 1px solid;
	BORDER-LEFT: #6699cc 1px solid;
	BORDER-BOTTOM: #6699cc 1px solid
}

.form-button-hover {
	BORDER-RIGHT: #6699cc 1px solid;
	BORDER-TOP: #ffffff 1px solid;
	BORDER-LEFT: #ffffff 1px solid;
	BORDER-BOTTOM: #6699cc 1px solid
}

.form-text {
	BORDER-RIGHT: #cccccc 1px solid;
	BORDER-TOP: #cccccc 1px solid;
	BORDER-LEFT: #cccccc 1px solid;
	BORDER-BOTTOM: #cccccc 1px solid;
	FONT-FAMILY: Arial
}

A.alpha {
	COLOR: #000000;
	TEXT-DECORATION: none
}

A.alpha:hover {
	COLOR: #000000;
	TEXT-DECORATION: underline
}

TR.alpha {
	BACKGROUND-COLOR: #6699cc
}

TD.alpha {
	BACKGROUND-COLOR: #6699cc
}

FONT.alpha {
	COLOR: #000000;
	FONT-FAMILY: Tahoma, Arial
}

.alpha-neg-alert {
	COLOR: #ff0000
}

.alpha-pos-alert {
	COLOR: #007f00
}

A.beta {
	COLOR: #000000;
	TEXT-DECORATION: none
}

A.beta:hover {
	COLOR: #000000;
	TEXT-DECORATION: underline
}

TR.beta {
	BACKGROUND-COLOR: #b6cbeb
}

TD.beta {
	BACKGROUND-COLOR: #b6cbeb
}

FONT.beta {
	COLOR: #000000;
	FONT-FAMILY: Tahoma, Arial
}

.beta-neg-alert {
	COLOR: #ff0000
}

.beta-pos-alert {
	COLOR: #007f00
}

A.gamma {
	COLOR: #000000;
	TEXT-DECORATION: none
}

A.gamma:hover {
	COLOR: #000000;
	TEXT-DECORATION: underline
}

TR.gamma {
	BACKGROUND-COLOR: #eeeeee
}

TD.gamma {
	BACKGROUND-COLOR: #eeeeee
}

FONT.gamma {
	COLOR: #000000;
	FONT-FAMILY: Tahoma, Arial
}

.gamma-neg-alert {
	COLOR: #ff0000
}

.gamma-pos-alert {
	COLOR: #007f00
}

A.bg {
	COLOR: #000000;
	TEXT-DECORATION: none
}

A.bg:hover {
	COLOR: #000000;
	TEXT-DECORATION: underline
}

TR.bg {
	BACKGROUND-COLOR: #ffffff
}

TD.bg {
	BACKGROUND-COLOR: #ffffff
}

FONT.bg {
	COLOR: #000000;
	FONT-FAMILY: Tahoma, Arial
}

.bg-neg-alert {
	COLOR: #ff0000
}

.bg-pos-alert {
	COLOR: #007f00
}

.resultInfo {
	color: #f80;
	background-color: transparent;
	text-transform: Uppercase;
	padding: 5px 5px 5px 0px;
	margin: 0;
	font-size: 1.5em;
}

.rnav {
	padding: 0;
	font-family: Verdana, Arial, Helvetica, Sans-serif;
	font-size: 1em;
	color: #333;
	background-color: #fff;
	font-weight: bold;
	font-size: 1.4em;
}

.rnavLabel {
	text-transform: Uppercase;
	color: #f80;
	background-color: transparent;
}

a.rnavLink {
	color: #415481;
	background-color: transparent;
}

a:visited .rnavLink {
	color: #8A9CBD;
	background-color: transparent;
}

a:hover .rnavLink {
	color: #f80;
	text-decoration: none;
	background-color: transparent;
}
</STYLE>

<html>


	<style>
div.auto_complete {
	width: 315px;
	background: #fff;
}

div.auto_complete ul {
	border: 1px solid #888;
	margin: 0;
	padding: 0;
	width: 100%;
	list-style-type: none;
}

div.auto_complete ul li {
	margin: 0;
	padding: 3px;
}

div.auto_complete ul li.selected {
	background-color: #ffb;
}

div.auto_complete ul strong.highlight {
	color: #800;
	margin: 0;
	padding: 0;
}

.k {
	BORDER-RIGHT: #666666 thin;
	BORDER-TOP: #666666 thin;
	BORDER-LEFT: #666666 thin;
	BORDER-BOTTOM: #666666 thin
}

.line2 {
	BORDER-TOP-WIDTH: 1px;
	BORDER-LEFT-WIDTH: 1px;
	BORDER-LEFT-COLOR: #999999;
	BORDER-TOP-COLOR: #999999;
	BORDER-BOTTOM: #999999 1px dotted;
	BORDER-RIGHT-WIDTH: 1px;
	BORDER-RIGHT-COLOR: #999999
}

.z12 {
	FONT-SIZE: 12px
}
</style>

	<head>
		<script language="JavaScript" type="text/javascript" src="./js/prompter/prototype.js"></script>
	    <script language="JavaScript" type="text/javascript" src="./js/prompter/scriptaculous.js"></script>
	    <script language="JavaScript" type="text/javascript" src="./js/prompter/controls.js"></script>
	    <script language="JavaScript" type="text/javascript" src="./js/prompter/effects.js"></script>
		<title>猎兔搜索</title>
	</head>
	<body>

		<form method="post" action="index.jsp">
			<TABLE border=0>
				<TR align="left">
					<TD>
						<A href="http://www.lietu.com/"></A>
					</TD>
					<TD>
						<TABLE>
							<TR>
								<TD>
									<input autocomplete="off" type="text" name="query" id="zip"
										style="width: 315px;" class="wd" value="<%=query%>" />
									<div class="auto_complete" id="zip_values"></div>
									<script type="text/javascript">
										new Ajax.Autocompleter('zip', 'zip_values','autoComplete', {afterUpdateElement : getSelectionId});
								        	function getSelectionId(text, li) {
												window.location = "./index.jsp?query="+encodeURIComponent(text.value);
											}
									</script>
								</TD>
								<TD>
									<INPUT type=submit value=猎兔搜索>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>

			<table width=100% border=0>
				<tr>
					<td rowspan=2>
						<table cellspacing=0 cellpadding=0 border=0 width=100%>
							<tr>
								<td bgcolor=#3366cc>
									<img width=1 height=1 alt="">
								</td>
							</tr>
						</table>
						<table cellspacing=0 cellpadding=0 border=0 width=100% height=24>
							<tr>
								<td nowrap bgcolor=#e5ecf9>
									&nbsp;
								</td>
								<td align=right nowrap bgcolor=#e5ecf9>
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
		<%
			String ruls = null;
		%>

		<%
			if (!query.equals("")) {
		%>

		<jsp:useBean id="search" class="com.bitmechanic.searchpage.Search">
			<!-- Specify the directory that stores our Lucene index created by the
	       spindle spider -->

			<!-- 指定存储Lucene索引的路径 -->
			<%
				search.init("F:/acube/Chapter7WebPart/index");
			%>
		</jsp:useBean>

		<!--把查询从http的get参数设置到搜索对象中去-->
		<jsp:setProperty name="search" property="query" value="<%=query%>" />
		
		<list:init name="customers" listCreator="search" max="10">
		<!-- 判断是否有结果 -->
			<list:hasResults>
				<!-- 遍历结果集 -->
				<list:iterate>
					<TABLE width="65%">
						<TR>
							<TD>
								<!-- 从Iterator的当前对象返回属性 -->
								<B><FONT style="FONT-SIZE: 14px"><list:iterateProp
											property="title" /> </FONT> </B>
							</TD>
						</TR>
						<TR>
							<TD>
								<FONT style="FONT-SIZE: 12px"><list:iterateProp
										property="body" /> </FONT>
							</TD>
						</TR>
					</TABLE>
					<br>
				</list:iterate>

				<pg:pager url="/Chapter7WebPart/index.jsp"
					items="<%=Integer.parseInt((String)
							pageContext.getAttribute("listSize"))%>"
					maxPageItems="10" maxIndexPages="10"
					export="pageOffset,currentPageNumber=pageNumber" scope="request">

					<pg:param name="query" value="<%=query%>" />

					<pg:index export="totalItems=itemCount">

						<pg:page export="firstItem, lastItem">
							<div class="resultInfo">
								<font style="FONT-SIZE: 13px"> 当前显示结果 <strong><%=firstItem%>-<%=lastItem%></strong>
									找到相关文档 <strong><%=Integer.parseInt((String) 
									pageContext.getAttribute("listSize"))%>
									</strong>篇</font>
							</div>
						</pg:page>

						<br />

						<div class="rnav">
							<table border=0 cellpadding=0 width=1% cellspacing=0 align=center>
								<tr align=center valign=top>
									<td valign=bottom nowrap>
										<font style="FONT-SIZE: 14px">结果页码：</font>&nbsp;

										<pg:first unless="current">
											<td nowrap>
												<a href="<%=pageUrl%>" class="rnavLink"><font
													style="FONT-SIZE: 14px"><nobr>
															首页
														</nobr> </font> </a>&nbsp;
										</pg:first>

										<pg:prev export="pageUrl">
											<td nowrap>

												<a href="<%=pageUrl%>" class="rnavLink"><font
													style="FONT-SIZE: 14px">&#171;&nbsp;上一页</font> </a>&nbsp;&nbsp;
											
										</pg:prev>

										<pg:pages>

											<%
												if (pageNumber == currentPageNumber) {
											%><font color="red" style="FONT-SIZE: 14px"><b><%=pageNumber%></b>
											</font>&nbsp;<%
												} else {
											%><td nowrap>
												&nbsp;
												<a href="<%=pageUrl%>"><font style="FONT-SIZE: 14px"><%=pageNumber%></font>
												</a>&nbsp;<%
													}
												%>
											
										</pg:pages>

										<pg:next export="pageUrl">
											<td nowrap>
												&nbsp;

												<a href="<%=pageUrl%>" class="rnavLink"><font
													style="FONT-SIZE: 14px">下一页&nbsp;&#187;</font> </a>
										</pg:next>

										<pg:last unless="current">
											<td nowrap>
												&nbsp;
												<a href="<%=pageUrl%>" class="rnavLink"><font
													style="FONT-SIZE: 14px"><nobr>
															末页
														</nobr> </font> </a>
										</pg:last>
								</tr>
							</table>
						</div>

					</pg:index>

				</pg:pager>


			</list:hasResults>



		</list:init>

		<%
			} else {
		%>
		<br>
		<br>
		<br>
		<br>
		<br>
		<br>
		<br>
		<br>
		<%
			}
		%>
		<br>
		<FONT style="FONT-SIZE: 14px"><CENTER>
				&copy;2007 Lietu
			</CENTER> </FONT>
	</body>
</html>
