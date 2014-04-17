package test;

public class posting implements Comparable<posting> {
	private int word_id;
	public String word;
	private int doc_id;
	private int position;
	private char tag;

	public posting( int doc_id, int position, char tag) {
		this.doc_id = doc_id;
		this.position = position;
		this.tag = tag;
	}

	public posting() {

	}
	
	public void set_word(String word){
		this.word=word;
	}

	public void set_word_id(int word_id) {
		this.word_id = word_id;
	}

	public int get_word_id() {
		return this.word_id;
	}

	public void set_doc_id(int doc_id) {
		this.doc_id = doc_id;
	}

	public int get_doc_id() {
		return this.doc_id;
	}

	public void set_position(int position) {
		this.position = position;
	}

	public int get_position() {
		return this.position;
	}

	public void set_tag(char tag) {
		this.tag = tag;
	}

	public char get_tag() {
		return this.tag;
	}
	
	public int compareTo(posting posting){
		if(this.word_id>posting.word_id){
			return 1;
		}
		else if(this.word_id<posting.word_id){
			return -1;
		}
		else if(this.doc_id>posting.doc_id){
			return 1;
		}
		else if(this.doc_id<posting.doc_id){
			return -1;
		}
		else if(this.position>posting.position){
			return 1;
		}
		else if(this.position<posting.position){
			return -1;
		}
		else{
			return 0;
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.word_id+","+this.doc_id+","+this.position+","+this.tag);
		return result.toString();
	}

}
