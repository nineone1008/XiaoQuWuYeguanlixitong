package com.zrz.property.model;

import java.util.Date;
//对应数据库表的Java对象映射
/**
 * 居民信息实体类
 * 对应数据库表：zrz_resident
 * 用于存储小区居民的基本信息
 */
public class ZrzResident {
    private Integer residentId;      // 居民ID（主键）
    private String roomNumber;       // 房号
    private String residentName;     // 居民姓名
    private String phone;            // 联系电话
    private String idCard;           // 身份证号
    private Integer familyMembers;   // 家庭成员数量
    private Date registerDate;       // 登记日期
    private String status;           // 状态（正常、欠费、已搬离）
    private Date createdAt;          // 创建时间
    private Date updatedAt;          // 更新时间

    public ZrzResident() {}

    public ZrzResident(String roomNumber, String residentName, String phone, String idCard, 
                       Integer familyMembers, Date registerDate) {
        this.roomNumber = roomNumber;
        this.residentName = residentName;
        this.phone = phone;
        this.idCard = idCard;
        this.familyMembers = familyMembers;
        this.registerDate = registerDate;
        this.status = "正常";
    }

    // Getters and Setters
    public Integer getResidentId() { return residentId; }
    public void setResidentId(Integer residentId) { this.residentId = residentId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getResidentName() { return residentName; }
    public void setResidentName(String residentName) { this.residentName = residentName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public Integer getFamilyMembers() { return familyMembers; }
    public void setFamilyMembers(Integer familyMembers) { this.familyMembers = familyMembers; }

    public Date getRegisterDate() { return registerDate; }
    public void setRegisterDate(Date registerDate) { this.registerDate = registerDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    @Override
    //重写Object类的toString()方法
    //返回对象的字符串表示
    //包含关键字段，便于调试和日志记录
    public String toString() {
        return "ZrzResident{" +
                "residentId=" + residentId +
                ", roomNumber='" + roomNumber + '\'' +
                ", residentName='" + residentName + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
