/**
 * 
 */
package util;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author changsi
 *
 */
public class IDAndWordTable {
	
	private ConcurrentHashMap<Integer,String> hashtable;
	
	public IDAndWordTable(ConcurrentHashMap<String,Integer> hashtable){
		this.hashtable=new  ConcurrentHashMap<Integer,String>();
		Set<String> keys=hashtable.keySet();
		Iterator<String> iterator=keys.iterator();
		while(iterator.hasNext()){
			String word=iterator.next();
			int id=hashtable.get(word);
			this.hashtable.put(id, word);
		}
		hashtable=null;
	}
	
	public String get_word(int id){
		return this.hashtable.get(id);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param hashtable the hashtable to set
	 */
	public void setHashtable(ConcurrentHashMap<Integer,String> hashtable) {
		this.hashtable = hashtable;
	}

	/**
	 * @return the hashtable
	 */
	public ConcurrentHashMap<Integer,String> getHashtable() {
		return hashtable;
	}

}
