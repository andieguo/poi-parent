package com.andieguo.poi;

import com.andieguo.poi.geohash.GeoHash;

import junit.framework.TestCase;

public class GeoHashTest extends TestCase {
	
	public void testGeoHash() {
		System.out.println(GeoHash.withCharacterPrecision(39.905994,116.399820, 12).toBase32());
		System.out.println(GeoHash.geoHashStringWithCharacterPrecision(39.905994,116.399820, 12));
	}
	
}
