package com.zrz.property.view;

import com.zrz.property.model.ZrzHeatingFee;
import com.zrz.property.service.ZrzFeeService;
import com.zrz.property.util.ZrzButtonUI;
import com.zrz.property.util.ZrzDialogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 取暖费管理面板
 */
public class ZrzHeatingFeePanel extends JPanel {
    private ZrzFeeService feeService = new ZrzFeeService();
    private JTable table;
    private DefaultTableModel tableModel;

    public ZrzHeatingFeePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(220, 38, 38));

        initComponents();
        loadData();
    }

    private void initComponents() {
        // 顶部按钮面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(185, 28, 28));
        topPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2));
        
        JButton btnRefresh = new JButton("刷新");
        styleButton(btnRefresh);
        btnRefresh.addActionListener(e -> loadData());
        topPanel.add(btnRefresh);
        
        JButton btnPay = new JButton("缴费");
        styleButton(btnPay);
        btnPay.addActionListener(e -> payFee());
        topPanel.add(btnPay);

        add(topPanel, BorderLayout.NORTH);

        // 中间表格
        String[] columns = {"取暖费ID", "房号", "姓名", "年份", "应缴金额", "已缴金额", "缴费状态", "应缴日期", "实缴日期", "逾期天数"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        table.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<ZrzHeatingFee> list = feeService.getAllHeatingFees();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (ZrzHeatingFee fee : list) {
            tableModel.addRow(new Object[]{
                fee.getHeatingId(),
                fee.getRoomNumber(),
                fee.getResidentName(),
                fee.getFeeYear(),
                fee.getAmount(),
                fee.getPaidAmount(),
                fee.getPaymentStatus(),
                fee.getDueDate() != null ? sdf.format(fee.getDueDate()) : "",
                fee.getPaymentDate() != null ? sdf.format(fee.getPaymentDate()) : "",
                fee.getOverdueDays()
            });
        }
    }

    private void payFee() {
        int row = table.getSelectedRow();
        if (row == -1) {
            ZrzDialogUtil.showMessageDialog(this, "请先选择要缴费的记录！");
            return;
        }
        
        String status = tableModel.getValueAt(row, 6).toString();
        if ("已缴".equals(status)) {
            ZrzDialogUtil.showMessageDialog(this, "该费用已缴纳！");
            return;
        }
        
        BigDecimal amount = (BigDecimal) tableModel.getValueAt(row, 4);
        String input = ZrzDialogUtil.showInputDialog(this, "请输入缴费金额:", "缴费", amount.toString());
        if (input != null && !input.trim().isEmpty()) {
            try {
                BigDecimal payAmount = new BigDecimal(input);
                int heatingId = (Integer) tableModel.getValueAt(row, 0);
                
                if (feeService.payHeatingFee(heatingId, payAmount)) {
                    ZrzDialogUtil.showMessageDialog(this, "缴费成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    ZrzDialogUtil.showMessageDialog(this, "缴费失败！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                ZrzDialogUtil.showMessageDialog(this, "金额格式不正确！", "错误", JOptionPane.ERROR_MESSAGE);
            }
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
