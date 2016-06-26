package com.qiangzi.xuezhajun.activity.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import android.os.Message;

import com.qiangzi.xuezhajun.activity.activity.MyApplication;

public class MyThread extends Thread {
	String CORE_DATA;
	public MyThread(String data) {
		this.CORE_DATA=data;
	}
	@Override
	public void run() {
		
		try {
			URL url = new URL("http://1.wpblog1.applinzi.com/shequ/index.php");
			HttpURLConnection connect = (HttpURLConnection) url.openConnection();
			connect.setRequestMethod("POST");
			connect.setConnectTimeout(5000);
			DataOutputStream dos =  new DataOutputStream(connect.getOutputStream());
			//CORE_DATA=URLEncoder.encode(CORE_DATA,"GBK");
			dos.writeBytes("core="+CORE_DATA); 
			InputStream is =connect.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String backdata=br.readLine();
			Message msg= new Message();
			msg.what=1;
			msg.obj=backdata;
			MyApplication.mainactivity.myhandle.sendMessage(msg);
			System.out.println("�߳̽��յ�����Ϣ"+backdata);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
