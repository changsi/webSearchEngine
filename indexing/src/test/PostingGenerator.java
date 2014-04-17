/**
 * 
 */
package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
//import java.io.FileInputStream;
import java.io.FileOutputStream;
//import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author changsi
 * 
 */
public class PostingGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// define the size of the thread pool
		ThreadPool pool = new ThreadPool(5);
		try {

//			Lexicon lexicon =new Lexicon();
//			WordIDGenerator wordid_generator =new WordIDGenerator();
//			DocidGenerator docid_generator =new DocidGenerator();
//			URLTable urltable = new URLTable();

			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					"lexicon.seri"));
			Lexicon lexicon = (Lexicon) in.readObject();
			in.close();

			in = new ObjectInputStream(new FileInputStream(
					"wordid_generator.seri"));
			WordIDGenerator wordid_generator = (WordIDGenerator) in
					.readObject();
			in.close();

			in = new ObjectInputStream(new FileInputStream(
					"docid_generator.seri"));
			DocidGenerator docid_generator = (DocidGenerator) in.readObject();
			in.close();

			in = new ObjectInputStream(new FileInputStream(
					"url_table.seri"));
			URLTable urltable = (URLTable) in.readObject();
			in.close();
			
			int begin = (int) System.currentTimeMillis() / 1000;
			
			for (int i = 4001; i <= 4179; i++) {
				PostingGenerationThread thread = new PostingGenerationThread(
						String.valueOf(i), urltable, lexicon, wordid_generator,
						docid_generator);
				pool.execute(thread);
			}
			pool.threads_run();
			Thread[] threadpool = pool.get_threadpool();
			for (int i = 0; i < threadpool.length; i++) {
				try {
					threadpool[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			File lexicon_serial = new File("lexicon.seri");
			FileOutputStream out = new FileOutputStream(lexicon_serial);
			ObjectOutputStream oout = new ObjectOutputStream(out);
			oout.writeObject(lexicon);

			File docid_generator_serial = new File("docid_generator.seri");
			out = new FileOutputStream(docid_generator_serial);
			oout = new ObjectOutputStream(out);
			oout.writeObject(docid_generator);

			File wordid_generator_serial = new File("wordid_generator.seri");
			out = new FileOutputStream(wordid_generator_serial);
			oout = new ObjectOutputStream(out);
			oout.writeObject(wordid_generator);

			File url_table_serial = new File("url_table.seri");
			out = new FileOutputStream(url_table_serial);
			oout = new ObjectOutputStream(out);
			oout.writeObject(urltable);

			oout.close();
			int end = (int) System.currentTimeMillis() / 1000;
			int timetaken = end - begin;
			System.out.println("time taken: " + timetaken + " seconds");
			System.out.println("time taken: " + timetaken / 60
					+ " minutes and " + timetaken % 60 + " seconds");
		} catch (Exception e) {
			System.err.println("Error: " + e);
			e.printStackTrace(System.err);
		}

	}

}
