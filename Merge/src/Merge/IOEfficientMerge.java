/**
 * 
 */
package Merge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

import util.IDAndWordTable;
import util.InputBuffer;
import util.IntAndBytes;
import util.InvertedIndexCompression;
import util.InvertedIndexItem;
import util.VByteCompression;
import test.Lexicon;
import test.LexiconItem;
import util.OutputBuffer;
import util.Posting;
import util.PriorityQueueItem;
import test.WordIDGenerator;

/**
 * @author changsi
 * 
 */
public class IOEfficientMerge {

	/**
	 * the priority queue for merge so that every time extract the smallest one
	 */
	private PriorityQueue<PriorityQueueItem> queue;
	/**
	 * input posting files' name array list
	 */
	private ArrayList<String> input_files_name;
	/**
	 * input posting buffer array list
	 */
	private InputBuffer[] input_buffer_list;
	/**
	 * output inverted index buffer
	 */
	private OutputBuffer output_buffer;
	/**
	 * input buffer size
	 */
	private int input_buffer_size;
	/**
	 * output buffer size
	 */
	private int output_buffer_size;
	/**
	 * output file name prefix
	 */
	private String output_buffer_filename_prefix;
	/**
	 * lexicon to add inverted index information to each word, such as "file name and start position"
	 */
	private Lexicon lexicon;
	/**
	 * id and word mapping table derived from word_id generator
	 */
	private IDAndWordTable id_word_table;
	/**
	 * maximum chunk size
	 */
	private int chunk_maximum_size;
	/**
	 * chunk bytes size
	 */
	private int chunk_byte_size;

	public IOEfficientMerge(int input_buffer_num,
			ArrayList<String> input_files_name, int input_buffer_size,
			int output_buffer_size, String output_buffer_filename_prefix, int chunk_maximum_size) {
		this.queue = new PriorityQueue<PriorityQueueItem>(input_buffer_num);
		this.input_files_name = input_files_name;
		this.output_buffer_filename_prefix=output_buffer_filename_prefix;
		this.input_buffer_size = input_buffer_size;
		this.output_buffer_size = output_buffer_size;
		this.chunk_maximum_size=chunk_maximum_size;
	}
	
	/**
	 * do inverted index compression and V-Byte compression for chunk
	 * the compression format looks like 
	 * [2Byte for chunk_size][4bytes uncompressed last doc_id][1bytes posting size][compressed doc_id][compressed frequency]
	 * [compressed positions][tags]
	 * 
	 * @param chunk
	 * @return
	 */
	public Map<String, ArrayList<Byte>>  compression(ArrayList<InvertedIndexItem> chunk){
		int last_item_index= chunk.size()-1;
		int last_docid=chunk.get(last_item_index).getDocid();
//		do the gap compression
		InvertedIndexCompression.compression(chunk);
//		do the V-Byte compression
		ArrayList<Byte> chunk_meta_data=new ArrayList<Byte>(); // bytes list for chunk meta data
		ArrayList<Byte> docid_byte_list=new ArrayList<Byte>(); // bytes list for doc_id
		ArrayList<Byte> frequency_byte_list=new ArrayList<Byte>(); // bytes list for frequency
		ArrayList<Byte> position_byte_list=new ArrayList<Byte>(); // bytes list for positions
		ArrayList<Byte> tag_byte_list=new ArrayList<Byte>(); // bytes list for tags
		ArrayList<Integer> temp_positions;
		ArrayList<Byte> temp_tag;
		InvertedIndexItem temp_item;
		byte[] temp_bytes;
		for(int i=0;i<chunk.size();i++){
			temp_item=chunk.get(i);
			// VBytes compression for doc_id
			VByteCompression.intToVByte(temp_item.getDocid(),docid_byte_list);
			// VBytes compression for frequency
			VByteCompression.intToVByte(temp_item.getFrequency(),frequency_byte_list);
			temp_positions=temp_item.getPosition();
			temp_tag=temp_item.getTag();
			for(int j=0;j<temp_positions.size();j++){
				VByteCompression.intToVByte(temp_positions.get(j),position_byte_list);
				tag_byte_list.add(temp_tag.get(j));
			}
		}
		temp_bytes=IntAndBytes.intToTwoBytes(docid_byte_list.size()+frequency_byte_list.size()+position_byte_list.size()+tag_byte_list.size()+5);
		// 2 bytes for recording chunk bytes size
		chunk_meta_data.add(temp_bytes[0]);
		chunk_meta_data.add(temp_bytes[1]);
		// 4 bytes for recording last doc_id(uncompressed)
	    IntAndBytes.intToByte(last_docid,chunk_meta_data);
	    // 1 bytes for posting number per chunk
	    byte temp_size=IntAndBytes.intToOneByte(chunk.size());
	    chunk_meta_data.add(temp_size);
	    Map<String, ArrayList<Byte>> chunk_byte_list= new HashMap<String, ArrayList<Byte>> ();
	    chunk_byte_list.put("metadata", chunk_meta_data);
	    chunk_byte_list.put("docid", docid_byte_list);
	    chunk_byte_list.put("frequency", frequency_byte_list);
	    chunk_byte_list.put("position", position_byte_list);
	    chunk_byte_list.put("tag", tag_byte_list);
	    this.chunk_byte_size=chunk_meta_data.size()+docid_byte_list.size()+frequency_byte_list.size()+position_byte_list.size()+tag_byte_list.size();
		return chunk_byte_list;
	}

