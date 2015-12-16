package com.andieguo.poi.util;

public class ExampleDriver {

	public static void main(String[] args) {
		ProgramDriver pgd = new ProgramDriver();
		try {
			pgd.addClass("modify", PropertiesModify.class,"modify the local_update_global.properties file");
			pgd.addClass("main", RestDownload.class, "rest download job");
			pgd.driver(args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
