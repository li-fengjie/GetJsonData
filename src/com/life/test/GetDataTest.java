package com.life.test;

import net.sf.json.JSONObject;
import sun.font.TrueTypeFont;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/*
https://max.book118.com/html/2017/0128/87198501.shtm
 */
public class GetDataTest {
    public static void main(String[] args) {
        String url = "https://view3652.book118.com/pdf/GetNextPage/?" +
                "f=RTpcT2ZmaWNlV2ViMzY1XE9mZmljZVdlYlxjYWNoZVxQREZcMTE3MDEyODEyMjU1MjExODMwMjEwMTIyMjlfMzY4MzMyOFwyMTEzNDM1LTU4OGMxZDUwMTg1NjkuZG9jLnRlbXA%3D" +
                "&isMobile=false" +
                "&isNet=True" +
                "&readLimit=6GD3TouGfnYSjbfHqSnh%40Q%3D%3D" +
                "&furl=o4j9ZG7fK97PX9vZopx9OZEQwygFDDlsYeLf%40VY1ztzuFpJRcU1Zd0Dd2GnEXoJ7XeYf6qlPHwZTuqYArDIPJkzX7wLRwEarlmKFOcWLLQF19FcDffsaUQ%3D%3D" +
                "&img=";
        String fileDir = "D:/文件/数据素材/江西理工大学/计算机组成原理第二版/";
//        "7o@o7xcocmnCKtMZqHMCpIVevAWGL_iQy_4ux1Oi5BpCcwTGEdRdzx@yxeMU63at98U3UOUNFGo="

        String nextPage ="7o%40o7xcocmnCKtMZqHMCpIVevAWGL_iQy_4ux1Oi5BpCcwTGEdRdz5pZlIF4lUAGC1WU0dboedQ%3D";
        int count = 0;

        while (true){
            String imgUrl = "https://view3652.book118.com/img/?img=" + nextPage;
            saveData(fileDir,count+"",imgUrl);
            count++;
            String urlNow = url + nextPage;
            String result = getURLContent(urlNow);
            JSONObject jsonObject = JSONObject.fromObject(result);
            nextPage = (String) jsonObject.get("NextPage");
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
