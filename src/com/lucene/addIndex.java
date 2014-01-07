package com.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.search.highlight.Formatter;
//import org.apache.lucene.search.highlight.QueryScorer;
//import org.apache.lucene.search.highlight.SimpleFragmenter; //import org.apache.lucene.search.highlight.RegexFragmenter;
//import org.apache.lucene.search.highlight.TokenGroup;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.TermPositionVector;
//import org.apache.lucene.search.highlight.TokenSources; //import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.QueryFilter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumberTools;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter.*;

import com.sohu.SohuNews;
import com.sohu.db.ConnectionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date; //import java.util.StringTokenizer;

import java.io.File;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import java.io.InputStream;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;    
import java.sql.ResultSet;     
import java.sql.Statement;  
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sohu.db.ConnectionManager;

public class addIndex {
	private Connection con = null;
    private ConnectionManager manager = null;    //鏁版嵁搴撹繛鎺ョ鐞嗗櫒銆�
	private boolean autoCommit=true;
	private String indexPath =null;
	
	public addIndex(String path)
	{
		indexPath =path;
	}
	
	private String getPath()
	{
		return indexPath;
	}
	
	private void setPath(String path)
	{
		indexPath =path;
	}
	
	public void add()
	{
		manager =new ConnectionManager();
		Connection con=manager.getConnection();
		Statement st= null;
		try{
			con.createStatement();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Adding files into new index ...");
		String sql="select * from news where indexed =0";
		boolean flag=true;
		ResultSet rs=null;
		Document doc= null;
		int numFile =0;
		try 
		{
			st =con.createStatement();
			rs = st.executeQuery(sql);
		}
		catch (SQLException ex)
		{
			Logger.getLogger(createIndex.class.getName()).log(Level.SEVERE, null, ex);
		}
		FSDirectory directory = null;
		Analyzer analyzer = new PaodingAnalyzer();  
		try {
			directory = FSDirectory.getDirectory(this.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        IndexWriter writer=null;
		try {
			writer = new IndexWriter(directory, analyzer);
		} catch (CorruptIndexException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (LockObtainFailedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try
		{
		
			while (rs.next())
			{
				String newstitle=rs.getString("newstitle");
				String newsdate=rs.getString("newsdate");
				String newscontent=rs.getString("newscontent");
				String newsurl=rs.getString("newsurl");
				String source =rs.getString("newsource");
				doc=new Document();
				doc.add(new Field("url",newsurl,Field.Store.YES,Field.Index.UN_TOKENIZED));
				doc.add(new Field("title",newstitle,Field.Store.YES,Field.Index.TOKENIZED,Field.TermVector.WITH_POSITIONS_OFFSETS));
				doc.add(new Field("body",newscontent,Field.Store.YES,Field.Index.TOKENIZED,Field.TermVector.WITH_POSITIONS_OFFSETS));
				doc.add(new Field("date",newsdate,Field.Store.YES,Field.Index.UN_TOKENIZED));
				doc.add(new Field("source",source,Field.Store.YES,Field.Index.TOKENIZED));
				System.out.print (newstitle+'\n');
				try {  
					writer.addDocument(doc);
					Statement s = con.createStatement();
					String updatesql="update news set indexed = 1 where newsurl='"+newsurl+"'";
					int countUpdate = s.executeUpdate(updatesql);
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
				numFile +=1;
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		manager.close();
		System.out.println("Finishes!");
		System.out.println(String.valueOf(numFile)+" Files Added to Index");
	}
	
    
    public static void main(String args[]) throws SQLException
    {
    	System.out.println(args[0]);
    	String path ="";
    	if (args.length ==0)
    		path ="E:/indextmp";
    	else
    		path =args[0];
    	
    	addIndex a =new addIndex(path);
    	a.add();
    	
    }
   
}
