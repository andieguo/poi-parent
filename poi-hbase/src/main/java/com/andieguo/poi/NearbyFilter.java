package com.andieguo.poi;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.filter.Filter.ReturnCode;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.util.Bytes;

import com.andieguo.poi.geohash.GeoHash;

public class NearbyFilter extends FilterBase {

	private GeoHash[] adjacent;
	private boolean filterRow = false;
	
	public NearbyFilter(GeoHash[] adjacent) {
		super();
		this.adjacent = adjacent;
	}

	@Override
	public ReturnCode filterKeyValue(KeyValue keyvalue) {
		// TODO Auto-generated method stub
		if(Bytes.toString(keyvalue.getQualifier()).equals("geohash")){
			for(int i=0;i<adjacent.length;i++){
				adjacent[i].toBase32();
			}
			Bytes.toString(keyvalue.getValue());
		}
		return null;
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
	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub

	}

}
