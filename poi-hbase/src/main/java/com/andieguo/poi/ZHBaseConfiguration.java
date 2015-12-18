package com.andieguo.poi;

import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

import com.andieguo.poi.util.PropertiesUtil;


public class ZHBaseConfiguration {
	private static Properties properties;
	private static Configuration conf = null;
	private ZHBaseConfiguration(){
		
	}
	static{
		properties = PropertiesUtil.loadFromInputStream(ZHBaseConfiguration.class.getClassLoader().getResourceAsStream("hbase-config.properties"));
	}
	public static synchronized Configuration getConfiguration(){
		if(conf == null){
			conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", properties.getProperty("hbase.zookeeper.quorum"));
		}
		return conf;
	}

}
