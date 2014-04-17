/**
 * 
 */
package query;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Vector;

import rank.BM25;
import rank.RankFunction;
import test.Lexicon;
import test.URLTable;
import util.BasicInvertedIndexQuery;
import util.InvertedIndexCache;
import util.InvertedIndexQuery;
import util.ResultItem;
import util.StopWords;
import util.TokenizerNew;

/**
 * @author changsi
 * 
 */
public class QueryProcessor {

	private String query;
	private TokenizerNew tokenizer;
	private StopWords stopword;
	private PriorityQueue<ResultItem> result_set;
	private URLTable url_table;
	/**
	 * lexicon injected by calling class
	 */
	private Lexicon lexicon;
	/**
	 * inverted index cache in memory injected by calling class
	 */
	private InvertedIndexCache cache;
	private int maximum_docid;

	public QueryProcessor() {
		this.setResult_set(new PriorityQueue<ResultItem>());
	}

	public QueryProcessor(String query, TokenizerNew tokenizer,
			StopWords stopword, Lexicon lexicon, URLTable url_table,
			InvertedIndexCache cache, int maximum_docid) {
		this.setQuery(query);
		this.setTokenizer(tokenizer);
		this.setStopword(stopword);
		this.setLexicon(lexicon);
		this.setUrl_table(url_table);
		this.setCache(cache);
		this.setMaximum_docid(maximum_docid);
		this.setResult_set(new PriorityQueue<ResultItem>());
	}

	public void process() {
		Vector<String> query_terms = this.tokenizer.split(this.query);
		query_terms = this.stopword.removeStopWords(query_terms);
		BasicInvertedIndexQuery index[] = new BasicInvertedIndexQuery[query_terms
				.size()];
		for (int i = 0; i < query_terms.size(); i++) {
			index[i] = new InvertedIndexQuery(
					"//media//d__//workshop//Merge//", this.cache, this.lexicon);
			index[i].openList(query_terms.get(i));
		}
		int docid = 0;
		int d = 0;
		while (docid <= this.maximum_docid) {
			docid = index[0].nextGEQ(docid);
			for (int j = 1; j < query_terms.size()
					&& (d = index[j].nextGEQ(docid)) == docid; j++)
				;
			if (d > docid) {
				docid = d;
			} else {
				// calculate rank score
				int[] frequency = new int[index.length];
				for (int i = 0; i < index.length; i++) {
					frequency[i] = index[i].getFreq();
				}
				 RankFunction rank_function= new BM25(this.maximum_docid,
				 this.lexicon, 306, this.url_table, frequency);
				 double score=rank_function.compute(query_terms, docid);
				 ResultItem temp=new ResultItem(docid, score,
				 this.url_table.get(docid).getURL());
				 this.result_set.add(temp);
				docid++;
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int begin = 0;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					"//media//d__//workshop//Merge//lexicon.seri"));
			Lexicon lexicon = (Lexicon) in.readObject();
			in.close();
			in = new ObjectInputStream(new FileInputStream("url_table.seri"));
			URLTable urltable = (URLTable) in.readObject();
			urltable.get(1);
			in.close();
			InvertedIndexCache cache = new InvertedIndexCache(1500000000);
			TokenizerNew tokenizer = new TokenizerNew();
			StopWords sw = new StopWords();
			QueryProcessor query = new QueryProcessor("chinese student",
					tokenizer, sw, lexicon, urltable, cache, 2571135);
			begin = (int) System.currentTimeMillis();
			query.process();
			PriorityQueue<ResultItem> result_set=query.getResult_set();
			int end = (int) System.currentTimeMillis();
			int timetaken = end - begin;
			while(result_set.size()>0){
				ResultItem temp=result_set.remove();
				System.out.println("docid: "+ temp.getDocid()+" score: "+temp.getScore()+"  url: "+ temp.getUrl());
			}
			System.out.println("time taken: " + timetaken + "million seconds");
			System.out.println("time taken: " + timetaken / 1000+ " seconds");
			Scanner sc = new Scanner(System.in);
			String input=sc.nextLine();
			while(input !=null&& input!=" " ){
				begin = (int) System.currentTimeMillis();
				query = new QueryProcessor(input,
						tokenizer, sw, lexicon, urltable, cache, 2571135);
				query.process();
			
				result_set=query.getResult_set();
				end = (int) System.currentTimeMillis();
				timetaken = end - begin;
				while(result_set.size()>0){
					ResultItem temp=result_set.remove();
					System.out.println("docid: "+ temp.getDocid()+" score: "+temp.getScore()+"  url: "+ temp.getUrl());
				}
				System.out.println("time taken: " + timetaken + "million seconds");
				System.out.println("time taken: " + timetaken / 1000+ " seconds");
				input=sc.nextLine();
			}
		} catch (Exception e) {
			System.err.println("Error: " + e);
			e.printStackTrace(System.err);
		} finally {
			
			
		}
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param tokenizer
	 *            the tokenizer to set
	 */
	public void setTokenizer(TokenizerNew tokenizer) {
		this.tokenizer = tokenizer;
	}

	/**
	 * @return the tokenizer
	 */
	public TokenizerNew getTokenizer() {
		return tokenizer;
	}

	/**
	 * @param cache
	 *            the cache to set
	 */
	public void setCache(InvertedIndexCache cache) {
		this.cache = cache;
	}

	/**
	 * @return the cache
	 */
	public InvertedIndexCache getCache() {
		return cache;
	}

	/**
	 * @param lexicon
	 *            the lexicon to set
	 */
	public void setLexicon(Lexicon lexicon) {
		this.lexicon = lexicon;
	}

	/**
	 * @return the lexicon
	 */
	public Lexicon getLexicon() {
		return lexicon;
	}

	/**
	 * @param maximum_docid
	 *            the maximum_docid to set
	 */
	public void setMaximum_docid(int maximum_docid) {
		this.maximum_docid = maximum_docid;
	}

	/**
	 * @return the maximum_docid
	 */
	public int getMaximum_docid() {
		return maximum_docid;
	}

	/**
	 * @param stopword
	 *            the stopword to set
	 */
	public void setStopword(StopWords stopword) {
		this.stopword = stopword;
	}

	/**
	 * @return the stopword
	 */
	public StopWords getStopword() {
		return stopword;
	}

	/**
	 * @param result_set
	 *            the result_set to set
	 */
	public void setResult_set(PriorityQueue<ResultItem> result_set) {
		this.result_set = result_set;
	}

	/**
	 * @return the result_set
	 */
	public PriorityQueue<ResultItem> getResult_set() {
		return result_set;
	}

	/**
	 * @param url_table
	 *            the url_table to set
	 */
	public void setUrl_table(URLTable url_table) {
		this.url_table = url_table;
	}

	/**
	 * @return the url_table
	 */
	public URLTable getUrl_table() {
		return url_table;
	}

}
