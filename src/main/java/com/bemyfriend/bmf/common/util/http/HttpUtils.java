package com.bemyfriend.bmf.common.util.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.bemyfriend.bmf.common.code.ErrorCode;
import com.bemyfriend.bmf.common.exception.ToAlertException;


public class HttpUtils {
	
	HttpURLConnection conn = null;
	
	
	//0. get/post
	//1. HttpUrlConnection 객체
	//2. 헤더 작성
	//3. 바디 작성
	//4. 응답
	//5. urlEncoded 포멧팅
	
	
	public String get(String url) { //get 방식을 받는 메소드
		String res = " ";		
		
			
		try {
			setConnection(url, "GET");
			res = getResponse();

		} catch (IOException e) {
			throw new ToAlertException(ErrorCode.AUTH01,e);
		} 
		
		return res ;
		
	}
	
	
	public String get(String url, Map<String, String> headers) {
		String res = " ";		
		
			
		try {
			setConnection(url, "GET");
			setHeaders(headers);
			res = getResponse();

		} catch (IOException e) {
			throw new ToAlertException(ErrorCode.API01,e);
		} 
		
		return res ;
		
	}
	
	
	public String post(String url, String body, Map<String,String> headers) { //post방식을 받는 메소드
		
		String res = "";
		try {
			
			setConnection(url, "POST");
			setHeaders(headers);
			setBody(body);
			res = getResponse();
			
		} catch (IOException e) {
			throw new ToAlertException(ErrorCode.API01,e);
		}
		return res;
	}
	
	
	
	
	
	
	//&name=key&name=value&=name=value ... 형태로 잡아주는 것
	public String urlEncodedForm(Map<String, String> params) {
		String res = "";
		
		
		
			try {
				
				for(String key : params.keySet()) {
					//아스키코드표로 표현가능하게끔 문자를 인코딩
					res += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(params.get(key),"UTF-8");
					}
					
					if(res.length() > 0) {
						res = res.substring(1);
					}
			
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		
			return res;
				
	}
	
	
	



	private void setConnection(String url, String method) throws IOException {
		
		URL u = new URL(url);
		conn= (HttpURLConnection) u.openConnection();
		conn.setRequestMethod(method); //메소드 값 받아서 넣어줄 수 있어야 함
		
		//post일 경우 doOutput 옵션을 true로 지정해 출력스트림 사용여부를 true로 지정
		if(method.contentEquals("POST")) {
			conn.setDoOutput(true);
			
		}
				
		//통신이 지연될 경우 통신을 종료할 시간을 10초로 지정
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);
		

	}
	
	
	
	
	private void setHeaders( Map<String, String> headers) {
		
		for(String key : headers.keySet()) {
			
			conn.addRequestProperty(key, headers.get(key));
		}
		
	}
	
	
	
	
	private void setBody(String body) throws IOException {
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		
		try {
			bw.write(body);
			bw.flush();
			
		} finally{
			bw.close();
		}
	}
	
	
	
	
	private String getResponse() throws IOException {
		BufferedReader br = null;
		String res = "";
		int status  = conn.getResponseCode();
		
		//응답코드가 200번 대인지 확인
		if(status >= 200 && status <= 300) {
			try {
				
				
				//body 정보를 inputStream으로 읽어서 문자열에 저장
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				StringBuffer sb = new StringBuffer();
				while((line = br.readLine()) != null) {
					
					sb.append(line);
				}
				res = sb.toString();
				System.out.println(res);
			
			} finally {
				
				
			}
			
			
		}else {
			throw new ToAlertException(ErrorCode.API01, new Exception(status + "이 응답되었습니다."));
			
		}
		return res;
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
}
