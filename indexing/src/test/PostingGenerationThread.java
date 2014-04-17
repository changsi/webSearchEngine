/**
 * 
 */
package test;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * @author changsi
 * 
 */
public class PostingGenerationThread implements Runnable {

	private String filename;
	private Lexicon lexicon;
	private WordIDGenerator wordid_generator;
	private DocidGenerator docid_generator;
	private URLTable urltable;

	public PostingGenerationThread(String filename, URLTable urltable,
			Lexicon lexicon, WordIDGenerator wordid_generator,
			DocidGenerator docid_generator) {
		this.filename = filename;
		this.urltable = urltable;
		this.lexicon = lexicon;
		this.wordid_generator = wordid_generator;
		this.docid_generator = docid_generator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String index_file = "d:\\web search engine\\homework3\\NZ\\data\\"
					+ this.filename + "_index";
			String data_file = "d:\\web search engine\\homework3\\NZ\\data\\"
					+ this.filename + "_data";
			ArrayList<String> index;
			String data;
			ArrayList<String> pages;
			ArrayList<String> URLs;
			Map<String, ArrayList<String>> result;
			
			int begin = (int) System.currentTimeMillis() / 1000;
            // unzip index file and data file
			unzip unzip = new unzip();
			index = unzip.unzipIndex(index_file);
			data = unzip.unzipData(data_file);
			unzip=null;
			System.gc();
			
			int end = (int) System.currentTimeMillis() / 1000;
			int timetaken = end - begin;
			System.out.println("unzip "+this.filename+" time taken: " + timetaken + " seconds");
			System.out.println("unzip "+this.filename+" time taken: " + timetaken / 60
					+ " minutes and " + timetaken % 60 + " seconds");
			
			begin = (int) System.currentTimeMillis() / 1000;
			// split data files into HTML pages
			Split_pages split = new Split_pages();
			result = split.split(index, data);
			index = null;
			data = null;
			split=null;
			System.gc();
			
			end = (int) System.currentTimeMillis() / 1000;
			timetaken = end - begin;
			System.out.println("split pages "+this.filename+" time taken: " + timetaken + " seconds");
			System.out.println("split pages "+this.filename+" time taken: " + timetaken / 60
					+ " minutes and " + timetaken % 60 + " seconds");
			
			URLs = result.get("0");
			pages = result.get("1");
			result=null;
			// parse HTML pages
			ArrayList<Integer> sizeTable = new ArrayList<Integer>();
			HTMLEditorKit.Parser parser;
			parser = new ParserDelegator();
			HTMLParseLister listener = new HTMLParseLister(lexicon,
					wordid_generator);
			TreeSet<posting> posting_list = new TreeSet<posting>();
			listener.set_posting_list(posting_list);
			listener.set_sizeTable(sizeTable);
			
			begin = (int) System.currentTimeMillis() / 1000;
			
			for (int i = 0; i < pages.size(); i++) {
				String temp = pages.get(i);
				StringReader r = new StringReader(temp);
				int docid = docid_generator.generate_docid();
				listener.set_docid(docid);
				try {
					parser.parse(r, listener, true);
					listener.resert_position();
					this.urltable.put(docid,new URLTableItem(docid, URLs.get(i), sizeTable.get(i).intValue()));
				} catch (Exception e) {
					continue;
				}
			}
			URLs=null;
			pages=null;
			parser=null;
			sizeTable=null;
			listener=null;
			System.gc();
			
			end = (int) System.currentTimeMillis() / 1000;
			timetaken = end - begin;
			System.out.println("parse pages "+this.filename+" time taken: " + timetaken + " seconds");
			System.out.println("parse pages "+this.filename+" time taken: " + timetaken / 60
					+ " minutes and " + timetaken % 60 + " seconds");
			
			// output postings into files
			FileOutputStream fos = new FileOutputStream(
					"d:\\workshop\\indexing\\postings_" + this.filename);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			Iterator<posting> i = posting_list.iterator();
			int previous_docid=-1;
			int previous_wordid=-1;
			boolean first=true;
			while (i.hasNext()) {
				posting temp= i.next();
				if (temp.get_word_id()!=previous_wordid){
					previous_wordid=temp.get_word_id();
					previous_docid= temp.get_doc_id();
					first=true;
				}
				if (temp.get_doc_id()!=previous_docid || first){
					previous_docid= temp.get_doc_id();
					this.lexicon.increase_doc_num(temp.word);
				}
				else{
					
				}
				bw.write(temp.toString());
				bw.newLine();
			}
			bw.close();

		} catch (Exception e) {
			System.err.println("Error: " + e);
			e.printStackTrace(System.err);
		}
	}

}
