package util;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class WordIDGenerator implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7020619474462344L;
	private int count=0;
//	private static WordIDGenerator instance;
	private ConcurrentHashMap<String,Integer> hashtable;

	public WordIDGenerator() {
		hashtable= new ConcurrentHashMap<String, Integer>();
	}
	
	public ConcurrentHashMap<String,Integer>  get_word_id_table(){
		return this.hashtable;
	}
	
	 

//	synchronized public static WordIDGenerator getInstance() {
//		if (instance == null) {
//			instance = new WordIDGenerator();
//			
//		}
//		return instance;
//	}
	
	public int get_wordnum(){
		return this.count;
	}
	
	public synchronized int get(String key){
		if (this.hashtable.containsKey(key)){
			Integer wordid=this.hashtable.get(key);
			return wordid.intValue();
		}
		else{
			return -1;
		}
	}
	
	public synchronized int  generate_woldid(String key){
		int value=++count;
		hashtable.put(key, Integer.valueOf(value));
		return value;	
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		Set<String> keys=this.hashtable.keySet();
		Iterator<String> iterator=keys.iterator();
		String lineSeparator = System.getProperty("line.separator");
		while(iterator.hasNext()){
			String temp=iterator.next();
			int item=this.hashtable.get(temp);
			result.append(temp+" "+item+lineSeparator);
		}
		
		return result.toString();
	} 

	
}
