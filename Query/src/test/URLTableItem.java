package test;

public class URLTableItem implements java.io.Serializable{
	private static final long serialVersionUID = -702061947759468L;
	private int docid;
	private String URL;
	private int size;
	
	public URLTableItem(int docid, String URL, int size){
		this.docid=docid;
		this.URL=URL;
		this.size=size;
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
	 * @param uRL the uRL to set
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}



	/**
	 * @return the uRL
	 */
	public String getURL() {
		return URL;
	}



	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}



	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.docid+" "+this.URL+" "+this.size);
		return result.toString();
	} 

}
