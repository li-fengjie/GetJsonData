package com.life.test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {
    public static void main(String[] args){
        String str = "{\"code\":\"0\",\"message\":\"\",\"data\":{\"id\":3741,\"title\":\"第一章　量子力学基础\",\"content\":\"<p><img src=\"http://img.kaoshibb.com/textbooks/20180619/69410372c8e0489ea07f73d11982fc22.Png\" title=\"015.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/7503d09b51a043bfaa65268572df7591.Png\" title=\"016.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/2c13dfac836c4ff2956e40a72d07e87a.Png\" title=\"017.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/da889342fd8b4ff29b5db10e8318e4a4.Png\" title=\"018.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/2771b20f3783403385eb8e5ae7fa1665.Png\" title=\"019.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/6798b0f9895b4333a5bc2e0a5900176a.Png\" title=\"020.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/187ddb42eadd48f8826c55dd71f5318e.Png\" title=\"021.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/d9cc11c5de034b51bbfb5f0625d9cfa5.Png\" title=\"022.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/62b6a2eb19624dc8a62704330508853b.Png\" title=\"023.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/9857e75c79b24d6c95203e18960bf1a7.Png\" title=\"024.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/4f203c43b661497cb91c21aaa13f1621.Png\" title=\"025.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/4e3782c0702b444dac7ca989239baa73.Png\" title=\"026.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/34bfcd53cdb64c718fa4bd09bd5c071b.Png\" title=\"027.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/18e6af099e374d84a48fdd173a83c47f.Png\" title=\"028.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/79fccfb37a90466a9da65b302ff7da99.Png\" title=\"029.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/8a2c2fff7f4c463f92b610b180f28696.Png\" title=\"030.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/4709acc6cff1414fac80ca78d76dd9ce.Png\" title=\"031.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/be20da119e674a0ba5e4ecafd02ffdac.Png\" title=\"032.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/07851c40b2424643bc7aaa48bae373af.Png\" title=\"033.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/effe5dbf1d0245bebe3502c98f91f2b9.Png\" title=\"034.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/6fd27ef1281b452fb79cb50e983523c4.Png\" title=\"035.Png\" /></p><p><img src=\"http://img.kaoshibb.com/textbooks/20180619/e9b7369744b042cdabed3c6f813d3287.Png\" title=\"036.Png\" /></p>\",\"textbook\":\"简明结构化学教程(第三版)\",\"index\":0}}";
//        String str = "";
        ArrayList list = getImgStr(str);
        System.out.println(list.toString());
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
}
