/**
 * 
 */
package rank;

import java.util.Vector;

/**
 * @author root
 *
 */
public interface RankFunction {
	public double compute(Vector<String> query_terms, int docid);
}
