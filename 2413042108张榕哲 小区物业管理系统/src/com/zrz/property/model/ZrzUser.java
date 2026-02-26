package com.zrz.property.model;

import java.util.Date;

/**
 * 系统用户实体类
 * 对应数据库表：zrz_user
 * 用于存储系统登录用户的信息，包括管理员和普通用户
 */
public class ZrzUser {
    private Integer userId;          // 用户ID（主键）
    private String username;         // 用户名（登录账号）
    private String password;         // 密码
    private String realName;         // 真实姓名
    private String role;             // 角色（管理员、普通用户）
    private String phone;            // 联系电话
    private String status;           // 账号状态（启用、禁用）
    private Date lastLogin;          // 最后登录时间
    private Date createdAt;          // 创建时间
    private Date updatedAt;          // 更新时间

    public ZrzUser() {}

    public ZrzUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getLastLogin() { return lastLogin; }
    public void setLastLogin(Date lastLogin) { this.lastLogin = lastLogin; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "ZrzUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
