package com.andieguo.poi.load;

import java.io.InputStream;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.andieguo.poi.dao.POIDao;
import com.andieguo.poi.pojo.POI;
import com.andieguo.poi.util.ExcelUtil;

public class LoadUtil {

	public int loadFromXlsx(InputStream input,POIDao poiDao) throws Exception{
		int count = 0;
		List<String> result = ExcelUtil.readXlsx(input);
		for(String key : result){
			String[] poi = key.split(";");
			if(poi.length == 3){
				count += poiDao.save(new POI(poi[2],key,0));
				count += poiDao.save(new POI(poi[1],key,1));
				count += poiDao.save(new POI(poi[0],key,2));
			}
		}
		return count;
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		LoadUtil load = new LoadUtil();
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		POIDao poiDao = (POIDao) context.getBean("poiDao");
		try {
			System.out.println(LoadUtil.class.getClassLoader().getResource("poi.xlsx").toString());
			int count = load.loadFromXlsx(LoadUtil.class.getClassLoader().getResourceAsStream("poi.xlsx"),poiDao);
			System.out.println("count:"+count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
