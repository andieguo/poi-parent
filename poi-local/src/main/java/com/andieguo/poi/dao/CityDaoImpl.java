package com.andieguo.poi.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.andieguo.poi.util.FileUtil;

public class CityDaoImpl implements CityDao {

	@Override
	public List<String> findAll() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		String result = FileUtil.readFile(getClass().getClassLoader().getResourceAsStream("city.json"));
		JSONObject jsonObj = new JSONObject(result);
		JSONArray array = jsonObj.getJSONArray("城市代码");
		for(int i=0;i<array.length();i++){
			JSONObject obj = array.getJSONObject(i);
			JSONArray cityArray = obj.getJSONArray("市");
			for(int j=0;j<cityArray.length();j++){
				JSONObject cityObj = cityArray.getJSONObject(j);
				list.add(cityObj.getString("市名"));
			}
		}
		return list;
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		CityDao cityDao = (CityDao) context.getBean("cityDao");
		System.out.println(cityDao.findAll().size());
		System.out.println(cityDao.findAll());
	}

}
