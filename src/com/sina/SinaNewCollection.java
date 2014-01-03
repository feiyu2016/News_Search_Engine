package com.sina;


import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NotFilter;
import org.htmlparser.filters.RegexFilter;
import org.htmlparser.filters.StringFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.ParserUtil;
import com.NewCollection;
import com.sohu.SohuNews;
import com.sohu.bean.NewsBean;
import com.sohu.db.ConnectionManager;

/*
 * 瀵规柊娴綉鐨勪竴鏉℃柊闂昏繘琛屾暟鎹噰闆�
 */
public class SinaNewCollection implements NewCollection{
	
	private ParserUtil parserUtil = new ParserUtil();
	private Parser parser = null;   //鐢ㄤ簬鍒嗘瀽缃戦〉鐨勫垎鏋愬櫒銆�
    private List newsList = new ArrayList();    //鏆傚瓨鏂伴椈鐨凩ist锛�
    private NewsBean bean = new NewsBean();
    private ConnectionManager manager = null;    //鏁版嵁搴撹繛鎺ョ鐞嗗櫒銆�
    private PreparedStatement pstmt = null;
    
    public SinaNewCollection(){
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
            pstmt.setString(7, "新浪新闻");
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
     * 鑾峰緱鏂伴椈鐨勬爣棰�
     * @param titleFilter
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
        	NodeList sourceList = (NodeList) parser.parse(newsauthorFilter);
        	newsAuthor = parserUtil.getNodeListText(sourceList);

        } catch (ParserException ex) {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newsAuthor;

    }
    
    private String getNewsDate(NodeFilter newsdateFilter, Parser parser) {
        String newsDate = "";
        try {
        	NodeList dateList = (NodeList) parser.parse(newsdateFilter);
        	newsDate = parserUtil.getNodeListText(dateList);
        	newsDate =newsDate.replace("&nbsp;", "");

        } catch (ParserException ex) {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newsDate;

    }
    
    /**
     * 鑾峰彇鏂伴椈鐨勫唴瀹�
     * @param newsContentFilter
     * @param parser
     * @return  content 鏂伴椈鍐呭
     */
    private String getNewsContent(NodeFilter newsContentFilter, Parser parser) {
        String content = "";
        StringBuilder builder = new StringBuilder();


        try {
            NodeList newsContentList = (NodeList) parser.parse(newsContentFilter);
            NodeList childList =new NodeList();
            if (newsContentList.size() !=0)
            	childList = newsContentList.elementAt(0).getChildren();
            
        	childList.keepAllNodesThatMatch(new NotFilter(new TagNameFilter("div")));
            
            content = parserUtil.getNodeListHTML(newsContentList);
        	content = ParserUtil.getPlainText(content);
           

        } catch (ParserException ex) {
            Logger.getLogger(SohuNews.class.getName()).log(Level.SEVERE, null, ex);
        }

        return content;
    }
    
    /**
     * 瀵规柊闂籙RL杩涜瑙ｆ瀽骞堕噰闆嗘暟鎹�
     * @param url 鏂伴椈杩炴帴銆�
     */
    public void parser(String url) {
    	String title = ""; 			//鏂伴椈鏍囬
    	String source = "";			//鏂伴椈鏉ユ簮
    	String sourceTime = "";		//鏂伴椈鏉ユ簮鏃堕棿
    	String author = "";			//鏂伴椈浣滆�
    	String Content = "";		//鏂伴椈鍐呭
    	String collectTime = "";	//鏂伴椈閲囬泦鏃堕棿-绯荤粺鏃堕棿
    	try {
			parser = new Parser(url);
			parser.setEncoding("GB2312");
			//鏍囬
    		NodeFilter titleFilter = new TagNameFilter("h1");
        	title = getTitle(titleFilter, parser);
        	parser.reset();		//姣忔鑾峰彇閮藉繀椤籸eset锛屼笉鐒跺悗闈㈣幏鍙栦笉鍒版暟鎹�
        	System.out.println(title);
        	//鏉ユ簮
        	NodeFilter sourceFilter = new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("id", "media_name"));
        	source = getNewsAuthor(sourceFilter, parser);
        	source =source.trim().replace("&nbsp;","");
        	parser.reset();	
        	System.out.println(source);
        	//鏉ユ簮鏃堕棿
        	NodeFilter sourceTimeFilter = new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("id", "pub_date"));
        	sourceTime =getNewsDate(sourceTimeFilter, parser);
        	parser.reset();	
        	System.out.println(sourceTime);
        	
        	//姝ｆ枃
        	NodeFilter ContentTimeFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "artibody"));        	
        	String newsContent = getNewsContent(ContentTimeFilter, parser);
            System.out.println(newsContent);   //杈撳嚭鏂伴椈鐨勫唴瀹癸紝鏌ョ湅鏄惁绗﹀悎瑕佹眰
            parser.reset();
            
            setNews(title, source, newsContent, sourceTime, url);
            //灏嗘柊闂绘坊鍔犲埌鏁版嵁涓�
            this.newsToDataBase();
 
        	
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    
    public static void main(String[] args) {
    	SinaNewCollection newCollection = new SinaNewCollection();
    	newCollection.parser("http://tech.sina.com.cn/d/2011-07-28/09485850149.shtml");   
    }
}