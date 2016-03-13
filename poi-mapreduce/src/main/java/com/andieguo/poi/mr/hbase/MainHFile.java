package com.andieguo.poi.mr.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;
import org.apache.hadoop.hbase.mapreduce.PutSortReducer;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.json.JSONArray;
import org.json.JSONObject;

import com.andieguo.poi.util.FileUtil;
import com.andieguo.poi.util.PropertiesUtil;

public class MainHFile {
	// 在MapReduce中，由Job对象负责管理和运行一个计算任务，并通过Job的一些方法对任>务的参数进行相关的设置。
	public static void main(String[] args) throws Exception {
		Configuration conf = HBaseConfiguration.create();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 4) {
			System.err.println("Usage: MainHFile [tableName] [WTable|HTable] [input] [output]");
			System.exit(2);
		}
		Properties properties = PropertiesUtil.loadFromInputStream(MainHFile.class.getResourceAsStream("/hbase-config.properties"));
		conf.set("hbase.zookeeper.quorum",properties.getProperty("hbase.zookeeper.quorum"));
		String tablename = otherArgs[0];
		conf.set("tablename", otherArgs[0]);
		conf.set("indextype", otherArgs[1]);
		conf.set("input", otherArgs[2]);
		Job job = new Job(conf, "PoiHBaseMain");
		job.setJarByClass(MainHFile.class);
		job.setMapperClass(PoiHfileMapper.class);
		/**** Map输入输出格式化类 **/
		job.setMapOutputKeyClass(ImmutableBytesWritable.class);
		job.setMapOutputValueClass(Put.class);
		/**** 输入输出格式化类 **/
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(HFileOutputFormat.class);
		/**** 创建HBase表 *****/
		prepareOutput(conf, tablename);
		/**** 准备输入 数据源 *****/
		FileInputFormat.setInputPaths(job,new Path(otherArgs[2]));
		/*******生成HFile **********/
		HTable table = new HTable(conf, tablename);
		table.setWriteBufferSize(6 * 1024 * 1024);  
		table.setAutoFlush(false); 
		job.setReducerClass(PutSortReducer.class);
		Path outputDir = new Path(otherArgs[3]);
		/**** 准备输出 *****/
		FileOutputFormat.setOutputPath(job, outputDir);
		HFileOutputFormat.configureIncrementalLoad(job, table);
		if(job.waitForCompletion(true)){
			LoadIncrementalHFiles loader = new LoadIncrementalHFiles(conf);
			loader.doBulkLoad(new Path(otherArgs[3]), table);
		}else{
			System.exit(1);
		}
	}
	
	public static List<String> findAll() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		String result = FileUtil.readFile(MainHFile.class.getClassLoader().getResourceAsStream("city.json"));
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

	public static void prepareOutput(Configuration conf, String tablename) {
		HBaseAdmin admin = null;
		try {
			admin = new HBaseAdmin(conf);
			if (!admin.tableExists(tablename)) {
				System.out.println("table not exists!creating.......");
				HTableDescriptor htd = new HTableDescriptor(tablename);
				HColumnDescriptor tcd = new HColumnDescriptor("info");
				htd.addFamily(tcd);// 创建列族
				admin.createTable(htd);// 创建表
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (admin != null)
				try {
					admin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static List<String> listFile(FileSystem fs, String filePath) throws IOException {
		List<String> listDir = new ArrayList<String>();
		Path path = new Path(filePath);
		if(fs.exists(path)){
			FileStatus[] fileStatus = fs.listStatus(path);
			for (FileStatus file : fileStatus) {
				listDir.add(file.getPath().toString());
			}
		}
		return listDir;
	}
}
