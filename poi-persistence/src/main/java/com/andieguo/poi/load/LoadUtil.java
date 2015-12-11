package com.andieguo.poi.load;

import java.io.InputStream;
import java.util.List;

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
}
