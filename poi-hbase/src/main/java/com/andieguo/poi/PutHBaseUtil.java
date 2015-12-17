package com.andieguo.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.JSONArray;
import org.json.JSONObject;

import com.andieguo.poi.util.FileUtil;

public class PutHBaseUtil {
	
	private HTable table;
	private MessageDigest md;
	
	public PutHBaseUtil() throws Exception{
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "121.42.217.105");
		String tableName = "tb_poi";
		table = new HTable(conf, tableName);
		md = MessageDigest.getInstance("MD5");//MD5加密
	}
	
	public void listFile(File file) throws Exception{
		if(file.isFile()){//C:\Users\andieguo\poi-data\北京\餐饮服务;茶艺;茶艺\北京-餐饮服务;茶艺;茶艺-0.json
			String[] names = file.getName().split("-");
			System.out.println("file:"+names[1]);
			if(names.length == 3){
				String[] types = names[1].split(";");
				InputStream input = new FileInputStream(file);
				List<PoiBean> poiBeans = parseFile(input);
				putHRow(types, poiBeans);
			}
		}else if(file.isDirectory()){//目录
			File[] files = file.listFiles();  
			for (int i = 0; i < files.length; i++) {
				listFile(files[i]);//递归
			}
		}
	}
	
	public void putWRow(String type,List<PoiBean> poiBeans){
		putRow(type, poiBeans);
	}

	public void putHRow(String[] types, List<PoiBean> poiBeans) {
		for(int i=0;i<types.length;i++){
			putRow(types[i], poiBeans);
		}
	}
	
	public void putRow(String typeInput,List<PoiBean> poiBeans){
		for(int j=0;j<poiBeans.size();j++){
			PoiBean poiBean = poiBeans.get(j);
			byte[] type = md.digest(Bytes.toBytes(typeInput));//type字段
			byte[] geohash = md.digest(Bytes.toBytes(geohash(poiBean.getLng(), poiBean.getLat())));//geohash字段
			byte[] uid = md.digest(Bytes.toBytes(poiBean.getUid()));//uid字段
			byte[] rowkey = new byte[type.length+geohash.length+uid.length];
			int offset = Bytes.putBytes(rowkey, 0, type, 0, type.length);//拼接type字段
			offset = Bytes.putBytes(rowkey, offset, geohash, 0, geohash.length);//拼接geohash字段
			Bytes.putBytes(rowkey, offset, uid, 0, uid.length);//拼接uid字段
			Put putRow = new Put(rowkey);//一条记录就生成一条rowkey
			if(!poiBean.getName().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("name"),Bytes.toBytes(poiBean.getName()));
			if(!poiBean.getAddress().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("address"), Bytes.toBytes(poiBean.getAddress()));
			if(!poiBean.getTelephone().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("telephone"), Bytes.toBytes(poiBean.getTelephone()));
			putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lng"), Bytes.toBytes(poiBean.getLng()));
			putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lat"), Bytes.toBytes(poiBean.getLat()));
		}
	}
	
	public static String geohash(double lng,double lat){
		return String.valueOf(lng)+String.valueOf(lat);
	}
	
	//解析JSON文件
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
	
	public static void main(String[] args) {
		
		
		
	}

}
