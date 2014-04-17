package test;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

public class MyHtmlParser {

	public static void main(String[] args) {
		try {

			HTMLEditorKit.Parser parser;
			parser = new ParserDelegator();
			Lexicon lexicon = new Lexicon();
			WordIDGenerator wordid_generator =new WordIDGenerator();
			HTMLParseLister listener = new HTMLParseLister(lexicon,
					wordid_generator);
			InputStreamReader r = new InputStreamReader(new FileInputStream(
					"D:\\workshop\\indexing\\1.htm"), "UTF-8");
			TreeSet<posting> posting_list = new TreeSet<posting>();
			listener.set_posting_list(posting_list);
			listener.set_docid(12);
			listener.resert_position();
			parser.parse(r, listener, true);
			FileOutputStream fos = new FileOutputStream("D:\\workshop\\indexing\\1");
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			Iterator<posting> i = posting_list.iterator();
	        while (i.hasNext()) {
	        	bw.write(i.next().toString());
				bw.newLine();
	        }
			
			bw.close();

			System.out
					.println("-------------------lexicon---------------------");
			System.out.println(lexicon);
			System.out
					.println("-------------------word and wordid---------------------");
			System.out.println(wordid_generator);
			r.close();
		} catch (Exception e) {
			System.err.println("Error: " + e);
			e.printStackTrace(System.err);
		}
	}
}

/**
 * HTML parsing proceeds by calling a callback for each and every piece of the
 * HTML document. This simple callback class simply prints an indented
 * structural listing of the HTML data.
 */
class HTMLParseLister extends HTMLEditorKit.ParserCallback {
	/** contents of last text element */
	private String lastText = null;
	private ArrayList<Character> tag = new ArrayList<Character>();
	private Lexicon lexicon;
	private WordIDGenerator wordid_generator;
	private TreeSet<posting> posting_list;
	private int docid;
	private int position = 0;
	private ArrayList<Integer> sizeTable;
	private TokenizerNew tokenizer = new TokenizerNew();
	private StopWords sw = new StopWords();

	/**
	 * Creates a new instance of SpiderParserCallback
	 * 
	 * @param atreenode
	 *            search tree node that is being parsed
	 */
	public HTMLParseLister() {

	}

	public HTMLParseLister(Lexicon lexicon, WordIDGenerator wordid_generator) {
		this.lexicon = lexicon;
		this.wordid_generator = wordid_generator;
	}

	public HTMLParseLister(Lexicon lexicon, WordIDGenerator wordid_generator,
			TreeSet<posting> posting_list, ArrayList<Integer> sizeTable) {
		this.lexicon = lexicon;
		this.wordid_generator = wordid_generator;
		this.posting_list = posting_list;
		this.position=0;
	}

	public void set_lexicon(Lexicon lexicon) {
		this.lexicon = lexicon;
	}

	public void set_wordid_generator(WordIDGenerator wordid_generator) {
		this.wordid_generator = wordid_generator;
	}
	
	public void set_sizeTable(ArrayList<Integer> sizeTable){
		this.sizeTable=sizeTable;
	}

	public void set_posting_list(TreeSet<posting> posting_list) {
		this.posting_list = posting_list;
	}

	public TreeSet<posting> get_posting_list() {
		return this.posting_list;
	}

	public void set_docid(int docid) {
		this.docid = docid;
	}

	public void resert_position() {
		this.sizeTable.add( new Integer(this.position));
		this.position = 0;
	}

