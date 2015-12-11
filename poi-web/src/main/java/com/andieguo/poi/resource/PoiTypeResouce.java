package com.andieguo.poi.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.andieguo.poi.dao.POIDao;
import com.andieguo.poi.pojo.POI;

@Path("/poi")
public class PoiTypeResouce {

	private POIDao poiDao;

	public void setPoiDao(POIDao poiDao) {
		this.poiDao = poiDao;
	}
	
	@GET
	@Path("/findByType/{type}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<POI> findByType(@PathParam("type") int type){
		return poiDao.findByType(type);
	}
}
