package com.andieguo.poi;

import java.io.File;
import java.util.List;

import com.andieguo.poi.geohash.GeoHash;
import com.andieguo.poi.util.Constants;

import junit.framework.TestCase;

public class PutHBaseUtilTest extends TestCase{
	
	public void testParseFile(){
		try {
			String path = PutHBaseUtil.class.getClassLoader().getResource("北京-餐饮服务;茶艺;茶艺-0.json").getPath();
			path = java.net.URLDecoder.decode(path,"utf-8"); 
			System.out.println(path);
			List<PoiBean> poiBeans = PutHBaseUtil.parseFile(new File(path));
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
		String[] citys = new String[]{"北京","武汉"};
		String[] pois = new String[]{"餐饮服务;咖啡;咖啡","餐饮服务;快餐;必胜客","餐饮服务;快餐;茶餐厅","餐饮服务;快餐;大家乐"};
		//遍历家目录下的poi-data目录
		for(String city : citys){
			for(String poi : pois){
				File home = new File(Constants.DATAPATH+File.separator+city+File.separator+poi);//例如C:\Users\andieguo\poi-data
				putHBaseUtil.listFile(home);
			}
		}
	}
}
