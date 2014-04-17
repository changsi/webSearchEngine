package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class StopWords {
	private ArrayList<String> stopwordslist = new ArrayList<String>();

	public StopWords() {
		try {
			FileReader fr = new FileReader("./stopwords.txt");
			BufferedReader br = new BufferedReader(fr);
			String s = br.readLine();
			while (s != null) {
				stopwordslist.add(s.trim());
				s = br.readLine();
			}
			fr.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Vector<String> removeStopWords(Vector<String> src) {
		Vector<String> temp = new Vector<String>();
		for (int i = 0; i < src.size(); i++) {
			if (!stopwordslist.contains(src.get(i)))
				temp.add(src.get(i));
		}
		return temp;
	}

	public ArrayList<String> getStopWordsList() {
		StopWords sw = new StopWords();
		return sw.stopwordslist;
	}
}
