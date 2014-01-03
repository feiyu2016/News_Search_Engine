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

public class mergeIndex {
	private String source =null;
	private String target =null;
	
	public mergeIndex(String source_file, String target_file)
	{
		source =source_file;
		target =target_file;
	}
	
	
	public void merge() throws IOException
	{
		IndexWriter writer =new IndexWriter(target, null,true);
		writer.setMergeFactor(50);
		writer.setUseCompoundFile(false);
		
		Directory[] dir =new Directory[2];
		dir[1] =FSDirectory.getDirectory(source);
		dir[0] =FSDirectory.getDirectory(target);
		writer.addIndexes(dir);
		writer.close();
	}
	
    
    public static void main(String args[]) throws SQLException
    {
    	
    }
   
}
