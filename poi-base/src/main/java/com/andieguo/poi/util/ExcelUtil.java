package com.andieguo.poi.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	//读取xls文件
	public static List<String> readXls(InputStream input) throws Exception{
		List<String> result = new ArrayList<String>();
		//HSSFWorkbook表示整个excel
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(input);
		//for(int numSheet=0;numSheet<hssfWorkbook.getNumberOfSheets();numSheet++);
		//获取第一个sheet
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
		if(hssfSheet == null) return null;
		for(int i=0;i< hssfSheet.getLastRowNum();i++){
			HSSFRow hssfRow = hssfSheet.getRow(i);//HSSFRow表示行
			HSSFCell cell = hssfRow.getCell(0);
			if(cell!=null) result.add(cell.toString());
		}
		return result;
	}
	
	public static List<String> readXls(String path) throws Exception{
		InputStream input = new FileInputStream(path);
		return readXls(input);
	}

	//读取xlsx文件
	public static List<String> readXlsx(InputStream input) throws Exception{
		List<String> result = new ArrayList<String>();
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(input);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if(xssfSheet == null) return null;
		for(int i=0;i<xssfSheet.getLastRowNum();i++){
			XSSFRow xssfRow = xssfSheet.getRow(i);
			XSSFCell cell = xssfRow.getCell(0);
			if(cell!=null) {
				result.add(cell.toString());
			}
		}
		return result;
	}
	
	public static List<String> readXlsx(String path) throws Exception{
		InputStream input = new FileInputStream(path);
		return readXlsx(input);
	}
	
}
