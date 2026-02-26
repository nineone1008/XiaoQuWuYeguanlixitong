package com.zrz.property.util;

import com.zrz.property.model.ZrzUser;

/**
 * 会话工具类
 * 用于保存和管理当前登录用户的会话信息
 * 采用静态变量存储，实现单例模式
 */
public class ZrzSessionUtil {
    // 当前登录的用户对象
    private static ZrzUser currentUser;

    /**
     * 设置当前登录用户
     * 在用户登录成功后调用
     * @param user 登录的用户对象
     */
    public static void setCurrentUser(ZrzUser user) {
        currentUser = user;
    }

    /**
     * 获取当前登录用户
     * @return 当前用户对象，未登录返回null
     */
    public static ZrzUser getCurrentUser() {
        return currentUser;
    }

    /**
     * 获取当前用户名
     * @return 用户名，未登录返回"未登录"
     */
    public static String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : "未登录";
    }

    /**
     * 获取当前用户真实姓名
     * @return 真实姓名，未登录返回"未登录"
     */
    public static String getCurrentRealName() {
        return currentUser != null ? currentUser.getRealName() : "未登录";
    }

    /**
     * 获取当前用户角色
     * @return 用户角色（管理员/普通用户），未登录返回空字符串
     */
    public static String getCurrentRole() {
        return currentUser != null ? currentUser.getRole() : "";
    }

    /**
     * 判断当前用户是否为管理员
     * @return 是管理员返回true，否则返回false
     */
    public static boolean isAdmin() {
        return currentUser != null && "管理员".equals(currentUser.getRole());
    }

    /**
     * 清除会话信息
     * 在用户退出登录时调用
     */
    public static void clearSession() {
        currentUser = null;
    }

    /**
     * 判断是否已登录
     * @return 已登录返回true，未登录返回false
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
