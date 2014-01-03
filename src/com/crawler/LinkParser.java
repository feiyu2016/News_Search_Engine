package com.crawler;

import com.sohu.SohuNews;

import java.util.HashSet;
import java.util.Set;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 *  杩欎釜绫绘槸鐢ㄦ潵鎼滈泦鏂伴椈閾炬帴鍦板潃鐨勩�灏嗙鍚堟鍒欒〃杈惧紡鐨刄RL娣诲姞鍒癠RL鏁扮粍涓�
 * @author Administrator
 */
public class LinkParser {
    // 鑾峰彇涓�釜缃戠珯涓婄殑閾炬帴,filter 鐢ㄦ潵杩囨护閾炬帴

	public static void main(String[] args) {
        String url = "http://news.sohu.com/";
        LinkParser parser = new LinkParser();
        parser.doParser(url);

    }
	
    public static Set<String> extracLinks(String url, LinkFilter filter) {

        Set<String> links = new HashSet<String>();
        try {
            Parser parser = new Parser(url);
            parser.setEncoding("gb2312");
            // 杩囨护 <frame >鏍囩鐨�filter锛岀敤鏉ユ彁鍙�frame 鏍囩閲岀殑 src 灞炴�鎵�〃绀虹殑閾炬帴
            NodeFilter frameFilter = new NodeFilter() {

                public boolean accept(Node node) {
                    if (node.getText().startsWith("frame src=")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            // OrFilter 鏉ヨ缃繃婊�<a> 鏍囩锛屽拰 <frame> 鏍囩
            OrFilter linkFilter = new OrFilter(new NodeClassFilter(
                    LinkTag.class), frameFilter);
            // 寰楀埌鎵�湁缁忚繃杩囨护鐨勬爣绛�            
            NodeList list = parser.extractAllNodesThatMatch(linkFilter);
            for (int i = 0; i < list.size(); i++) {
                Node tag = list.elementAt(i);
                if (tag instanceof LinkTag)// <a> 鏍囩
                {
                    LinkTag link = (LinkTag) tag;
                    String linkUrl = link.getLink();// url
                    if (filter.accept(linkUrl)) {
                        links.add(linkUrl);
                    }
                } else// <frame> 鏍囩
                {
                    // 鎻愬彇 frame 閲�src 灞炴�鐨勯摼鎺ュ <frame src="test.html"/>
                    String frame = tag.getText();
                    int start = frame.indexOf("src=");
                    frame = frame.substring(start);
                    int end = frame.indexOf(" ");
                    if (end == -1) {
                        end = frame.indexOf(">");
                    }
                    String frameUrl = frame.substring(5, end - 1);
                    if (filter.accept(frameUrl)) {
                        links.add(frameUrl);
                    }
                }
            }
        } catch (ParserException e) {
            e.printStackTrace();
        }
        return links;
    }

    public void doParser(String url) {
        SohuNews news = new SohuNews();
        Set<String> links = LinkParser.extracLinks(
                url, new LinkFilter() {
            //鎻愬彇浠�http://www.twt.edu.cn 寮�ご鐨勯摼鎺�
            public boolean accept(String url) {
                if (url.matches("http://news.sohu.com/[\\d]+/n[\\d]+.shtml")) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        //寰幆杩唬鍑鸿繛鎺ワ紝鐒跺悗鎻愬彇璇ヨ繛鎺ヤ腑鐨勬柊闂汇�
        for (String link : links) {
            System.out.println(link);
            news.parser(link);
            
        }
    }

    //娴嬭瘯涓婚〉鏂伴椈锛屽彲浠ュ緱鍒颁富椤典笂鎵�湁绗﹀悎瑕佹眰鐨勭綉椤靛湴鍧�紝骞惰繘琛岃闂�
    
}

