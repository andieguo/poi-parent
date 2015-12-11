package com.andieguo.poi;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.andieguo.poi.dao.POIDao;
import com.andieguo.poi.load.LoadUtil;
import com.andieguo.poi.pojo.POI;

import junit.framework.TestCase;

public class LoadUtilTest extends TestCase {

	private POIDao poiDao ;
	@SuppressWarnings("resource")
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		poiDao = (POIDao) context.getBean("poiDao");
	}
	
	public void testLoadFromXlsx(){
		LoadUtil load = new LoadUtil();
		try {
			System.out.println(getClass().getClassLoader().getResource("poi.xlsx").toString());
			int count = load.loadFromXlsx(getClass().getClassLoader().getResourceAsStream("poi.xlsx"),poiDao);
			System.out.println("count:"+count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testReadAll(){
		for(POI poi : poiDao.findAll()){
			System.out.println(poi);
		}
	}
	
	public void testFindByType(){
		List<POI> poiList = poiDao.findByType(0);
		System.out.println(poiList.size());
		for(POI poi : poiList){
			System.out.println(poi);
		}
	}
	
	public void testFindByTypeAndKey(){
		List<POI> poiList = poiDao.findByTypeAndKey(2,"餐饮服务");
		System.out.println(poiList.size());
		for(POI poi : poiList){
			System.out.println(poi);
		}
	}

	
}
