/**
 * 
 */
package util;

import java.util.ArrayList;

/**
 * @author changsi
 *
 */
public class InvertedIndexCompression {
	
	public static void compression(ArrayList <InvertedIndexItem> chunk){
		int previous;
		previous=chunk.get(0).getDocid();
		chunk.get(0).InvertedIndexCompression();
		for(int i=1;i<chunk.size();i++){
			InvertedIndexItem temp_item=chunk.get(i);
			int temp=temp_item.getDocid();
			temp_item.setDocid(temp_item.getDocid()-previous);
			temp_item.InvertedIndexCompression();
			previous=temp;
		}
	}
	
	public static void decompression(ArrayList <InvertedIndexItem> chunk){
		int previous=0;
		for(int i=0;i<chunk.size();i++){
			InvertedIndexItem temp_item=chunk.get(i);
			temp_item.setDocid(temp_item.getDocid()+previous);
			temp_item.InvertedIndexDecompression();
			previous=temp_item.getDocid();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
