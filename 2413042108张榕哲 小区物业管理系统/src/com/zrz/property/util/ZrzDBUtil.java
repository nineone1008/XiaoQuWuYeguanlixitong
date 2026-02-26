package com.zrz.property.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 * 提供数据库连接的获取、关闭和测试功能
 * 使用MySQL数据库，采用JDBC连接方式
 */
public class ZrzDBUtil {
    // 数据库连接URL（包含时区、字符编码等配置）
    private static final String URL = "jdbc:mysql://localhost:3306/zrz_property_management?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
    // 数据库用户名
    private static final String USER = "root";
    // 数据库密码
    private static final String PASSWORD = "zrz20050929@";

    // 静态代码块：在类加载时执行，用于加载MySQL驱动
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL驱动加载失败！");
        }
    }

    /**
     * 获取数据库连接
     * @return 数据库连接对象
     * @throws SQLException 数据库连接异常
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 关闭数据库连接
     * 释放数据库资源，防止连接泄漏
     * @param conn 要关闭的连接对象
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试数据库连接
     * 用于系统启动时检查数据库是否可用
     * @return 连接成功返回true，失败返回false
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
