package com.life.test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.*;

import static com.life.test.GetDataTest.readInputStream;

/**
 * 获取校徽图片
 */
public class GetImagesData {
    public static void main(String[] args) {
       String json = sendPost("https://zouxiaoming.xyz:8443/avatar_change/search?name=");
//        JSONObject jsonObject =  JSONObject.fromObject(json);
        System.out.println(json);
        JSONArray jsonArray = JSONArray.fromObject(json);
        for(int i = 0; i <= 2588; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            try {
                String logo = (String)jsonObject.get("logo");
                if(logo.equals("") || logo == null){
                    continue;
                }
                String name = (String) jsonObject.get("name");
                String url = "https://zouxiaoming.xyz:8443//avatar_change/images/logo/";
                url += logo;
                saveData("D:\\文件\\数据素材\\其他",name,url);
            }catch (Exception e){
                continue;
            }


        }
    }

    public static String sendPost(String url) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print("{\n" +
                    "\"name\":\"\"\n" +
                    "}");
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static void saveData(String fileDir, String name, String url) {
        HttpURLConnection conn = null;
        try {
            URL url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream inputStream = conn.getInputStream();
                //得到图片的二进制数据，以二进制封装得到数据，具有通用性
                byte[] data = readInputStream(inputStream);
                String path = fileDir;
                File filePath = new File(path);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                File file = new File(path + "/" +name + ".png");
                FileOutputStream outStream = new FileOutputStream(file);
                outStream.write(data);
                outStream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
