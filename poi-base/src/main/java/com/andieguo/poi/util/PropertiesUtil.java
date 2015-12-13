package com.andieguo.poi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class PropertiesUtil {

	public static Properties loadFromFile(File file){
		InputStream input = null;
		try {
			 input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return loadFromInputStream(input);
	}
	
	public static Properties loadFromInputStream(InputStream input){//input文件使用UTF-8编码，读取的时候使用UTF-8编码，则不会出现乱码
		Properties properties = new Properties();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(input,"UTF-8"));
			properties.load(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
	
	public static void store(Properties properties,OutputStream output){//保存的时候用UTF-8保存
		 OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(output,"UTF-8");
			properties.store(writer, "update");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(writer!=null){
				try {
					writer.close();//为什么没有执行关闭操作，store操作会将内存中的信息保存多次
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		//在家目录生成临时文件
		InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream("local.log");
		Properties properties = PropertiesUtil.loadFromInputStream(input);
		properties.setProperty("武汉&北京", "done");
		properties.setProperty("武汉&北京1", "done");
		properties.setProperty("武汉&北京2", "done");
		OutputStream out = new FileOutputStream(new File("e:/workspace/poi-parent/poi-base/src/main/resources/local.log"));
		PropertiesUtil.store(properties, out);
		PropertiesUtil.store(properties, out);
		PropertiesUtil.store(properties, out);
	}

}