package com.andieguo.poi.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class Rest {

	public static String doRest(String type, String surl, String data) throws Exception {
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		URL url = new URL(surl);
		connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(5000);
		connection.setRequestMethod(type);
		connection.setRequestProperty("ContentType", "text;charset=utf-8");
		if (data != null) {
			connection.setDoOutput(true);
			OutputStream os = connection.getOutputStream();
			os.write(data.getBytes("utf-8"));// 写入data信息
		}
		connection.setDoInput(true);
		in = new InputStreamReader(connection.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(in);
		StringBuffer strBuffer = new StringBuffer();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			strBuffer.append(line);
		}
		connection.disconnect();
		return strBuffer.toString();
	}
	
	public static String poiRest(String query,Integer pageSize,Integer pageNumber,String region) throws Exception{
		String url = String.format("http://api.map.baidu.com/place/v2/search?ak=%s&output=json&query=%s&page_size=%d&page_num=%d&scope=1&region=%s", "115718263ae305054511732fe1d484d3",query,pageSize,pageNumber,region);
		return Rest.doRest("GET", url, null);
	}
	
	public static void main(String[] args) {
		try {
			String result = Rest.poiRest("银行", 0, 10, "全国");
			System.out.println(result);
			JSONObject jsonObj = new JSONObject(result);
			System.out.println(jsonObj.getInt("status"));
			System.out.println(jsonObj.getString("message"));
			System.out.println(jsonObj.getInt("total"));
			JSONArray jsonArray;
			if(jsonObj.getString("message").equals("ok")){
				jsonArray = jsonObj.getJSONArray("results");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
