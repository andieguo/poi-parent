package com.andieguo.poi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.hadoop.hbase.util.Bytes;

public class BytesUtil {
	private static MessageDigest md;
	static{
		try {
			md = MessageDigest.getInstance("MD5");//MD5加密，统一字节长度
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getByteSize(int skipIndex,String... keys){
		int length = 0;
		for(int i=0;i<keys.length;i++){
			byte[] keyByte = null;
			if(i != skipIndex){
				keyByte = md.digest(Bytes.toBytes(keys[i]));
			}else{
				keyByte = Bytes.toBytes(keys[i]);
			}
			length = length + keyByte.length;
		}
		return length;
	}
	
	/**
	 * 所有的keys均采用MD5进行散列存储
	 * @param keys
	 * @return
	 */
	public static byte[] startkeyGen(String... keys){
		return startkeyGen(-1,keys);
	}
	
	/**
	 * skipIndex位置的字符串跳过MD5进行散列存储
	 * @param skipIndex
	 * @param keys
	 * @return
	 */
	public static byte[] startkeyGen(int skipIndex,String... keys){
		int length = getByteSize(skipIndex,keys);
		byte[] startkey = new byte[length];
		int offset = 0;
		byte[] keyByte = null;
		for(int i=0;i<keys.length;i++){
			if(i != skipIndex){
				keyByte = md.digest(Bytes.toBytes(keys[i]));
			}else{
				keyByte = Bytes.toBytes(keys[i]);
			}
			offset = Bytes.putBytes(startkey, offset, keyByte, 0, keyByte.length);
		}
		return startkey;
	}
	
	/**
	 * 所有的keys均采用MD5进行散列存储
	 * @param keys
	 * @return
	 */
	public static byte[] endkeyGen(String... keys){
		return endkeyGen(-1,keys);
	}
	
	/**
	 * skipIndex位置的字符串跳过MD5进行散列存储
	 * @param skipIndex
	 * @param keys
	 * @return
	 */
	public static byte[] endkeyGen(int skipIndex,String... keys){
		byte[] startkey = startkeyGen(skipIndex,keys);
		byte[] endkey = new byte[startkey.length];
		System.arraycopy(startkey, 0, endkey, 0, endkey.length);
		endkey[endkey.length - 1]++;
		return endkey;
	}
	
	public static void main(String[] args) {
		//byte是一个字节保存的，有8个位，即8个0、1。
		System.out.println(startkeyGen("城市").length);//16个byte（字节）
	}
}
