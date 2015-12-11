package com.andieguo.poi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileUtil {

	//保持JSON数据到文本
	public static void saveJSON(byte[] bytes, String filePath) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(new File(filePath));
			out.write(bytes);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public static String readFile(File file) {
	    try { 
	        return readFile(new FileInputStream(file));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public static String readFile(InputStream input) {//当Test.java使用UTF-8编码，而使用GBK编码进行读取的时候会出现乱码；
	    try { 
	        //为什么要对FileInputStream进行再一次封装，为了使用InputStreamReader对字节和字符进行转换；
	        //为什么要对InputStreamReader进行再一次封装，为了使用BufferedReader的readline方法；
	        BufferedReader in = new BufferedReader(new InputStreamReader(input,"UTF-8"));//使用本地环境中的默认字符集，例如在中文环境中将使用 GBK编码
	        StringBuffer result = new StringBuffer();
	        String line = null;
	        while((line = in.readLine()) != null){
	        	result.append(line);
	        }
	        in.close();
	        return result.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public static void main(String[] args) {
		String result = FileUtil.readFile(FileUtil.class.getClassLoader().getResourceAsStream("province.json"));
		System.out.println(result);
	}
	
}
