package com.andieguo.poi.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.andieguo.poi.dao.CityDao;
import com.andieguo.poi.dao.POIDao;
import com.andieguo.poi.pojo.POI;

public class RestDownload {
	private Logger logger;
	private static volatile int count = 0;//线程安全

	public RestDownload() throws Exception{
		//从类路径下加载配置文件
		PropertyConfigurator.configure(this.getClass().getClassLoader().getResourceAsStream("log4j/log4j.properties"));
		logger = Logger.getLogger(RestDownload.class);
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			RestDownload restDownload = new RestDownload();
			ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
			POIDao poiDao = (POIDao) context.getBean("poiDao");
			CityDao cityDao = (CityDao) context.getBean("cityDao");
			restDownload.executeJob(poiDao,cityDao);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized int downPage(POI poi,String city,Integer page) throws Exception {
		int sum = 0;
		String query = poi.getPoikey();
		String type = poi.getPoivalue();
		String result = Rest.poiRest(query, 20, page, city);
		JSONObject resultJSON = new JSONObject(result);//JSON格式化
		String message = resultJSON.getString("message");
		if(message.equals("ok") ){//获取数据成功
			int total = resultJSON.getInt("total");
			if(total > 0){
				sum = total/20 + 1;
				File dataMkdir = new File(Constants.DATAPATH);
				if(!dataMkdir.exists()){
					dataMkdir.mkdir();
				}
				File cityMkdir = new File(dataMkdir.getPath()+File.separator+city);
				if(!cityMkdir.exists()){
					cityMkdir.mkdir();
				}
				File typeMkdir = new File(cityMkdir.getPath()+File.separator+type);
				if(!typeMkdir.exists()){
					typeMkdir.mkdir();
				}
				//保存到文本
				File file = new File(typeMkdir.getPath()+File.separator+city+"-"+type+"-"+page+".json");
				if(!file.exists()){
					FileUtil.saveJSON(resultJSON.toString().getBytes(), file.getPath());
					logger.info("成功下载："+file.getPath());
				}else{
					logger.info("文件已存在："+file.getPath());
				}
			}
		}else{
			count ++ ;
			logger.info("---------------------------------->拒绝访问:"+count);
			if(count > 100) System.exit(0);
		}
		return sum;
	}
	
	public class SkipRunnable implements Runnable{
		public Exchanger<Integer> exchanger;   
		public CountDownLatch downLatch;
		public int num;

		public SkipRunnable(CountDownLatch downLatch,Exchanger<Integer> exchanger,int num){
			this.exchanger = exchanger;
			this.downLatch = downLatch;
			this.num = num;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				downLatch.countDown();// 计数减少1
				exchanger.exchange(num);// 与主线程交换信息
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public  class QueryRunnable implements Runnable{
		public Properties properties;
		public CountDownLatch downLatch;
		public CountDownLatch cityDownLatch;
		public Exchanger<Integer> exchanger;   
		public POI poi;
		public String city;
		public QueryRunnable(Properties properties,POI poi,String city,CountDownLatch downLatch,CountDownLatch cityDownLatch,Exchanger<Integer> exchanger){
			this.properties = properties;
			this.poi = poi;
			this.downLatch = downLatch;
			this.cityDownLatch = cityDownLatch;
			this.exchanger = exchanger;
			this.city = city;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				if(properties.getProperty(city+"&"+poi.getPoivalue()) == null){//城市配置文件中不存在记录
					int sum = downPage(poi,city, 0);
					if(sum >= 1){
						for (int i = 1; i < sum; i++) {
							downPage(poi,city, i);
						}
						logger.info(city+"--->"+poi.getPoivalue()+"-->下载结束");
						properties.setProperty(city+"&"+poi.getPoivalue(), "done");
					}else{
						logger.info(city+"--->"+poi.getPoivalue()+"-->下载失败");
					}
				}else{
					logger.info(city+"--->"+poi.getPoivalue()+"-->已经下载");
				}
				exchanger.exchange(1);// 与主线程交换信息
				cityDownLatch.countDown();
				downLatch.countDown();// 计数减少1
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("",e);
			}
		}
	}
	
	public class CityJobRunable implements Runnable{
		public String city;
		public Properties properties;
		public Properties globalProperties;
		public File globalFile;
		public File file;
		public CountDownLatch downLatch;
		public ExecutorService service;
		public CityJobRunable(String city,CountDownLatch downLatch,Properties globalProperties,File globalFile,Properties properties,File file,ExecutorService service){
			this.city = city;
			this.downLatch = downLatch;
			this.globalProperties = globalProperties;
			this.globalFile = globalFile;
			this.properties = properties;
			this.file = file;
			this.service = service;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				logger.info(city+"--->在等待所有的爬取Baidu POI线程执行完毕！");
				this.downLatch.await();
				PropertiesUtil.store(properties,file);
				PropertiesUtil.store(globalProperties, globalFile);
				logger.info(city+"--->保存记录点到日志文件！");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public class FinnishJobRunable implements Runnable{
		public CountDownLatch downLatch;
		public FinnishJobRunable(CountDownLatch downLatch){
			this.downLatch = downLatch;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				logger.info("FinnishJobRunable在等待所有的读历史数据线程执行完毕！");
				this.downLatch.await();
				logger.info("FinnishJobRunable释放连接资源！");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void executeJob(POIDao poiDao,CityDao cityDao){
		try{
			ExecutorService service = Executors.newSingleThreadExecutor(); //创建线程池：区别newSingleThreadExecutor与newCachedThreadPool
			Exchanger<Integer> exchanger = new Exchanger<Integer>();//子线程与主线程交换数据
			List<POI> poiList = poiDao.findByType(0);
			List<String> cityList = cityDao.findAll();
			int sum = poiList.size()*cityList.size();
			CountDownLatch downLatch = new CountDownLatch(sum);
			CountDownLatch cityDownLatch = new CountDownLatch(poiList.size());
			//创建家目录文件夹
			File home = new File(Constants.MKDIRPATH);
			if(!home.exists()) home.mkdirs();
			//在家目录生成全局配置文件
			File globalFile = new File(Constants.MKDIRPATH + File.separator+"local_update_global.properties");
			if(!globalFile.exists()) globalFile.createNewFile();
			Properties globalProperties = PropertiesUtil.loadFromInputStream(new FileInputStream(globalFile));
			for(String city:cityList){
				if(globalProperties.getProperty(city, null) == null){//值为NULL则继续，值为done则跳过
					//在家目录生成城市配置文件
					File file = new File(Constants.MKDIRPATH + File.separator+"local_update_"+city+".properties");
					if(!file.exists()) file.createNewFile();
					Properties properties = PropertiesUtil.loadFromInputStream(new FileInputStream(file));
					for (POI poi : poiList) {
						QueryRunnable queryRunnable = new QueryRunnable(properties,poi,city,downLatch,cityDownLatch,exchanger);
						service.execute(queryRunnable);// 为线程池添加任务
					}
					//每次遍历完一次城市都要执行CityJobRunable
					CityJobRunable cityJobRunable = new CityJobRunable(city,cityDownLatch,globalProperties,globalFile,properties,file,service);
					service.execute(cityJobRunable);
				}else{//执行skip线程，将任务数减(1*poiList.size())个,其中countdown是没有处理的
					SkipRunnable skipRunnable = new SkipRunnable(downLatch, exchanger, poiList.size());
					service.execute(skipRunnable);
				}
			}
			FinnishJobRunable finnishJobRunable = new FinnishJobRunable(downLatch);
			service.execute(finnishJobRunable);// 为线程池添加任务
			//主线程交换数据
			Integer totalResult = Integer.valueOf(0);
			for(int i=0;i<sum;i++){
				 //当主线程调用Exchange对象的exchange()方法后，他会陷入阻塞状态
				 //直到queryRunnable线程也调用了exchange()方法，然后以线程安全的方式交换数据，之后主线程继续运行
				Integer partialResult = exchanger.exchange(Integer.valueOf(0));
				if (partialResult != 0) {
					totalResult = totalResult + partialResult;
					//更新进度条
					logger.info(String.format("Progress: %s/%s",totalResult, sum));
				}
			}
			service.shutdown();//关闭线程池
		}catch(Exception e){
			e.printStackTrace();
			logger.error("",e);
		}
	}
	
}
