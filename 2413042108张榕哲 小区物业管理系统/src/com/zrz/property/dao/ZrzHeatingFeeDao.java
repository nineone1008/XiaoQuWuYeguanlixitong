package com.zrz.property.dao;

import com.zrz.property.model.ZrzHeatingFee;
import com.zrz.property.util.ZrzDBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 取暖费数据访问层
 */
public class ZrzHeatingFeeDao {

    /**
     * 添加取暖费记录
     */
    public boolean addHeatingFee(ZrzHeatingFee fee) {
        String sql = "INSERT INTO zrz_heating_fee (resident_id, fee_year, amount, paid_amount, payment_status, due_date, payment_date, overdue_days) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, fee.getResidentId());
            pstmt.setInt(2, fee.getFeeYear());
            pstmt.setBigDecimal(3, fee.getAmount());
            pstmt.setBigDecimal(4, fee.getPaidAmount());
            pstmt.setString(5, fee.getPaymentStatus());
            pstmt.setDate(6, new java.sql.Date(fee.getDueDate().getTime()));
            pstmt.setDate(7, fee.getPaymentDate() != null ? new java.sql.Date(fee.getPaymentDate().getTime()) : null);
            pstmt.setInt(8, fee.getOverdueDays());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 缴纳取暖费
     */
    public boolean payHeatingFee(int heatingId, java.math.BigDecimal amount) {
        String sql = "UPDATE zrz_heating_fee SET paid_amount=?, payment_status='已缴', payment_date=CURDATE(), overdue_days=0 WHERE heating_id=?";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, heatingId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询所有取暖费记录
     */
    public List<ZrzHeatingFee> findAllHeatingFees() {
        List<ZrzHeatingFee> list = new ArrayList<>();
        String sql = "SELECT hf.*, r.room_number, r.resident_name FROM zrz_heating_fee hf " +
                     "LEFT JOIN zrz_resident r ON hf.resident_id = r.resident_id " +
                     "ORDER BY hf.fee_year DESC";
        try (Connection conn = ZrzDBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractHeatingFee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询逾期未缴费记录
     */
    public List<ZrzHeatingFee> findOverdueFees() {
        List<ZrzHeatingFee> list = new ArrayList<>();
        String sql = "SELECT hf.*, r.room_number, r.resident_name FROM zrz_heating_fee hf " +
                     "LEFT JOIN zrz_resident r ON hf.resident_id = r.resident_id " +
                     "WHERE hf.payment_status='未缴' AND hf.overdue_days > 0 " +
                     "ORDER BY hf.overdue_days DESC";
        try (Connection conn = ZrzDBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractHeatingFee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 检查居民是否有未缴取暖费
     */
    public boolean hasUnpaidFees(int residentId) {
        String sql = "SELECT COUNT(*) FROM zrz_heating_fee WHERE resident_id=? AND payment_status='未缴'";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, residentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从ResultSet提取取暖费对象
     */
    private ZrzHeatingFee extractHeatingFee(ResultSet rs) throws SQLException {
        ZrzHeatingFee fee = new ZrzHeatingFee();
        fee.setHeatingId(rs.getInt("heating_id"));
        fee.setResidentId(rs.getInt("resident_id"));
        fee.setFeeYear(rs.getInt("fee_year"));
        fee.setAmount(rs.getBigDecimal("amount"));
        fee.setPaidAmount(rs.getBigDecimal("paid_amount"));
        fee.setPaymentStatus(rs.getString("payment_status"));
        fee.setDueDate(rs.getDate("due_date"));
        fee.setPaymentDate(rs.getDate("payment_date"));
        fee.setOverdueDays(rs.getInt("overdue_days"));
        fee.setCreatedAt(rs.getTimestamp("created_at"));
        fee.setRoomNumber(rs.getString("room_number"));
        fee.setResidentName(rs.getString("resident_name"));
        return fee;
    }
}
