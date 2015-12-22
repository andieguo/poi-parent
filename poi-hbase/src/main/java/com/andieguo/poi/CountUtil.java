package com.andieguo.poi;

import java.io.File;
import java.io.FileInputStream;
import org.json.JSONObject;

import com.andieguo.poi.util.FileUtil;

public class CountUtil {
	private long poiNumber = 0;
	private long fileNumber = 0;
	public void listFile(File file) throws Exception{
		if(file.isFile()){//C:\Users\andieguo\poi-data\北京\餐饮服务;茶艺;茶艺\北京-餐饮服务;茶艺;茶艺-0.json
			String result = FileUtil.readFile(new FileInputStream(file));//读取JSON文件为字符串
			JSONObject jsonResult = new JSONObject(result);
			poiNumber += jsonResult.getInt("total");
			fileNumber++;
			System.out.println("fileNumber:"+fileNumber+",poiNumber:"+poiNumber);
		}else if(file.isDirectory()){//目录
			File[] files = file.listFiles();  
			for (int i = 0; i < files.length; i++) {
				listFile(files[i]);//递归
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		CountUtil util = new CountUtil();
		File file = new File("C:\\Users\\andieguo\\poi-data");
		util.listFile(file);
	}

}
