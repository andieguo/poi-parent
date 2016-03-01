package com.andieguo.poi.pojo;

public class POIData {
	private Integer id;
	private String levelOne;
	private String levelTwo;
	private String levelThree;
	private String name;
	private String address;
	private String telephone;
	private double lng;
	private double lat;
	private String city;
	
	public POIData(String levelOne, String levelTwo, String levelThree, String name, String address, String telephone, double lng, double lat,String city) {
		super();
		this.levelOne = levelOne;
		this.levelTwo = levelTwo;
		this.levelThree = levelThree;
		this.name = name;
		this.address = address;
		this.telephone = telephone;
		this.lng = lng;
		this.lat = lat;
		this.city = city;
	}
	public POIData() {
		super();
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the levelOne
	 */
	public String getLevelOne() {
		return levelOne;
	}
	/**
	 * @param levelOne the levelOne to set
	 */
	public void setLevelOne(String levelOne) {
		this.levelOne = levelOne;
	}
	/**
	 * @return the levelTwo
	 */
	public String getLevelTwo() {
		return levelTwo;
	}
	/**
	 * @param levelTwo the levelTwo to set
	 */
	public void setLevelTwo(String levelTwo) {
		this.levelTwo = levelTwo;
	}
	/**
	 * @return the levelThree
	 */
	public String getLevelThree() {
		return levelThree;
	}
	/**
	 * @param levelThree the levelThree to set
	 */
	public void setLevelThree(String levelThree) {
		this.levelThree = levelThree;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}
	/**
	 * @param lng the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}
	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "POIData [id=" + id + ", levelOne=" + levelOne + ", levelTwo=" + levelTwo + ", levelThree=" + levelThree + ", name=" + name + ", address=" + address + ", telephone=" + telephone
				+ ", lng=" + lng + ", lat=" + lat + "]";
	}
	
}
