/*
 * Copyright 2010, Silvio Heuberger @ IFS www.ifs.hsr.ch
 *
 * This code is release under the LGPL license.
 * You should have received a copy of the license
 * in the LICENSE file. If you have not, see
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package com.andieguo.poi.geohash.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.andieguo.poi.geohash.BoundingBox;
import com.andieguo.poi.geohash.GeoHash;
import com.andieguo.poi.geohash.WGS84Point;
import com.andieguo.poi.geohash.util.GeoHashSizeTable;

/**
 * 该类返回一系列geohash集合，覆盖一个特定的矩形框。
 * This class returns the hashes covering a certain bounding box. There are
 * either 1,2 or 4 susch hashes, depending on the position of the bounding box
 * on the geohash grid.
 */
public class GeoHashBoundingBoxQuery implements GeoHashQuery, Serializable {
	private static final long serialVersionUID = 9223256928940522683L;
	/* there's not going to be more than 4 hashes. */
	private List<GeoHash> searchHashes = new ArrayList<>(4);
	/* the combined bounding box of those hashes. */
	private BoundingBox boundingBox;

	public GeoHashBoundingBoxQuery(BoundingBox bbox) {
		//根据矩形框获取适合的geohash精度
		int fittingBits = GeoHashSizeTable.numberOfBitsForOverlappingGeoHash(bbox);
		WGS84Point center = bbox.getCenterPoint();//获取矩形的中心点
		//根据经纬度+geohash精度获取geohash
		GeoHash centerHash = GeoHash.withBitPrecision(center.getLatitude(), center.getLongitude(), fittingBits);
		//geohash所在区域是否包含矩形框bbox
		if (hashFits(centerHash, bbox)) {//包含
			addSearchHash(centerHash);
		} else {//不包含
			expandSearch(centerHash, bbox);
		}
	}

	private void addSearchHash(GeoHash hash) {
		if (boundingBox == null) {
			boundingBox = new BoundingBox(hash.getBoundingBox());//创建一个矩形框
		} else {
			//合并矩形框
			boundingBox.expandToInclude(hash.getBoundingBox());
		}
		searchHashes.add(hash);//将hash添加到集合
	}

	private void expandSearch(GeoHash centerHash, BoundingBox bbox) {
		addSearchHash(centerHash);

		for (GeoHash adjacent : centerHash.getAdjacent()) {//获取到geohash的8个矩形框，执行遍历
			BoundingBox adjacentBox = adjacent.getBoundingBox();
			//adjacent所在区域与bbox相交，且目标集合中所在的geohash区域不存在，则添加
			if (adjacentBox.intersects(bbox) && !searchHashes.contains(adjacent)) {
				addSearchHash(adjacent);
			}
		}
	}
	/**
	 * geohash所在区域是否包含矩形框bbox
	 * @param hash
	 * @param bbox
	 * @return
	 */
	private boolean hashFits(GeoHash hash, BoundingBox bbox) {
		//geohash所在区域包含矩形框的右上点和左下点，即geohash所在区域包含矩形框bbox
		return hash.contains(bbox.getUpperLeft()) && hash.contains(bbox.getLowerRight());
	}

	@Override
	public boolean contains(GeoHash hash) {
		for (GeoHash searchHash : searchHashes) {
			if (hash.within(searchHash)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean contains(WGS84Point point) {
		return contains(GeoHash.withBitPrecision(point.getLatitude(), point.getLongitude(), 64));
	}

	@Override
	public List<GeoHash> getSearchHashes() {
		return searchHashes;
	}

	@Override
	public String toString() {
		StringBuilder bui = new StringBuilder();
		for (GeoHash hash : searchHashes) {
			bui.append(hash).append("\n");
		}
		return bui.toString();
	}

	@Override
	public String getWktBox() {
		return "BOX(" + boundingBox.getMinLon() + " " + boundingBox.getMinLat() + "," + boundingBox.getMaxLon() + " "
				+ boundingBox.getMaxLat() + ")";
	}
}
