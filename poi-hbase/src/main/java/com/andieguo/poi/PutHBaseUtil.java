package com.andieguo.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;

import com.andieguo.poi.geohash.GeoHash;
import com.andieguo.poi.util.FileUtil;

public class PutHBaseUtil {
	
	private HTable table;
	private Logger logger;
	
	public PutHBaseUtil(String tableName) throws Exception{
		Configuration conf = ZHBaseConfiguration.getConfiguration();
		table = new HTable(conf, tableName);
		//从类路径下加载配置文件
		PropertyConfigurator.configure(this.getClass().getClassLoader().getResourceAsStream("log4j/log4j.properties"));
		logger = Logger.getLogger(PutHBaseUtil.class);
	}
	
	/**
	 * 递归遍历file文件夹下的所有文件
	 * @param file
	 * @throws Exception
	 */
	public void listFile(File file) throws Exception{
		if(file.isFile()){//C:\Users\andieguo\poi-data\北京\餐饮服务;茶艺;茶艺\北京-餐饮服务;茶艺;茶艺-0.json
			String[] names = file.getName().split("-");
			if(names.length == 3){
				String[] types = names[1].split(";");
				InputStream input = new FileInputStream(file);
				List<PoiBean> poiBeans = parseFile(input);
				putWRow(types, poiBeans);
			}
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
	public void putWRow(String[] types, List<PoiBean> poiBeans) throws IOException {
		for(int j=0;j<poiBeans.size();j++){
			PoiBean poiBean = poiBeans.get(j);
			String geohash = GeoHash.geoHashStringWithCharacterPrecision(poiBean.getLat(), poiBean.getLng(), 12);
			byte[] rowkey = BytesUtil.startkeyGen(types[0],types[1],types[2],geohash,poiBean.getUid());
			Put putRow = new Put(rowkey);//一条记录就生成一条rowkey
			if(!poiBean.getName().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("name"),Bytes.toBytes(poiBean.getName()));
			if(!poiBean.getAddress().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("address"), Bytes.toBytes(poiBean.getAddress()));
			if(!poiBean.getTelephone().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("telephone"), Bytes.toBytes(poiBean.getTelephone()));
			putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lng"), Bytes.toBytes(poiBean.getLng()));
			putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lat"), Bytes.toBytes(poiBean.getLat()));
			table.put(putRow);
		}
	}
	/**
	 * 高表构建
	 * @param typeInput
	 * @param poiBeans
	 * @throws IOException
	 */
	public void putHRow(String typeInput,List<PoiBean> poiBeans) throws IOException{
		for(int j=0;j<poiBeans.size();j++){
			PoiBean poiBean = poiBeans.get(j);
			String geohash = GeoHash.geoHashStringWithCharacterPrecision(poiBean.getLat(), poiBean.getLng(), 12);
			byte[] rowkey = BytesUtil.startkeyGen(typeInput,geohash,poiBean.getUid());
			Put putRow = new Put(rowkey);//一条记录就生成一条rowkey
			if(!poiBean.getName().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("name"),Bytes.toBytes(poiBean.getName()));
			if(!poiBean.getAddress().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("address"), Bytes.toBytes(poiBean.getAddress()));
			if(!poiBean.getTelephone().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("telephone"), Bytes.toBytes(poiBean.getTelephone()));
			putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lng"), Bytes.toBytes(poiBean.getLng()));
			putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lat"), Bytes.toBytes(poiBean.getLat()));
			table.put(putRow);
		}
	}
	
	/**
	 * 解析JSON文件，并装载PoiBean到集合
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static List<PoiBean> parseFile(InputStream input) throws Exception{
		String result = FileUtil.readFile(input);//读取JSON文件为字符串
		JSONObject jsonResult = new JSONObject(result);
		JSONArray array = jsonResult.getJSONArray("results");
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		if(array.length() > 0){
			for(int i=0;i<array.length();i++){
				String telephone="",name="",address="",uid="";
				JSONObject obj = array.getJSONObject(i);
				if(!obj.isNull("uid")) uid = obj.getString("uid");
				if(!obj.isNull("address")) address = obj.getString("address");
				JSONObject location = obj.getJSONObject("location");
				double lng = location.getDouble("lng");
				double lat = location.getDouble("lat");
				if(!obj.isNull("name")) name = obj.getString("name");
				if(!obj.isNull("telephone")) telephone = obj.getString("telephone");
				PoiBean poiBean = new PoiBean(uid, address, name, telephone, lng, lat);
				poiBeans.add(poiBean);
			}
		}
		return poiBeans;
	}
	
}
