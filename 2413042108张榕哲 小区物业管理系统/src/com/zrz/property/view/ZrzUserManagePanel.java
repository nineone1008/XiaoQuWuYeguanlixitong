package com.zrz.property.view;

import com.zrz.property.model.ZrzUser;
import com.zrz.property.service.ZrzUserService;
import com.zrz.property.util.ZrzSessionUtil;
import com.zrz.property.util.ZrzButtonUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 用户管理面板（仅管理员可见）
 */
public class ZrzUserManagePanel extends JPanel {
    private ZrzUserService userService = new ZrzUserService();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtUsername, txtRealName, txtPhone;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole, cmbStatus;

    public ZrzUserManagePanel() {
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

        add(topPanel, BorderLayout.NORTH);

        // 中间表格
        String[] columns = {"用户ID", "用户名", "真实姓名", "角色", "电话", "状态", "最后登录"};
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
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 10));
        formPanel.setBackground(new Color(185, 28, 28));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2), 
            "用户信息", 
            0, 0, 
            new Font("微软雅黑", Font.BOLD, 13), 
            Color.WHITE));
        
        formPanel.add(createLabel("用户名:"));
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        formPanel.add(txtUsername);
        
        formPanel.add(createLabel("密码:"));
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        formPanel.add(txtPassword);
        
        formPanel.add(createLabel("真实姓名:"));
        txtRealName = new JTextField();
        txtRealName.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        formPanel.add(txtRealName);
        
        formPanel.add(createLabel("角色:"));
        cmbRole = new JComboBox<>(new String[]{"管理员", "普通用户"});
        cmbRole.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        formPanel.add(cmbRole);
        
        formPanel.add(createLabel("电话:"));
        txtPhone = new JTextField();
        txtPhone.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        formPanel.add(txtPhone);
        
        formPanel.add(createLabel("状态:"));
        cmbStatus = new JComboBox<>(new String[]{"启用", "禁用"});
        cmbStatus.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        formPanel.add(cmbStatus);
        
        rightPanel.add(formPanel, BorderLayout.NORTH);
        
        // 按钮面板
        JPanel btnPanel = new JPanel(new GridLayout(4, 1, 5, 10));
        btnPanel.setBackground(new Color(185, 28, 28));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnAdd = new JButton("添加");
        styleButton(btnAdd);
        btnAdd.addActionListener(e -> addUser());
        btnPanel.add(btnAdd);
        
        JButton btnUpdate = new JButton("修改");
        styleButton(btnUpdate);
        btnUpdate.addActionListener(e -> updateUser());
        btnPanel.add(btnUpdate);
        
        JButton btnDelete = new JButton("删除");
        styleButton(btnDelete);
        btnDelete.addActionListener(e -> deleteUser());
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

    private void loadData() {
        tableModel.setRowCount(0);
        List<ZrzUser> list = userService.getAllUsers();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //循环遍历用户列表，逐行添加到表格
        //处理lastLogin字段的null值，显示"从未登录"
        for (ZrzUser user : list) {
            tableModel.addRow(new Object[]{
                user.getUserId(),
                user.getUsername(),
                user.getRealName(),
                user.getRole(),
                user.getPhone(),
                user.getStatus(),
                user.getLastLogin() != null ? sdf.format(user.getLastLogin()) : "从未登录"
            });
        }
    }

    private void addUser() {
        if (!validateForm()) return;
        //从界面组件获取数据
        ZrzUser user = new ZrzUser();
        user.setUsername(txtUsername.getText().trim());
        user.setPassword(new String(txtPassword.getPassword()).trim());
        user.setRealName(txtRealName.getText().trim());
        user.setRole((String) cmbRole.getSelectedItem());
        user.setPhone(txtPhone.getText().trim());
        user.setStatus((String) cmbStatus.getSelectedItem());
        
        if (userService.addUser(user)) {
            JOptionPane.showMessageDialog(this, "添加成功！");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "添加失败！用户名可能已存在。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的记录！");
            return;
        }
        if (!validateForm()) return;
        
        ZrzUser user = new ZrzUser();
        user.setUserId((Integer) tableModel.getValueAt(row, 0));
        user.setUsername(txtUsername.getText().trim());
        user.setPassword(new String(txtPassword.getPassword()).trim());
        user.setRealName(txtRealName.getText().trim());
        user.setRole((String) cmbRole.getSelectedItem());
        user.setPhone(txtPhone.getText().trim());
        user.setStatus((String) cmbStatus.getSelectedItem());
        
        if (userService.updateUser(user)) {
            JOptionPane.showMessageDialog(this, "修改成功！");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "修改失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的记录！");
            return;
        }
        
        int userId = (Integer) tableModel.getValueAt(row, 0);
        String username = tableModel.getValueAt(row, 1).toString();
        
        // 不能删除当前登录用户
        if (username.equals(ZrzSessionUtil.getCurrentUsername())) {
            JOptionPane.showMessageDialog(this, "不能删除当前登录用户！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该用户吗？", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (userService.deleteUser(userId)) {
                JOptionPane.showMessageDialog(this, "删除成功！");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //从表格选择行填充到表单 从表格获取选中行 将各列数据设置到对应表单控件
    //密码安全：不显示原密码，清空密码框 下拉框通过setSelectedItem设置选中项
    private void fillForm() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtUsername.setText(tableModel.getValueAt(row, 1).toString());
            txtPassword.setText(""); // 不显示密码
            txtRealName.setText(tableModel.getValueAt(row, 2).toString());
            cmbRole.setSelectedItem(tableModel.getValueAt(row, 3).toString());
            txtPhone.setText(tableModel.getValueAt(row, 4).toString());
            cmbStatus.setSelectedItem(tableModel.getValueAt(row, 5).toString());
        }
    }

    //清空所有文本字段 设置下拉框默认选项 清除表格选择状态 便于开始新的操作
    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtRealName.setText("");
        txtPhone.setText("");
        cmbRole.setSelectedIndex(1);
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
    }

    private boolean validateForm() {
        if (txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名不能为空！");
            return false;
        }
        if (new String(txtPassword.getPassword()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "密码不能为空！");
            return false;
        }
        return true;
    }
    //创建统一样式的标签
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
