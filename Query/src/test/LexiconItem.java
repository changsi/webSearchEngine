package test;

import java.util.ArrayList;
import java.util.Map;

public class LexiconItem  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 75264711556147L;
	private String word;
	private int doc_num;
	private ArrayList<Map<String, Object>> pointer=null;
	
	public LexiconItem(String word){
		this.word=word;
		this.doc_num=1;
	}
	
	public void set_word(String word){
		this.word=word;
	}
	public String get_word(){
		return this.word;
	}
	
	public void set_doc_num(int doc_num){
		this.doc_num=doc_num;
	}
	public int get_doc_num(){
		return this.doc_num;
	}
	
	
	public void increase_doc_num(){
		this.doc_num++;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String str="";
		if(this.pointer !=null){
			StringBuffer temp= new StringBuffer();
			for(int i=0;i<this.pointer.size();i++){
				temp.append(" "+this.pointer.get(i).get("filename"));
				temp.append(" "+this.pointer.get(i).get("position").toString());
			}
			str=temp.toString();
		}
		result.append(this.word+" "+this.doc_num+str);
		return result.toString();
	}

	/**
	 * @param pointer the pointer to set
	 */
	public void setPointer(ArrayList<Map<String, Object>> pointer) {
		this.pointer = pointer;
	}

	/**
	 * @return the pointer
	 */
	public ArrayList<Map<String, Object>> getPointer() {
		return pointer;
	} 
	
}
