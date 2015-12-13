package com.andieguo.poi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.andieguo.poi.dao.POIDao;
import com.andieguo.poi.pojo.POI;

import junit.framework.TestCase;

public class POIDaoImplTest extends TestCase {

	private POIDao poiDao ;
	@SuppressWarnings("resource")
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		poiDao = (POIDao) context.getBean("poiDao");
	}

	public void testSave(){
		System.out.println(poiDao.save(new POI("郭栋","test",0)));
	}
	
	public void testFindById(){
		System.out.println(poiDao.findById(1));
	}
	
	public void testFindByType(){
		System.out.println(poiDao.findByType(0).size());
		for(POI poi:poiDao.findByType(0)){
			System.out.println(poi);
		}
	}
	
	public void testFindByKey(){
		for(POI poi:poiDao.findByKey("郭栋")){
			System.out.println(poi);
		}
	}
	
	public void testFindAll(){
		for(POI poi:poiDao.findAll()){
			System.out.println(poi);
		}
	}
	
	public void testUpdate(){
		POI poi = poiDao.findById(1549);
		poi.setPoikey("test1");
		poi.setPoivalue("test1");
		poiDao.update(poi);
		System.out.println(poiDao.findByKey("test1"));
	}
	
	public void testDelete(){
		System.out.println(poiDao.delete(2));
	}
}
