package com.andieguo.poi.dao;

import java.util.List;

import com.andieguo.poi.pojo.POIData;

public interface POIDataDao {

	public int save(POIData poiData);
	
	public List<POIData> findLevelOne(String levelOne);
	
	public List<POIData> findLevelTwo(String levelTwo);
	
	public List<POIData> findLevelThree(String levelThree);
	
	public List<POIData> findLevelOneAndCity(String levelOne,String city);
	
	public List<POIData> findLevelTwoAndCity(String levelTwo,String city);
	
	public List<POIData> findLevelThreeAndCity(String levelThree,String city);
}
