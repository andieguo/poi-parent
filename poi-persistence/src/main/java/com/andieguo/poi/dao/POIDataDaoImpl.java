package com.andieguo.poi.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.andieguo.poi.pojo.POIData;

public class POIDataDaoImpl extends JdbcDaoSupport implements POIDataDao {

	@Override
	public int save(POIData poiData) {
		// TODO Auto-generated method stub
		System.out.println("save:"+poiData.toString());
		return getJdbcTemplate().update(
				"insert into tb_poidata (name,address,telephone,lat,lng,levelOne,levelTwo,levelThree,city) values (?,?,?,?,?,?,?,?,?)",
				new Object[] { poiData.getName(),poiData.getAddress(),poiData.getTelephone(),poiData.getLat(),poiData.getLng(),poiData.getLevelOne(),
						poiData.getLevelTwo(),poiData.getLevelThree(),poiData.getCity()});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<POIData> findLevelOne(String levelOne) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query(
				"select * from tb_poidata where levelOne = ?", new Object[] {levelOne},
				new BeanPropertyRowMapper(POIData.class));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<POIData> findLevelTwo(String levelTwo) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query(
				"select * from tb_poidata where levelTwo = ?", new Object[] {levelTwo},
				new BeanPropertyRowMapper(POIData.class));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<POIData> findLevelThree(String levelThree) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query(
				"select * from tb_poidata where levelThree = ?", new Object[] {levelThree},
				new BeanPropertyRowMapper(POIData.class));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<POIData> findLevelOneAndCity(String levelOne, String city) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query(
				"select * from tb_poidata where levelOne = ? and city=?", new Object[] {levelOne,city},
				new BeanPropertyRowMapper(POIData.class));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<POIData> findLevelTwoAndCity(String levelTwo, String city) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query(
				"select * from tb_poidata where levelTwo = ? and city=?", new Object[] {levelTwo,city},
				new BeanPropertyRowMapper(POIData.class));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<POIData> findLevelThreeAndCity(String levelThree, String city) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query(
				"select * from tb_poidata where levelThree = ? and city=?", new Object[] {levelThree,city},
				new BeanPropertyRowMapper(POIData.class));
	}

}
