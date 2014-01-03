/*
 * Created on 2004-5-12
 *
 */

//import java.util.Set;
//import seg.result.SingleFilter;
//import org.apache.lucene.analysis.standard.StandardTokenizer;
//import org.apache.lucene.analysis.WhitespaceAnalyzer;
//import org.apache.lucene.analysis.StopFilter;

package com;

import java.io.Reader;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;

import com.lietu.jseg.ja.JpTokenizer;

/**
 * ʹ��CnTokenizer��Analyzerʾ���ࡣ
 *
 */
public class JpAnalyzer extends Analyzer {
	/**
	 * get token stream from input
	 *
	 * @param fieldName lucene field name
	 * @param reader input reader
	 *
	 * @return TokenStream
	 */
	public final TokenStream tokenStream(String fieldName, Reader reader) {
		System.out.println("文件名字"+fieldName);
		TokenStream result =  new JpTokenizer(reader);
		result = new LowerCaseFilter(result);
		return result;
	}
}
