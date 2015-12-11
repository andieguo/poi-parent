package com.andieguo.poi.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.andieguo.poi.util.FileUtil;

public class PoiServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3930565973722955451L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			//解析返回的JSON数据
			String resultJSON = readJSONString(request);
			System.out.println("resultJSON:" + resultJSON);
			JSONObject jsonObj = new JSONObject(resultJSON);
			String province = jsonObj.getString("province");
			String city = jsonObj.getString("city");
			String type = jsonObj.getString("type");
			Integer page = jsonObj.getInt("page");
			JSONArray jsonArray = jsonObj.getJSONArray("poi");
			File provinceMkdir = new File("E:\\"+province);
			if(!provinceMkdir.exists()){
				provinceMkdir.mkdir();
			}
			File cityMkdir = new File(provinceMkdir.getPath()+File.separator+city);
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
				FileUtil.saveJSON(jsonArray.toString().getBytes(), file.toString());
			}
			//返回JSON数据
			JSONObject messageJSON = new JSONObject();// 构建一个JSONObject
			messageJSON.accumulate("page", page);
			messageJSON.accumulate("status", 1);
			response.setContentType("application/x-json");// 需要设置ContentType为"application/x-json"
			PrintWriter out = response.getWriter();
			out.println(messageJSON.toString());// 向客户端输出JSONObject字符串
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	//读取Request返回的JSON数据
	public String readJSONString(HttpServletRequest request) {
		StringBuffer json = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} 
		return json.toString();
	}

}
