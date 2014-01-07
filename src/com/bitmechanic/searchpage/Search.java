package com.bitmechanic.searchpage;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SortField;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter; //import org.apache.lucene.search.highlight.RegexFragmenter;
import org.apache.lucene.search.highlight.TokenGroup;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.TermPositionVector;
import org.apache.lucene.search.highlight.TokenSources; //import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.QueryFilter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date; //import java.util.StringTokenizer;

import javax.servlet.jsp.PageContext;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import com.JpAnalyzer;
import com.bitmechanic.listlib.ListCreator;
import com.bitmechanic.listlib.ListContainer;
import java.io.InputStream;

import net.paoding.analysis.analyzer.PaodingAnalyzer;
/**
 * Use this class to search indexes created by the Spider class.
 * 
 * @author James Cooper
 * @author luogang change history: add index to support search by catalog. add
 *         init to speed up search.
 * 
 * @author yuwen qin change history: modify to add support for date, source
 * rank according to hit count.
 */
// Search绫荤户鎵夸簡ListCreator绫�鎵�互灏卞叿澶囦簡鍏剁埗绫绘墍鍏锋湁鐨勭壒鎬�
public class Search implements ListCreator {

	private static String[] cats = null;
	private static QueryParser bodyParser = new QueryParser("body", new PaodingAnalyzer());
	private static QueryParser titleParser = new QueryParser("title", new PaodingAnalyzer());
	private static QueryParser sourceParser = new QueryParser("source", new PaodingAnalyzer());

	// yyyy-M-d H:m
	private static SimpleDateFormat timeFormatter = new SimpleDateFormat(
			"yyyy-M-d");
	private static int descLenth = 50;
	private static int maxNumFragmentsRequired = 2;
	private static String fragmentSeparator = "...";
	private static String _dir;
	private IndexReader reader = null;
	private Searcher searcher = null;
	private File file =null;
	private String _query;
	
	private static long interview = 60000L;

	private static Date indexVersion = new Date();

