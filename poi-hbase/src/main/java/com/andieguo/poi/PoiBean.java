package com.andieguo.poi;

public class PoiBean {

	private String uid;
	private String address;
	private String name;
	private String telephone;
	private double lng;
	private double lat;
	private String city;
	private String type;
	private String geohash;
	private double distance;
	
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getGeohash() {
		return geohash;
	}
	public void setGeohash(String geohash) {
		this.geohash = geohash;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public PoiBean(String uid, String address, String name, String telephone, double lng, double lat,String city,String type,String geohash) {
		super();
		this.uid = uid;
		this.address = address;
		this.name = name;
		this.telephone = telephone;
		this.lng = lng;
		this.lat = lat;
		this.city = city;
		this.type = type;
		this.geohash = geohash;
	}
	
	public PoiBean() {
		super();
	}
	
	@Override
	public String toString() {
		return "PoiBean [uid=" + uid + ", address=" + address + ", name=" + name + ", telephone=" + telephone + ", lng=" + lng + ", lat=" + lat + ", city=" + city + ", type=" + type +", geohash=" + geohash + ", distance=" + distance+"]";
	}
	
}
