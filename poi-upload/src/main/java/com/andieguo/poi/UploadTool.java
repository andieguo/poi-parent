package com.andieguo.poi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.andieguo.poi.dao.CityDao;
import com.andieguo.poi.dao.POIDao;
import com.andieguo.poi.pojo.POI;
import com.andieguo.poi.util.Constants;
import com.andieguo.poi.util.ExcelWrite;
import com.andieguo.poi.util.Record;

public class UploadTool {
	
	private Logger logger;

	public UploadTool() {
		super();
		//从类路径下加载配置文件
		PropertyConfigurator.configure(this.getClass().getClassLoader().getResourceAsStream("log4j/log4j.properties"));
		logger =  Logger.getLogger(UploadTool.class);
	}

	/**
	 * 拷贝文件夹
	 * 
	 * @param srcDir
	 * @param dstDir
	 * @param conf
	 * @return
	 * @throws Exception
	 */
	public  boolean copyDirectory(String srcDir, String dstDir,FileSystem fs) throws Exception {

		if (!fs.exists(new Path(dstDir))) {//目的路径是否存在，不存在则创建
			fs.mkdirs(new Path(dstDir));
		}
		FileStatus status = fs.getFileStatus(new Path(dstDir));
		File file = new File(srcDir);
		if (!status.isDir()) {
			System.exit(2);
			logger.info("You put in the " + dstDir + "is file !");
		} 
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				// 准备复制的源文件夹
				String srcDir1 = srcDir + File.separator + f.getName();
				String dstDir1 = dstDir + "/" + f.getName();//目标文件系统为linxu系统
				logger.info("src-dir:"+srcDir1);
				logger.info("dst-dir:"+dstDir1);
				copyDirectory(srcDir1, dstDir1, fs);
			} else {
				String dstfile = dstDir +"/"+f.getName() ;
				copyFile(f.getPath(),dstfile, fs);
				logger.info("成功上传文件:"+f.getPath()+"-->"+dstfile);
			}
		}
		return true;
	}

	/**
	 * 拷贝文件
	 * 
	 * @param src
	 * @param dst
	 * @param conf
	 * @return
	 * @throws Exception
	 */
	public  boolean copyFile(String src, String dst, FileSystem fs)
			throws Exception {
		Path dstPath = new Path(dst);
		if(!fs.exists(dstPath)){
			File file = new File(src);
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			//FieSystem的create方法可以为文件不存在的父目录进行创建，
			OutputStream out = fs.create(dstPath, new Progressable() {
				public void progress() {
				}
			});
			IOUtils.copyBytes(in, out, 4096, true);
			return true;
		}else{
			return false;
		}
	}
	
	/* excel column formate:column_#_width, excel中每一列的名称 */
	public static final String[] RECORES_COLUMNS = new String[] { "timedifference_#_5000", "poiNumber_#_5000","fileNumber_#_5000" };
	/* the column will display on xls files. must the same as the entity fields.对应上面的字段. */
	public static final String[] RECORES_FIELDS = new String[] { "timedifference", "poiNumber","fileNumber" };
	
	public static void main(String[] args) throws Exception {
		UploadTool uploadTool = new UploadTool();
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		CityDao cityDao = (CityDao) context.getBean("cityDao");
		POIDao poiDao = (POIDao) context.getBean("poiDao");
		List<String> cityList = cityDao.findAll();
		cityList.remove(0);
		List<POI> poiList = poiDao.findByType(0);
		List<Record> records = new ArrayList<Record>();
		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://master:9000");//很关键
		FileSystem fs = FileSystem.get(conf);
		long starttime = System.currentTimeMillis();
		int count = 0;
		//遍历本地的文件，同一将本地的文件拷贝到poi-data目录中
		String dst = "/user/hadoop/poi-data-2/";
		for(String city : cityList){
			for(POI poi : poiList){
				//E://poi-data//北京//餐饮服务;中餐厅;上海菜
				String localSrc = Constants.DATAPATH+File.separator+city+File.separator+poi.getPoivalue();
				File srcFile = new File(localSrc);
				if (srcFile.exists() && srcFile.isDirectory()) {
					File[] files = srcFile.listFiles();
					for(File file : files){
						String dstfile = dst +"/"+file.getName();
						uploadTool.copyFile(file.getPath(), dstfile, fs);
					}
				}
				break;
			}
			long endtime = System.currentTimeMillis();
			Record record = new Record(endtime-starttime,0,0);
			records.add(record);
			count ++;
			if(count==1)break;
		}
		if(fs != null) fs.close();
		HSSFWorkbook workbook = new HSSFWorkbook();
		ExcelWrite<Record> userSheet = new ExcelWrite<Record>();
		userSheet.creatAuditSheet(workbook, "上传数据记录", records, RECORES_COLUMNS, RECORES_FIELDS);
		FileOutputStream fileOut = new FileOutputStream(new File(Constants.MKDIRPATH + File.separator + "loadrecord-hdfs.xls"));
		workbook.write(fileOut);
		fileOut.close();
	}
	
	public static void uploadAll() throws Exception {
		UploadTool uploadTool = new UploadTool();
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		CityDao cityDao = (CityDao) context.getBean("cityDao");
		POIDao poiDao = (POIDao) context.getBean("poiDao");
		List<String> cityList = cityDao.findAll();
		cityList.remove(0);
		List<POI> poiList = poiDao.findByType(0);
		List<Record> records = new ArrayList<Record>();
		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://master:9000");//很关键
		FileSystem fs = FileSystem.get(conf);
		long starttime = System.currentTimeMillis();
		int count = 0;
		for(String city : cityList){
			for(POI poi : poiList){
				String localSrc = Constants.DATAPATH+File.separator+city+File.separator+poi.getPoivalue();
				String dst = "/user/hadoop/poi-data/"+city+"/"+poi.getPoivalue();
				File srcFile = new File(localSrc);
				if(srcFile.exists()){
					if (srcFile.isDirectory()) {
						uploadTool.copyDirectory(localSrc, dst, fs);
					} else if(srcFile.isFile()) {
						uploadTool.copyFile(localSrc, dst, fs);
					}
				}
			}
			long endtime = System.currentTimeMillis();
			Record record = new Record(endtime-starttime,0,0);
			records.add(record);
			count ++;
			if(count==63)break;
		}
		if(fs != null) fs.close();
		HSSFWorkbook workbook = new HSSFWorkbook();
		ExcelWrite<Record> userSheet = new ExcelWrite<Record>();
		userSheet.creatAuditSheet(workbook, "上传数据记录", records, RECORES_COLUMNS, RECORES_FIELDS);

		FileOutputStream fileOut = new FileOutputStream(new File(Constants.MKDIRPATH + File.separator + "loadrecord-hdfs.xls"));
		workbook.write(fileOut);
		fileOut.close();
	}
}
