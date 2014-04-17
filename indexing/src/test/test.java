package test;

//import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Map;
//import java.io.ObjectInputStream;

public class test {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
//		ObjectInputStream in = new ObjectInputStream(new FileInputStream("wordid_generator.seri"));
//		WordIDGenerator wordid_generator = (WordIDGenerator) in.readObject();
//		System.out.println(wordid_generator);
//		in.close();
		
		//print lexicon into file
//		ObjectInputStream in = new ObjectInputStream(new FileInputStream("lexicon.seri"));
//		Lexicon lexicon = (Lexicon) in.readObject();
//		File f = new File("lexicon");
//		BufferedWriter  out = new BufferedWriter(new FileWriter(f));
//		out.write(lexicon.toString());
//		System.out.println(lexicon.get_wordnum());
//		in.close();
//		out.close();
//		
		//print wordid_generator
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(
				"url_table.seri"));
		URLTable url_table = (URLTable) in.readObject();
		Map<Integer, URLTableItem> table=url_table.get_hashtable();
		Iterator<Integer> iterator=table.keySet().iterator();
		long length=0;
		int count=0;
		while(iterator.hasNext()){
			int key=iterator.next();
			
			if(table.get(key).getSize()>10&& table.get(key).getURL()!=null&& table.get(key).getURL()!=""){
				length=length+table.get(key).getSize();
				count++;
			}
		}
		long ave= length/count;
		System.out.println(ave);
		in.close();
		
		
	
	}

}
