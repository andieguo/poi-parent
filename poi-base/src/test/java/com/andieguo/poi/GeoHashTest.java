package com.andieguo.poi;

import java.util.List;

import com.andieguo.poi.geohash.GeoHash;
import com.andieguo.poi.geohash.WGS84Point;
import com.andieguo.poi.geohash.query.GeoHashCircleQuery;

import junit.framework.TestCase;

public class GeoHashTest extends TestCase {
	
	public void testGeoHash() {
		System.out.println(GeoHash.withCharacterPrecision(39.905994,116.399820, 12).toBase32());
		System.out.println(GeoHash.withCharacterPrecision(39.905994,116.399820, 12));
		System.out.println(GeoHash.geoHashStringWithCharacterPrecision(39.905994,116.399820, 12));
	}
	
	public void testCircle(){
		WGS84Point center = new WGS84Point(39.983168, 116.376796);
		GeoHashCircleQuery geoHashCircleQuery = new GeoHashCircleQuery(center,30000);
		List<GeoHash> geohashList = geoHashCircleQuery.getSearchHashes();
		
		for(GeoHash geohash:geohashList){
			if(geohash.significantBits%5 != 0) geohash.significantBits = (byte) (5 + geohash.significantBits -geohash.significantBits % 5);
			System.out.println(geohash);
		}
	}
	
}
