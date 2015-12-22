package com.andieguo.poi;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import junit.framework.TestCase;

public class DataInputTest extends TestCase {

	@SuppressWarnings("resource")
	public void testDatInput() throws Exception{
		FileOutputStream fos = new FileOutputStream("xxx.data"); 
		DataOutput output = new DataOutputStream(fos);
		output.writeDouble(1.0);
		output.writeDouble(2.0);
		output.writeDouble(3.0);
		FileInputStream fis=new FileInputStream("xxx.data");  
        DataInputStream dis=new DataInputStream(fis);  
        System.out.println("dobule_1:"+dis.readDouble());  
        System.out.println("double_2:"+dis.readDouble()); 
        System.out.println("double_3:"+dis.readDouble()); 
	}
}
