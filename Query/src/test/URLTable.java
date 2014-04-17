/**
 * 
 */
package test;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author changsi
 * 
 */
public class URLTable implements java.io.Serializable {

	private static final long serialVersionUID = 75264711556226547L;
	private ConcurrentHashMap<Integer, URLTableItem> hashtable;

	public URLTable() {
		hashtable = new ConcurrentHashMap<Integer, URLTableItem>();
	}

	public URLTableItem get(Integer key) {
		return this.hashtable.get(key);
	}

	public URLTableItem put(Integer key, URLTableItem value) {
		return this.hashtable.putIfAbsent(key, value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		Set<Integer> keys = this.hashtable.keySet();
		Iterator<Integer> iterator = keys.iterator();
		String lineSeparator = System.getProperty("line.separator");
		while (iterator.hasNext()) {
			URLTableItem item = this.hashtable.get(iterator.next());
			result.append(item.toString() + lineSeparator);
		}
		return result.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int begin = (int) System.currentTimeMillis() / 1000;
		try {

			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					"D:\\workshop\\indexing\\url_table.seri"));
			URLTable urltable = (URLTable) in.readObject();
			urltable.get(1);
			in.close();
			int mid=(int) System.currentTimeMillis()/1000;
			int timetaken=mid-begin;
			System.out.println("time taken: "+timetaken+" seconds");
			System.out.println("time taken: "+timetaken/60+" minutes and "+timetaken%60+" seconds");
		} catch (Exception e) {
			System.err.println("Error: " + e);
			e.printStackTrace(System.err);
		}
	}

}
