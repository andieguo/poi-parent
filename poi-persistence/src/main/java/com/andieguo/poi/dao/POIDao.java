package com.andieguo.poi.dao;

import java.util.List;

import com.andieguo.poi.pojo.POI;

public interface POIDao {

	public  List<POI> findAll();
	
	public POI findById(int id);
	
	public List<POI> findByKey(String key);

	public List<POI> findByType(int type);
	
	public List<POI> findByTypeAndKey(int type,String key);
	
	public int save(POI poi);
	
	public int delete(int id);
	
	public int update(POI poi);
}
