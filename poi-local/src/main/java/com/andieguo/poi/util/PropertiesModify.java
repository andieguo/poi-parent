package com.andieguo.poi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * modify the local_update_global.properties file
 * @author andieguo
 *
 */
public class PropertiesModify {

	public static void main(String[] args) throws IOException {
		File globalFile = new File(Constants.MKDIRPATH + File.separator+"local_update_global.properties");
		if(!globalFile.exists()) globalFile.createNewFile();
		Properties globalProperties = PropertiesUtil.loadFromInputStream(new FileInputStream(globalFile));
		File home = new File(Constants.MKDIRPATH);
		if(home.isDirectory()){
			File[] files = home.listFiles();  
			for (int i = 0; i < files.length; i++) { 
				String name = files[i].getName().trim().toLowerCase();  
                if (name.endsWith(".properties") && name.startsWith("local_update_") && files[i].length() > 1024) {
                	name = name.substring(name.lastIndexOf("_")+1,name.lastIndexOf("."));
                	globalProperties.setProperty(name, "done");
                    System.out.println(name + "\t" + files[i].length());  
                }  
			}
			PropertiesUtil.store(globalProperties, globalFile);
		}
	}
}
