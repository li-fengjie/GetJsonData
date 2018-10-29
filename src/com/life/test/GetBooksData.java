package com.life.test;

import com.life.dao.BooksDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetBooksData {
    public static void main(String[] args){
        String url = "https://kaoshibb.com/api/v3/textbookDetail?token=null&appid=daxueyoudaan&appsecret=daxue20171101&id=";
        String fileDir = "D:/文件/数据素材/教材答案/";
        for(int i = 539; i <= 800; i++){
            String urlNow = url+ i;
            String result = getURLContent(urlNow);
            if(result.isEmpty() || result.equals("")){
                System.out.println(i +" 空");
                continue;
            }
            JSONObject jsonObject2 = JSONObject.fromObject(result);
            JSONObject jsonObject = (JSONObject) jsonObject2.get("data");
            String name = (String) jsonObject.get("name");
            String cover = (String) jsonObject.get("cover");
            String isbn = (String) jsonObject.get("isbn");
            String author = (String) jsonObject.get("author");
            String publisher = (String) jsonObject.get("publisher");
            System.out.println(i+" " + isbn + " " +name + " " +author +" "+publisher +" " + cover);
            System.out.println(i +" " + isbn + " " + cover);
            saveData(fileDir,name.trim(),isbn,cover);
            BooksDao booksDao = new BooksDao();
            booksDao.insertToBooks(isbn,name,author,publisher,cover);

            JSONArray list = jsonObject.getJSONArray("list");

            for(int j = 0; j < list.size(); j++){
                int id = (int) list.getJSONObject(j).get("id");
                int index = (int) list.getJSONObject(j).get("index");
                String urlTitle = "https://kaoshibb.com/api/v3/chapterDetail?appid=daxueyoudaan&appsecret=daxue20171101&id=";
                urlTitle += id;
                String titleData = getURLContent(urlTitle);
                if(titleData.isEmpty() || titleData.equals("")){
                    continue;
                }
                JSONObject jsonObject1 = JSONObject.fromObject(titleData);
                JSONObject jsonObject3 = jsonObject1.getJSONObject("data");
                String title = (String) jsonObject3.get("title");
                String content = (String) jsonObject3.get("content");
                String textbook = (String) jsonObject3.get("textbook");
                booksDao.insertToDir(textbook,title,content,index + "");
                ArrayList arrayList = getImgStr(content);
                if(!arrayList.isEmpty()){
                    for (int k = 0; k < arrayList.size(); k++){
                        saveData(fileDir,textbook.trim(),title.trim(),k+"",arrayList.get(k).toString());
                    }
                }
            }
        }
    }

    private static void saveData(String fileDir, String name, String title,String id, String url) {
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
                String path = fileDir + name + "/" + title;
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

    private static void saveData(String fileDir, String name,String isbn, String url) {
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
                String path = fileDir + name;
                File filePath = new File(path);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                File file = new File(path + "/" + isbn + "-"+name + ".png");
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

    /**
     * 得到网页中图片的地址
     */
    public static ArrayList<String> getImgStr(String htmlStr) {
        ArrayList<String> pics = new ArrayList<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);

            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
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
