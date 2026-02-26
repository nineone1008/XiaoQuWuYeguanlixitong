package com.zrz.property.dao;

import com.zrz.property.model.ZrzElectricCard;
import com.zrz.property.model.ZrzWaterCard;
import com.zrz.property.util.ZrzDBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 电卡水卡数据访问层
 */
public class ZrzCardDao {

    /**
     * 添加电卡购买记录
     */
    public boolean addElectricCard(ZrzElectricCard card) {
        String sql = "INSERT INTO zrz_electric_card (resident_id, purchase_amount, purchase_date, kwh_amount, operator) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, card.getResidentId());
            pstmt.setBigDecimal(2, card.getPurchaseAmount());
            pstmt.setTimestamp(3, new Timestamp(card.getPurchaseDate().getTime()));
            pstmt.setBigDecimal(4, card.getKwhAmount());
            pstmt.setString(5, card.getOperator());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加水卡购买记录
     */
    public boolean addWaterCard(ZrzWaterCard card) {
        String sql = "INSERT INTO zrz_water_card (resident_id, purchase_amount, purchase_date, cubic_amount, operator) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, card.getResidentId());
            pstmt.setBigDecimal(2, card.getPurchaseAmount());
            pstmt.setTimestamp(3, new Timestamp(card.getPurchaseDate().getTime()));
            pstmt.setBigDecimal(4, card.getCubicAmount());
            pstmt.setString(5, card.getOperator());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询所有电卡购买记录
     */
    public List<ZrzElectricCard> findAllElectricCards() {
        List<ZrzElectricCard> list = new ArrayList<>();
        String sql = "SELECT ec.*, r.room_number, r.resident_name FROM zrz_electric_card ec " +
                     "LEFT JOIN zrz_resident r ON ec.resident_id = r.resident_id " +
                     "ORDER BY ec.purchase_date DESC";
        try (Connection conn = ZrzDBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractElectricCard(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询所有水卡购买记录
     */
    public List<ZrzWaterCard> findAllWaterCards() {
        List<ZrzWaterCard> list = new ArrayList<>();
        String sql = "SELECT wc.*, r.room_number, r.resident_name FROM zrz_water_card wc " +
                     "LEFT JOIN zrz_resident r ON wc.resident_id = r.resident_id " +
                     "ORDER BY wc.purchase_date DESC";
        try (Connection conn = ZrzDBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractWaterCard(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 从ResultSet提取电卡对象
     */
    private ZrzElectricCard extractElectricCard(ResultSet rs) throws SQLException {
        ZrzElectricCard card = new ZrzElectricCard();
        card.setElectricId(rs.getInt("electric_id"));
        card.setResidentId(rs.getInt("resident_id"));
        card.setPurchaseAmount(rs.getBigDecimal("purchase_amount"));
        card.setPurchaseDate(rs.getTimestamp("purchase_date"));
        card.setKwhAmount(rs.getBigDecimal("kwh_amount"));
        card.setOperator(rs.getString("operator"));
        card.setRoomNumber(rs.getString("room_number"));
        card.setResidentName(rs.getString("resident_name"));
        return card;
    }

    /**
     * 从ResultSet提取水卡对象
     */
    private ZrzWaterCard extractWaterCard(ResultSet rs) throws SQLException {
        ZrzWaterCard card = new ZrzWaterCard();
        card.setWaterId(rs.getInt("water_id"));
        card.setResidentId(rs.getInt("resident_id"));
        card.setPurchaseAmount(rs.getBigDecimal("purchase_amount"));
        card.setPurchaseDate(rs.getTimestamp("purchase_date"));
        card.setCubicAmount(rs.getBigDecimal("cubic_amount"));
        card.setOperator(rs.getString("operator"));
        card.setRoomNumber(rs.getString("room_number"));
        card.setResidentName(rs.getString("resident_name"));
        return card;
    }
}
