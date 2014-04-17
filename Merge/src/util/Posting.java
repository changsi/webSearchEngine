package util;

public class Posting implements Comparable<Posting> {
	private int word_id;
	private int doc_id;
	private int position;
	private char tag;
	private byte[] bytes;

	public Posting( int doc_id, int position, char tag) {
		this.doc_id = doc_id;
		this.position = position;
		this.tag = tag;
	}
	
	public byte tagToByte(){
		if (this.tag=='H') {
			return 0x01;
		}
		if (this.tag=='B') {
			return 0x02;
		}
		if (this.tag=='T') {
			return 0x03;
		}
		if (this.tag=='A') {
			return 0x04;
		}
		if (this.tag=='I') {
			return 0x05;
		}
		if (this.tag=='P') {
			return 0x06;
		}
		if (this.tag=='b') {
			return 0x07;
		}
		if (this.tag=='1') {
			return 0x08;
		}
		if (this.tag=='2') {
			return 0x09;
		}
		if (this.tag=='3') {
			return 0x0a;
		}
		if (this.tag=='4') {
			return 0x0b;
		}
		if (this.tag=='S') {
			return 0x0c;
		}
		if (this.tag=='5') {
			return 0x0d;
		}
		if (this.tag=='6') {
			return 0x0e;
		}
		else{
			return 0x00;
		}
	}
	
	

	public Posting() {

	}
	
	// convert word_id into 3 bytes
	// convert doc_id into 3 bytes
	// convert position and tag into 3 bytes(position 20 bits and tag 4 bits)
	public byte[] postingToByte(){
		this.bytes=new byte[9];
		this.bytes[0] = (byte) (0xff & this.word_id);
		this.bytes[1] = (byte) ((0xff00 & this.word_id) >> 8);
		this.bytes[2] = (byte) ((0xff0000 & this.word_id) >> 16);
		this.bytes[3] = (byte) (0xff & this.doc_id);
		this.bytes[4] = (byte) ((0xff00 & this.doc_id) >> 8);
		this.bytes[5] = (byte) ((0xff0000 & this.doc_id) >> 16);
		this.bytes[6] = (byte) ((0xff)&this.position);
		this.bytes[7] = (byte) ((0xff00 & this.position) >> 8);
		this.bytes[8] = (byte) ((0xf0000)>>16 & (0x0f & this.tagToByte())<<4);
		return this.bytes;
	}
	
	// convert position and tag into 3 bytes(position 20 bits and tag 4 bits)
	public byte[] partToByte(){
		this.bytes=new byte[3];
		this.bytes[0] = (byte) ((0xff)&this.position);
		this.bytes[1] = (byte) ((0xff00 & this.position) >> 8);
		this.bytes[2] = (byte) ((0xf0000)>>16 & (0x0f & this.tagToByte())<<4);
		return this.bytes;
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
	
	public int compareTo(Posting posting){
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

