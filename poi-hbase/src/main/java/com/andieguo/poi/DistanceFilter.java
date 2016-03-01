package com.andieguo.poi;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.util.Bytes;
import com.andieguo.poi.util.DistanceUtil;

public class DistanceFilter extends FilterBase {
	private double centerLat;
	private double centerLng;
	private double radius;
	private boolean filterRow = false;
	
	public DistanceFilter() {
	}
	
	public DistanceFilter(double centerLat,double centerLng, double radius) {
		this.centerLat = centerLat;
		this.centerLng = centerLng;
		this.radius = radius;
	}
	
	@Override
	public ReturnCode filterKeyValue(KeyValue keyvalue) {
		// TODO Auto-generated method stub
		return ReturnCode.INCLUDE;
	}


	@Override
	public void filterRow(List<KeyValue> keyValues) {
		// TODO Auto-generated method stub
		double lng = 0;
		double lat = 0;
		for(int i=0;i<keyValues.size();i++){
			KeyValue keyvalue = keyValues.get(i);
			if(Bytes.toString(keyvalue.getQualifier()).equals("lng")){
				lng = Bytes.toDouble(keyvalue.getValue());
			}
			if(Bytes.toString(keyvalue.getQualifier()).equals("lat")){
				lat = Bytes.toDouble(keyvalue.getValue());
			}
		}
		double distance = DistanceUtil.computeDistance(lat, lng,centerLat,centerLng);
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
	public boolean hasFilterRow(){
		return true;
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
