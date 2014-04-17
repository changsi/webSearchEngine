package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

public class unzip {

	public  ArrayList<String> unzipIndex(String filename) {
		// String a="D:\\web search engine\\homework2\\nz2\\nz2_merged\\1_data";
		ArrayList<String> result = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new GZIPInputStream(new FileInputStream(filename))));
			String s;
			while ((s = in.readLine()) != null){
				result.add(s);
			}
			in.close();
			in=null;
		} catch (IOException e) {
		}
		return result;
	}
	
	public  String unzipData(String filename) {
		// String a="D:\\web search engine\\homework2\\nz2\\nz2_merged\\1_data";
		String content=null;
		try {
			GZIPInputStream in = new GZIPInputStream(new FileInputStream(filename));
			int c;
			StringBuffer strb = new StringBuffer();
			while((c=in.read())>-1)
			{
			   strb.append((char)c);
			}
			content = strb.toString();
			in.close();
			in=null;
		} catch (IOException e) {
		}
		return content;
	}
	
	

	public static void main(String[] args) {
		String a="D:\\web search engine\\homework2\\nz2\\nz2_merged\\59_index";
		String result;
		unzip unzip=new unzip();
		result=unzip.unzipData(a);
		try{
			File f = new File("59_index");
			FileOutputStream out = new FileOutputStream(f);
			out.write(result.toString().getBytes());
		}
		catch(IOException e){
		}
		System.out.print(result);
		System.out.print("______________");
	}

}
