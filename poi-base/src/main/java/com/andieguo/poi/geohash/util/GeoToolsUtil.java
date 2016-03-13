package com.andieguo.poi.geohash.util;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Geometry;
//import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;

public class GeoToolsUtil {

	public static void main(String[] args) throws Exception {
		//在构成一个面的时候，第一个点的经纬度一定要与最后一个点的经纬度相同。
		String wktPoly = "POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))"; // 请自行搜素了解wkt格式
		String wktPoint = "POINT (30 30)";
		WKTReader reader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
		// GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		Geometry point = reader.read(wktPoint);
		Geometry poly = reader.read(wktPoly);
		System.out.println(poly.intersects(point)); // 返回true或false
	}
}
