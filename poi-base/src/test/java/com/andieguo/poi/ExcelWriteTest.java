package com.andieguo.poi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.andieguo.poi.util.ExcelWrite;
import com.andieguo.poi.util.Record;

public class ExcelWriteTest {

	/* excel column formate:column_#_width, excel中每一列的名称 */
	public static final String[] RECORES_COLUMNS = new String[] { "timedifference_#_3000", "poiNumber_#_7000","fileNumber_#_11000" };
	/* the column will display on xls files. must the same as the entity fields.对应上面的字段. */
	public static final String[] RECORES_FIELDS = new String[] { "timedifference", "poiNumber","fileNumber" };

	public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		List<Record> records = new ArrayList<Record>();
		for (int i = 0; i < 10; i++) {
			Record record = new Record();
			record.setFileNumber(10000+i);
			record.setPoiNumber(2000+i);
			record.setTimedifference(8888+i);
			records.add(record);
		}

		// 实际项目中，这个list 估计是从数据库中得到的

		HSSFWorkbook workbook = new HSSFWorkbook();
		ExcelWrite<Record> userSheet = new ExcelWrite<Record>();
		userSheet.creatAuditSheet(workbook, "user sheet xls", records, RECORES_COLUMNS, RECORES_FIELDS);

		FileOutputStream fileOut = new FileOutputStream("E:/test.xls");
		workbook.write(fileOut);
		fileOut.close();
	}
	
}
