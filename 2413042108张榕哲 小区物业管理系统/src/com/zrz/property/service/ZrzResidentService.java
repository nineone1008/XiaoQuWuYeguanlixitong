package com.zrz.property.service;

import com.zrz.property.dao.ZrzResidentDao;
import com.zrz.property.model.ZrzResident;

import java.util.List;

/**
 * 居民信息业务逻辑层
 * 负责处理居民信息的业务逻辑，包括增删改查等操作
 */
public class ZrzResidentService {
    private ZrzResidentDao residentDao = new ZrzResidentDao();

    /**
     * 添加居民信息
     * @param resident 居民对象
     * @return 添加成功返回true，失败返回false
     */
    public boolean addResident(ZrzResident resident) {
        return residentDao.addResident(resident);
    }

    /**
     * 更新居民信息
     * @param resident 居民对象（包含要更新的ID）
     * @return 更新成功返回true，失败返回false
     */
    public boolean updateResident(ZrzResident resident) {
        return residentDao.updateResident(resident);
    }

    /**
     * 删除居民信息
     * @param residentId 居民ID
     * @return 删除成功返回true，失败返回false
     */
    public boolean deleteResident(int residentId) {
        return residentDao.deleteResident(residentId);
    }

    /**
     * 获取所有居民信息
     * @return 居民列表
     */
    public List<ZrzResident> getAllResidents() {
        return residentDao.findAllResidents();
    }

    /**
     * 根据房号搜索居民（支持模糊查询）
     * @param roomNumber 房号关键字
     * @return 匹配的居民列表
     */
    public List<ZrzResident> searchByRoomNumber(String roomNumber) {
        return residentDao.findByRoomNumber(roomNumber);
    }

    /**
     * 根据姓名搜索居民（支持模糊查询）
     * @param name 姓名关键字
     * @return 匹配的居民列表
     */
    public List<ZrzResident> searchByName(String name) {
        return residentDao.findByName(name);
    }

    /**
     * 根据ID获取居民信息
     * @param residentId 居民ID
     * @return 居民对象，不存在返回null
     */
    public ZrzResident getResidentById(int residentId) {
        return residentDao.findById(residentId);
    }
}
