package com.andieguo.poi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

public class LogReader {
	
	public static void main(String[] args) {
		try {
			InputStream srcInput = new FileInputStream(new File("C:\\Users\\andieguo\\poi-parent\\poi-local.log"));
			InputStream desInput = new FileInputStream(new File("C:\\Users\\andieguo\\poi-parent\\local_update.properties"));
			OutputStream desOutput = new FileOutputStream(new File("C:\\Users\\andieguo\\poi-parent\\local_update.properties"));
			LogReader.readLog(srcInput,desInput,desOutput);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readLog(InputStream srcInput,InputStream desInput,OutputStream desOutput){
		 try {
			 Properties properties = PropertiesUtil.loadFromInputStream(desInput);
	        //为什么要对FileInputStream进行再一次封装，为了使用InputStreamReader对字节和字符进行转换；
	        //为什么要对InputStreamReader进行再一次封装，为了使用BufferedReader的readline方法；
	        BufferedReader in = new BufferedReader(new InputStreamReader(srcInput,"UTF-8"));//使用本地环境中的默认字符集，例如在中文环境中将使用 GBK编码
	        String line = null;
	        while((line = in.readLine()) != null){
	        	if(line.contains("--->")){
	        		String city = line.substring(line.indexOf("--->")-2,line.indexOf("--->"));
	        		int begin = line.indexOf("--->")+4;
	        		int end = line.lastIndexOf("-->");
	        		String poi = line.substring(begin, end);
	        		String key = city+"&"+ poi;
	        		properties.setProperty(key, "done");
	        		System.out.println(key+":done");
	        	}
	        }
	        PropertiesUtil.store(properties, desOutput);
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
