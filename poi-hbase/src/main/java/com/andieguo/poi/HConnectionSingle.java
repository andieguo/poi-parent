package com.andieguo.poi;

import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;

import com.andieguo.poi.util.PropertiesUtil;


public class HConnectionSingle {
	private static Properties properties;
	private static Configuration conf = null;
	private static HConnection connection;//保证全局只有一个HConnection;
	
	private HConnectionSingle(){
	}
	
	static{
		properties = PropertiesUtil.loadFromInputStream(HConnectionSingle.class.getClassLoader().getResourceAsStream("hbase-config.properties"));
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", properties.getProperty("hbase.zookeeper.quorum"));
	}
	
	public static synchronized HConnection getHConnection(){
		if(connection == null){
			 try {
				connection = HConnectionManager.createConnection(conf);
			} catch (ZooKeeperConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return connection;
	}
	
	public static synchronized Configuration getConfiguration(){
		return conf;
	}

}
