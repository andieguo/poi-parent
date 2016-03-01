package com.andieguo.poi;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.andieguo.poi.dao.CityDao;
import com.andieguo.poi.dao.POIDao;
import com.andieguo.poi.geohash.GeoHash;
import com.andieguo.poi.pojo.POI;
import com.andieguo.poi.util.Constants;
import com.andieguo.poi.util.Record;

import junit.framework.TestCase;

public class PutHBaseUtilTest extends TestCase{
	
	private POIDao poiDao;
	private CityDao cityDao;
	@SuppressWarnings("resource")
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		cityDao = (CityDao) context.getBean("cityDao");
		poiDao = (POIDao) context.getBean("poiDao");
	}
	
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
		PutHBaseUtil putHBaseUtil = new PutHBaseUtil();
		HTableInterface table = HConnectionSingle.getHConnection().getTable("gw-index");
		List<String> cityList = cityDao.findAll();
		List<POI> poiList = poiDao.findByType(0);
		Map<String,Record> maps = new TreeMap<String,Record>();
		//遍历家目录下的poi-data目录
		long starttime = System.currentTimeMillis();
		for(String city : cityList){
			for(POI poi : poiList){
				File home = new File(Constants.DATAPATH+File.separator+city+File.separator+poi.getPoivalue());//例如C:\Users\andieguo\poi-data
				putHBaseUtil.listFile(home,"wtable",table);
			}
			long endtime = System.currentTimeMillis();
			long fileNumber = putHBaseUtil.getFileNumber();
			long poiNumber = putHBaseUtil.getPoiNumber();
			Record record = new Record(endtime-starttime,poiNumber,fileNumber);
			new File(Constants.MKDIRPATH + File.separator + "loadrecord.xls");
			maps.put(city,record);
		}
	}
}
