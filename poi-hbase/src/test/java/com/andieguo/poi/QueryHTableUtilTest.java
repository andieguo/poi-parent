package com.andieguo.poi;

import java.util.List;

import org.mortbay.util.ajax.AjaxFilter;

import com.andieguo.poi.geohash.GeoHash;

import junit.framework.TestCase;

public class QueryHTableUtilTest extends TestCase {

	public void testQueryTypeA(){
		try {
			QueryHTableUtil queryHBaseUtil  = new QueryHTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findBytypeA("htable-6",10000,"餐饮服务");
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			long endtime = System.currentTimeMillis();
			System.out.println("time------------->"+(endtime-starttime));
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryTypeB(){
		try {
			QueryHTableUtil queryHBaseUtil  = new QueryHTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findBytypeB("htable-6",1000,"快餐");
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			long endtime = System.currentTimeMillis();
			System.out.println("time------------->"+(endtime-starttime));
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryTypeC(){
		try {
			QueryHTableUtil queryHBaseUtil  = new QueryHTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findBytypeC("htable-6",100,"肯德基");
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			long endtime = System.currentTimeMillis();
			System.out.println("time------------->"+(endtime-starttime));
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryNearByTypeA(){
		try {
			QueryHTableUtil queryHBaseUtil  = new QueryHTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findByNearbyAndtypeA("htable-6",100,"餐饮服务", 39.983168, 116.376796);
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			long endtime = System.currentTimeMillis();
			System.out.println("time------------->"+(endtime-starttime));
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryNearByTypeB(){
		try {
			QueryHTableUtil queryHBaseUtil  = new QueryHTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findByNearbyAndtypeA("htable-6",100,"快餐", 39.983168, 116.376796);
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			long endtime = System.currentTimeMillis();
			System.out.println("time------------->"+(endtime-starttime));
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryNearByTypeC(){
		try {
			QueryHTableUtil queryHBaseUtil  = new QueryHTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findByNearbyAndtypeA("htable-6",100,"肯德基", 39.983168, 116.376796);
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			long endtime = System.currentTimeMillis();
			System.out.println("time------------->"+(endtime-starttime));
			System.out.println("size------------->"+poiBeans.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryGeohash(){
		String geohash = GeoHash.geoHashStringWithCharacterPrecision(39.983168, 116.376796, 5);//根据经纬度生成geohash字符串
		GeoHash[] adjacent = GeoHash.fromGeohashString(geohash).getAdjacent();//根据geohash字符串找到附近的8个小方块
		for(GeoHash geo : adjacent){
			System.out.println(geo.toBase32());
		}
		
	}
}
