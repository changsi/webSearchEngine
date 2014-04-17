/**
 * 
 */
package util;

import java.util.ArrayList;

/**
 * @author changsi
 * 
 */
public class VByteCompression {

	public static void intToVByte(int i, ArrayList<Byte> byte_list) {
		ArrayList<Byte> temp_byte_list;
		temp_byte_list = new ArrayList<Byte>();
		byte temp_byte;
		boolean first = true;
		while (i > 128) {
			temp_byte = (byte) (i & 0x7f);
			if (first) {
				first = false;
			} else {
				temp_byte = (byte) (temp_byte | 0x80);
			}
			temp_byte_list.add(temp_byte);
			i = i >> 7;
		}
		temp_byte = (byte) (i & 0x7f);
		if (first) {
			first = false;
		} else {
			temp_byte = (byte) (i | 0x80);
		}
		temp_byte_list.add(temp_byte);
		for (int j = temp_byte_list.size() - 1; j >= 0; j--) {
			byte_list.add(temp_byte_list.get(j));
		}
	}
	
	public static byte[] FindVByte(byte[] bytes, int offset){
		ArrayList <Byte> VByte= new ArrayList<Byte>(); 
		for(int i=offset; i<bytes.length;i++){
			byte temp = bytes[i];
			VByte.add(temp);
			if((temp&0x80)==0){
				break;
			}
		}
		byte[] result= new byte[VByte.size()];
		for(int i=0;i<VByte.size();i++){
			result[i]=VByte.get(i);
		}
		VByte=null;
		return result;
	}
	
	public static int VByteToInt(byte[] bytes){
		int result=0;
		for(int i=0, j=bytes.length-1; j>=0; j--, i++){
			int temp=bytes[j]&0x7f;
			result=(int) (result+temp*Math.pow(128,i));
		}
		return result;
	}
	
//    convert eight byte to binary string 
//	 used for print byte
	public static String getEigthBitsStringFromByte(int b) {
		// if this is a positive number its bits number will be less than 8
		// so we have to fill it to be a 8 digit binary string
		// b=b+100000000(2^8=256) then only get the lower 8 digit
		b |= 256; // mark the 9th digit as 1 to make sure the string has at
					// least 8 digits
		String str = Integer.toBinaryString(b);
		int len = str.length();
		return str.substring(len - 8, len);
	}
//   the reverse operation
	public static byte getByteFromEigthBitsString(String str) throws Exception {
		if (str.length() != 8)
			throw new Exception("It's not a 8 length string");
		byte b;
		// check if it's a minus number
		if (str.substring(0, 1).equals("1")) {
			// get lower 7 digits original code
			str = "0" + str.substring(1);
			b = Byte.valueOf(str, 2);
			// then recover the 8th digit as 1 equal to plus 1000000
			b |= 128;
		} else {
			b = Byte.valueOf(str, 2);
		}
		return b;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		int t = 89;
//		byte[] bytes = VByteCompression.intToVByte(t);
//		for (int i = 0; i < bytes.length; i++) {
//			System.out.println(VByteCompression
//					.getEigthBitsStringFromByte(bytes[i]));
//		}
//		int result;
//		result = VByteCompression.VByteToInt(bytes);
//		System.out.println(result);
	}

}
