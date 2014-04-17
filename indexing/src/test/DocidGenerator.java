package test;


public class DocidGenerator implements java.io.Serializable{
	/**
	 */
	private static final long serialVersionUID = -7020619477594468968L;
	private int count=0;
//	private static DocidGenerator instance;

	public DocidGenerator() {
		
	}
	
	public int return_count(){
		return count;
	}

//	synchronized public static DocidGenerator getInstance() {
//		if (instance == null) {
//			instance = new DocidGenerator();
//		}
//		return instance;
//	}
	
	public synchronized int  generate_docid(){
		return ++count;
	}
	
}
