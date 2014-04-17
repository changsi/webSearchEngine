/**
 * 
 */
package util;

/**
 * @author changsi
 *
 */
public class IntAndBytes {
	
	/**
	 * convert integer into 4 bytes
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] intToByte(int i) {
		byte[] bt = new byte[4];
		bt[0] = (byte) (0xff & i);
		bt[1] = (byte) ((0xff00 & i) >> 8);
		bt[2] = (byte) ((0xff0000 & i) >> 16);
		bt[3] = (byte) ((0xff000000 & i) >> 24);
		return bt;
	}
	
	/**
	 * convert 4 bytes into integer
	 * 
	 * @param bytes
	 * @return
	 */
	public static int bytesToInt(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		num |= ((bytes[1] << 8) & 0xFF00);
		num |= ((bytes[2] << 16) & 0xFF0000);
		num |= ((bytes[3] << 24) & 0xFF000000);
		return num;
	}
	
	/**
	 * convert 4 bytes into integer
	 * 
	 * @param bytes
	 * @return
	 */
	public static int bytesToInt(byte[] bytes, int offset) {
		int num = bytes[0+offset] & 0xFF;
		num |= ((bytes[1+offset] << 8) & 0xFF00);
		num |= ((bytes[2+offset] << 16) & 0xFF0000);
		num |= ((bytes[3+offset] << 24) & 0xFF000000);
		return num;
	}
	
	/**
	 * convert integer into 2 bytes
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] intToTwoBytes(int i){
		byte[] bt = new byte[2];
		bt[0] = (byte) (0xff & i);
		bt[1] = (byte) ((0xff00 & i) >> 8);
		return bt;
	}
	
	/**
	 * convert 2 bytes into integer
	 * 
	 * @param bytes
	 * @return
	 */
	public static int twoBytesToInt(byte[] bytes){
		int num=bytes[0]&0xFF;
		num |= ((bytes[1]<<8)& 0xFF00);
		return num;
	}
	
	/**
	 * convert 2 bytes into integer with offset
	 * 
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static int twoBytesToInt(byte[] bytes, int offset){
		int num=bytes[offset]&0xFF;
		num |= ((bytes[offset+1]<<8)& 0xFF00);
		return num;
	}

	/**
	 * convert integer into 3 bytes
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] intToThreeByte(int i){
		byte[] bytes=new byte[3];
		bytes[0] = (byte) (0xff & i);
		bytes[1] = (byte) ((0xff00 & i) >> 8);
		bytes[2] = (byte) ((0xff0000 & i) >> 16);
		return bytes;
	}
	
	/**
	 * convert integer into 1 byte
	 * 
	 * @param i
	 * @return
	 */
	public static byte intToOneByte(int i){
		byte bytes;
		bytes= (byte) (0xff & i);
		return bytes;
	}
	
	/**
	 * convert 1 bytes into integer with offset
	 * 
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static int oneBytesToInt(byte[] bytes, int offset){
		int num=bytes[offset]&0xFF;
		return num;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
