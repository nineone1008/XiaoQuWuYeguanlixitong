package com.zrz.property.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 水卡购买记录实体类
 * 对应数据库表：zrz_water_card
 * 用于记录居民的水卡购买历史
 * 业务规则：必须缴清物业费和取暖费才能购买
 */
public class ZrzWaterCard {
    private Integer waterId;           // 水卡记录ID（主键）
    private Integer residentId;        // 居民ID（外键）
    private BigDecimal purchaseAmount; // 购买金额
    private Date purchaseDate;         // 购买日期
    private BigDecimal cubicAmount;    // 水量（立方米）
    private String operator;           // 操作员

    // 关联查询字段（用于显示）
    private String roomNumber;       // 房号
    private String residentName;     // 居民姓名

    public ZrzWaterCard() {}

    // Getters and Setters
    public Integer getWaterId() { return waterId; }
    public void setWaterId(Integer waterId) { this.waterId = waterId; }

    public Integer getResidentId() { return residentId; }
    public void setResidentId(Integer residentId) { this.residentId = residentId; }

    public BigDecimal getPurchaseAmount() { return purchaseAmount; }
    public void setPurchaseAmount(BigDecimal purchaseAmount) { this.purchaseAmount = purchaseAmount; }

    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }

    public BigDecimal getCubicAmount() { return cubicAmount; }
    public void setCubicAmount(BigDecimal cubicAmount) { this.cubicAmount = cubicAmount; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getResidentName() { return residentName; }
    public void setResidentName(String residentName) { this.residentName = residentName; }
}
