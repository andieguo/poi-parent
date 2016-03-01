package com.andieguo.poi.mr.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONArray;
import org.json.JSONObject;

import com.andieguo.poi.BytesUtil;
import com.andieguo.poi.PoiBean;
import com.andieguo.poi.geohash.GeoHash;
import com.andieguo.poi.util.Constants;

public class PoiMapper extends
		Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		Configuration conf = context.getConfiguration();
		String tablename = conf.get("tablename");
		String indextype = conf.get("indextype");
		String source = value.toString();
		String city="",type = "";
		try {
			Counter counterin = context.getCounter("Map", "输入个数：");
			counterin.increment(1);
			Counter counterout = context.getCounter("Map", "输出个数：");
			//输入路径：hdfs://master.zonesion:9000/user/hadoop/zcloud/安庆/餐饮服务;茶艺;茶艺/安庆-餐饮服务;茶艺;茶艺-0.json
            String path = ((FileSplit) context.getInputSplit()).getPath().toString();//读取输入PATH
            //从输入路径中获取city和type
            String [] paths =  path.substring(path.indexOf("poi-data/"), path.length()).split("/");  
            if(paths.length==4){
            	city = paths[1];
            	type = paths[2];
            }
			JSONObject jsonResult = new JSONObject(source);
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
					String geohash = GeoHash.geoHashStringWithCharacterPrecision(lat,lng,12);
					if(!obj.isNull("name")) name = obj.getString("name");
					if(!obj.isNull("telephone")) telephone = obj.getString("telephone");
					PoiBean poiBean = new PoiBean(uid, address, name, telephone, lng, lat,city,type,geohash);
					poiBeans.add(poiBean);
				}
				if(indextype.equals(Constants.WTABLE)){
					putWRow(tablename, poiBeans, context, counterout);
				}else if(indextype.equals(Constants.HTABLE)){
					putHRow(tablename, poiBeans, context, counterout);
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	/**
	 * 宽表构建
	 * @param types
	 * @param poiBeans
	 * @throws IOException
	 */
	public void putWRow(String tableName,List<PoiBean> poiBeans,Context context,Counter counterout) throws Exception {
		for(int j=0;j<poiBeans.size();j++){
			counterout.increment(1);
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
			context.write(new ImmutableBytesWritable(rowkey), putRow);
		}
	}
	
	/**
	 * 高表构建
	 * @param typeInput
	 * @param poiBeans
	 * @throws IOException
	 */
	public void putHRow(String tableName,List<PoiBean> poiBeans,Context context,Counter counterout) throws Exception{
		for(int i=0;i<poiBeans.size();i++){
			PoiBean poiBean = poiBeans.get(i);
			String[] types = poiBean.getType().split(";");
			for(int j=0;j<types.length;j++){
				counterout.increment(1);
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
				context.write(new ImmutableBytesWritable(rowkey), putRow);
			}
		}
	}
	
}
