package com.andieguo.poi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;

import com.andieguo.poi.geohash.GeoHash;

/**
 * 宽表查询
 * 
 * @author andieguo
 * 
 */
public class QueryWTableUtil {

	private Configuration conf;

	public QueryWTableUtil() throws Exception {
		conf = ZHBaseConfiguration.getConfiguration();
	}
	
	public List<PoiBean> findByNearbyAndtypeA(String tableName,String typeA,double lat,double lng){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		ResultScanner rs = null;
		HTable table = null;
		try {
			String geohash = GeoHash.geoHashStringWithCharacterPrecision(lat, lng, 5);//根据经纬度生成geohash字符串
			GeoHash[] adjacent = GeoHash.fromGeohashString(geohash).getAdjacent();//根据geohash字符串找到附近的8个小方块
			table = new HTable(conf, tableName);
			for(int i=0;i<adjacent.length;i++){
				System.out.println(adjacent[i].toBase32());
				byte[] startkey = BytesUtil.startkeyGen(typeA);
				byte[] endkey = BytesUtil.endkeyGen(typeA);
				Filter filter = new RowFilter(CompareOp.EQUAL, new SubstringComparator(adjacent[i].toBase32()));
				Scan scan = new Scan(startkey, endkey);
				scan.setFilter(filter);
				rs = table.getScanner(scan);
				putRow(poiBeans, rs);
			}
			if (rs != null) rs.close();
			if (table != null) table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeans;
	}
	
	public List<PoiBean> findByNearbyAndtypeB(String tableName,String typeA,String typeB,double lat,double lng){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		ResultScanner rs = null;
		HTable table = null;
		try {
			String geohash = GeoHash.geoHashStringWithCharacterPrecision(lat, lng, 5);//根据经纬度生成geohash字符串
			GeoHash[] adjacent = GeoHash.fromGeohashString(geohash).getAdjacent();//根据geohash字符串找到附近的8个小方块
			table = new HTable(conf, tableName);
			for(int i=0;i<adjacent.length;i++){
				System.out.println(adjacent[i].toBase32());
				byte[] startkey = BytesUtil.startkeyGen(typeA,typeB);
				byte[] endkey = BytesUtil.endkeyGen(typeA,typeB);
				Filter filter = new RowFilter(CompareOp.EQUAL, new SubstringComparator(adjacent[i].toBase32()));
				Scan scan = new Scan(startkey, endkey);
				scan.setFilter(filter);
				rs = table.getScanner(scan);
				putRow(poiBeans, rs);
			}
			if (rs != null) rs.close();
			if (table != null) table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeans;
	}
	
	public List<PoiBean> findByNearbyAndtypeC(String tableName,String typeA,String typeB,String typeC,double lat,double lng){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		ResultScanner rs = null;
		HTable table = null;
		try {
			String geohash = GeoHash.geoHashStringWithCharacterPrecision(lat, lng, 5);//根据经纬度生成geohash字符串
			GeoHash[] adjacent = GeoHash.fromGeohashString(geohash).getAdjacent();//根据geohash字符串找到附近的8个小方块
			table = new HTable(conf, tableName);
			for(int i=0;i<adjacent.length;i++){
				System.out.println(adjacent[i].toBase32());
				byte[] startkey = BytesUtil.startkeyGen(3,typeA,typeB,typeC,adjacent[i].toBase32());
				byte[] endkey = BytesUtil.endkeyGen(3,typeA,typeB,typeC,adjacent[i].toBase32());
				Scan scan = new Scan(startkey, endkey);
				rs = table.getScanner(scan);
				putRow(poiBeans, rs);
			}
			if (rs != null) rs.close();
			if (table != null) table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeans;
	}

	/**
	 * 测试成功：根据大类型+city获取POI列表
	 * @param tableName
	 * @param typeA
	 * @param city
	 * @return
	 */
	public List<PoiBean> findByCityAndtypeA(String tableName,String typeA,String city){
		Filter filter = new SingleColumnValueFilter(Bytes.toBytes("info"),Bytes.toBytes("city"),CompareOp.EQUAL, Bytes.toBytes(city));
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		try {
			byte[] startkey = BytesUtil.startkeyGen(typeA);
			byte[] endkey = BytesUtil.endkeyGen(typeA);
			poiBeans = putPoiBean(tableName,filter,startkey, endkey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeans;
	}
	/**
	 * 测试成功：根据中类型+city获取POI列表
	 * @param tableName
	 * @param typeA
	 * @param typeB
	 * @param city
	 * @return
	 */
	public List<PoiBean> findByCityAndtypeB(String tableName,String typeA,String typeB,String city){
		Filter filter = new SingleColumnValueFilter(Bytes.toBytes("info"),Bytes.toBytes("city"),CompareOp.EQUAL, Bytes.toBytes(city));
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		try {
			byte[] startkey = BytesUtil.startkeyGen(typeA,typeB);
			byte[] endkey = BytesUtil.endkeyGen(typeA,typeB);
			poiBeans = putPoiBean(tableName,filter,startkey, endkey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeans;
	}
	
	/**
	 * 测试成功：根据小类型+city获取POI列表
	 * @param tableName
	 * @param typeA
	 * @param typeB
	 * @param typeC
	 * @param city
	 * @return
	 */
	public List<PoiBean> findByCityAndtypeC(String tableName,String typeA,String typeB,String typeC,String city){
		Filter filter = new SingleColumnValueFilter(Bytes.toBytes("info"),Bytes.toBytes("city"),CompareOp.EQUAL, Bytes.toBytes(city));
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		try {
			byte[] startkey = BytesUtil.startkeyGen(typeA,typeB,typeC);
			byte[] endkey = BytesUtil.endkeyGen(typeA,typeB,typeC);
			poiBeans = putPoiBean(tableName,filter,startkey, endkey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeans;
	}


	/**
	 * 测试成功：根据小类型获取所有的POI
	 * @param tableName
	 * @param typeA
	 * @return
	 */
	public List<PoiBean> findBytypeC(String tableName,String typeA,String typeB,String typeC){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		try {
			byte[] startkey = BytesUtil.startkeyGen(typeA,typeB,typeC);
			byte[] endkey = BytesUtil.endkeyGen(typeA,typeB,typeC);
			poiBeans = putPoiBean(tableName, startkey, endkey);
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
	public List<PoiBean> findBytypeB(String tableName,String typeA,String typeB){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		try {
			byte[] startkey = BytesUtil.startkeyGen(typeA,typeB);
			byte[] endkey = BytesUtil.endkeyGen(typeA,typeB);
			poiBeans = putPoiBean(tableName, startkey, endkey);
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
	public List<PoiBean> findBytypeA(String tableName,String typeA){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		try {
			byte[] startkey = BytesUtil.startkeyGen(typeA);
			byte[] endkey = BytesUtil.endkeyGen(typeA);
			poiBeans = putPoiBean(tableName, startkey, endkey);
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
	public List<PoiBean> putPoiBean(String tableName, byte[] startkey, byte[] endkey) throws IOException {
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		HTable table = new HTable(conf, tableName);
		Scan scan = new Scan(startkey, endkey);
		ResultScanner rs = table.getScanner(scan);
		putRow(poiBeans, rs);
		if (rs != null) rs.close();
		if (table != null) table.close();
		return poiBeans;
	}
	
	public List<PoiBean> putPoiBean(String tableName, Filter filter,byte[] startkey, byte[] endkey) throws IOException {
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		HTable table = new HTable(conf, tableName);
		Scan scan = new Scan(startkey, endkey);
		scan.setFilter(filter);
		ResultScanner rs = table.getScanner(scan);
		putRow(poiBeans, rs);
		if (rs != null) rs.close();
		if (table != null) table.close();
		return poiBeans;
	}
	
	private void putRow(List<PoiBean> poiBeans, ResultScanner rs) {
		for (Result row : rs) {
			PoiBean poiBean = new PoiBean();
			for (Map.Entry<byte[], byte[]> entry : row.getFamilyMap("info".getBytes()).entrySet()) {
				String column = new String(entry.getKey());
				if(column.equals("name")) poiBean.setName(Bytes.toString(entry.getValue()));
				if(column.equals("address")) poiBean.setAddress(Bytes.toString(entry.getValue()));
				if(column.equals("telephone")) poiBean.setTelephone(Bytes.toString(entry.getValue()));
				if(column.equals("lat")) poiBean.setLat(Bytes.toDouble(entry.getValue()));
				if(column.equals("lng")) poiBean.setLng(Bytes.toDouble(entry.getValue()));
				if(column.equals("type")) poiBean.setType(Bytes.toString(entry.getValue()));
				if(column.equals("city")) poiBean.setCity(Bytes.toString(entry.getValue()));
				if(column.equals("geohash")) poiBean.setGeohash(Bytes.toString(entry.getValue()));
			}
			poiBeans.add(poiBean);
		}
	}

}
