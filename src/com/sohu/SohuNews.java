package com.sohu;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.*;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import java.security.*;
import com.sohu.bean.NewsBean;
import com.sohu.db.ConnectionManager;

/**
 * 鐢ㄤ簬瀵规悳鐙愮綉绔欎笂鐨勬柊闂昏繘琛屾姄鍙� * @author yuwen qin
 */
public class SohuNews {

    private Parser parser = null;   //鐢ㄤ簬鍒嗘瀽缃戦〉鐨勫垎鏋愬櫒銆�    
    private List newsList = new ArrayList();    //鏆傚瓨鏂伴椈鐨凩ist锛�    
    private NewsBean bean = new NewsBean();
    private ConnectionManager manager = null;    //鏁版嵁搴撹繛鎺ョ鐞嗗櫒銆�   
    private PreparedStatement pstmt = null;

    public SohuNews() {
    }

    /**
     * 鑾峰緱涓�潯瀹屾暣鐨勬柊闂汇�
     * @param newsBean
     * @return
     */
    public List getNewsList(final NewsBean newsBean) {
        List list = new ArrayList();
        String newstitle = newsBean.getNewsTitle();
        String newsauthor = newsBean.getNewsAuthor();
        String newscontent = newsBean.getNewsContent();
        String newsdate = newsBean.getNewsDate();
        list.add(newstitle);
        list.add(newsauthor);
        list.add(newscontent);
        list.add(newsdate);
        return list;
    }
    public static String bytesToHex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();
		//鎶婃暟缁勬瘡涓�瓧鑺傛崲鎴�6杩涘埗杩炴垚md5瀛楃涓�		
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			 digital = bytes[i];
			if(digital < 0) {
				digital += 256;
			}
			if(digital < 16){
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString().toUpperCase();
	}
	
