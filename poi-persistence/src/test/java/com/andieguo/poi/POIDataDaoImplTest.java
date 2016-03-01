package com.andieguo.poi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.andieguo.poi.dao.POIDataDao;
import com.andieguo.poi.pojo.POIData;

import junit.framework.TestCase;

public class POIDataDaoImplTest  extends TestCase{

	private POIDataDao poiDataDao;
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		poiDataDao  = (POIDataDao) context.getBean("poiDataDao");
	}
	
	public void testSave(){
		int count = poiDataDao.save(new POIData("1", "2", "3", "4", "5", "6", 11.00, 22.0, "2"));
		System.out.println(count);
	}
	
	

}
