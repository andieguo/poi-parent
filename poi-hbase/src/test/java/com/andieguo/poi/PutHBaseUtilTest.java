package com.andieguo.poi;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.andieguo.poi.util.Constants;

import junit.framework.TestCase;

public class PutHBaseUtilTest extends TestCase{

	public void testParseFile(){
		try {
			InputStream input = PutHBaseUtil.class.getClassLoader().getResourceAsStream("北京-餐饮服务;茶艺;茶艺-0.json");
			List<PoiBean> poiBeans = PutHBaseUtil.parseFile(input);
			System.out.println(poiBeans.size());
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testListFile() throws Exception{
		PutHBaseUtil putHBaseUtil = new PutHBaseUtil();
		//遍历家目录下的poi-data目录
		File home = new File(Constants.DATAPATH);//例如C:\Users\andieguo\poi-data
		putHBaseUtil.listFile(home);
	}
}
