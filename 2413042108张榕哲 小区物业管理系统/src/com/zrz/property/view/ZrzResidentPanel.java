package com.zrz.property.view;

import com.zrz.property.model.ZrzResident;
import com.zrz.property.service.ZrzResidentService;
import com.zrz.property.util.ZrzButtonUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 居民管理面板
 */
public class ZrzResidentPanel extends JPanel {
    private ZrzResidentService residentService = new ZrzResidentService();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtRoomNumber, txtName, txtPhone, txtIdCard, txtFamilyMembers;
    private JComboBox<String> cmbStatus;
    private JTextField txtSearchRoom, txtSearchName;

    //设置整体布局为BorderLayout 添加边距，美化界面 设置红色背景（#DC2626） 初始化组件并加载数据
    public ZrzResidentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(220, 38, 38));

        initComponents();
        loadData();
    }

    private void initComponents() {
        // 顶部搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(185, 28, 28));
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2), 
            "查询条件", 
            0, 0, 
            new Font("微软雅黑", Font.BOLD, 13), 
            Color.WHITE));
        
        JLabel lblRoom = new JLabel("房号:");
        lblRoom.setForeground(Color.WHITE);
        lblRoom.setFont(new Font("微软雅黑", Font.BOLD, 13));
        searchPanel.add(lblRoom);
        txtSearchRoom = new JTextField(10);
        txtSearchRoom.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        searchPanel.add(txtSearchRoom);
        
        JLabel lblName = new JLabel("姓名:");
        lblName.setForeground(Color.WHITE);
        lblName.setFont(new Font("微软雅黑", Font.BOLD, 13));
        searchPanel.add(lblName);
        txtSearchName = new JTextField(10);
        txtSearchName.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        searchPanel.add(txtSearchName);
        
        JButton btnSearch = new JButton("查询");
        styleButton(btnSearch);
        btnSearch.addActionListener(e -> searchResident());
        searchPanel.add(btnSearch);
        
        JButton btnRefresh = new JButton("刷新");
        styleButton(btnRefresh);
        btnRefresh.addActionListener(e -> loadData());
        searchPanel.add(btnRefresh);

        add(searchPanel, BorderLayout.NORTH);

        // 中间表格
        String[] columns = {"ID", "房号", "姓名", "电话", "身份证", "家庭人数", "入住日期", "状态"};
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

        // 右侧操作面板
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(185, 28, 28));
        
        // 表单面板
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 10));
        formPanel.setBackground(new Color(185, 28, 28));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2), 
            "居民信息", 
            0, 0, 
            new Font("微软雅黑", Font.BOLD, 13), 
            Color.WHITE));
        
        formPanel.add(createLabel("房号:"));
        txtRoomNumber = new JTextField();
        formPanel.add(txtRoomNumber);
        
        formPanel.add(createLabel("姓名:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        
        formPanel.add(createLabel("电话:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);
        
        formPanel.add(createLabel("身份证:"));
        txtIdCard = new JTextField();
        formPanel.add(txtIdCard);
        
        formPanel.add(createLabel("家庭人数:"));
        txtFamilyMembers = new JTextField();
        formPanel.add(txtFamilyMembers);
        
        formPanel.add(createLabel("状态:"));
        cmbStatus = new JComboBox<>(new String[]{"正常", "欠费", "已搬离"});
        cmbStatus.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        formPanel.add(cmbStatus);
        
        rightPanel.add(formPanel, BorderLayout.NORTH);
        
        // 按钮面板
        JPanel btnPanel = new JPanel(new GridLayout(4, 1, 5, 10));
        btnPanel.setBackground(new Color(185, 28, 28));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnAdd = new JButton("添加");
        styleButton(btnAdd);
        btnAdd.addActionListener(e -> addResident());
        btnPanel.add(btnAdd);
        
        JButton btnUpdate = new JButton("修改");
        styleButton(btnUpdate);
        btnUpdate.addActionListener(e -> updateResident());
        btnPanel.add(btnUpdate);
        
        JButton btnDelete = new JButton("删除");
        styleButton(btnDelete);
        btnDelete.addActionListener(e -> deleteResident());
        btnPanel.add(btnDelete);
        
        JButton btnClear = new JButton("清空");
        styleButton(btnClear);
        btnClear.addActionListener(e -> clearForm());
        btnPanel.add(btnClear);
        
        rightPanel.add(btnPanel, BorderLayout.CENTER);
        
        add(rightPanel, BorderLayout.EAST);

        // 表格选择监听
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillForm();
            }
        });
    }

    //清空表格现有数据 调用服务层获取所有居民 使用SimpleDateFormat格式化日期 处理null值，避免空指针异常 添加行到表格模型
    private void loadData() {
        tableModel.setRowCount(0);
        List<ZrzResident> list = residentService.getAllResidents();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (ZrzResident r : list) {
            tableModel.addRow(new Object[]{
                r.getResidentId(),
                r.getRoomNumber(),
                r.getResidentName(),
                r.getPhone(),
                r.getIdCard(),
                r.getFamilyMembers(),
                r.getRegisterDate() != null ? sdf.format(r.getRegisterDate()) : "",
                r.getStatus()
            });
        }
    }

    //清空表格 获取搜索条件 优先按房号搜索，其次按姓名 条件都为空时重新加载全部数据
    private void searchResident() {
        tableModel.setRowCount(0);
        String room = txtSearchRoom.getText().trim();
        String name = txtSearchName.getText().trim();
        
        List<ZrzResident> list;
        if (!room.isEmpty()) {
            list = residentService.searchByRoomNumber(room);
        } else if (!name.isEmpty()) {
            list = residentService.searchByName(name);
        } else {
            loadData();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (ZrzResident r : list) {
            tableModel.addRow(new Object[]{
                r.getResidentId(),
                r.getRoomNumber(),
                r.getResidentName(),
                r.getPhone(),
                r.getIdCard(),
                r.getFamilyMembers(),
                r.getRegisterDate() != null ? sdf.format(r.getRegisterDate()) : "",
                r.getStatus()
            });
        }
    }

    //先验证表单 从表单构建居民对象 设置当前日期为登记日期 调用服务层添加居民 显示操作结果提示 成功则刷新表格和清空表单
    private void addResident() {
        if (!validateForm()) return;
        
        ZrzResident resident = new ZrzResident();
        resident.setRoomNumber(txtRoomNumber.getText().trim());
        resident.setResidentName(txtName.getText().trim());
        resident.setPhone(txtPhone.getText().trim());
        resident.setIdCard(txtIdCard.getText().trim());
        resident.setFamilyMembers(Integer.parseInt(txtFamilyMembers.getText().trim()));
        resident.setRegisterDate(new Date());
        resident.setStatus((String) cmbStatus.getSelectedItem());
        
        if (residentService.addResident(resident)) {
            JOptionPane.showMessageDialog(this, "添加成功！");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "添加失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    //检查是否有选中行 从表格获取选中行的ID 构建更新对象 调用服务层更新 提供用户反馈
    private void updateResident() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的记录！");
            return;
        }
        if (!validateForm()) return;
        
        ZrzResident resident = new ZrzResident();
        resident.setResidentId((Integer) tableModel.getValueAt(row, 0));
        resident.setRoomNumber(txtRoomNumber.getText().trim());
        resident.setResidentName(txtName.getText().trim());
        resident.setPhone(txtPhone.getText().trim());
        resident.setIdCard(txtIdCard.getText().trim());
        resident.setFamilyMembers(Integer.parseInt(txtFamilyMembers.getText().trim()));
        resident.setRegisterDate(new Date());
        resident.setStatus((String) cmbStatus.getSelectedItem());
        
        if (residentService.updateResident(resident)) {
            JOptionPane.showMessageDialog(this, "修改成功！");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "修改失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    //删除前的二次确认
    //防止误操作
    //从表格获取要删除的ID
    //调用服务层删除
    private void deleteResident() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的记录！");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该居民信息吗？", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (Integer) tableModel.getValueAt(row, 0);
            if (residentService.deleteResident(id)) {
                JOptionPane.showMessageDialog(this, "删除成功！");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //表格行选中时自动填充表单
    //便于修改操作
    private void fillForm() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtRoomNumber.setText(tableModel.getValueAt(row, 1).toString());
            txtName.setText(tableModel.getValueAt(row, 2).toString());
            txtPhone.setText(tableModel.getValueAt(row, 3).toString());
            txtIdCard.setText(tableModel.getValueAt(row, 4).toString());
            txtFamilyMembers.setText(tableModel.getValueAt(row, 5).toString());
            cmbStatus.setSelectedItem(tableModel.getValueAt(row, 7).toString());
        }
    }

    private void clearForm() {
        txtRoomNumber.setText("");
        txtName.setText("");
        txtPhone.setText("");
        txtIdCard.setText("");
        txtFamilyMembers.setText("");
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
    }

    //基本的表单验证 检查必填字段 验证数字格式
    private boolean validateForm() {
        if (txtRoomNumber.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "房号不能为空！");
            return false;
        }
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "姓名不能为空！");
            return false;
        }
        try {
            Integer.parseInt(txtFamilyMembers.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "家庭人数必须是数字！");
            return false;
        }
        return true;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("微软雅黑", Font.BOLD, 13));
        return label;
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
