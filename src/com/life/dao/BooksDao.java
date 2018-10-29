package com.life.dao;

import com.life.utils.C3P0Utils;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.SQLException;

public class BooksDao {
    public void insertToBooks(String isbn,String name,String author,String publisher,String cover) {
        QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
        String sql="insert into books(isbn,name,author,publisher,cover) values(?,?,?,?,?)";
        try {
            qr.update(sql,isbn,name,author,publisher,cover);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertToDir(String name,String title,String content,String index){
        QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
        String sql="insert into directory(name,title,content,i) values(?,?,?,?)";
        try {
            qr.update(sql,name,title,content,index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
