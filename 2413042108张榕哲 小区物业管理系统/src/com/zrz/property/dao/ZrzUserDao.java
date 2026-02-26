package com.zrz.property.dao;

import com.zrz.property.model.ZrzUser;
import com.zrz.property.util.ZrzDBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问层
 */
public class ZrzUserDao {

    /**
     * 用户登录验证
     * 验证用户名和密码是否正确，且账号状态为启用
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户对象，失败返回null
     */
    public ZrzUser login(String username, String password) {
        // SQL查询：验证用户名、密码，并检查账号状态
        String sql = "SELECT * FROM zrz_user WHERE username=? AND password=? AND status='启用'";

        //使用try-with-resources语句，自动关闭资源
          //      从数据库工具类获取连接
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //设置SQL参数
            //参数索引从1开始
            //将用户名和密码绑定到PreparedStatement
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            //执行查询，返回结果集
            //查询单个用户，使用executeQuery
            ResultSet rs = pstmt.executeQuery();
            //rs.next()：检查是否有查询结果
            //调用extractUser方法将ResultSet转换为User对象
            //登录成功后，更新用户的最后登录时间
            //返回用户对象
            if (rs.next()) {
                ZrzUser user = extractUser(rs);
                updateLastLogin(user.getUserId());
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新最后登录时间
     * 在用户成功登录后自动调用
     * @param userId 用户ID
     */
    private void updateLastLogin(int userId) {
        String sql = "UPDATE zrz_user SET last_login=NOW() WHERE user_id=?";
        //执行更新操作
        //executeUpdate()：返回受影响的行数
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加用户
     * 插入新用户记录到数据库
     * @param user 用户对象（包含用户名、密码、真实姓名、角色、电话、状态）
     * @return 添加成功返回true，失败返回false
     */
    public boolean addUser(ZrzUser user) {
        String sql = "INSERT INTO zrz_user (username, password, real_name, role, phone, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //按顺序设置所有参数
            //从user对象获取数据
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRealName());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getStatus());
            //执行插入操作
            //executeUpdate()返回受影响的行数
            //大于0表示至少插入了1行，操作成功
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新用户
     */
    public boolean updateUser(ZrzUser user) {
        String sql = "UPDATE zrz_user SET username=?, password=?, real_name=?, role=?, phone=?, status=? WHERE user_id=?";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRealName());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getStatus());
            pstmt.setInt(7, user.getUserId());
            //注意参数顺序：前面6个是字段值，第7个是WHERE条件
            //使用用户ID作为更新条件
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除用户
     */
    //根据用户ID删除用户
    //返回是否删除成功
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM zrz_user WHERE user_id=?";
        //根据主键删除记录
        //使用参数化查询确保安全
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询所有用户
     */
    public List<ZrzUser> findAllUsers() {
        List<ZrzUser> list = new ArrayList<>();
        //创建ArrayList存储查询结果
        //使用接口类型List，提高灵活性
        String sql = "SELECT * FROM zrz_user ORDER BY user_id";
        try (Connection conn = ZrzDBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            //使用Statement（因为没有参数）
            //三个资源都放在try-with-resources中
            while (rs.next()) {
                list.add(extractUser(rs));
                //遍历结果集
                //将每条记录转换为User对象并添加到列表
                //while循环处理多行结果
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据用户名查询
     */
    //用于检查用户名是否已存在
    public ZrzUser findByUsername(String username) {
        String sql = "SELECT * FROM zrz_user WHERE username=?";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改密码
     */
    //修改指定用户的密码
    //只更新密码字段
    public boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE zrz_user SET password=? WHERE user_id=?";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从ResultSet提取用户对象
     * 将数据库查询结果转换为用户对象
     * @param rs 数据库查询结果集
     * @return 用户对象
     * @throws SQLException 数据库异常
     */
    //私有工具方法
    //从ResultSet中提取数据构建User对象
    //可能抛出SQLException，由调用者处理
    private ZrzUser extractUser(ResultSet rs) throws SQLException {
        ZrzUser user = new ZrzUser();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRealName(rs.getString("real_name"));
        user.setRole(rs.getString("role"));
        user.setPhone(rs.getString("phone"));
        user.setStatus(rs.getString("status"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}
