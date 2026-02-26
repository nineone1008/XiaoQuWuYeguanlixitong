package com.zrz.property.view;

import com.zrz.property.model.ZrzPropertyFee;
import com.zrz.property.model.ZrzHeatingFee;
import com.zrz.property.service.ZrzFeeService;
import com.zrz.property.util.ZrzButtonUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 逾期查询面板
 */
public class ZrzOverdueQueryPanel extends JPanel {
    private ZrzFeeService feeService = new ZrzFeeService();
    private JTable propertyTable, heatingTable;
    private DefaultTableModel propertyTableModel, heatingTableModel;

    public ZrzOverdueQueryPanel() {
        setLayout(new GridLayout(2, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(220, 38, 38));

        initComponents();
        loadData();
    }

    private void initComponents() {
        // 物业费逾期面板
        JPanel propertyPanel = new JPanel(new BorderLayout());
        propertyPanel.setBackground(new Color(220, 38, 38));
        propertyPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2), 
            "物业费逾期记录", 
            0, 0, 
            new Font("微软雅黑", Font.BOLD, 13), 
            Color.WHITE));
        
        JPanel propertyTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        propertyTopPanel.setBackground(new Color(185, 28, 28));
        JButton btnRefreshProperty = new JButton("刷新");
        styleButton(btnRefreshProperty);
        btnRefreshProperty.addActionListener(e -> loadPropertyOverdue());
        propertyTopPanel.add(btnRefreshProperty);
        propertyPanel.add(propertyTopPanel, BorderLayout.NORTH);
        
        String[] propertyColumns = {"费用ID", "房号", "姓名", "年份", "月份", "应缴金额", "缴费状态", "应缴日期", "逾期天数"};
        propertyTableModel = new DefaultTableModel(propertyColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        propertyTable = new JTable(propertyTableModel);
        propertyTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        propertyTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        propertyTable.setRowHeight(25);
        propertyPanel.add(new JScrollPane(propertyTable), BorderLayout.CENTER);
        
        add(propertyPanel);

        // 取暖费逾期面板
        JPanel heatingPanel = new JPanel(new BorderLayout());
        heatingPanel.setBackground(new Color(220, 38, 38));
        heatingPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2), 
            "取暖费逾期记录", 
            0, 0, 
            new Font("微软雅黑", Font.BOLD, 13), 
            Color.WHITE));
        
        JPanel heatingTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        heatingTopPanel.setBackground(new Color(185, 28, 28));
        JButton btnRefreshHeating = new JButton("刷新");
        styleButton(btnRefreshHeating);
        btnRefreshHeating.addActionListener(e -> loadHeatingOverdue());
        heatingTopPanel.add(btnRefreshHeating);
        heatingPanel.add(heatingTopPanel, BorderLayout.NORTH);
        
        String[] heatingColumns = {"取暖费ID", "房号", "姓名", "年份", "应缴金额", "缴费状态", "应缴日期", "逾期天数"};
        heatingTableModel = new DefaultTableModel(heatingColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        heatingTable = new JTable(heatingTableModel);
        heatingTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        heatingTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        heatingTable.setRowHeight(25);
        heatingPanel.add(new JScrollPane(heatingTable), BorderLayout.CENTER);
        
        add(heatingPanel);
    }

    private void loadData() {
        loadPropertyOverdue();
        loadHeatingOverdue();
    }

    private void loadPropertyOverdue() {
        propertyTableModel.setRowCount(0);
        List<ZrzPropertyFee> list = feeService.getOverduePropertyFees();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (ZrzPropertyFee fee : list) {
            propertyTableModel.addRow(new Object[]{
                fee.getFeeId(),
                fee.getRoomNumber(),
                fee.getResidentName(),
                fee.getFeeYear(),
                fee.getFeeMonth(),
                fee.getAmount(),
                fee.getPaymentStatus(),
                fee.getDueDate() != null ? sdf.format(fee.getDueDate()) : "",
                fee.getOverdueDays()
            });
        }
    }

    private void loadHeatingOverdue() {
        heatingTableModel.setRowCount(0);
        List<ZrzHeatingFee> list = feeService.getOverdueHeatingFees();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //遍历取暖费列表
        for (ZrzHeatingFee fee : list) {
            heatingTableModel.addRow(new Object[]{
                fee.getHeatingId(),
                fee.getRoomNumber(),
                fee.getResidentName(),
                fee.getFeeYear(),
                fee.getAmount(),
                fee.getPaymentStatus(),
                fee.getDueDate() != null ? sdf.format(fee.getDueDate()) : "",
                fee.getOverdueDays()
            });
        }
    }
    
    private void styleButton(JButton btn) {
        btn.setUI(new ZrzButtonUI());
        btn.setFont(new Font("微软雅黑", Font.BOLD, 15));
        btn.setBackground(new Color(0, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(100, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
    }
}
