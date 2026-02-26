package com.zrz.property.service;

import com.zrz.property.dao.ZrzUserDao;
import com.zrz.property.model.ZrzUser;

import java.util.List;// 导入Java标准库的List接口，用于返回用户列表

/**
 * 用户业务逻辑层
 * 负责处理用户相关的业务逻辑，包括登录、用户管理等
 */
public class ZrzUserService {
    private ZrzUserDao userDao = new ZrzUserDao();

    /**
     * 用户登录验证
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户对象，失败返回null
     */
    public ZrzUser login(String username, String password) {
        // 参数验证
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            return null;
        }
        // 调用DAO层进行登录验证
        //传递修剪过空格的用户名和密码
        //将DAO层的结果直接返回给调用者
        return userDao.login(username.trim(), password.trim());
    }

    /**
     * 添加用户
     * @param user 用户对象
     * @return 添加成功返回true，用户名已存在或失败返回false
     */
    public boolean addUser(ZrzUser user) {
        // 检查用户名是否已存在
        if (userDao.findByUsername(user.getUsername()) != null) {
            return false;
        }
        return userDao.addUser(user);//用户名不重复时，调用DAO层添加用户，将DAO层的结果直接返回
    }

    /**
     * 更新用户信息
     * @param user 用户对象（包含要更新的ID）
     * @return 更新成功返回true，失败返回false
     */
    //更新用户信息
    //直接调用DAO层的updateUser方法
    //假设user对象包含有效的用户ID
    public boolean updateUser(ZrzUser user) {
        return userDao.updateUser(user);
    }

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 删除成功返回true，失败返回false
     */
    public boolean deleteUser(int userId) {
        return userDao.deleteUser(userId);
    }

    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    public List<ZrzUser> getAllUsers() {
        return userDao.findAllUsers();
    }

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码（预留参数，可用于验证）
     * @param newPassword 新密码
     * @return 修改成功返回true，失败返回false
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        // 这里可以添加旧密码验证逻辑
        return userDao.changePassword(userId, newPassword);
    }
}
