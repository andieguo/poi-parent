package com.andieguo.poi;

import java.util.List;

import junit.framework.TestCase;

public class QueryWTableUtilTest extends TestCase {

	public void testQueryByCircleAndtypeA(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findByCircleAndtypeA("tb_poi", "餐饮服务", 39.983168, 116.376796,100);
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
			List<PoiBean> poiBeans = queryHBaseUtil.findByCircleAndtypeALocal("tb_poi", "餐饮服务", 39.983168, 116.376796,3000);
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
			List<PoiBean> poiBeans = queryHBaseUtil.findByNearbyAndtypeA("tb_poi", "餐饮服务", 39.983168, 116.376796);
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
			List<PoiBean> poiBeans = queryHBaseUtil.findByNearbyAndtypeB("tb_poi", "餐饮服务","快餐", 39.983168, 116.376796);
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
			List<PoiBean> poiBeans = queryHBaseUtil.findByNearbyAndtypeC("tb_poi", "餐饮服务","快餐","必胜客", 39.983168, 116.376796);
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
			List<PoiBean> poiBeans = queryHBaseUtil.findBytypeA("tb_poi","餐饮服务");
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
			List<PoiBean> poiBeans = queryHBaseUtil.findBytypeB("tb_poi","餐饮服务","快餐");
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
			List<PoiBean> poiBeans = queryHBaseUtil.findBytypeC("tb_poi","餐饮服务","快餐","必胜客");
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
			List<PoiBean> poiBeans = queryHBaseUtil.findByCityAndtypeA("tb_poi", "餐饮服务", "北京");
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
			List<PoiBean> poiBeans = queryHBaseUtil.findByCityAndtypeC("tb_poi", "餐饮服务","快餐","必胜客", "北京");
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
