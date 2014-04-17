/**
 * 
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Map;

import test.Lexicon;

/**
 * @author changsi
 *
 */
public class InvertedIndexQuery implements BasicInvertedIndexQuery {
	
	/**
	 * inverted index cache in memory injected by calling class
	 */
	private InvertedIndexCache cache;
	/**
	 * inverted index files path route
	 */
	private String path;
	/**
	 * lexicon injected by calling class
	 */
	private Lexicon lexicon;
	private FileInputStream r;
	/**
	 * store the compressed inverted index which is bytes array
	 */
	private byte[][] bytes;
	/**
	 * store the chunk pointers for inverted index bytes array
	 * the format like this (chunk start position row, start position column, chunk size)
	 */
	private ArrayList<int[]> chunk_positions_list;
	/**
	 * store the uncompressed last docid of each chunk
	 */
	private ArrayList<Integer> last_docid_list;
	/**
	 * store the de_compressed posting chunk array for doc_id
	 */
	private ArrayList<Integer> chunk_docid;
	/**
	 * store the de_compressed posting chunk array for frequency
	 */
	private ArrayList<Integer> chunk_frequency;
	/**
	 * store the de_compressed posting chunk array for position
	 */
	private ArrayList<Integer> chunk_positions;
	/**
	 * store the de_compressed posting chunk array for tags
	 */
	private ArrayList<Byte> chunk_tags;
	/**
	 * store the index of the current chunk
	 */
	private int chunk_pointer;
	/**
	 * store the current positing position in chunk
	 */
	private int posting_position;
	
	public InvertedIndexQuery(){
		
	}
	
    public InvertedIndexQuery(String path, InvertedIndexCache cache, Lexicon lexicon){
		this.setPath(path);
		this.setCache(cache);
		this.setLexicon(lexicon);
		this.chunk_docid=null;
		this.chunk_frequency=null;
		this.chunk_positions=null;
		this.chunk_tags=null;
		this.bytes=null;
		this.posting_position=-1;
		this.chunk_pointer=0;
	}
    
    /**
     * initialize the chunk position list and last doc_id list based on bytes array
     */
    private void initialize_chunk_position_docid_list(){
    	for(int i=0;i<this.bytes.length;i++){
    		for(int j=0;j<this.bytes[i].length;){
    			int temp=IntAndBytes.twoBytesToInt(this.bytes[i], j);
        		j=j+2;
        		int docid_temp=IntAndBytes.bytesToInt(this.bytes[i], j);
        		j=j+4;
        		this.last_docid_list.add(docid_temp);
        		int temp_list[]={i,j,temp-4};
            	this.chunk_positions_list.add(temp_list);
            	j=j+temp-4;
    		}
    	}
    	this.chunk_pointer=0;
    }
    
    /**
     * @param begining
     * @param size
     * de_compress one chunk based on chunk position and store the de_compressed
     * in chunk data structure. 
     */
    public void chunk_decompression(int begining_row, int begining_column, int size){
    	this.chunk_docid=new ArrayList<Integer>();
    	this.chunk_frequency=new ArrayList<Integer>(); 
    	this.chunk_positions=new ArrayList<Integer>();
    	this.chunk_tags=new ArrayList<Byte>();
    	int chunk_size=IntAndBytes.oneBytesToInt(this.bytes[begining_row], begining_column);
    	int previous_docid=0;   // for inverted index decompression
    	// decompress docid
    	int i=1;
    	for(int counter=0;counter<chunk_size && i<size;counter++){
    		// Variable bytes decompression for docid
    		int VByte_size=VByteCompression.FindVByte(this.bytes[begining_row], begining_column+i);
    		int docid_temp=VByteCompression.VByteToInt(this.bytes[begining_row],begining_column+i, VByte_size);
    		i=i+VByte_size;
    		// gap decompression for docid
    		this.chunk_docid.add(docid_temp+previous_docid);
    		previous_docid=docid_temp+previous_docid;
    	}
    	// decompress frequency
    	for(int counter=0;counter<chunk_size && i<size;counter++){
    		// Variable bytes decompression for frequency
    		int VByte_size=VByteCompression.FindVByte(this.bytes[begining_row], begining_column+i);
    		int frequency_temp=VByteCompression.VByteToInt(this.bytes[begining_row],begining_column+i, VByte_size);
    		i=i+VByte_size;
    		this.chunk_frequency.add(frequency_temp);
    	}
    	// decompress positions
    	for(int counter=0;counter<chunk_size && i<size;counter++){
    		int position_num=this.chunk_frequency.get(counter);
    		int previous_position=0;
    		for(int j=0;j<position_num;j++){
    			// Variable bytes decompression for position
        		int VByte_size=VByteCompression.FindVByte(this.bytes[begining_row], begining_column+i);
        		int position_temp=VByteCompression.VByteToInt(this.bytes[begining_row],begining_column+i, VByte_size);
        		i=i+VByte_size;
        		// gap decompression for position
        		this.chunk_positions.add(position_temp+previous_position);
        		previous_position=position_temp+previous_position;
    		}	
    	}
    	// decompress tag
    	for(int counter=0;counter<chunk_size && i<size;counter++){
    		int tags_num=this.chunk_frequency.get(counter);
    		for(int j=0;j<tags_num;j++){
    			byte tag_temp=this.bytes[begining_row][begining_column+i];
    			i++;
    			this.chunk_tags.add(tag_temp);
    		}	
    	}
    	this.posting_position=0;
    }
	
