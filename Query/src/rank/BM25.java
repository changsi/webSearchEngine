/**
 * 
 */
package rank;

import java.util.Vector;

import test.Lexicon;
import test.URLTable;

/**
 * @author root
 *
 */
public class BM25 implements RankFunction {
	/**
	 * number of all documents
	 */
	private int total_doc_num;
	/**
	 * lexicon used for get doc number of specific term
	 */
	private Lexicon lexicon;
	/**
	 * the average length of documents
	 */
	private int average_doc_length;
	/**
	 * docid and URL mapping table used for get document size
	 */
	private URLTable urltable;
	/**
	 * document term frequency
	 */
	private int []frequency;
	
	public BM25(){
		
	}
	
	public BM25(int total_doc_num, Lexicon lexicon, int average_doc_length, URLTable urltable, int []frequency){
		this.setTotal_doc_num(total_doc_num);
		this.setLexicon(lexicon);
		this.setAverage_doc_length(average_doc_length);
		this.setUrltable(urltable);
		this.setFrequency(frequency);
	}
	
	private double getidf(String t){
		double idfres=0;
		int doc_num_term=this.lexicon.get(t).get_doc_num();
		idfres=Math.log((this.total_doc_num-doc_num_term+0.5)/(doc_num_term+0.5));
		return  idfres;
	}
	
	public double compute(Vector<String> query_terms,int did){
		double k1=1.2;
		double b=0.75;
		double value=0;
		for(int i=0;i<query_terms.size();i++){
			double first=getidf(query_terms.get(i));
			double k=k1*((1-b)+b*Math.abs(this.urltable.get(did).getSize())/this.average_doc_length);
			double second=(k1+1)*this.frequency[i]/(k+this.frequency[i]);
			value=value+first*second;
		}
		return value;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param total_doc_num the total_doc_num to set
	 */
	public void setTotal_doc_num(int total_doc_num) {
		this.total_doc_num = total_doc_num;
	}

	/**
	 * @return the total_doc_num
	 */
	public int getTotal_doc_num() {
		return total_doc_num;
	}

	/**
	 * @param lexicon the lexicon to set
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
	 * @param average_doc_length the average_doc_length to set
	 */
	public void setAverage_doc_length(int average_doc_length) {
		this.average_doc_length = average_doc_length;
	}

	/**
	 * @return the average_doc_length
	 */
	public int getAverage_doc_length() {
		return average_doc_length;
	}

	/**
	 * @param urltable the urltable to set
	 */
	public void setUrltable(URLTable urltable) {
		this.urltable = urltable;
	}

	/**
	 * @return the urltable
	 */
	public URLTable getUrltable() {
		return urltable;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(int [] frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the frequency
	 */
	public int [] getFrequency() {
		return frequency;
	}

}
