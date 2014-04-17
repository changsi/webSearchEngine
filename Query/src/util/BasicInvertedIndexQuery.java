/**
 * 
 */
package util;

/**
 * @author changsi
 *
 */
public interface BasicInvertedIndexQuery {
	public byte[][] openList(String t);
	public void closeList();
	/**
	 * get next smallest greater or equal docid.
	 * else return -1
	 * 
	 * @param k
	 * @return
	 */
	public int nextGEQ( int k);
	/**
	 * get the frequency for current docid
	 * 
	 * @return
	 */
	public int getFreq( );
}
