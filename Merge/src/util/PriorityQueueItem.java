/**
 * 
 */
package util;

/**
 * @author changsi
 *
 */
public class PriorityQueueItem implements Comparable<PriorityQueueItem> {
	
	private int position;
	private Posting posting;
	
	public PriorityQueueItem( int position, Posting posting){
		this.position=position;
		this.posting=posting;
	}
	
	public int compareTo(PriorityQueueItem item){
		return this.posting.compareTo(item.posting);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}


	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}


	/**
	 * @param posting the posting to set
	 */
	public void setPosting(Posting posting) {
		this.posting = posting;
	}


	/**
	 * @return the posting
	 */
	public Posting getPosting() {
		return posting;
	}

}