	/**
	 * take care of start tags
	 * 
	 * @param t
	 *            HTML tag
	 * @param a
	 *            HTML attributes
	 * @param pos
	 *            Position within file
	 */
	public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
		if (t.equals(HTML.Tag.HEAD)) {
			tag.add('H');
			lastText = null;
			return;
		}
		if (t.equals(HTML.Tag.BODY)) {
			tag.add('B');
			lastText = null;
			return;
		}
		if (t.equals(HTML.Tag.TITLE)) {
			tag.add('T');
			lastText = null;
			return;
		}
		if (t.equals(HTML.Tag.A)) {
			tag.add('A');
			lastText = null;
			return;
		}
		if (t.equals(HTML.Tag.I)) {
			tag.add('I');
			lastText = null;
			return;
		}
		if (t.equals(HTML.Tag.P)) {
			tag.add('P');
			lastText = null;
			return;
		}
		if (t.equals(HTML.Tag.B)) {
			tag.add('b');
			lastText = null;
			return;
		}
		if (t.equals(HTML.Tag.H1)) {
			tag.add('1');
			lastText = null;
			return;
		}
		if (t.equals(HTML.Tag.H2)) {
			tag.add('2');
			lastText = null;
			return;
		}
		if (t.equals(HTML.Tag.H3)) {
			tag.add('3');
			lastText = null;
			return;
		}

		if (t.equals(HTML.Tag.H4)) {
			tag.add('4');
			lastText = null;
			return;
		}
		if (t.equals(HTML.Tag.STYLE) || t.equals(HTML.Tag.SCRIPT)) {
			tag.add('S');
			lastText = null;
			return;
		}
		if (t.equals(HTML.Tag.H5)) {
			tag.add('5');
			lastText = null;
			return;
		}
		if (t.equals(HTML.Tag.H6)) {
			tag.add('6');
			lastText = null;
			return;
		}
	}

	/**
	 * take care of start tags
	 * 
	 * @param t
	 *            HTML tag
	 * @param pos
	 *            Position within file
	 */
	public void handleEndTag(HTML.Tag t, int pos) {
		if (t.equals(HTML.Tag.HEAD) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
		if (t.equals(HTML.Tag.BODY) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
		if (t.equals(HTML.Tag.TITLE) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
		if (t.equals(HTML.Tag.A) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
		if (t.equals(HTML.Tag.I) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
		if (t.equals(HTML.Tag.P) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
		if (t.equals(HTML.Tag.B) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
		if (t.equals(HTML.Tag.H1) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
		if (t.equals(HTML.Tag.H2) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
		if (t.equals(HTML.Tag.H3) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}

		if (t.equals(HTML.Tag.H4) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
		if ((t.equals(HTML.Tag.STYLE) || t.equals(HTML.Tag.SCRIPT))
				&& tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
		if (t.equals(HTML.Tag.H5) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
		if (t.equals(HTML.Tag.H6) && tag.size() > 0) {
			tag.remove(tag.size() - 1);
			return;
		}
	}

	/**
	 * 
	 * @param data
	 *            Text between tags
	 * @param pos
	 *            position of text within web page
	 */
	public void handleText(char[] data, int pos) {
		lastText = new String(data);
		Vector<String> str = this.tokenizer.split(lastText);
		str = this.sw.removeStopWords(str);
		char tag_temp;
		posting posting;
		LexiconItem lexicon_item;
		if (str.size() == 0) {
			return;
		}
		if (tag.size() > 0) {
			if (tag.get(tag.size() - 1) == 'S') {
				return;
			} else {
				tag_temp = tag.get(tag.size() - 1);
			}
		} else {
			tag_temp = ' ';
		}
		for (int i = 0; i < str.size(); i++) {
			String word = str.get(i);
			if (!this.lexicon.contains_key(word)) {
				lexicon_item = new LexiconItem(word);
				this.lexicon.put(word, lexicon_item);
			}
			posting = new posting();
			int wordid;
			if ((wordid = this.wordid_generator.get(word)) == -1) {
				wordid = this.wordid_generator.generate_woldid(word);
			}
			posting.set_word(word);
			posting.set_word_id(wordid);
			posting.set_doc_id(docid);
			posting.set_tag(tag_temp);
			posting.set_position(++position);
			// posting.set_word(word);
			this.posting_list.add(posting);
		}
		return;

	}

}
