package com.andieguo.poi.dao;

import java.util.List;

import org.json.JSONException;

public interface ProvinceDao {

	public List<String> findAll() throws JSONException;
}