	/* (non-Javadoc)
	 * @see util.BasicInvertedIndexQuery#openList(java.lang.String)
	 */
	@Override
	public byte[][] openList(String word) {
		// TODO Auto-generated method stub
		this.chunk_positions_list=new ArrayList<int[]>();
		this.last_docid_list=new ArrayList<Integer>();
		// check whether the inverted index is already in cache
		// if yea, get it from cache
		if(this.cache.contains_key(word)){
			this.bytes=this.cache.get(word);
		}
		// if not, load it from disk into cache
		else{
			int index_length=0;// the length of inverted index
			ArrayList<Map<String, Object>> temp_pointer=this.lexicon.get(word).getPointer();
			byte[][] index= new byte[temp_pointer.size()][];    // store inverted index bytes format
			for(int i=0;i<temp_pointer.size();i++){
				String temp_filename=(String) temp_pointer.get(i).get("filename");
				int temp_position=(Integer) temp_pointer.get(i).get("position");
//				System.out.println("file name: "+temp_filename+". position: "+temp_position);
				try {
					r = new FileInputStream(new File(this.path+temp_filename));
					byte[] temp_bytes= new byte[4];
					r.skip(temp_position);
					r.read(temp_bytes, 0, 4);
					int length=IntAndBytes.bytesToInt(temp_bytes);
					temp_bytes=null;
					index[i]=new byte[length];
					index_length=index_length+length;
					r.read(index[i], 0, length);
					r.close();
					r=null;
				} catch (Exception e) {
					System.err.println("Error: " + e);
					e.printStackTrace(System.err);
				}
			}
			this.cache.put(word, index, index_length);
			this.bytes=index;
		}
		initialize_chunk_position_docid_list();
		return this.bytes;
	}

	/* (non-Javadoc)
	 * @see util.BasicInvertedIndexQuery#closeList(byte[])
	 */
	@Override
	public void closeList() {
		// TODO Auto-generated method stub
		this.bytes=null;
		this.chunk_positions_list=null;
		this.last_docid_list=null;
		this.chunk_docid=null;
		this.chunk_frequency=null;
		this.chunk_positions=null;
		this.chunk_tags=null;
	}

	/* (non-Javadoc)
	 * @see util.BasicInvertedIndexQuery#nextGEQ(byte[], int)
	 */
	@Override
	public int nextGEQ( int k) {
		// TODO Auto-generated method stub
		if(this.chunk_docid!=null){
			for(;this.posting_position<this.chunk_docid.size();){
				if(this.chunk_docid.get(this.posting_position)>=k){
					return this.chunk_docid.get(this.posting_position);
				}
				this.posting_position++;
			}
			this.chunk_pointer++;
			this.chunk_docid=null;
			this.chunk_positions=null;
			this.chunk_frequency=null;
			this.chunk_tags=null;
			this.posting_position=-1;
		}
		for(;this.chunk_pointer<this.last_docid_list.size();){
    		int docid_temp=this.last_docid_list.get(this.chunk_pointer);
			if(docid_temp>k){
				int position[]=this.chunk_positions_list.get(this.chunk_pointer);
				chunk_decompression(position[0],position[1],position[2]);
				this.posting_position=0;
				break;
			}
			else{
				this.chunk_pointer++;
			}
		}
		if(this.chunk_docid!=null){
			for(;this.posting_position<this.chunk_docid.size();){
				if(this.chunk_docid.get(this.posting_position)>=k){
					return this.chunk_docid.get(this.posting_position);
				}
				this.posting_position++;
			}
			this.chunk_pointer++;
			this.chunk_docid=null;
			this.chunk_positions=null;
			this.chunk_frequency=null;
			this.chunk_tags=null;
			this.posting_position=-1;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see util.BasicInvertedIndexQuery#getFreq(byte[], int)
	 */
	@Override
	public int getFreq() {
		// TODO Auto-generated method stub
		int freq=-1;
		if(this.chunk_pointer!=-1 && this.posting_position!=-1){
			freq=this.chunk_frequency.get(this.posting_position);
		}
		return freq;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int begin=(int) System.currentTimeMillis()/1000;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					"//media//d__//workshop//Merge//lexicon.seri"));
			Lexicon lexicon = (Lexicon) in.readObject();
			in.close();
			InvertedIndexCache cache= new InvertedIndexCache(1500000000);
			BasicInvertedIndexQuery query=new InvertedIndexQuery("//media//d__//workshop//Merge//",cache,lexicon);
			query.openList("table");
			int result=query.nextGEQ(10000);
			System.out.println("docid: "+result);
			System.out.println("frequency: "+query.getFreq());
		}
		catch (Exception e) {
			System.err.println("Error: " + e);
			e.printStackTrace(System.err);
		}
		finally{
			int end=(int) System.currentTimeMillis()/1000;
			int timetaken=end-begin;
			System.out.println("time taken: "+timetaken+" seconds");
			System.out.println("time taken: "+timetaken/60+" minutes and "+timetaken%60+" seconds");
		}
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(InvertedIndexCache cache) {
		this.cache = cache;
	}

	/**
	 * @return the cache
	 */
	public InvertedIndexCache getCache() {
		return cache;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
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

}
