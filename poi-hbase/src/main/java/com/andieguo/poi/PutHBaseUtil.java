package com.andieguo.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.andieguo.poi.dao.CityDao;
import com.andieguo.poi.dao.POIDao;
import com.andieguo.poi.dao.POIDataDao;
import com.andieguo.poi.geohash.GeoHash;
import com.andieguo.poi.pojo.POI;
import com.andieguo.poi.pojo.POIData;
import com.andieguo.poi.util.Constants;
import com.andieguo.poi.util.ExcelWrite;
import com.andieguo.poi.util.FileUtil;
import com.andieguo.poi.util.Record;

public class PutHBaseUtil {
	
	@SuppressWarnings("unused")
	private Logger logger;
	private long poiNumber = 0;
	private long fileNumber = 0;
	private static POIDataDao poiDataDao;
	private static CityDao cityDao ;
	private static POIDao poiDao ;
	/* excel column formate:column_#_width, excel中每一列的名称 */
	public static final String[] RECORES_COLUMNS = new String[] { "timedifference_#_5000", "poiNumber_#_5000","fileNumber_#_5000" };
	/* the column will display on xls files. must the same as the entity fields.对应上面的字段. */
	public static final String[] RECORES_FIELDS = new String[] { "timedifference", "poiNumber","fileNumber" };
	
	public PutHBaseUtil() throws Exception{
		logger = Logger.getLogger(PutHBaseUtil.class);
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		cityDao = (CityDao) context.getBean("cityDao");
		poiDao = (POIDao) context.getBean("poiDao");
		poiDataDao = (POIDataDao) context.getBean("poiDataDao");
	}
	
	/**
	 * 递归遍历file文件夹下的所有文件
	 * @param file
	 * @throws Exception
	 */
	public void listFile(File file,String type,HTableInterface table) throws Exception{
		if(file.exists() && file.isFile()){//C:\Users\andieguo\poi-data\北京\餐饮服务;茶艺;茶艺\北京-餐饮服务;茶艺;茶艺-0.json
			List<PoiBean> poiBeans = parseFile(file);
			poiNumber = poiNumber + poiBeans.size();
			fileNumber++;
			if(type.equals(Constants.WTABLE)){
				putWRow(table,poiBeans);
			}else if(type.equals(Constants.HTABLE)){
				putHRow(table, poiBeans);
			}else if(type.equals(Constants.MYSQLTABEL)){
				putMySQL(poiDataDao, poiBeans);
			}
		}else if(file.exists() && file.isDirectory()){//目录
			File[] files = file.listFiles();  
			for (int i = 0; i < files.length; i++) {
				listFile(files[i],type,table);//递归
			}
		}
	}
	/**
	 * 宽表构建
	 * @param types
	 * @param poiBeans
	 * @throws IOException
	 */
	public void putWRow(HTableInterface table,List<PoiBean> poiBeans) throws IOException {
		for(int j=0;j<poiBeans.size();j++){
			PoiBean poiBean = poiBeans.get(j);
			String[] types = poiBean.getType().split(";");
			if(types.length == 3){
				byte[] rowkey = BytesUtil.startkeyGen(3,types[0],types[1],types[2],poiBean.getGeohash(),poiBean.getUid());
				Put putRow = new Put(rowkey);//一条记录就生成一条rowkey
				if(!poiBean.getName().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("name"),Bytes.toBytes(poiBean.getName()));
				if(!poiBean.getAddress().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("address"), Bytes.toBytes(poiBean.getAddress()));
				if(!poiBean.getTelephone().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("telephone"), Bytes.toBytes(poiBean.getTelephone()));
				putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lng"), Bytes.toBytes(poiBean.getLng()));
				putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lat"), Bytes.toBytes(poiBean.getLat()));
				putRow.add(Bytes.toBytes("info"), Bytes.toBytes("city"), Bytes.toBytes(poiBean.getCity()));
				putRow.add(Bytes.toBytes("info"), Bytes.toBytes("type"), Bytes.toBytes(poiBean.getType()));
				putRow.add(Bytes.toBytes("info"), Bytes.toBytes("geohash"), Bytes.toBytes(poiBean.getGeohash()));
				//table.setAutoFlush(false);
				table.put(putRow);
			}
		}
	}
	/**
	 * 高表构建
	 * @param typeInput
	 * @param poiBeans
	 * @throws IOException
	 */
	public void putHRow(HTableInterface table,List<PoiBean> poiBeans) throws IOException{
		
		for(int i=0;i<poiBeans.size();i++){
			PoiBean poiBean = poiBeans.get(i);
			String[] types = poiBean.getType().split(";");
			if(types.length == 3){
				for(int j=0;j<types.length;j++){
					if(j == 1 && types[1] == types[2]) continue;
					byte[] rowkey = BytesUtil.startkeyGen(1,types[j],poiBean.getGeohash(),poiBean.getUid());
					Put putRow = new Put(rowkey);//一条记录就生成一条rowkey
					if(!poiBean.getName().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("name"),Bytes.toBytes(poiBean.getName()));
					if(!poiBean.getAddress().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("address"), Bytes.toBytes(poiBean.getAddress()));
					if(!poiBean.getTelephone().equals("")) putRow.add(Bytes.toBytes("info"), Bytes.toBytes("telephone"), Bytes.toBytes(poiBean.getTelephone()));
					putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lng"), Bytes.toBytes(poiBean.getLng()));
					putRow.add(Bytes.toBytes("info"), Bytes.toBytes("lat"), Bytes.toBytes(poiBean.getLat()));
					putRow.add(Bytes.toBytes("info"), Bytes.toBytes("city"), Bytes.toBytes(poiBean.getCity()));
					putRow.add(Bytes.toBytes("info"), Bytes.toBytes("type"), Bytes.toBytes(poiBean.getType()));
					putRow.add(Bytes.toBytes("info"), Bytes.toBytes("geohash"), Bytes.toBytes(poiBean.getGeohash()));
					table.put(putRow);
				}
			}
		}
		
	}
	
