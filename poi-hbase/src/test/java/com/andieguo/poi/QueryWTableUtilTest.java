package com.andieguo.poi;

import java.util.List;

import junit.framework.TestCase;

public class QueryWTableUtilTest extends TestCase {

	public void testQueryByCircleAndtypeA(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findByCircleAndtypeA("poi_wtable", "餐饮服务", 39.983168, 116.376796,100);
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryByCircleAndtypeALocal(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findByCircleAndtypeALocal("poi_wtable", "餐饮服务", 39.983168, 116.376796,3000);
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryByNearbyAndtypeA(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findByNearbyAndtypeA("poi_wtable", "餐饮服务", 39.983168, 116.376796);
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryByNearbyAndtypeB(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findByNearbyAndtypeB("poi_wtable", "餐饮服务","快餐", 39.983168, 116.376796);
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryByNearbyAndtypeC(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findByNearbyAndtypeC("poi_wtable", "餐饮服务","快餐","必胜客", 39.983168, 116.376796);
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void testQueryTypeA(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findBytypeA("poi_wtable","餐饮服务");
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryTypeB(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findBytypeB("poi_wtable","餐饮服务","快餐");
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryTypeC(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findBytypeC("poi_wtable","餐饮服务","快餐","必胜客");
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryCityAndTypeA(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findByCityAndtypeA("poi_wtable", "住宿服务", "北京");
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryCityAndTypeC(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findByCityAndtypeC("poi_wtable", "餐饮服务","快餐","必胜客", "北京");
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
