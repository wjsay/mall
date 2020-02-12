package com.wjsay.mall.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wjsay.mall.domain.MiaoshaUser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserUtil {
    private static void createUser(int count) throws Exception {
        List<MiaoshaUser> users = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            MiaoshaUser user = new MiaoshaUser();
            user.setId(10000 + i);
            user.setPhoneno(13000000000L + i);
            user.setNickname("user" + i);
            user.setRegisterDate(new Date());
            user.setSalt("1a2b3c");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
            users.add(user);
        }
        System.out.printf("create user%n");

        Connection conn = DBUtil.getConn();
        String sql = "insert into miaoshauser (nackname, register_date, salt, password, id)  values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < users.size(); ++i) {
            MiaoshaUser user = users.get(i);
            pstmt.setString(1, user.getNickname());
            pstmt.setTimestamp(2, new Timestamp(user.getRegisterDate().getTime()));
            pstmt.setString(3, user.getSalt());
            pstmt.setString(4, user.getPassword());
            pstmt.setInt(5, user.getId());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
        System.out.println("insert to db");

        String urlString = "http://localhost:8080/login/do_login";
        File file= new File("D:/tockns.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i< users.size(); ++i) {
            MiaoshaUser user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection)url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getPhoneno() + "&password=" + user.getPassword();
            out.write(params.getBytes());
            out.flush();
            InputStream in = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = in.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            in.close();
            bout.close();
            String response = new String(bout.toByteArray());
            JSONObject jo = JSON.parseObject(response);
            String token = jo.getString("data");
            System.out.println("create token: "  + user.getPhoneno());

            String row = user.getPhoneno() + "," + token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file: " + user.getId());
        }
        raf.close();
        System.out.println("over");
    }
}
