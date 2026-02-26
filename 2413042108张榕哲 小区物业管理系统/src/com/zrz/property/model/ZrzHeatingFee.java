package com.zrz.property.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 取暖费记录实体类
 * 对应数据库表：zrz_heating_fee
 * 用于记录居民的取暖费缴纳情况（按年度收取）
 */
public class ZrzHeatingFee {
    private Integer heatingId;       // 取暖费ID（主键）
    private Integer residentId;      // 居民ID（外键）
    private Integer feeYear;         // 费用年度
    private BigDecimal amount;       // 应缴金额
    private BigDecimal paidAmount;   // 已缴金额
    private String paymentStatus;    // 缴费状态（未缴、已缴）
    private Date dueDate;            // 应缴日期
    private Date paymentDate;        // 实际缴费日期
    private Integer overdueDays;     // 逾期天数
    private Date createdAt;          // 创建时间

    // 关联查询字段（用于显示）
    private String roomNumber;       // 房号
    private String residentName;     // 居民姓名

    public ZrzHeatingFee() {}

    // Getters and Setters
    public Integer getHeatingId() { return heatingId; }
    public void setHeatingId(Integer heatingId) { this.heatingId = heatingId; }

    public Integer getResidentId() { return residentId; }
    public void setResidentId(Integer residentId) { this.residentId = residentId; }

    public Integer getFeeYear() { return feeYear; }
    public void setFeeYear(Integer feeYear) { this.feeYear = feeYear; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    public Integer getOverdueDays() { return overdueDays; }
    public void setOverdueDays(Integer overdueDays) { this.overdueDays = overdueDays; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getResidentName() { return residentName; }
    public void setResidentName(String residentName) { this.residentName = residentName; }
}
