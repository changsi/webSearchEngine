/**
 * 
 */
package util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author changsi
 * 
 */
public class OutputBuffer {
	private int maximum_size;                          // the maximum size of 
	private byte[] buffer;                             // the output buffer
	private String filename;                           // record the current inverted index file name
	private int position;                              // record the current position 
	private int begining;                              // record the beginning position of each word
	private FileOutputStream output_file = null;       
	private ArrayList<Map<String, Object>> word_index; // record index information for each word, including file name and its beginning position
	private int count;                                 // file name counter
	private boolean first = true;

	public OutputBuffer(int maximum_size, String filename) {
		this.maximum_size = maximum_size;
		this.filename = filename;
		this.count = 0;
	}

	public void initialization() {
		this.buffer = new byte[this.maximum_size];
		this.position = 0;
		this.begining = 0;
	}

	public static byte[] intToByte(int i) {
		byte[] bt = new byte[4];
		bt[0] = (byte) (0xff & i);
		bt[1] = (byte) ((0xff00 & i) >> 8);
		bt[2] = (byte) ((0xff0000 & i) >> 16);
		bt[3] = (byte) ((0xff000000 & i) >> 24);
		return bt;
	}

	public static int bytesToInt(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		num |= ((bytes[1] << 8) & 0xFF00);
		num |= ((bytes[2] << 16) & 0xFF0000);
		num |= ((bytes[3] << 24) & 0xFF000000);
		return num;
	}
	
	public int get_position(){
		return this.position;
	}
	
	public void flush() {
		try {
			this.output_file = new FileOutputStream(new File(filename
					+ String.valueOf(count)));
			Map<String, Object> file_position_pairs = new HashMap<String, Object>();
			file_position_pairs.put("filename",
					filename + String.valueOf(count));
			file_position_pairs.put("position", this.begining);
			this.word_index.add(file_position_pairs);
			byte[] lenth = OutputBuffer.intToByte(this.position
					- (this.begining + 4));
			int temp = this.begining;
			for (int i = 0; i < 4; i++) {
				this.buffer[temp] = lenth[i];
				temp++;
			}
			this.output_file.write(this.buffer, 0, this.position);
		} catch (Exception e) {
			System.err.println("Error: " + e);
			e.printStackTrace(System.err);
		}
	}

	
	/**
	 * add bytes into output buffer, sameword to indicate that whether this chunk is belong to previous word
	 * 
	 * @param posting
	 * @param sameword
	 * @return if sameword is true , return null
	 *         else return inverted index name and the beginning position
	 *         notice that the inverted index for a single word maybe spread into two or more files.
	 */
	public ArrayList<Map<String, Object>> insertBuffer(Map<String, ArrayList<Byte>> chunk,
			boolean sameword, int size) {
		ArrayList<Map<String, Object>> temp_index = null;
		// the output buffer is full
		if (size + this.position >= this.maximum_size) {
			try {
				this.output_file = new FileOutputStream(new File(filename+ String.valueOf(count)));
				if (sameword) {
					Map<String, Object> file_position_pairs = new HashMap<String, Object>();
					file_position_pairs.put("filename",filename + String.valueOf(count));
					file_position_pairs.put("position", this.begining);
					this.word_index.add(file_position_pairs);
					byte[] lenth = OutputBuffer.intToByte(this.position- (this.begining + 4));
					int temp = this.begining;
					for (int i = 0; i < 4; i++) {
						this.buffer[temp] = lenth[i];
						temp++;
					}
				}
				this.count++;
				this.output_file.write(this.buffer, 0, this.position);
				System.out.println(count);
				this.initialization();
			} catch (Exception e) {
				System.err.println("Error: " + e);
				e.printStackTrace(System.err);
			}
		}
		String key_set[]={"metadata","docid","frequency","position","tag"};
		int k=0;
		while(k<5){
			ArrayList<Byte> bytes_list=chunk.get(key_set[k]);
			for (int j = 0; j < bytes_list.size(); j++) {
				this.buffer[this.position] = bytes_list.get(j).byteValue();
				this.position++;
			}
			k++;
		}
		// this chunk belong to the same word as previous one
		if (sameword) {
		} else {
		// this chunk belong to a new word
			if (this.first) {
				this.first = false;
				this.word_index = new ArrayList<Map<String, Object>>();
				temp_index = null;
			} else {
				Map<String, Object> file_position_pairs = new HashMap<String, Object>();
				file_position_pairs.put("filename",filename + String.valueOf(count));
				file_position_pairs.put("position", this.begining);
				this.word_index.add(file_position_pairs);
				byte[] lenth = OutputBuffer.intToByte(this.position- (this.begining + 4));
				int temp = this.begining;
				for (int i = 0; i < 4; i++) {
					this.buffer[temp] = lenth[i];
					temp++;
				}
			}
		}
		if(!sameword){
			if(!first){
				temp_index = this.word_index;
				this.word_index = new ArrayList<Map<String, Object>>();
			}
			this.begining = this.position;
			for (int i = 0; i < 4; i++) {
				this.buffer[this.position] = 0;
				this.position++;
			}
		}
		return temp_index;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
