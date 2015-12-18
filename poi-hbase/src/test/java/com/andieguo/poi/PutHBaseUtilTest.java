package com.andieguo.poi;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.andieguo.poi.geohash.GeoHash;
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
				System.out.println(GeoHash.geoHashStringWithCharacterPrecision(poiBean.getLat(), poiBean.getLng(), 12));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testListFile() throws Exception{
		PutHBaseUtil putHBaseUtil = new PutHBaseUtil("tb_poi");
		//遍历家目录下的poi-data目录
		File home = new File(Constants.DATAPATH+File.separator+"北京"+File.separator+"餐饮服务;快餐;茶餐厅");//例如C:\Users\andieguo\poi-data
		putHBaseUtil.listFile(home);
	}
}
