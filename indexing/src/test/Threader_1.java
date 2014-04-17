package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class Threader_1 {

//	public static void main(String[] args) {
//		try {
//			String index_file = "D:\\web search engine\\homework2\\nz2\\nz2_merged\\59_index";
//			String data_file = "D:\\web search engine\\homework2\\nz2\\nz2_merged\\59_data";
//			ArrayList<String> index;
//			String data;
//			ArrayList<String> pages;
//			unzip unzip = new unzip();
//			index = unzip.unzipIndex(index_file);
//			data = unzip.unzipData(data_file);
//			Split_pages split = new Split_pages();
//			pages = split.split(index, data);
//			index = null;
//			data = null;
//			split = null;
//			HTMLEditorKit.Parser parser;
//			parser = new ParserDelegator();
//			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
//					"lexicon.seri"));
//			Lexicon lexicon = (Lexicon) in.readObject();
//			in.close();
//			// Lexicon lexicon =new Lexicon();
//			// WordIDGenerator wordid_generator =new WordIDGenerator();
//			// DocidGenerator docid_generator =new DocidGenerator();
//			in = new ObjectInputStream(new FileInputStream(
//					"wordid_generator.seri"));
//			WordIDGenerator wordid_generator = (WordIDGenerator) in
//					.readObject();
//			in.close();
//
//			in = new ObjectInputStream(new FileInputStream(
//					"docid_generator.seri"));
//			DocidGenerator docid_generator = (DocidGenerator) in.readObject();
//			in.close();
//
//			HTMLParseLister listener = new HTMLParseLister(lexicon,
//					wordid_generator);
//			TreeSet<posting> posting_list = new TreeSet<posting>();
//			listener.set_posting_list(posting_list);
//			for (int i = 0; i < pages.size(); i++) {
//				StringReader r = new StringReader(pages.get(i));
//				int docid = docid_generator.generate_docid();
//				listener.set_docid(docid);
//				listener.resert_position();
//				System.out.println(pages.get(i));
//				System.out.println("---------------------------------------------------------------------------");
//				try {
//					parser.parse(r, listener, true);
//				} catch (Exception e) {
//					System.out.println(docid);
//					System.err.println("Error: " + e);
//					e.printStackTrace(System.err);
//					continue;
//				}
//			}
//
//			System.out.println(posting_list.size());
//			FileOutputStream fos = new FileOutputStream(
//					"D:\\workshop\\indexing\\postings_59");
//			OutputStreamWriter osw = new OutputStreamWriter(fos);
//			BufferedWriter bw = new BufferedWriter(osw);
//			Iterator<posting> i = posting_list.iterator();
//			while (i.hasNext()) {
//				bw.write(i.next().toString());
//				bw.newLine();
//			}
//			bw.close();
//
//			File lexicon_serial = new File("lexicon.seri");
//			FileOutputStream out = new FileOutputStream(lexicon_serial);
//			ObjectOutputStream oout = new ObjectOutputStream(out);
//			oout.writeObject(lexicon);
//
//			File docid_generator_serial = new File("docid_generator.seri");
//			out = new FileOutputStream(docid_generator_serial);
//			oout = new ObjectOutputStream(out);
//			oout.writeObject(docid_generator);
//
//			File wordid_generator_serial = new File("wordid_generator.seri");
//			out = new FileOutputStream(wordid_generator_serial);
//			oout = new ObjectOutputStream(out);
//			oout.writeObject(wordid_generator);
//			oout.close();
//		} catch (Exception e) {
//			System.err.println("Error: " + e);
//			e.printStackTrace(System.err);
//		}
//	}
}
