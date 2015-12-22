package com.andieguo.poi;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.andieguo.poi.util.DistanceUtil;

public class DistanceFilter extends FilterBase {
	private final Log LOG = LogFactory.getLog(this.getClass().getName());
	private double centerLat;
	private double centerLng;
	private double radius;
	private boolean filterRow = false;
	
	public DistanceFilter() {
		super();
		LOG.debug("执行了DistanceFilter构造方法");
	}
	
	@Override
	public ReturnCode filterKeyValue(KeyValue keyvalue) {
		// TODO Auto-generated method stub
		LOG.debug("key:"+Bytes.toString(keyvalue.getQualifier()));
		LOG.debug("value:"+Bytes.toString(keyvalue.getValue()));
		return ReturnCode.INCLUDE;
	}



	public DistanceFilter(double centerLat,double centerLng, double radius) {
		super();
		this.centerLat = centerLat;
		this.centerLng = centerLng;
		this.radius = radius;
	}

	@Override
	public void filterRow(List<KeyValue> keyValues) {
		// TODO Auto-generated method stub
		double lng = 0;
		double lat = 0;
		LOG.debug("执行了filterRow方法:"+keyValues.size());
		for(int i=0;i<keyValues.size();i++){
			KeyValue keyvalue = keyValues.get(i);
			LOG.debug("qualifier-key:"+Bytes.toString(keyvalue.getQualifier()));
			LOG.debug("qualifier-value:"+Bytes.toString(keyvalue.getQualifier()));
			if(Bytes.toString(keyvalue.getQualifier()).equals("lng")){
				lng = Bytes.toDouble(keyvalue.getValue());
			}
			if(Bytes.toString(keyvalue.getQualifier()).equals("lat")){
				lat = Bytes.toDouble(keyvalue.getValue());
			}
		}
		LOG.debug("lat:"+lat+";lng:"+lng);
		double distance = DistanceUtil.computeDistance(lat, lng,centerLat,centerLng);
		LOG.debug("distance:"+distance);
		if(distance < radius){//如果在距离范围内，返回给客户端
			this.filterRow = false;
		}else{//如果不在距离范围内，不返回给客户端
			this.filterRow = true;//执行过滤行操作
		}
	}

	@Override
	public boolean filterRow() {
		// TODO Auto-generated method stub
		return this.filterRow;
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		this.filterRow = false;
	}

	@Override
	public void readFields(DataInput input) throws IOException {
		// TODO Auto-generated method stub
		this.centerLat = input.readDouble();
		this.centerLng = input.readDouble();
		this.radius = input.readDouble();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeDouble(centerLat);
		out.writeDouble(centerLng);
		out.writeDouble(radius);
	}
	
}