	public void putMySQL(POIDataDao dao,List<PoiBean> poiBeans) throws IOException{
		for(int i=0;i<poiBeans.size();i++){
			PoiBean poiBean = poiBeans.get(i);
			String[] types = poiBean.getType().split(";");
			POIData poiData = new POIData(types[0],types[1],types[2],poiBean.getName(),poiBean.getAddress(),poiBean.getTelephone(),
					poiBean.getLng(),poiBean.getLat(),poiBean.getCity());
			dao.save(poiData);
		}
	}
	
	/**
	 * 解析JSON文件，并装载PoiBean到集合
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static List<PoiBean> parseFile(File file) throws Exception{
		String result = FileUtil.readFile(new FileInputStream(file));//读取JSON文件为字符串
		JSONObject jsonResult = new JSONObject(result);
		JSONArray array = jsonResult.getJSONArray("results");
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		String[] names = file.getName().split("-");//北京-餐饮服务;茶艺;茶艺-0.json
		if(array.length() > 0){
			for(int i=0;i<array.length();i++){
				String telephone="",name="",address="",uid="";
				JSONObject obj = array.getJSONObject(i);
				if(!obj.isNull("location")){
					if(!obj.isNull("uid")) uid = obj.getString("uid");
					if(!obj.isNull("address")) address = obj.getString("address");
					JSONObject location = obj.getJSONObject("location");
					double lng = location.getDouble("lng");
					double lat = location.getDouble("lat");
					String geohash = GeoHash.geoHashStringWithCharacterPrecision(lat,lng,12);
					if(!obj.isNull("name")) name = obj.getString("name");
					if(!obj.isNull("telephone")) telephone = obj.getString("telephone");
					PoiBean poiBean = new PoiBean(uid, address, name, telephone, lng, lat,names[0],names[1],geohash);
					poiBeans.add(poiBean);
				}
			}
		}
		return poiBeans;
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length == 2){
			HTableInterface table = null;
			String type = args[0];
			String tableName = args[1];
			PutHBaseUtil putHBaseUtil = new PutHBaseUtil();
			if(type.equals(Constants.WTABLE) || type.equals(Constants.HTABLE)){
				table = HConnectionSingle.getHConnection().getTable(tableName);
			}
//			HBaseUtil hbaseUtil = new HBaseUtil();
//			hbaseUtil.create(tableName,"info");
			List<String> cityList = cityDao.findAll();
			List<POI> poiList = poiDao.findByType(0);
			List<Record> records = new ArrayList<Record>();
			//遍历家目录下的poi-data目录
			long starttime = System.currentTimeMillis();
			for(String city : cityList){
				for(POI poi : poiList){
					File home = new File(Constants.DATAPATH+File.separator+city+File.separator+poi.getPoivalue());//例如C:\Users\andieguo\poi-data
					putHBaseUtil.listFile(home,type,table);
				}
				long endtime = System.currentTimeMillis();
				long fileNumber = putHBaseUtil.getFileNumber();
				long poiNumber = putHBaseUtil.getPoiNumber();
				Record record = new Record(endtime-starttime,poiNumber,fileNumber);
				records.add(record);
			}
			if(table != null) table.close();
			HSSFWorkbook workbook = new HSSFWorkbook();
			ExcelWrite<Record> userSheet = new ExcelWrite<Record>();
			userSheet.creatAuditSheet(workbook, "串行导入记录", records, RECORES_COLUMNS, RECORES_FIELDS);

			FileOutputStream fileOut = new FileOutputStream(new File(Constants.MKDIRPATH + File.separator + "loadrecord.xls"));
			workbook.write(fileOut);
			fileOut.close();
		}else{
			System.out.println("main [WTable|HTable] [tableName]");
		}
		
	}
	
	/**
	 * @return the poiNumber
	 */
	public long getPoiNumber() {
		return poiNumber;
	}

	/**
	 * @return the fileNumber
	 */
	public long getFileNumber() {
		return fileNumber;
	}
}