	/**
	 * initialize input buffer, output buffer and priority queue
	 */
	public void initial() {
		this.input_buffer_list = new InputBuffer[this.input_files_name.size()];
		// initialize input buffers
		for (int i = 0; i < this.input_files_name.size(); i++) {
			InputBuffer temp = new InputBuffer(this.input_buffer_size,
			this.input_files_name.get(i));
			this.input_buffer_list[i] = temp;
		}
		// initialize output buffers
		this.output_buffer = new OutputBuffer(this.output_buffer_size,
				this.output_buffer_filename_prefix);
		this.output_buffer.initialization();
		// initialize priority queue
		int begin=(int) System.currentTimeMillis()/1000;
		for (int i = 0; i < this.input_buffer_list.length; i++) {
			this.queue.add(new PriorityQueueItem(i, this.input_buffer_list[i]
					.getNext()));
//			System.out.println(i);
		}
		int mid=(int) System.currentTimeMillis()/1000;
		int timetaken=mid-begin;
		System.out.println("initialization priority queue time taken: "+timetaken+" seconds");
		System.out.println("initialization priority queue time taken: "+timetaken/60+" minutes and "+timetaken%60+" seconds");
	}

	public void merge() {
		int previous_wordid = -1;
		int previous_docid = -1;
		int chunk_size=0;
		InvertedIndexItem temp_inverted_index_item= new InvertedIndexItem();
		ArrayList<InvertedIndexItem> inverted_index_chunk= new ArrayList<InvertedIndexItem>();
		Map<String, ArrayList<Byte>> temp_byte_list;
		int frequency = 0;
		boolean first_time = true;
		while (this.queue.size() > 0) {
			// remove one posting from priority queue and insert a new one from that input buffer
			PriorityQueueItem temp = this.queue.remove();
			int index = temp.getPosition();
			Posting minmum_posting = temp.getPosting();
			if (this.input_buffer_list[index] != null) {
				Posting next_posting = this.input_buffer_list[index].getNext();
				if (next_posting != null) {
					this.queue.add(new PriorityQueueItem(index, next_posting));
				} else {
					this.input_buffer_list[index] = null;
				}
			}
			// if this posting's word_id is the same as the previous posting
			if (previous_wordid == minmum_posting.get_word_id()) {
				// if the posting's doc_id is the same as the previous posting
				if (previous_docid == minmum_posting.get_doc_id()) {
					temp_inverted_index_item.addPosition(minmum_posting.get_position());
					temp_inverted_index_item.addTag(minmum_posting.tagToByte());
					frequency++;
				} else 
			    // if the poting's doc_id is different from the previous one
				{
					temp_inverted_index_item.setFrequency(frequency);
					inverted_index_chunk.add(temp_inverted_index_item);
					chunk_size++;
				   // if the current chunk size is equal to the maximum size
					// do compression operating and insert to output buffer
					if(chunk_size==this.chunk_maximum_size){
						chunk_size=0;
						temp_byte_list=this.compression(inverted_index_chunk);
						inverted_index_chunk=null;
						ArrayList<Map<String, Object>> pointer=this.output_buffer.insertBuffer(temp_byte_list, true,this.chunk_byte_size);
						this.chunk_byte_size=0;
//						System.out.println("chunk size "+temp_byte_list.size());
//						System.out.println("position "+this.output_buffer.get_position());
						temp_byte_list=null;
						if (pointer!=null){
							System.out.println("this should not happen..");
							LexiconItem temp_lexicon_item=this.lexicon.get(this.id_word_table.get_word(previous_wordid));
							temp_lexicon_item.setPointer(pointer);
						}
						inverted_index_chunk= new ArrayList<InvertedIndexItem>();
					}
					frequency = 0;
					temp_inverted_index_item = new InvertedIndexItem();
					temp_inverted_index_item.setDocid(minmum_posting.get_doc_id());
					temp_inverted_index_item.addPosition(minmum_posting.get_position());
					temp_inverted_index_item.addTag(minmum_posting.tagToByte());
					previous_docid = minmum_posting.get_doc_id();
					frequency++;
				}
			} else {
				if (first_time) {
					first_time = false;
				} else {
					temp_inverted_index_item.setFrequency(frequency);
					inverted_index_chunk.add(temp_inverted_index_item);
					temp_byte_list=this.compression(inverted_index_chunk);
					inverted_index_chunk=null;
					ArrayList<Map<String, Object>> pointer=this.output_buffer.insertBuffer(temp_byte_list, false,this.chunk_byte_size);
					this.chunk_byte_size=0;
//					System.out.println("chunk size "+temp_byte_list.size());
//					System.out.println("position "+this.output_buffer.get_position());
					temp_byte_list=null;
					if (pointer!=null){
						LexiconItem temp_lexicon_item=this.lexicon.get(this.id_word_table.get_word(previous_wordid));
						temp_lexicon_item.setPointer(pointer);
					}
					frequency = 0;
					temp_inverted_index_item = new InvertedIndexItem();
					inverted_index_chunk= new ArrayList<InvertedIndexItem>();
				}
				temp_inverted_index_item.setDocid(minmum_posting.get_doc_id());
				temp_inverted_index_item.addPosition(minmum_posting.get_position());
				temp_inverted_index_item.addTag(minmum_posting.tagToByte());
				frequency++;
				previous_wordid = minmum_posting.get_word_id();
				previous_docid = minmum_posting.get_doc_id();
			}
		}
		this.output_buffer.flush();
	}
	
