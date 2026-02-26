package com.zrz.property.dao;

import com.zrz.property.model.ZrzPropertyFee;
import com.zrz.property.util.ZrzDBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 物业费数据访问层
 */
public class ZrzPropertyFeeDao {

    /**
     * 添加物业费记录
     */
    public boolean addPropertyFee(ZrzPropertyFee fee) {
        String sql = "INSERT INTO zrz_property_fee (resident_id, fee_year, fee_month, amount, paid_amount, payment_status, due_date, payment_date, overdue_days) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, fee.getResidentId());
            pstmt.setInt(2, fee.getFeeYear());
            pstmt.setInt(3, fee.getFeeMonth());
            pstmt.setBigDecimal(4, fee.getAmount());
            pstmt.setBigDecimal(5, fee.getPaidAmount());
            pstmt.setString(6, fee.getPaymentStatus());
            pstmt.setDate(7, new java.sql.Date(fee.getDueDate().getTime()));
            pstmt.setDate(8, fee.getPaymentDate() != null ? new java.sql.Date(fee.getPaymentDate().getTime()) : null);
            pstmt.setInt(9, fee.getOverdueDays());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 缴纳物业费
     */
    public boolean payPropertyFee(int feeId, java.math.BigDecimal amount) {
        String sql = "UPDATE zrz_property_fee SET paid_amount=?, payment_status='已缴', payment_date=CURDATE(), overdue_days=0 WHERE fee_id=?";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, feeId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询所有物业费记录（带居民信息）
     */
    public List<ZrzPropertyFee> findAllPropertyFees() {
        List<ZrzPropertyFee> list = new ArrayList<>();
        String sql = "SELECT pf.*, r.room_number, r.resident_name FROM zrz_property_fee pf " +
                     "LEFT JOIN zrz_resident r ON pf.resident_id = r.resident_id " +
                     "ORDER BY pf.fee_year DESC, pf.fee_month DESC";
        try (Connection conn = ZrzDBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractPropertyFee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询逾期未缴费记录
     */
    public List<ZrzPropertyFee> findOverdueFees() {
        List<ZrzPropertyFee> list = new ArrayList<>();
        String sql = "SELECT pf.*, r.room_number, r.resident_name FROM zrz_property_fee pf " +
                     "LEFT JOIN zrz_resident r ON pf.resident_id = r.resident_id " +
                     "WHERE pf.payment_status='未缴' AND pf.overdue_days > 0 " +
                     "ORDER BY pf.overdue_days DESC";
        try (Connection conn = ZrzDBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractPropertyFee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据居民ID查询物业费
     */
    public List<ZrzPropertyFee> findByResidentId(int residentId) {
        List<ZrzPropertyFee> list = new ArrayList<>();
        String sql = "SELECT pf.*, r.room_number, r.resident_name FROM zrz_property_fee pf " +
                     "LEFT JOIN zrz_resident r ON pf.resident_id = r.resident_id " +
                     "WHERE pf.resident_id=? ORDER BY pf.fee_year DESC, pf.fee_month DESC";
        try (Connection conn = ZrzDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, residentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractPropertyFee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 检查居民是否有未缴物业费
     */
    public boolean hasUnpaidFees(int residentId) {
        String sql = "SELECT COUNT(*) FROM zrz_property_fee WHERE resident_id=? AND payment_status='未缴'";
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
     * 从ResultSet提取物业费对象
     */
    private ZrzPropertyFee extractPropertyFee(ResultSet rs) throws SQLException {
        ZrzPropertyFee fee = new ZrzPropertyFee();
        fee.setFeeId(rs.getInt("fee_id"));
        fee.setResidentId(rs.getInt("resident_id"));
        fee.setFeeYear(rs.getInt("fee_year"));
        fee.setFeeMonth(rs.getInt("fee_month"));
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
