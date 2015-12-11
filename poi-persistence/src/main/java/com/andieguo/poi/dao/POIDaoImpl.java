package com.andieguo.poi.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.andieguo.poi.pojo.POI;

public class POIDaoImpl extends JdbcDaoSupport implements POIDao {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<POI> findAll() {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query("select * from tb_poi",
				new BeanPropertyRowMapper(POI.class));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public POI findById(int id) {
		// TODO Auto-generated method stub
		return (POI)getJdbcTemplate().queryForObject(
				"select * from tb_poi where id = ?", new Object[] { id },
				new BeanPropertyRowMapper(POI.class));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<POI> findByKey(String key) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query(
				"select * from tb_poi where poikey = ?", new Object[] { key },
				new BeanPropertyRowMapper(POI.class));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<POI> findByType(int type) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query(
				"select * from tb_poi where poitype = ?", new Object[] { type },
				new BeanPropertyRowMapper(POI.class));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<POI> findByTypeAndKey(int type,String key) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().query(
				"select * from tb_poi where poitype = ? and poikey = ?", new Object[] { type ,key},
				new BeanPropertyRowMapper(POI.class));
	}
	

	@Override
	public int save(POI poi) {
		// TODO Auto-generated method stub
		System.out.println("save:"+poi.toString());
		return getJdbcTemplate().update(
				"insert into tb_poi (poikey,poivalue,poitype) values (?,?,?)",
				new Object[] { poi.getPoikey(), poi.getPoivalue(),poi.getPoitype() });
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().update("delete from tb_poi where id = ?",
				new Object[] { id });
	}

	@Override
	public int update(POI poi) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().update(
				"update tb_poi set poikey = ?,poivalue=? ,poitype=? where id = ?",
				new Object[] { poi.getPoikey(), poi.getPoivalue(), poi.getPoitype(),poi.getId() });
	}



}
