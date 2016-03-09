package com.andieguo.poi.mr.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.json.JSONArray;
import org.json.JSONObject;

import com.andieguo.poi.util.FileUtil;
import com.google.common.collect.Sets;

public class Test {
	
	public static void main(String[] args) throws IOException {
		int count = 0;
		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://KVM-Master:9000");
		FileSystem fs = FileSystem.get(conf);
//		for(String city : findAll()){
//			System.out.println(city);
//			String dst = "/user/hadoop/poi-data/"+city;
//			for (String str : listFile(fs, dst)) {// 遍历/user/hadoop/poi-data/city下的输入
//				System.out.println(str);
//			}
//			count ++;//表示城市的个数
//			//城市的个数进行比较，城市的个数相等则退出循环
//			if(count == 3) break;
//		}
		
//		String dst = "/user/hadoop/poi-data/天津";
//		for (String str : listFile(fs, dst)) {// 遍历/user/hadoop/poi-data/city下的输入
//			System.out.println(str);
//		}
		Set<String> other = Sets.newHashSet();
		Set<String> old = findAll();
		for(String city : findAll()){
			other.add(city);
			count ++;//表示城市的个数
			if(count == 35) break;
		}
		Set<String> result = getDifferent2(other,old);
		System.out.println(result.size());
	}
	
	public static Set<String> getDifferent2(Set<String> set ,Set<String> otherSet){
		long starttime = System.currentTimeMillis();
		Set<String> result = Sets.newHashSet();
		Map<String,Integer> map = new LinkedHashMap<String, Integer>();
		for(String key : set){
			map.put(key, 1);//v=1,set有，otherSet没有
		}
		for(String key : otherSet){
			Integer v = map.get(key);
			if(v != null){
				map.put(key, ++v);//v=2,set&otherSet均有
			}else{
				map.put(key,0);//v=0,set没有，otherset有
			}
		}
		for(String key : map.keySet()){
			if(map.get(key) == 1){
				result.add(key);
			}
		}
		long endtime = System.currentTimeMillis();
		System.out.println("时间2："+(endtime-starttime));
		return result;
	}
	
	public static List<String> listFile(FileSystem fs, String filePath) throws IOException {
		List<String> listDir = new ArrayList<String>();
		Path path = new Path(filePath);
		if(fs.exists(path)){
			FileStatus[] fileStatus = fs.listStatus(path);
			for (FileStatus file : fileStatus) {
				System.out.println(file.getPath().toString());
				listDir.add(file.getPath().toString());
			}
		}
		return listDir;
	}
	
	public static Set<String> findAll() {
		// TODO Auto-generated method stub
		Set<String> list =  Sets.newHashSet();
		String result = FileUtil.readFile(Main.class.getClassLoader().getResourceAsStream("city.json"));
		JSONObject jsonObj = new JSONObject(result);
		JSONArray array = jsonObj.getJSONArray("城市代码");
		for(int i=0;i<array.length();i++){
			JSONObject obj = array.getJSONObject(i);
			JSONArray cityArray = obj.getJSONArray("市");
			for(int j=0;j<cityArray.length();j++){
				JSONObject cityObj = cityArray.getJSONObject(j);
				list.add(cityObj.getString("市名"));
			}
		}
		return list;
	}
}
