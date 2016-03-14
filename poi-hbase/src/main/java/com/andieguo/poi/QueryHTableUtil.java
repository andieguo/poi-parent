package com.andieguo.poi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

import com.andieguo.poi.geohash.GeoHash;

/**
 * 高表查询
 * 
 * @author andieguo
 * 
 */
public class QueryHTableUtil {

	private HConnection connection;

	public QueryHTableUtil() throws Exception {
		connection = HConnectionSingle.getHConnection();
	}
	
	public List<PoiBean> findByCityAndtypeA(String tableName,String typeA,String city){
		return null;
	}
	
	public List<PoiBean> findByCityAndtypeB(String tableName,String typeB,String city){
		return null;
	}
	
	public List<PoiBean> findByCityAndtypeC(String tableName,String typeC,String city){
		return null;
	}
	
	public List<PoiBean> findByNearbyAndtypeA(String tableName,Integer cache,String typeA,double lat,double lng){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		List<PoiBean> poiBeanList =  new ArrayList<PoiBean>();
		ResultScanner rs = null;
		HTableInterface table = null;
		try {
			String geohash = GeoHash.geoHashStringWithCharacterPrecision(lat, lng, 4);//根据经纬度生成geohash字符串
			GeoHash[] adjacent = GeoHash.fromGeohashString(geohash).getAdjacent();//根据geohash字符串找到附近的8个小方块
			table = connection.getTable(tableName);
			for(int i=0;i<adjacent.length;i++){
				System.out.println(adjacent[i].toBase32());
				byte[] startkey = BytesUtil.startkeyGen(1,typeA,adjacent[i].toBase32());
				byte[] endkey = BytesUtil.endkeyGen(1,typeA,adjacent[i].toBase32());
				Scan scan = new Scan(startkey, endkey);
				scan.setCaching(cache);
				rs = table.getScanner(scan);
				poiBeans = putPoiBean(tableName,cache, startkey, endkey);
				poiBeanList.addAll(poiBeans);
			}
			if (rs != null) rs.close();
			if (table != null) table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeanList;
	}


	/**
	 * 测试成功：根据小类型获取所有的POI
	 * @param tableName
	 * @param typeA
	 * @return
	 */
	public List<PoiBean> findBytypeC(String tableName,Integer cache,String typeC){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		try {
			byte[] startkey = BytesUtil.startkeyGen(typeC);
			byte[] endkey = BytesUtil.endkeyGen(typeC);
			poiBeans = putPoiBean(tableName, cache,startkey, endkey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeans;
	}
	
	/**
	 * 测试成功：根据中类型获取所有的POI
	 * @param tableName
	 * @param typeA
	 * @return
	 */
	public List<PoiBean> findBytypeB(String tableName,Integer cache,String typeB){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		try {
			byte[] startkey = BytesUtil.startkeyGen(typeB);
			byte[] endkey = BytesUtil.endkeyGen(typeB);
			poiBeans = putPoiBean(tableName,cache, startkey, endkey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeans;
	}
	
	/**
	 * 测试成功：根据大类型获取所有的POI
	 * @param tableName
	 * @param typeA
	 * @return
	 */
	public List<PoiBean> findBytypeA(String tableName,Integer cache,String typeA){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		try {
			byte[] startkey = BytesUtil.startkeyGen(typeA);
			byte[] endkey = BytesUtil.endkeyGen(typeA);
			poiBeans = putPoiBean(tableName,cache, startkey, endkey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeans;
	}

	/**
	 * 根据startkey和endkey查询数据并填充到PoiBean集合
	 * @param tableName
	 * @param startkey
	 * @param endkey
	 * @return
	 * @throws IOException
	 */
	private List<PoiBean> putPoiBean(String tableName,Integer cache, byte[] startkey, byte[] endkey) throws IOException {
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		HTableInterface table = connection.getTable(tableName);
		Scan scan = new Scan(startkey, endkey);
		scan.setCaching(cache);
		ResultScanner rs = table.getScanner(scan);
		for (Result row : rs) {
			PoiBean poiBean = new PoiBean();
			for (Map.Entry<byte[], byte[]> entry : row.getFamilyMap("info".getBytes()).entrySet()) {
				String column = new String(entry.getKey());
				if(column.equals("name")) poiBean.setName(Bytes.toString(entry.getValue()));
				if(column.equals("address")) poiBean.setAddress(Bytes.toString(entry.getValue()));
				if(column.equals("telephone")) poiBean.setTelephone(Bytes.toString(entry.getValue()));
				if(column.equals("lat")) poiBean.setLat(Bytes.toDouble(entry.getValue()));
				if(column.equals("lng")) poiBean.setLng(Bytes.toDouble(entry.getValue()));
			}
			poiBeans.add(poiBean);
		}
		if (rs != null) rs.close();
		if (table != null) table.close();
		return poiBeans;
	}

}