	public void init(String indexPath) throws Exception {
		if(searcher!=null)
			searcher.close();
		if(reader != null)
			reader.close();
		_dir = indexPath;
		//璁剧疆绯荤粺杩愯鎵�渶瑕佺殑灞炴�
		System.setProperty("index.dir", "E:/index");
		System.setProperty("dic.dir", "D:/dic");
		System.setProperty("log.dir", "D:/search/log");
		
		if (!existFile())
		{
			try {
				file = new File(getLogName());
				file.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			file =new File(getLogName());
		}
		
		searcher = new IndexSearcher(indexPath);
		reader = IndexReader.open(indexPath);
	}
	public void reinit() throws Exception{
		if(searcher != null)
			searcher.close();
		if(reader != null)
			reader.close();
		
		searcher = new IndexSearcher(_dir);
		reader = IndexReader.open(_dir);
	}
	private void refreshIndexReader(){
		Date currentIndexVersion = new Date();
		if(currentIndexVersion.getTime()>(indexVersion.getTime()+interview)){
			synchronized(this){
				try{
					reinit();
					indexVersion = currentIndexVersion;
				}catch(Exception e){
					
				}
			}
		}
	}
	
	private static String getLogName() {
        StringBuffer logPath = new StringBuffer();
        logPath.append(System.getProperty("log.dir"));
        File file = new File(logPath.toString());
        if (!file.exists())
            file.mkdir();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        logPath.append("/"+sdf.format(new Date())+".log");
        
        return logPath.toString();
    }
	
	public static boolean existFile(){
		String filename =getLogName();
		File file = new File(filename);
		return (file.exists());
	}
	
	/*parse query input
	 * for example: 体育游戏    will be searched as 体育，游戏 respectively
	 */
	
    public ArrayList<String> queryProcess(String queryInput)
    {
    	PaodingAnalyzer analyzer =new PaodingAnalyzer();
    	String[] tempStr=queryInput.split(" ");
    	ArrayList<String> queryResult =new ArrayList<String>();
    	int i=0;
    	while (i<tempStr.length)
    	{
    		if (tempStr.length ==0)
    		{
    			i++;
    			continue;
    		}
    		
    		TokenStream source =analyzer.tokenStream("", new StringReader(tempStr[i]));
    		Token term;
    		while (true)
    		{
    			try{
    				term =source.next();
    			}
    			catch (IOException e)
    			{
    				term =null;
    			}
			
    			if (term == null)
    				break;
			
    			if (term.termText().length() !=0)
    				queryResult.add(term.termText());
    			
    		}
    		i++;
    		
    	}
    	return queryResult;
    }
    
	public synchronized ListDesc search(int category, String queryString,
			int offset, int max, boolean sortBydate, boolean refind,
			String inputstr) throws Exception {
		Query bodyQuery = null, titleQuery = null, sourceQuery = null, query = null,bodyOrTitleQuery = null;
		TokenStream tokenStream = null;
		this.refreshIndexReader();
		System.out.println("QueryString is:"+_query);
		ArrayList<String> queryProcess=queryProcess(queryString);
		BooleanQuery bodyOrTitle = new BooleanQuery();
		BooleanQuery tmpQuery =new BooleanQuery();
		
		for (int i=0; i<queryProcess.size(); i++)
		{
			bodyQuery = bodyParser.parse(queryProcess.get(i));
			titleQuery = titleParser.parse(queryProcess.get(i));

			tmpQuery.add(bodyQuery, BooleanClause.Occur.SHOULD);
			tmpQuery.add(titleQuery, BooleanClause.Occur.SHOULD);
		}
		
		bodyOrTitle.add(tmpQuery,BooleanClause.Occur.MUST);
		
		if (!inputstr.equals("全部"))
		{
			sourceQuery = sourceParser.parse(inputstr);
			bodyOrTitle.add(sourceQuery,BooleanClause.Occur.MUST);
		}
		
		
		if (reader == null)
			return null;
		Hits hits = null;
		try {
			query = bodyOrTitle.rewrite(reader);
			bodyOrTitleQuery =tmpQuery.rewrite(reader);
			
			Sort scoresort = new Sort(new SortField[]  
                        {  
                               //如果两个都没有注释，那么就是按照先按照相关度，然后按照id排序（相关度相同的情况下）  
                                SortField.FIELD_SCORE,//注释掉这个就是按照id排序  
                                new SortField("postId", SortField.INT, false)//注释掉这个就是按照相关度排序  
                        }  
                );  
				
			hits = searcher.search(query,scoresort);
			
		} catch (IOException e) {
			e.printStackTrace(System.err);
			return null;
		}
		
		// 瀵瑰叧閿瓧杩涜楂樹寒澶勭悊
		Highlighter highlighter = new Highlighter(new HighlightFormatter(),
				new QueryScorer(bodyOrTitleQuery));
		highlighter.setTextFragmenter(new SimpleFragmenter(descLenth));
		ArrayList list = new ArrayList(hits.length());
		// 鐪熸鐨勫垎椤垫墜鑴氭槸鍦ㄨ繖閲屽仛鐨�涓昏灏辨槸瀵圭粨鏋滈泦鐨勫惊鐜鐞�
		int maxHit = ((max + offset) > hits.length()) ? hits.length()
				: (max + offset);
		String text;
		try {
			for (int i = offset; i < maxHit; i++) {
				HashMap map = new HashMap();
//				System.out.println("寮�璁板綍鍙� " + offset + "   缁撴潫璁板綍鍙� + maxHit
//						+ "***********************************************");
				text = hits.doc(i).get("title");

				TermPositionVector tpv = (TermPositionVector) reader
						.getTermFreqVector(hits.id(i), "title");
				String result = null;
				if (tpv != null) {
					tokenStream = TokenSources.getTokenStream(tpv, false);
					result = highlighter.getBestFragment(tokenStream, text);
				}
				if (result != null) {
					if (result.length() < 100) {
						map.put("title", result);
					} else {
						map.put("title", result.substring(0, 10));
					}

				} else {
					if (text.length() < 100) {
						map.put("title", text);
					} else {
						map.put("title", text.substring(0, 10));
					}

				}
				System.out.println("title 鏄細" + map.get("title"));
				text = hits.doc(i).get("body");
				tpv = (TermPositionVector) reader.getTermFreqVector(hits.id(i),
						"body");
				if (tpv == null) {
					result = "";
				} else {
					tokenStream = TokenSources.getTokenStream(tpv, false);
					result = highlighter.getBestFragments(tokenStream, text,
							maxNumFragmentsRequired, fragmentSeparator);
					/*
					 * int maxLength = descLenth > text.length() ? text
					 * .length() : descLenth; System.out.println("maxLength闀垮害鏄�
					 * 锛�+maxLength); result = text.substring(0, maxLength);
					 */
				}

				if ("".equals(result)) {
					int maxLength = descLenth > text.length() ? text.length()
							: descLenth;
					System.out.println("maxLength闀垮害鏄� 锛� + maxLength");
					result = text.substring(0, maxLength);
				}

				System.out.println("result " + result.length());
				map.put("body", result);
				text=hits.doc(i).get("url");
				System.out.println(text);
				map.put("url", text);
				text=hits.doc(i).get("date");
				map.put("date", text);
				System.out.println("body " + map.get("body"));
				list.add(map);
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
			return null;
		}
		ListDesc desc = new ListDesc();
		desc.list = list;
		desc.count = hits.length();
		return desc;
	}

	public void setDir(String dir) {
		_dir = dir;
	}

	public void setQuery(String query) {
		System.out.println("setQuery" + query);

		_query = query;
	}

	public void logwrite(String query)
	{
		FileWriter filewriter =null;
		String queryLog ="";
		try{
			filewriter =new FileWriter(file,true);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			queryLog =df.format(new Date())+ "|RUN|" +query +"\r\n";
			System.out.println(queryLog);
			filewriter.write(queryLog);
			filewriter.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				filewriter.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Runs a query against the index in the directory specified. Returns a list
	 * of HashMap objects. The objects have the following keys: 'url', 'title',
	 * 'desc', 'score'.
	 * 
	 * @param dir
	 *            Directory containing the index to search
	 * @param query
	 *            Query to run against the index
	 * @param offset
	 *            Position in the result set to start returning results
	 * @param max
	 *            Max number of results to return
	 */
	public ListContainer execute(PageContext context, int cat, int offset,
			int max, boolean dateSort) throws Exception {
		System.out.println("杩愯浜唀xecute");
		init("D:/index");
		System.setProperty("dic", "D:/dic");
		String inputstr = context.getRequest().getParameter("sel");
		if (inputstr ==null)
			inputstr ="全部";
		
		boolean ref = false;
		System.out.println(offset);
		setQuery(context.getRequest().getParameter("query"));
		System.out.println("query is :"+context.getRequest().getParameter("query"));
		System.out.println("site is :"+context.getRequest().getParameter("sel"));
		ListDesc desc = search(1, _query, offset, max, false, ref, inputstr);
		logwrite(_query);
		return new ListContainer(desc.list.iterator(), desc.count);
	}
	
	public static void main(String[] args) throws Exception{
		Search search=new Search();
		search.init("D:/index");
		System.setProperty("dic", "D:/dic");
		System.out.println(System.getProperty("dic"));
		System.out.println(getLogName());
		//String result=search.queryProcess("fff");
		//System.out.println(result);
		//Query bodyQuery = null, titleQuery = null, query = null;
		//bodyQuery = bodyParser.parse("g");
		search.setQuery("2");
		//search.search(1, "2", 1, 10, false, false, "");
		//System.err.println("fffffffffff");//鎵撳嵃鍑烘潵鏄孩鑹茬殑銆�
		
	}
}

// 鍦ㄨ繖閲屽彲浠ユ帶鍒堕绾㈢殑鏍峰紡銆�
class HighlightFormatter implements Formatter {
	int numHighlights = 0;

	public String highlightTerm(String originalText, TokenGroup group) {
		if (group.getTotalScore() <= 0) {
			return originalText;
		}
		numHighlights++; // update stats used in assertions
		return "<font color=red>" + originalText + "</font>";
	}
}
