package com.andieguo.poi.util;

public class Record{
	private long timedifference;
	private long poiNumber;
	private long fileNumber;
	/**
	 * @return the timedifference
	 */
	public long getTimedifference() {
		return timedifference;
	}
	/**
	 * @param timedifference the timedifference to set
	 */
	public void setTimedifference(long timedifference) {
		this.timedifference = timedifference;
	}
	/**
	 * @return the poiNumber
	 */
	public long getPoiNumber() {
		return poiNumber;
	}
	/**
	 * @param poiNumber the poiNumber to set
	 */
	public void setPoiNumber(long poiNumber) {
		this.poiNumber = poiNumber;
	}
	/**
	 * @return the fileNumber
	 */
	public long getFileNumber() {
		return fileNumber;
	}
	/**
	 * @param fileNumber the fileNumber to set
	 */
	public void setFileNumber(long fileNumber) {
		this.fileNumber = fileNumber;
	}
	public Record(long timedifference, long poiNumber, long fileNumber) {
		super();
		this.timedifference = timedifference;
		this.poiNumber = poiNumber;
		this.fileNumber = fileNumber;
	}
	public Record() {
		super();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Record [timedifference=" + timedifference + ", poiNumber=" + poiNumber + ", fileNumber=" + fileNumber + "]";
	}
	
}
