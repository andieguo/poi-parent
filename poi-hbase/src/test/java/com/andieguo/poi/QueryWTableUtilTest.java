package com.andieguo.poi;

import java.util.List;

import junit.framework.TestCase;

public class QueryWTableUtilTest extends TestCase {

	public void testQueryByCircleAndtypeA(){
		try {
			long starttime = System.currentTimeMillis();
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findByCircleAndtypeA("wtable-7", "住宿服务", 39.983168, 116.376796,300000);
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
			System.out.println("size------------->"+poiBeans.size());
			long endtime = System.currentTimeMillis();
			System.out.println("time:"+(endtime-starttime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryByCircleAndtypeALocal(){
		try {
			long starttime = System.currentTimeMillis();
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findByCircleAndtypeALocal("wtable", "住宿服务", 39.983168, 116.376796,1000);
			for(PoiBean poiBean : poiBeans){
				System.out.println(poiBean);
			}
			System.out.println("size------------->"+poiBeans.size());
			long endtime = System.currentTimeMillis();
			System.out.println("time:"+(endtime-starttime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryByNearbyAndtypeA(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findByNearbyAndtypeA("wtable-6",100, "餐饮服务", 39.983168, 116.376796);
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			System.out.println("size------------->"+poiBeans.size());
			long endtime = System.currentTimeMillis();
			System.out.println("time:"+(endtime-starttime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryByNearbyAndtypeB(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findByNearbyAndtypeB("wtable-6",100, "餐饮服务","快餐", 39.983168, 116.376796);
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			System.out.println("size------------->"+poiBeans.size());
			long endtime = System.currentTimeMillis();
			System.out.println("time:"+(endtime-starttime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryByNearbyAndtypeC(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findByNearbyAndtypeC("wtable-6", 100,"餐饮服务","快餐","肯德基", 39.983168, 116.376796);
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			System.out.println("size------------->"+poiBeans.size());
			long endtime = System.currentTimeMillis();
			System.out.println("time:"+(endtime-starttime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void testQueryTypeA(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findBytypeA("wtable-6",10000,"餐饮服务");
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			long endtime = System.currentTimeMillis();
			System.out.println("size:"+poiBeans.size());
			System.out.println("time:"+(endtime-starttime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryTypeB(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findBytypeB("wtable-6",1000,"餐饮服务","快餐");
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			long endtime = System.currentTimeMillis();
			System.out.println("size:"+poiBeans.size());
			System.out.println("time:"+(endtime-starttime));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryTypeC(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findBytypeC("wtable-6",100,"餐饮服务","快餐","肯德基");
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			long endtime = System.currentTimeMillis();
			System.out.println("size:"+poiBeans.size());
			System.out.println("time:"+(endtime-starttime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryCityAndTypeA(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findByCityAndtypeA(1000,"wtable-7", "体育休闲服务", "北京");//餐饮服务-体育休闲服务
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			System.out.println("size------------->"+poiBeans.size());
			long endtime = System.currentTimeMillis();
			System.out.println(endtime-starttime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryCityAndTypeB(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findByCityAndtypeB(1000,"wtable-1", "餐饮服务","快餐", "北京");
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			System.out.println("size------------->"+poiBeans.size());
			long endtime = System.currentTimeMillis();
			System.out.println(endtime-starttime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testQueryCityAndTypeC(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			long starttime = System.currentTimeMillis();
			List<PoiBean> poiBeans = queryHBaseUtil.findByCityAndtypeC(1000,"wtable-7", "餐饮服务","快餐","必胜客", "北京");
//			for(PoiBean poiBean : poiBeans){
//				System.out.println(poiBean);
//			}
			System.out.println("size------------->"+poiBeans.size());
			long endtime = System.currentTimeMillis();
			System.out.println(endtime-starttime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