	/**
	 * 鎶婂瓧鑺傛暟缁勮浆鎹㈡垚md5
	 * @param input
	 * @return
	 */
	public static String bytesToMD5(byte[] input) {
		String md5str = null;
		try {
			//鍒涘缓涓�釜鎻愪緵淇℃伅鎽樿绠楁硶鐨勫璞★紝鍒濆鍖栦负md5绠楁硶瀵硅薄
			MessageDigest md = MessageDigest.getInstance("MD5");
			//璁＄畻鍚庤幏寰楀瓧鑺傛暟缁�			
			byte[] buff = md.digest(input);
			//鎶婃暟缁勬瘡涓�瓧鑺傛崲鎴�6杩涘埗杩炴垚md5瀛楃涓�			
			md5str = bytesToHex(buff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5str;
	}
	/**
	 * 鎶婂瓧绗︿覆杞崲鎴恗d5
	 * @param str
	 * @return
	 */
	public static String strToMD5(String str) {
		byte[] input = str.getBytes();
		return bytesToMD5(input);
	}
    
    /**
     *  璁剧疆鏂伴椈瀵硅薄锛岃鏂伴椈瀵硅薄閲屾湁鏂伴椈鏁版嵁
     * @param newsTitle 鏂伴椈鏍囬
     * @param newsauthor  鏂伴椈浣滆�
     * @param newsContent 鏂伴椈鍐呭
     * @param newsDate  鏂伴椈鏃ユ湡
     * @param url  鏂伴椈閾炬帴
     */
    public void setNews(String newsTitle, String newsauthor, String newsContent, String newsDate, String url) {
        bean.setNewsTitle(newsTitle);
        bean.setNewsAuthor(newsauthor);
        bean.setNewsContent(newsContent);
        bean.setNewsDate(newsDate);
        bean.setNewsURL(url);
    }

    /**
     * 璇ユ柟娉曠敤浜庡皢鏂伴椈娣诲姞鍒版暟鎹簱涓�
     */
    protected void newsToDataBase() {

        //寤虹珛涓�釜绾跨▼鐢ㄦ潵鎵ц灏嗘柊闂绘彃鍏ュ埌鏁版嵁搴撲腑銆�       
    	Thread thread = new Thread(new Runnable() {

            public void run() {
                boolean sucess = saveToDB(bean);
                if (sucess != false) {
                    System.out.println("鎻掑叆鏁版嵁澶辫触");
                }
            }
        });
        thread.start();
    }

    /**
     * 灏嗘柊闂绘彃鍏ュ埌鏁版嵁搴撲腑
     * @param bean
     * @return
     */
    public boolean saveToDB(NewsBean bean) {
        boolean flag = true;
        String sql = "insert ignore into news(urlhash,newstitle,newsauthor,newscontent,newsurl,newsdate,newsource) values(?,?,?,?,?,?,?)";
        manager = new ConnectionManager();
        String titleLength = bean.getNewsTitle();
        if (titleLength.length() > 60) {  //鏍囬澶暱鐨勬柊闂讳笉瑕併�
            return flag;
        }
        try {
            pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.setString(1, strToMD5(bean.getNewsURL()));
            pstmt.setString(2, bean.getNewsTitle());
            pstmt.setString(3, bean.getNewsAuthor());
            pstmt.setString(4, bean.getNewsContent());
            pstmt.setString(5, bean.getNewsURL());
            pstmt.setString(6, bean.getNewsDate());
            pstmt.setString(7, "搜狐新闻");
            System.out.println(bean.getNewsAuthor());
            flag = pstmt.execute();

        } catch (SQLException ex) {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pstmt.close();
                manager.close();
            } catch (SQLException ex) {
                Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return flag;
    }

    /**
     * 鑾峰緱鏂伴椈鐨勬爣棰�     * @param titleFilter
     * @param parser
     * @return
     */
    private String getTitle(NodeFilter titleFilter, Parser parser) {
        String titleName = "";
        try {

            NodeList titleNodeList = (NodeList) parser.parse(titleFilter);
            for (int i = 0; i < titleNodeList.size(); i++) {
                HeadingTag title = (HeadingTag) titleNodeList.elementAt(i);
                titleName = title.getStringText();
            }

        } catch (ParserException ex) {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        }
        return titleName;
    }

    /**
     * 鑾峰緱鏂伴椈鐨勮矗浠荤紪杈戯紝涔熷氨鏄綔鑰呫�
     * @param newsauthorFilter
     * @param parser
     * @return
     */
    private String getNewsAuthor(NodeFilter newsauthorFilter, Parser parser) {
        String newsAuthor = "";
        try {
            NodeList authorList = (NodeList) parser.parse(newsauthorFilter);
            for (int i = 0; i < authorList.size(); i++) {
                Div authorSpan = (Div) authorList.elementAt(i);
                newsAuthor = authorSpan.getStringText();
            }

        } catch (ParserException ex) {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newsAuthor;

    }

    /*
     * 鑾峰緱鏂伴椈鐨勬棩鏈�     */
    private String getNewsDate(NodeFilter dateFilter, Parser parser) {
        String newsDate = null;
        try {
            NodeList dateList = (NodeList) parser.parse(dateFilter);
            for (int i = 0; i < dateList.size(); i++) {
                Div dateTag = (Div) dateList.elementAt(i);
                newsDate = dateTag.getStringText();
            }
        } catch (ParserException ex) {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        }

        return newsDate;
    }

    /**
     * 鑾峰彇鏂伴椈鐨勫唴瀹�     * @param newsContentFilter
     * @param parser
     * @return  content 鏂伴椈鍐呭
     */
    private String getNewsContent(NodeFilter newsContentFilter, Parser parser) {
        String content = null;
        StringBuilder builder = new StringBuilder();


        try {
            NodeList newsContentList = (NodeList) parser.parse(newsContentFilter);
            for (int i = 0; i < newsContentList.size(); i++) {
                Div newsContenTag = (Div) newsContentList.elementAt(i);
                builder = builder.append(newsContenTag.getStringText());
            }
            content = builder.toString();  //杞崲涓篠tring 绫诲瀷銆�           
            if (content != null) {
                parser.reset();
                parser = Parser.createParser(content, "utf8");
                StringBean sb = new StringBean();
                sb.setCollapse(true);
                parser.visitAllNodesWith(sb);
                content = sb.getStrings();
//                String s = "\";} else{ document.getElementById('TurnAD444').innerHTML = \"\";} } showTurnAD444(intTurnAD444); }catch(e){}";
                if (content==null)
                	return null;
                content = content.replaceAll("\\\".*[a-z].*\\}", "");
             
                content = content.replace("[鎴戞潵璇翠袱鍙", "");


            } else {
               System.out.println("娌℃湁寰楀埌鏂伴椈鍐呭锛�");
            }

        } catch (ParserException ex) {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        }

        return content;
    }

    /**
     * 鏍规嵁鎻愪緵鐨刄RL锛岃幏鍙栨URL瀵瑰簲缃戦〉鎵�湁鐨勭函鏂囨湰淇℃伅锛屾鏂规硶寰楀埌鐨勪俊鎭笉鏄緢绾紝
     *甯稿父浼氬緱鍒版垜浠笉鎯宠鐨勬暟鎹�涓嶈繃濡傛灉浣犲彧鏄兂寰楀埌鏌愪釜URL 閲岀殑鎵�湁绾枃鏈俊鎭紝璇ユ柟娉曡繕鏄緢濂界敤鐨勩�
     * @param url 鎻愪緵鐨刄RL閾炬帴
     * @return RL瀵瑰簲缃戦〉鐨勭函鏂囨湰淇℃伅
     * @throws ParserException
     * @deprecated 璇ユ柟娉曡 getNewsContent()鏇夸唬銆�     */
    @Deprecated
    public String getText(String url) throws ParserException {
        StringBean sb = new StringBean();

        //璁剧疆涓嶉渶瑕佸緱鍒伴〉闈㈡墍鍖呭惈鐨勯摼鎺ヤ俊鎭�       
        sb.setLinks(false);
        //璁剧疆灏嗕笉闂存柇绌烘牸鐢辨瑙勭┖鏍兼墍鏇夸唬
        sb.setReplaceNonBreakingSpaces(true);
        //璁剧疆灏嗕竴搴忓垪绌烘牸鐢变竴涓崟涓�┖鏍兼墍浠ｆ浛
        sb.setCollapse(true);
        //浼犲叆瑕佽В鏋愮殑URL
        sb.setURL(url);

        //杩斿洖瑙ｆ瀽鍚庣殑缃戦〉绾枃鏈俊鎭�       
        return sb.getStrings();
    }

    /**
     * 瀵规柊闂籙RL杩涜瑙ｆ瀽鎻愬彇鏂伴椈锛屽悓鏃跺皢鏂伴椈鎻掑叆鍒版暟鎹簱涓�
     * @param url 鏂伴椈杩炴帴銆�     */
    public void parser(String url) {
        try {
            parser = new Parser(url);
            NodeFilter titleFilter = new TagNameFilter("h1");
            NodeFilter contentFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "contentText"));
            NodeFilter newsauthorFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "source"));

            NodeFilter innerFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "time"));
            
            
            
