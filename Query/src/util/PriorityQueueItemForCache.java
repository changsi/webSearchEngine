/**
 * 
 */
package util;

/**
 * @author changsi
 *
 */
public class PriorityQueueItemForCache implements Comparable<PriorityQueueItemForCache> {
	private String word;
	private int visited_times;
	
	public PriorityQueueItemForCache( String word, int visited_times){
		this.setWord(word);
		this.setVisited_times(visited_times);
	}
	
	public void add_visited_times(){
		this.visited_times++;
	}
	
	public int compareTo(PriorityQueueItemForCache item){
		if(this.visited_times>item.visited_times){
			return 1;
		}
		else{
			if(this.visited_times==item.visited_times){
				return 0;
			}
			else{
				return -1;
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param visited_times the visited_times to set
	 */
	public void setVisited_times(int visited_times) {
		this.visited_times = visited_times;
	}

	/**
	 * @return the visited_times
	 */
	public int getVisited_times() {
		return visited_times;
	}


	
}
