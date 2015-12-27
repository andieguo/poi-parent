package com.andieguo.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.andieguo.poi.geohash.GeoHash;
import com.andieguo.poi.util.FileUtil;

public class PutHBaseUtil {
	
	private HConnection connection;
	@SuppressWarnings("unused")
	private Logger logger;
	
	public PutHBaseUtil(String tableName) throws Exception{
		connection = HConnectionSingle.getHConnection();
		logger = Logger.getLogger(PutHBaseUtil.class);
	}
	
	/**
	 * 递归遍历file文件夹下的所有文件
	 * @param file
	 * @throws Exception
	 */
	public void listFile(File file) throws Exception{
		if(file.isFile()){//C:\Users\andieguo\poi-data\北京\餐饮服务;茶艺;茶艺\北京-餐饮服务;茶艺;茶艺-0.json
			List<PoiBean> poiBeans = parseFile(file);
			putWRow("tb_poi",poiBeans);
		}else if(file.isDirectory()){//目录
			File[] files = file.listFiles();  
			for (int i = 0; i < files.length; i++) {
				listFile(files[i]);//递归
			}
		}
	}
	/**
	 * 宽表构建
	 * @param types
	 * @param poiBeans
	 * @throws IOException
	 */
	public void putWRow(String tableName,List<PoiBean> poiBeans) throws IOException {
		HTableInterface table = connection.getTable(tableName);
		for(int j=0;j<poiBeans.size();j++){
			PoiBean poiBean = poiBeans.get(j);
			String[] types = poiBean.getType().split(";");
			byte[] rowkey = BytesUtil.startkeyGen(3,types[0],types[1],types[2],poiBean.getGeohash(),poiBean.getUid());
			Put putRow = new Put(rowkey);//一条记录就生成一条rowkey
			if(!poiBean.getName().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("name"),Bytes.toBytes(poiBean.getName()));
			if(!poiBean.getAddress().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("address"), Bytes.toBytes(poiBean.getAddress()));
			if(!poiBean.getTelephone().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("telephone"), Bytes.toBytes(poiBean.getTelephone()));
			putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lng"), Bytes.toBytes(poiBean.getLng()));
			putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lat"), Bytes.toBytes(poiBean.getLat()));
			putRow.add(Bytes.toBytes("info"), Bytes.toBytes("city"), Bytes.toBytes(poiBean.getCity()));
			putRow.add(Bytes.toBytes("info"), Bytes.toBytes("type"), Bytes.toBytes(poiBean.getType()));
			putRow.add(Bytes.toBytes("info"), Bytes.toBytes("geohash"), Bytes.toBytes(poiBean.getGeohash()));
			table.put(putRow);
		}
		if(table != null) table.close();
	}
	/**
	 * 高表构建
	 * @param typeInput
	 * @param poiBeans
	 * @throws IOException
	 */
	public void putHRow(String tableName,List<PoiBean> poiBeans) throws IOException{
		HTableInterface table = connection.getTable(tableName);
		for(int i=0;i<poiBeans.size();i++){
			PoiBean poiBean = poiBeans.get(i);
			String[] types = poiBean.getType().split(";");
			for(int j=0;j<types.length;j++){
				if(j == 1 && types[1] == types[2]) continue;
				byte[] rowkey = BytesUtil.startkeyGen(1,types[j],poiBean.getGeohash(),poiBean.getUid());
				Put putRow = new Put(rowkey);//一条记录就生成一条rowkey
				if(!poiBean.getName().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("name"),Bytes.toBytes(poiBean.getName()));
				if(!poiBean.getAddress().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("address"), Bytes.toBytes(poiBean.getAddress()));
				if(!poiBean.getTelephone().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("telephone"), Bytes.toBytes(poiBean.getTelephone()));
				putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lng"), Bytes.toBytes(poiBean.getLng()));
				putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lat"), Bytes.toBytes(poiBean.getLat()));
				putRow.add(Bytes.toBytes("info"), Bytes.toBytes("city"), Bytes.toBytes(poiBean.getCity()));
				putRow.add(Bytes.toBytes("info"), Bytes.toBytes("type"), Bytes.toBytes(poiBean.getType()));
				putRow.add(Bytes.toBytes("info"), Bytes.toBytes("geohash"), Bytes.toBytes(poiBean.getGeohash()));
				table.put(putRow);
			}
		}
		if(table != null) table.close();
	}
	
	/**
	 * 解析JSON文件，并装载PoiBean到集合
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static List<PoiBean> parseFile(File file) throws Exception{
		String result = FileUtil.readFile(new FileInputStream(file));//读取JSON文件为字符串
		JSONObject jsonResult = new JSONObject(result);
		JSONArray array = jsonResult.getJSONArray("results");
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		String[] names = file.getName().split("-");//北京-餐饮服务;茶艺;茶艺-0.json
		if(array.length() > 0){
			for(int i=0;i<array.length();i++){
				String telephone="",name="",address="",uid="";
				JSONObject obj = array.getJSONObject(i);
				if(!obj.isNull("uid")) uid = obj.getString("uid");
				if(!obj.isNull("address")) address = obj.getString("address");
				JSONObject location = obj.getJSONObject("location");
				double lng = location.getDouble("lng");
				double lat = location.getDouble("lat");
				String geohash = GeoHash.geoHashStringWithCharacterPrecision(lat,lng,12);
				if(!obj.isNull("name")) name = obj.getString("name");
				if(!obj.isNull("telephone")) telephone = obj.getString("telephone");
				PoiBean poiBean = new PoiBean(uid, address, name, telephone, lng, lat,names[0],names[1],geohash);
				poiBeans.add(poiBean);
			}
		}
		return poiBeans;
	}
}
