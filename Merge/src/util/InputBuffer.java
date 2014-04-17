/**
 * 
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * @author changsi
 * 
 */
public class InputBuffer {

	/**
	 * maximum size of the inputbuffer
	 */
	private int maximum_size;
	/**
	 * 
	 */
	private int real_size;
	private String filename;
	private int pointer;
	private byte[] buffer;
	private byte[] tail;
	private int tail_num;
	private FileInputStream r;

	public InputBuffer(int size, String filename) {
		this.maximum_size = size;
		this.filename = filename;
		this.pointer = 0;
		this.real_size = 0;
		this.tail=new byte[40];
		this.tail_num=0;
		try {
			r = new FileInputStream(new File(this.filename));
		} catch (Exception e) {
			System.err.println("Error: " + e);
			e.printStackTrace(System.err);
		}

	}

	public int FillBuffer() {
		this.real_size = -1;
		this.pointer = 0;
		try {
			this.buffer = new byte[maximum_size];
			if(this.tail_num!=0){
				int j=0;
				for(int i=this.tail_num-1;i>=0;i--){
					this.buffer[j]=this.tail[i];
					j++;
				}
			}
			this.real_size = r.read(buffer, this.tail_num, this.maximum_size-this.tail_num);
			this.real_size=this.real_size+this.tail_num;
			this.tail=new byte[30];
			this.tail_num=0;
			// align postings
			if (this.real_size >= this.maximum_size) {
				while (true) {
					if (this.buffer[this.real_size - 1] == 10) {
						break;
					}
					this.tail[this.tail_num]=this.buffer[this.real_size-1];
					this.tail_num++;
					this.real_size--;
				}
			}
			if (this.real_size != -1) {

			}
		} catch (Exception e) {
			System.err.println("Error: " + e);
			e.printStackTrace(System.err);
		}
		return this.real_size;
	}

	public ArrayList<Posting> byteToPostingArray() {
		Posting temp = new Posting();
		ArrayList<Posting> posting_list = new ArrayList<Posting>();
		int wordid;
		int docid;
		int position;
		char tag;
		int type = 0;
		int i = 0;
		StringBuffer element = new StringBuffer();
		while (i < this.real_size) {
			if (this.buffer[i] != 44) {
				if (this.buffer[i] == 10 && i!=0) {
					posting_list.add(temp);
					temp = new Posting();
					type = 0;
					i++;
				} else {
					element.append(Character.toString((char) this.buffer[i]));
					i++;
				}

			} else {
				if (type == 0) {
					wordid = Integer.parseInt(element.toString());
					temp.set_word_id(wordid);
				} else {
					if (type == 1) {
						docid = Integer.parseInt(element.toString());
						temp.set_doc_id(docid);
					} else {
						if (type == 2) {
							position = Integer.parseInt(element.toString());
							temp.set_position(position);
						} else {
							if (type == 3) {
								tag = element.charAt(0);
								temp.set_tag(tag);
							}
						}
					}
				}
				type++;
				i++;
				element = new StringBuffer();
			}

		}
		return posting_list;

	}

	public Posting getNext() {
		Posting posting = new Posting();
		int wordid;
		int docid;
		int position;
		char tag;
		int type = 0;
		StringBuffer element;
		element = new StringBuffer();
		if (this.real_size == this.pointer) {
			this.buffer=null;
			if (FillBuffer() == -1) {
				return null;
			}
		}
		while (this.pointer < this.real_size) {
			if (this.buffer[this.pointer] != 44) {
				if (this.buffer[this.pointer] == 10&& this.pointer!=0) {
					this.pointer++;
					if (element.toString().length() >= 1) {
						tag = element.toString().charAt(0);
						posting.set_tag(tag);
					}
					return posting;
				} else {
					element.append(Character
							.toString((char) this.buffer[this.pointer]));
					this.pointer++;
				}

			} else {
				if (type == 0) {
					wordid = Integer.parseInt(element.toString().trim());
					posting.set_word_id(wordid);
				} else {
					if (type == 1) {
						docid = Integer.parseInt(element.toString().trim());
						posting.set_doc_id(docid);
					} else {
						if (type == 2) {
							position = Integer.parseInt(element.toString().trim());
							posting.set_position(position);
						} else {
						}
					}
				}
				type++;
				this.pointer++;
				element = new StringBuffer();
			}

		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filename = "D:\\workshop\\indexing\\postings_";
		for (int i = 0; i < 10; i++) {

			InputBuffer input = new InputBuffer(200000, filename
					+ String.valueOf(i));
			try {
				int count=1;
				int begin=(int) System.currentTimeMillis()/1000;
				Posting temp = input.getNext();
				while (temp != null) {
					try {
						temp = input.getNext();
						count++;
					} catch (Exception e) {
						System.out.println("-----------page:" + i
								+ "-----------");
						System.out.println(temp);
						System.err.println("Error: " + e);
						e.printStackTrace(System.err);
						throw new Exception();
					}
				}
				System.out.println("file: "+i+" has "+count+" postings!");
				int mid=(int) System.currentTimeMillis()/1000;
				int timetaken=mid-begin;
				System.out.println("processing page "+i+" time taken: "+timetaken+" seconds");
				System.out.println("processing page "+i+" time taken: "+timetaken/60+" minutes and "+timetaken%60+" seconds");
				System.out.println("---------------------------------------------------------------------");
			} catch (Exception e) {
				System.out.println("-----------page:" + i + "-----------");
				System.out.println();
				System.err.println("Error: " + e);
				e.printStackTrace(System.err);
				break;
			}

		}
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.maximum_size = size;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return this.maximum_size;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * @param pointer
	 *            the pointer to set
	 */
	public void setPointer(int pointer) {
		this.pointer = pointer;
	}

	/**
	 * @return the pointer
	 */
	public int getPointer() {
		return this.pointer;
	}

}
