/**
 * 
 */
package util;

/**
 * @author root
 *
 */
public class ResultItem implements Comparable<ResultItem>{
	private int docid;
	private double score;
	private String url;
	
	public ResultItem(){
		
	}
	
	public ResultItem( int docid, double score, String url){
		this.setDocid(docid);
		this.setScore(score);
		this.setUrl(url);
	}
	
	public int compareTo(ResultItem item){
		if(this.score<item.score){
			return 1;
		}
		else{
			if(this.score>item.score){
				return -1;
			}
			else{
				return 0;
			}
		}
	}

	/**
	 * @param docid the docid to set
	 */
	public void setDocid(int docid) {
		this.docid = docid;
	}

	/**
	 * @return the docid
	 */
	public int getDocid() {
		return docid;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
}
