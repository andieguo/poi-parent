package com.andieguo.poi;

import java.util.List;

import junit.framework.TestCase;

public class QueryWTableUtilTest extends TestCase {

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
	
	public void testQueryCityAndTypeC(){
		try {
			QueryWTableUtil queryHBaseUtil  = new QueryWTableUtil();
			List<PoiBean> poiBeans = queryHBaseUtil.findByCityAndtypeC("tb_poi", "餐饮服务", "茶艺", "茶艺", "武汉");
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
