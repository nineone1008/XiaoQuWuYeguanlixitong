package com.zrz.property.service;

import com.zrz.property.dao.ZrzCardDao;
import com.zrz.property.model.ZrzElectricCard;
import com.zrz.property.model.ZrzWaterCard;

import java.util.List;

/**
 * 电卡水卡业务逻辑层
 * 负责处理电卡和水卡的购买业务，包含费用缴纳状态检查
 */
public class ZrzCardService {
    private ZrzCardDao cardDao = new ZrzCardDao();
    private ZrzFeeService feeService = new ZrzFeeService();

    /**
     * 购买电卡
     * 业务规则：必须先缴清物业费和取暖费才能购买
     * @param card 电卡购买信息
     * @return 返回操作结果消息
     */
    public String purchaseElectricCard(ZrzElectricCard card) {
        // 检查是否有未缴费用
        if (!feeService.canPurchaseCard(card.getResidentId())) {
            return "该用户有未缴纳的物业费或取暖费，无法购买电卡！";
        }
        // 添加购买记录
        boolean success = cardDao.addElectricCard(card);
        return success ? "电卡购买成功！" : "电卡购买失败！";
    }

    /**
     * 购买水卡
     * 业务规则：必须先缴清物业费和取暖费才能购买
     * @param card 水卡购买信息
     * @return 返回操作结果消息
     */
    public String purchaseWaterCard(ZrzWaterCard card) {
        // 检查是否有未缴费用
        if (!feeService.canPurchaseCard(card.getResidentId())) {
            return "该用户有未缴纳的物业费或取暖费，无法购买水卡！";
        }
        // 添加购买记录
        boolean success = cardDao.addWaterCard(card);
        return success ? "水卡购买成功！" : "水卡购买失败！";
    }

    /**
     * 获取所有电卡购买记录
     * @return 电卡购买记录列表（包含居民信息）
     */
    //获取所有电卡购买记录
    //直接委托给DAO层
    //返回包含关联信息的完整记录列表
    public List<ZrzElectricCard> getAllElectricCards() {
        return cardDao.findAllElectricCards();
    }

    /**
     * 获取所有水卡购买记录
     * @return 水卡购买记录列表（包含居民信息）
     */
    public List<ZrzWaterCard> getAllWaterCards() {
        return cardDao.findAllWaterCards();
    }
}
