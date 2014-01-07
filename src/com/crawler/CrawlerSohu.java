package com.crawler;

import com.sohu.SohuNews;
import java.util.Set;

/**
 *
 * @author yuwen qin <qywsjtu@gmail.com>
 */
public class CrawlerSohu {

    SohuNews news = new SohuNews();
    private int crawled_num =0;
    private int MAX_NUM =200;

    /* 浣跨敤绉嶅瓙 url 鍒濆鍖�URL 闃熷垪*/
    private void initCrawlerWithSeeds(String[] seeds) {
        for (int i = 0; i < seeds.length; i++) {
            LinkDB.addUnvisitedUrl(seeds[i]);
        }
    }

    /* 鐖彇鏂规硶*/
    public void crawling(String[] seeds) {
        LinkFilter filter = new LinkFilter() {
            //鎻愬彇浠�http://www.twt.edu.cn 寮�ご鐨勯摼鎺�
            public boolean accept(String url) {
                if (url.matches("http://news.sohu.com/[\\d]+/n[\\d]+.shtml")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        //鍒濆鍖�URL 闃熷垪
        initCrawlerWithSeeds(seeds);
        //寰幆鏉′欢锛氬緟鎶撳彇鐨勯摼鎺ヤ笉绌轰笖鎶撳彇鐨勭綉椤典笉澶氫簬 1000
        while (!LinkDB.unVisitedUrlsEmpty() && LinkDB.getVisitedUrlNum() <= 100) {
            if (crawled_num >MAX_NUM)
            	break;
           
            //闃熷ご URL 鍑哄
            String visitUrl = LinkDB.unVisitedUrlDeQueue();
            if (visitUrl == null) {
                
                continue;
            }
            crawled_num +=1;
           //璇�url 鏀惧叆鍒板凡璁块棶鐨�URL 涓�            LinkDB.addVisitedUrl(visitUrl);
            //鎻愬彇鍑轰笅杞界綉椤典腑鐨�URL
            Set<String> links = LinkParser.extracLinks(visitUrl, filter);
            //鏂扮殑鏈闂殑 URL 鍏ラ槦
            for (String link : links) {
                LinkDB.addUnvisitedUrl(link);
                System.out.println(link);
                 news.parser(link);
            }
        }
    }

    //main 鏂规硶鍏ュ彛锛屾洿鍔燽ase url 杩涜鍒嗘瀽
    public static void main(String[] args) {
        CrawlerSohu crawler = new CrawlerSohu();
        crawler.crawling(new String[]{"http://news.sohu.com/"});
    }
}

