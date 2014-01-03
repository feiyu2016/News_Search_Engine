package com;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;


/*
 * 解析器Parser工具类
 */
public class ParserUtil{
	/*
	 * 获取纯文本信息
	 */
	public static String getPlainText(String str) {
		try {
			Parser parser = new Parser();
			parser.setInputHTML(str);
			
			StringBean sb = new StringBean();
			// 设置不需要得到页面所包含的链接信息
			sb.setLinks(false);
			// 设置将不间断空格由正规空格所替代
			sb.setReplaceNonBreakingSpaces(true);
			// 设置将一序列空格由一个单一空格所代替
			sb.setCollapse(true);
			parser.visitAllNodesWith(sb);
			str = sb.getStrings();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return str;
	}
 
    /*
     * 获取NodeList中的HTML 
     */
    public String getNodeListHTML(NodeList nodeList){
    	String NodeHTML = "";
    	for (int i = 0; i < nodeList.size(); i++) {
        	Node node = (Node) nodeList.elementAt(i);
        	NodeHTML = node.toHtml();
//        	System.out.println("----------------");
//        	System.out.println(NodeHTML);
        }
    	return NodeHTML;
    }
    
    /*
     * 获取NodeList中的TEXT
     */   
    public String getNodeListText (NodeList nodeList){
    	String NodeText = "";
    	for (int i = 0; i < nodeList.size(); i++) {
        	Node node = (Node) nodeList.elementAt(i);
        	NodeText = node.toPlainTextString();
        }
    	return NodeText;
    }
    
}