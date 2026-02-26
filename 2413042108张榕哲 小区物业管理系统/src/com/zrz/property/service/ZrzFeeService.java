package com.zrz.property.service;

import com.zrz.property.dao.ZrzPropertyFeeDao;
import com.zrz.property.dao.ZrzHeatingFeeDao;
import com.zrz.property.model.ZrzPropertyFee;
import com.zrz.property.model.ZrzHeatingFee;

import java.math.BigDecimal;
import java.util.List;

/**
 * 费用管理业务逻辑层
 * 负责处理物业费和取暖费的业务逻辑
 */
//两个DAO实例，分别处理不同费用类型
public class ZrzFeeService {
    private ZrzPropertyFeeDao propertyFeeDao = new ZrzPropertyFeeDao();
    private ZrzHeatingFeeDao heatingFeeDao = new ZrzHeatingFeeDao();

    // ==================== 物业费相关 ====================
    
    /**
     * 添加物业费记录
     * @param fee 物业费对象
     * @return 添加成功返回true，失败返回false
     */
    public boolean addPropertyFee(ZrzPropertyFee fee) {
        return propertyFeeDao.addPropertyFee(fee);
    }

    /**
     * 缴纳物业费
     * @param feeId 费用ID
     * @param amount 缴纳金额
     * @return 缴纳成功返回true，失败返回false
     */
    public boolean payPropertyFee(int feeId, BigDecimal amount) {
        return propertyFeeDao.payPropertyFee(feeId, amount);
    }

    /**
     * 获取所有物业费记录
     * @return 物业费列表（包含居民信息）
     */
    public List<ZrzPropertyFee> getAllPropertyFees() {
        return propertyFeeDao.findAllPropertyFees();
    }

    /**
     * 获取逾期未缴的物业费记录
     * @return 逾期物业费列表
     */
    public List<ZrzPropertyFee> getOverduePropertyFees() {
        return propertyFeeDao.findOverdueFees();
    }

    /**
     * 根据居民ID获取物业费记录
     * @param residentId 居民ID
     * @return 该居民的物业费列表
     */
    public List<ZrzPropertyFee> getPropertyFeesByResident(int residentId) {
        return propertyFeeDao.findByResidentId(residentId);
    }

    // ==================== 取暖费相关 ====================
    
    /**
     * 添加取暖费记录
     * @param fee 取暖费对象
     * @return 添加成功返回true，失败返回false
     */
    public boolean addHeatingFee(ZrzHeatingFee fee) {
        return heatingFeeDao.addHeatingFee(fee);
    }

    /**
     * 缴纳取暖费
     * @param heatingId 取暖费ID
     * @param amount 缴纳金额
     * @return 缴纳成功返回true，失败返回false
     */
    public boolean payHeatingFee(int heatingId, BigDecimal amount) {
        return heatingFeeDao.payHeatingFee(heatingId, amount);
    }

    /**
     * 获取所有取暖费记录
     * @return 取暖费列表（包含居民信息）
     */
    public List<ZrzHeatingFee> getAllHeatingFees() {
        return heatingFeeDao.findAllHeatingFees();
    }

    /**
     * 获取逾期未缴的取暖费记录
     * @return 逾期取暖费列表
     */
    public List<ZrzHeatingFee> getOverdueHeatingFees() {
        return heatingFeeDao.findOverdueFees();
    }

    /**
     * 检查居民是否可以购买电卡/水卡
     * 业务规则：必须没有未缴的物业费和取暖费才能购买
     * @param residentId 居民ID
     * @return 可以购买返回true，否则返回false
     */
    public boolean canPurchaseCard(int residentId) {
        boolean hasUnpaidProperty = propertyFeeDao.hasUnpaidFees(residentId);
        boolean hasUnpaidHeating = heatingFeeDao.hasUnpaidFees(residentId);
        return !hasUnpaidProperty && !hasUnpaidHeating;
    }
}
