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

	public static int getByteSize(String... keys){
		int length = 0;
		for(String key : keys){
			byte[] keyByte = md.digest(Bytes.toBytes(key));
			length = length + keyByte.length;
		}
		return length;
	}
	
	public static byte[] startkeyGen(String... keys){
		int length = getByteSize(keys);
		byte[] startkey = new byte[length];
		int offset = 0;
		for(String key : keys){
			byte[] keyByte = md.digest(Bytes.toBytes(key));
			offset = Bytes.putBytes(startkey, offset, keyByte, 0, keyByte.length);
		}
		return startkey;
	}
	
	public static byte[] endkeyGen(String... keys){
		byte[] startkey = startkeyGen(keys);
		byte[] endkey = new byte[startkey.length];
		System.arraycopy(startkey, 0, endkey, 0, endkey.length);
		endkey[endkey.length - 1]++;
		return endkey;
	}
}
