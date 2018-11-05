package com.life.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.life.test.GetDataTest.readInputStream;

/**
 * https://jianli.51job.com/xiaohui/322.html
 * 校徽图片
 */
public class GetXiaoHui {
    public static void main(String[] args) {
        String fileDir = "D:\\文件\\数据素材\\高校校徽\\";
        String path = "https://jianli.51job.com/ajax/download.php?type=image&resumetype=xiaohui&id=";
        for(int i = 5; i < 400; i ++){
            try {
                path += i;
                saveData(fileDir,path);
            }catch (Exception e){
                System.out.println(e.toString());
                continue;
            }
        }
    }

    private static void saveData(String fileDir, String url) {
        String fileName;
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
                String disposition=conn.getHeaderField("Content-Disposition");
//                String str = new String(disposition.getBytes("ISO-8859-1"),"UTF-8");
                fileName=disposition.split(";")[1].split("=")[1].replaceAll("\"","");
                System.out.println(fileName);
                //得到图片的二进制数据，以二进制封装得到数据，具有通用性
                byte[] data = readInputStream(inputStream);
                String path = fileDir;
//                File filePath = new File(path);
//                if (!filePath.exists()) {
//                    filePath.mkdirs();
//                }
//                File file = new File(path + "/" +fileName + ".png");
//                FileOutputStream outStream = new FileOutputStream(file);
//                outStream.write(data);
//                outStream.close();
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
