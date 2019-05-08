package com.xupt.zhumeng.speedkill.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接
 */
public class DBUtil {

    private static Properties props;

    static {
        InputStream in = null;
        try {
            in = DBUtil.class.getClassLoader().getResourceAsStream("application.properties");
            props = new Properties();
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConn() throws Exception {
        String url = props.getProperty("spring.datasource.url");
        String username = props.getProperty("spring.datasource.username");
        String password = props.getProperty("spring.datasource.password");
        String driver = props.getProperty("spring.datasource.driver-class-name");
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DBUtil.getConn();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
