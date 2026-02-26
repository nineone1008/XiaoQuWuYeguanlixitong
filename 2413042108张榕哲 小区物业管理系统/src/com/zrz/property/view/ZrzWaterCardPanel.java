package com.zrz.property.view;

import com.zrz.property.model.ZrzWaterCard;
import com.zrz.property.model.ZrzResident;
import com.zrz.property.service.ZrzCardService;
import com.zrz.property.service.ZrzResidentService;
import com.zrz.property.util.ZrzButtonUI;
import com.zrz.property.util.ZrzDialogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 水卡管理面板
 */
public class ZrzWaterCardPanel extends JPanel {
    private ZrzCardService cardService = new ZrzCardService();
    private ZrzResidentService residentService = new ZrzResidentService();
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbResident;
    private JTextField txtAmount, txtCubic;

    public ZrzWaterCardPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(220, 38, 38));

        initComponents();
        loadData();
    }

    private void initComponents() {
        // 顶部操作面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(185, 28, 28));
        topPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2), 
            "水卡购买", 
            0, 0, 
            new Font("微软雅黑", Font.BOLD, 13), 
            Color.WHITE));
        
        JLabel lblResident = new JLabel("选择居民:");
        lblResident.setForeground(Color.WHITE);
        lblResident.setFont(new Font("微软雅黑", Font.BOLD, 13));
        topPanel.add(lblResident);
        cmbResident = new JComboBox<>();
        cmbResident.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        loadResidents();
        topPanel.add(cmbResident);
        
        JLabel lblAmount = new JLabel("购买金额:");
        lblAmount.setForeground(Color.WHITE);
        lblAmount.setFont(new Font("微软雅黑", Font.BOLD, 13));
        topPanel.add(lblAmount);
        txtAmount = new JTextField(10);
        txtAmount.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        topPanel.add(txtAmount);
        
        JLabel lblCubic = new JLabel("水量(m³):");
        lblCubic.setForeground(Color.WHITE);
        lblCubic.setFont(new Font("微软雅黑", Font.BOLD, 13));
        topPanel.add(lblCubic);
        txtCubic = new JTextField(10);
        txtCubic.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        topPanel.add(txtCubic);
        
        JButton btnPurchase = new JButton("购买");
        styleButton(btnPurchase);
        btnPurchase.addActionListener(e -> purchaseCard());
        topPanel.add(btnPurchase);
        
        JButton btnRefresh = new JButton("刷新");
        styleButton(btnRefresh);
        btnRefresh.addActionListener(e -> loadData());
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH);

        // 中间表格
        String[] columns = {"记录ID", "房号", "姓名", "购买金额", "水量(m³)", "购买日期", "操作员"};
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

    private void loadResidents() {
        cmbResident.removeAllItems();
        List<ZrzResident> list = residentService.getAllResidents();
        for (ZrzResident r : list) {
            cmbResident.addItem(r.getResidentId() + " - " + r.getRoomNumber() + " - " + r.getResidentName());
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<ZrzWaterCard> list = cardService.getAllWaterCards();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (ZrzWaterCard card : list) {
            tableModel.addRow(new Object[]{
                card.getWaterId(),
                card.getRoomNumber(),
                card.getResidentName(),
                card.getPurchaseAmount(),
                card.getCubicAmount(),
                card.getPurchaseDate() != null ? sdf.format(card.getPurchaseDate()) : "",
                card.getOperator()
            });
        }
    }

    private void purchaseCard() {
        if (cmbResident.getSelectedItem() == null) {
            ZrzDialogUtil.showMessageDialog(this, "请选择居民！");
            return;
        }
        
        String selected = cmbResident.getSelectedItem().toString();
        int residentId = Integer.parseInt(selected.split(" - ")[0]);
        
        try {
            BigDecimal amount = new BigDecimal(txtAmount.getText().trim());
            BigDecimal cubic = new BigDecimal(txtCubic.getText().trim());
            
            ZrzWaterCard card = new ZrzWaterCard();
            card.setResidentId(residentId);
            card.setPurchaseAmount(amount);
            card.setCubicAmount(cubic);
            card.setPurchaseDate(new Date());
            card.setOperator("管理员");
            
            String result = cardService.purchaseWaterCard(card);
            if (result.contains("成功")) {
                ZrzDialogUtil.showMessageDialog(this, result, "成功", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                txtAmount.setText("");
                txtCubic.setText("");
            } else {
                ZrzDialogUtil.showMessageDialog(this, result, "提示", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            ZrzDialogUtil.showMessageDialog(this, "金额或水量格式不正确！", "错误", JOptionPane.ERROR_MESSAGE);
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
