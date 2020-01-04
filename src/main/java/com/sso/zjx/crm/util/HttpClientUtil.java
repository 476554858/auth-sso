package com.sso.zjx.crm.util;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpClientUtil {

    public static String doPost(String myUrl,Map<String,String> paramMap) throws IOException {
        //1.定义需要访问的地址
        URL url = new URL(myUrl);
        //2.开启连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //3.设置请求的方式
        conn.setRequestMethod("POST");
        //4.需要输出参数
        conn.setDoOutput(true);
        //5.拼接单数的信息
        StringBuilder params = new StringBuilder();
        for(Map.Entry<String,String> entry:paramMap.entrySet()){
            params.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String paramStr = params.substring(0,params.length()-1);
        //6.写出参数
        conn.getOutputStream().write(paramStr.getBytes("UTF-8"));
        //7.发起请求
        conn.connect();
        //8.接收对方给我们的信息
        String respStr =StreamUtils.copyToString(conn.getInputStream(), Charset.forName("UTF-8"));
        return respStr;
    }

    public static void sendRequest(String httpUrl,String jsessionId) throws IOException {
        URL url = new URL(httpUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.addRequestProperty("Cookie","JSESSIONID="+jsessionId);
        conn.connect();
        String respStr =StreamUtils.copyToString(conn.getInputStream(), Charset.forName("UTF-8"));
        System.out.println(respStr);
    }
}
