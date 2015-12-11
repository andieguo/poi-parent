package com.andieguo.poi.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andieguo.poi.util.FileUtil;

public class ProvinceDaoImpl implements ProvinceDao {

	public List<String> findAll() throws JSONException {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		String result = FileUtil.readFile(getClass().getClassLoader().getResourceAsStream("province.json"));
		JSONObject jsonObj = new JSONObject(result);
		JSONArray array = jsonObj.getJSONArray("provinces");
		for(int i=0;i<array.length();i++){
			JSONObject obj = array.getJSONObject(i);
			list.add(obj.getString("name"));
		}
		return list;
	}
	
	public static void main(String[] args) {
		ProvinceDao dao = new ProvinceDaoImpl();
		System.out.println(dao.findAll().size());
	}

}