            String newsTitle = getTitle(titleFilter, parser);
            System.out.println(newsTitle);    
            parser.reset();   //璁板緱姣忔鐢ㄥ畬parser鍚庯紝瑕侀噸缃竴娆arser銆傝涓嶇劧灏卞緱涓嶅埌鎴戜滑鎯宠鐨勫唴瀹逛簡銆�            
            String newsDate = getNewsDate(innerFilter,parser);
           
            NodeFilter xk = new HasParentFilter(innerFilter);
            NodeList nodes = parser.extractAllNodesThatMatch(xk);
 
            for (int i = 0; i < nodes.size(); i++) {
            	Node time = (Node) nodes.elementAt(i);
                System.out.println(time.toPlainTextString().trim());
                System.out.print(i);
                System.out.println("======================================================");
            }
            parser.reset();
            
            String newsContent = getNewsContent(contentFilter, parser);
            System.out.println(newsContent);   //杈撳嚭鏂伴椈鐨勫唴瀹癸紝鏌ョ湅鏄惁绗﹀悎瑕佹眰
            parser.reset();
 
            String newsauthor = getNewsAuthor(newsauthorFilter, parser);
            newsauthor=newsauthor.replaceAll("<(/?\\S+)\\s*?[^<]*?(/?)>","");
            newsauthor=newsauthor.trim();
            //鍏堣缃柊闂诲璞★紝璁╂柊闂诲璞￠噷鏈夋柊闂诲唴瀹广�
            setNews(newsTitle, newsauthor, newsContent, newsDate, url);
            //灏嗘柊闂绘坊鍔犲埌鏁版嵁涓�
            this.newsToDataBase();
            
        } catch (ParserException ex) {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //鍗曚釜鏂囦欢娴嬭瘯缃戦〉
    public static void main(String[] args) {
        SohuNews news = new SohuNews();
        news.parser("http://news.sohu.com/20120616/n345802648.shtml");   
    }
}
