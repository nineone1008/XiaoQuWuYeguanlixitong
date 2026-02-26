package com.zrz.property.dao;

import com.zrz.property.model.ZrzResident;
import com.zrz.property.util.ZrzDBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 居民信息数据访问层
 */
public class ZrzResidentDao {

    /**
     * 添加居民信息
     */
    public boolean addResident(ZrzResident resident) {
        String sql = "INSERT INTO zrz_resident (room_number, resident_name, phone, id_card, family_members, register_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resident.getRoomNumber());
            pstmt.setString(2, resident.getResidentName());
            pstmt.setString(3, resident.getPhone());
            pstmt.setString(4, resident.getIdCard());
            pstmt.setInt(5, resident.getFamilyMembers());
            pstmt.setDate(6, new java.sql.Date(resident.getRegisterDate().getTime()));
            pstmt.setString(7, resident.getStatus());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新居民信息
     */
    public boolean updateResident(ZrzResident resident) {
        String sql = "UPDATE zrz_resident SET room_number=?, resident_name=?, phone=?, id_card=?, family_members=?, register_date=?, status=? WHERE resident_id=?";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resident.getRoomNumber());
            pstmt.setString(2, resident.getResidentName());
            pstmt.setString(3, resident.getPhone());
            pstmt.setString(4, resident.getIdCard());
            pstmt.setInt(5, resident.getFamilyMembers());
            pstmt.setDate(6, new java.sql.Date(resident.getRegisterDate().getTime()));
            pstmt.setString(7, resident.getStatus());
            pstmt.setInt(8, resident.getResidentId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除居民信息
     */
    public boolean deleteResident(int residentId) {
        String sql = "DELETE FROM zrz_resident WHERE resident_id=?";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, residentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询所有居民
     */
    public List<ZrzResident> findAllResidents() {
        List<ZrzResident> list = new ArrayList<>();
        String sql = "SELECT * FROM zrz_resident ORDER BY resident_id DESC";
        try (Connection conn = ZrzDBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractResident(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据房号查询
     */
    public List<ZrzResident> findByRoomNumber(String roomNumber) {
        List<ZrzResident> list = new ArrayList<>();
        String sql = "SELECT * FROM zrz_resident WHERE room_number LIKE ?";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + roomNumber + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractResident(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据姓名查询
     */
    public List<ZrzResident> findByName(String name) {
        List<ZrzResident> list = new ArrayList<>();
        String sql = "SELECT * FROM zrz_resident WHERE resident_name LIKE ?";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractResident(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据ID查询
     */
    public ZrzResident findById(int residentId) {
        String sql = "SELECT * FROM zrz_resident WHERE resident_id=?";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, residentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractResident(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从ResultSet提取居民对象
     */
    private ZrzResident extractResident(ResultSet rs) throws SQLException {
        ZrzResident resident = new ZrzResident();
        resident.setResidentId(rs.getInt("resident_id"));
        resident.setRoomNumber(rs.getString("room_number"));
        resident.setResidentName(rs.getString("resident_name"));
        resident.setPhone(rs.getString("phone"));
        resident.setIdCard(rs.getString("id_card"));
        resident.setFamilyMembers(rs.getInt("family_members"));
        resident.setRegisterDate(rs.getDate("register_date"));
        resident.setStatus(rs.getString("status"));
        resident.setCreatedAt(rs.getTimestamp("created_at"));
        resident.setUpdatedAt(rs.getTimestamp("updated_at"));
        return resident;
    }
}
