package com.life.test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetPapersData {
    public static void main(String[] args) throws InterruptedException {
        String fileDir = "D:/文件/数据素材/试卷/";
        String path = "https://kaoshibb.com/api/v3/paperDetail?appid=daxueyoudaan&appsecret=daxue20171101&id=";

        for (int i = 19000; i < 20000; i++) {
            Thread.sleep(200);
            String pathNow = path + i;
            String result = getURLContent(pathNow);
            System.out.println(result);
            if (result.equals("") || result.isEmpty()) {
                System.out.println(i + " 空");
                continue;
            }
            JSONObject jsonObject = JSONObject.fromObject(result);

            JSONObject jsonData = (JSONObject) jsonObject.get("data");

            JSONArray jsonArray = jsonData.getJSONArray("list");
            System.out.println(jsonArray);

            String name = (String) jsonData.get("name");
            System.out.println(name);

            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject jsonObjects = jsonArray.getJSONObject(j);
                System.out.println(jsonObjects);
                int id = (int) jsonObjects.get("id");
                String url = (String) jsonObjects.get("url");
                saveData(fileDir,name,id+"",url);
            }
        }
    }

    private static void saveData(String fileDir, String name, String id, String url) {
        HttpURLConnection conn = null;
        try {
            URL url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream inputStream = conn.getInputStream();
                //得到图片的二进制数据，以二进制封装得到数据，具有通用性
                byte[] data = readInputStream(inputStream);
                String[] split = name.split("《|》");
                String path = fileDir + split[0] + "/" + split[1] + "/" + split[2];
                File filePath = new File(path);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                File file = new File(path + "/" + id + ".png");
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
    private static String getURLContent(String urlStr) {
        //请求的url
        URL url = null;
        //建立的http链接
        HttpURLConnection httpConn = null;
        //请求的输入流
        BufferedReader in = null;
        //输入流的缓冲
        StringBuffer sb = new StringBuffer();
        try {
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String str = null;
            //一行一行进行读入
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception ex) {

        } finally {
            try {
                if (in != null) {
                    in.close(); //关闭流
                }
            } catch (IOException ex) {
            }
        }
        String result = sb.toString();
        return result;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}
