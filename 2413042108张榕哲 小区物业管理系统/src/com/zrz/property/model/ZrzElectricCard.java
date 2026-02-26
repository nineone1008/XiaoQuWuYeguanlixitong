package com.zrz.property.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 电卡购买记录实体类
 * 对应数据库表：zrz_electric_card
 * 用于记录居民的电卡购买历史
 * 业务规则：必须缴清物业费和取暖费才能购买
 */
//该层类为POJO（简单Java对象），严格映射数据库表结构，仅包含属性、get/set方法、无参/有参构造，用于封装业务数据
public class ZrzElectricCard {
    private Integer electricId;      // 电卡记录ID（主键）
    private Integer residentId;      // 居民ID（外键）
    private BigDecimal purchaseAmount; // 购买金额
    private Date purchaseDate;       // 购买日期
    private BigDecimal kwhAmount;    // 电量（千瓦时）
    //记录操作此业务的员工姓名或工号  用于审计追踪  字符串类型，存储操作员标识
    private String operator;         // 操作员

    // 关联查询字段（用于显示）
    private String roomNumber;       // 房号
    private String residentName;     // 居民姓名

    public ZrzElectricCard() {}

    // Getters and Setters
    public Integer getElectricId() { return electricId; }
    public void setElectricId(Integer electricId) { this.electricId = electricId; }

    public Integer getResidentId() { return residentId; }
    public void setResidentId(Integer residentId) { this.residentId = residentId; }

    public BigDecimal getPurchaseAmount() { return purchaseAmount; }
    public void setPurchaseAmount(BigDecimal purchaseAmount) { this.purchaseAmount = purchaseAmount; }

    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }

    public BigDecimal getKwhAmount() { return kwhAmount; }
    public void setKwhAmount(BigDecimal kwhAmount) { this.kwhAmount = kwhAmount; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getResidentName() { return residentName; }
    public void setResidentName(String residentName) { this.residentName = residentName; }
}
