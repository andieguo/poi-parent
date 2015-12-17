package com.andieguo.poi;

public class PoiBean {

	private String uid;
	private String address;
	private String name;
	private String telephone;
	private double lng;
	private double lat;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public PoiBean(String uid, String address, String name, String telephone, double lng, double lat) {
		super();
		this.uid = uid;
		this.address = address;
		this.name = name;
		this.telephone = telephone;
		this.lng = lng;
		this.lat = lat;
	}
	public PoiBean() {
		super();
	}
	@Override
	public String toString() {
		return "PoiBean [uid=" + uid + ", address=" + address + ", name=" + name + ", telephone=" + telephone + ", lng=" + lng + ", lat=" + lat + "]";
	}
	
	
}
