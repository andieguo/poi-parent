package com.andieguo.poi.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class POI {

	private Integer id;
	private String poikey;
	private String poivalue;
	private Integer poitype;
	
	public POI() {
		super();
	}

	public POI(String poikey, String poivalue,Integer poitype) {
		super();
		this.poikey = poikey;
		this.poivalue = poivalue;
		this.poitype = poitype;
	}

	public POI(Integer id, String poikey, String poivalue,Integer poitype) {
		super();
		this.id = id;
		this.poikey = poikey;
		this.poivalue = poivalue;
		this.poitype = poitype;
	}
	@XmlElement
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@XmlElement
	public String getPoikey() {
		return poikey;
	}

	public void setPoikey(String poikey) {
		this.poikey = poikey;
	}
	@XmlElement
	public String getPoivalue() {
		return poivalue;
	}

	public void setPoivalue(String poivalue) {
		this.poivalue = poivalue;
	}
	@XmlElement
	public Integer getPoitype() {
		return poitype;
	}

	public void setPoitype(Integer poitype) {
		this.poitype = poitype;
	}

	@Override
	public String toString() {
		return "POI [id=" + id + ", poikey=" + poikey + ", poivalue="
				+ poivalue + ", poitype=" + poitype + "]";
	}
	
}