	public void finallize(){
		this.queue=null;
		this.input_buffer_list=null;
		this.input_files_name=null;
		this.output_buffer=null;
		this.lexicon=null;
		this.id_word_table=null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int begin=(int) System.currentTimeMillis()/1000;
		try {
			
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					"//media//d__//workshop//indexing//lexicon.seri"));
			Lexicon lexicon = (Lexicon) in.readObject();
			in.close();
			
			in = new ObjectInputStream(new FileInputStream(
					"//media//d__//workshop//indexing//wordid_generator.seri"));
			WordIDGenerator wordid_generator = (WordIDGenerator) in.readObject();
			in.close();
			
			int mid=(int) System.currentTimeMillis()/1000;
			int timetaken=mid-begin;
			System.out.println("initializing lexicon and wordid_generator time taken: "+timetaken+" seconds");
			System.out.println("initializing lexicon and wordid_generator time taken: "+timetaken/60+" minutes and "+timetaken%60+" seconds");
			
			String filename = "//media//d__//workshop//indexing//postings_";
			ArrayList<String> filenames = new ArrayList<String>();
			for (int i = 0; i < 4179; i++) {
				if (!(i >1991 && i< 2100)&&!(i>2480 && i < 2993) && i!=1571 && i!= 1875&& i!=1878&& i!=1928 && i!=2104&& i!=2107 && i!=2243 && i!=2244 && i!=2406 && i!=3492 && i!=3493) {
					filenames.add(filename + String.valueOf(i));
				}
			}
//			System.out.println(filenames.size());
			IOEfficientMerge merge = new IOEfficientMerge(filenames.size(),
					filenames, 300000, 50000000, "index_",128);
			merge.setLexicon(lexicon);
			merge.setId_word_table(wordid_generator.get_word_id_table());
			wordid_generator=null;
			merge.initial();
			mid=(int) System.currentTimeMillis()/1000;
			timetaken=mid-begin;
			System.out.println("initialization time taken: "+timetaken+" seconds");
			System.out.println("initialization time taken: "+timetaken/60+" minutes and "+timetaken%60+" seconds");
			System.out.println("merge operatiion begin!");
			begin=(int) System.currentTimeMillis()/1000;
			merge.merge();
			merge.finallize();
			System.gc();
			mid=(int) System.currentTimeMillis()/1000;
			timetaken=mid-begin;
			System.out.println("merge time taken: "+timetaken+" seconds");
			System.out.println("merge time taken: "+timetaken/60+" minutes and "+timetaken%60+" seconds");
			begin=(int) System.currentTimeMillis()/1000;
			File lexicon_serial = new File("//media//d__//workshop//Merge//lexicon.seri");
			FileOutputStream out = new FileOutputStream(lexicon_serial);
			ObjectOutputStream oout = new ObjectOutputStream(out);
			oout.writeObject(lexicon);
			oout.close();
		} catch (Exception e) {
			System.err.println("Error: " + e);
			e.printStackTrace(System.err);
		}
		finally{
			int end=(int) System.currentTimeMillis()/1000;
			int timetaken=end-begin;
			System.out.println("store lexicon time taken: "+timetaken+" seconds");
			System.out.println("store lexicon time taken: "+timetaken/60+" minutes and "+timetaken%60+" seconds");
		}
	}

	/**
	 * @param output_buffer_size
	 *            the output_buffer_size to set
	 */
	public void setOutput_buffer_size(int output_buffer_size) {
		this.output_buffer_size = output_buffer_size;
	}

	/**
	 * @return the output_buffer_size
	 */
	public int getOutput_buffer_size() {
		return output_buffer_size;
	}

	/**
	 * @param input_buffer_size
	 *            the input_buffer_size to set
	 */
	public void setInput_buffer_size(int input_buffer_size) {
		this.input_buffer_size = input_buffer_size;
	}
	/**
	 * @return the input_buffer_size
	 */
	public int getInput_buffer_size() {
		return input_buffer_size;
	}
	/**
	 * @param input_files_name
	 *            the input_files_name to set
	 */
	public void setInput_files_name(ArrayList<String> input_files_name) {
		this.input_files_name = input_files_name;
	}
	/**
	 * @return the input_files_name
	 */
	public ArrayList<String> getInput_files_name() {
		return input_files_name;
	}
	/**
	 * @param output_buffer_filename_prefix
	 *            the output_buffer_filename_prefix to set
	 */
	public void setOutput_buffer_filename_prefix(
			String output_buffer_filename_prefix) {
		this.output_buffer_filename_prefix = output_buffer_filename_prefix;
	}
	/**
	 * @return the output_buffer_filename_prefix
	 */
	public String getOutput_buffer_filename_prefix() {
		return output_buffer_filename_prefix;
	}
	/**
	 * @param lexicon the lexicon to set
	 */
	public void setLexicon(Lexicon lexicon) {
		this.lexicon = lexicon;
	}
	/**
	 * @return the lexicon
	 */
	public Lexicon getLexicon() {
		return lexicon;
	}
	/**
	 * @param id_word_table the id_word_table to set
	 */
	public void setId_word_table(ConcurrentHashMap<String,Integer> hashtable) {
		this.id_word_table = new IDAndWordTable(hashtable);
		hashtable=null;
	}
	/**
	 * @return the id_word_table
	 */
	public IDAndWordTable getId_word_table() {
		return id_word_table;
	}
}
