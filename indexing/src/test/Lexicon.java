package test;


import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Lexicon implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7526471155622776147L;
	private ConcurrentHashMap<String,LexiconItem> hashtable;
//	private static Lexicon lexicon = null;
	
	public int get_wordnum(){
		return this.hashtable.size();
	}

	public Lexicon() {
		hashtable = new ConcurrentHashMap<String, LexiconItem>();
	}

//	synchronized public static Lexicon getInstance() {
//		if (lexicon == null) {
//			lexicon = new Lexicon();
//		}
//		return lexicon;
//	}
	
	public LexiconItem get(String key){
		return this.hashtable.get(key);
	}
	
	public LexiconItem put(String key, LexiconItem value){
		return this.hashtable.putIfAbsent(key, value);
	}
	
	public boolean contains_key(String key){
		return this.hashtable.containsKey(key);
	}
	
	public synchronized boolean increase_doc_num(String key){
		if (this.hashtable.containsKey(key)){
			LexiconItem item=this.hashtable.get(key);
			item.increase_doc_num();
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		Set<String> keys=this.hashtable.keySet();
		Iterator<String> iterator=keys.iterator();
		String lineSeparator = (String) java.security.AccessController.doPrivileged(
	            new sun.security.action.GetPropertyAction("line.separator"));
		while(iterator.hasNext()){
			LexiconItem item=this.hashtable.get(iterator.next());
			result.append(item.toString()+lineSeparator);
		}
		
		return result.toString();
	} 

}
