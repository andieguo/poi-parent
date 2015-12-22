package com.andieguo.poi.geohash.query;

import java.util.List;

import com.andieguo.poi.geohash.GeoHash;
import com.andieguo.poi.geohash.WGS84Point;

public interface GeoHashQuery {

	/**
	 * check wether a geohash is within the hashes that make up this query.
	 */
	public boolean contains(GeoHash hash);

	/**
	 * returns whether a point lies within a query.
	 */
	public boolean contains(WGS84Point point);

	/**
	 * should return the hashes that re required to perform this search.
	 */
	public List<GeoHash> getSearchHashes();

	public String getWktBox();

}