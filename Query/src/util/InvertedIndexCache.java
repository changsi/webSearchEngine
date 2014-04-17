/**
 * 
 */
package util;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author changsi
 *
 */
public class InvertedIndexCache {
	
	/**
	 * store mappings of word and its cache items(stored in a hash_map)
	 * cache item includes "bytes" index byte array, "queueitem" priorityqueue item, "size" index bytes size.
	 */
	private ConcurrentHashMap<String,HashMap<String,Object>> hashtable;
	private PriorityQueue<PriorityQueueItemForCache> queue; 
	private int cache_size;
	private int maximum_cache_size;
	
	public InvertedIndexCache(){
		this.cache_size=0;
		this.hashtable=new ConcurrentHashMap<String,HashMap<String,Object>>();
		this.queue= new  PriorityQueue<PriorityQueueItemForCache> ();
		this.maximum_cache_size=2000000000;
	}
	
	public InvertedIndexCache(int maximum_cache_size){
		this.maximum_cache_size=maximum_cache_size;
		this.cache_size=0;
		this.hashtable=new ConcurrentHashMap<String,HashMap<String,Object>>();
		this.queue= new  PriorityQueue<PriorityQueueItemForCache> ();
	}
	
	
	public byte[][] get(String word){
		HashMap<String,Object> temp =this.hashtable.get(word);
		PriorityQueueItemForCache temp_item=(PriorityQueueItemForCache) temp.get("queueitem");
		this.queue.remove(temp_item);
		temp_item.add_visited_times();
		this.queue.add(temp_item);
		return (byte[][]) temp.get("bytes");
	}
	
	/**
	 * put a new word index into cache 
	 * if the size of cache is full, remove the most unvisited index recently from cache until the empty space is enough
	 * 
	 * @param word
	 * @param value
	 * @param size
	 * @return
	 */
	public HashMap<String,Object> put(String word, byte[][] value, int size){
		while(this.cache_size+size>this.maximum_cache_size){
			PriorityQueueItemForCache temp_item=this.queue.remove();
			this.cache_size=this.cache_size-(Integer)(this.hashtable.get(temp_item.getWord()).get("size"));
			this.hashtable.remove(temp_item.getWord());
			temp_item=null;
		}
		HashMap<String,Object> temp = new HashMap<String,Object>();
		PriorityQueueItemForCache temp_item= new PriorityQueueItemForCache(word, 0);
		this.queue.add(temp_item);
		temp.put("queueitem", temp_item);
		temp.put("bytes", value);
		temp.put("size", size);
		this.cache_size=this.cache_size+size;
		return this.hashtable.putIfAbsent(word, temp);
	}
	
	public boolean contains_key(String word){
		return this.hashtable.containsKey(word);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param maximum_cache_size the maximum_cache_size to set
	 */
	public void setMaximum_cache_size(int maximum_cache_size) {
		this.maximum_cache_size = maximum_cache_size;
	}

	/**
	 * @return the maximum_cache_size
	 */
	public int getMaximum_cache_size() {
		return maximum_cache_size;
	}

}
