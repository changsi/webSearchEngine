/**
 * 
 */
package util;

import java.util.ArrayList;

/**
 * @author changsi
 *
 */
public class InvertedIndexItem {
	private int docid;
	private int frequency;
	private ArrayList <Integer>position;
	private ArrayList <Byte> tag;
	
	public InvertedIndexItem(){
		this.position=new ArrayList<Integer>();
		this.tag=new ArrayList<Byte>();
	}
	
	public InvertedIndexItem(int docid, int frequency){
		this.docid=docid;
		this.frequency=frequency;
		this.position=new ArrayList<Integer>();
		this.tag=new ArrayList<Byte>();
	}
	
	public void InvertedIndexCompression(){
		int previous=this.position.get(0);
		for(int i=1;i<this.position.size();i++){
			int temp=this.position.get(i);
			this.position.set(i, this.position.get(i)-previous);
			previous=temp;
		}
	}
	
	public void InvertedIndexDecompression(){
		int previous=0;
		for(int i=0;i<this.position.size();i++){
			this.position.set(i, this.position.get(i)+previous);
			previous=this.position.get(i);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * @return the position
	 */
	public ArrayList<Integer> getPosition() {
		return position;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	/**
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
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
	
	public void addTag(byte tag){
		this.tag.add(tag);
	}

	public void addPosition(int position){
		this.position.add(position);
	}

	/**
	 * @return the tag
	 */
	public ArrayList<Byte> getTag() {
		return tag;
	}

}
