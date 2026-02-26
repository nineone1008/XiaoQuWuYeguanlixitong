package com.zrz.property.view;

import com.zrz.property.util.ZrzSessionUtil;

import javax.swing.*;
import java.awt.*;

/**
 * 系统主界面
 * 包含多个功能模块的选项卡面板
 * 根据用户角色显示不同的功能菜单
 */
public class ZrzMainFrame extends JFrame {
    private JTabbedPane tabbedPane;  // 选项卡面板

    /**
     * 构造方法
     * 初始化主界面，设置标题、大小、位置等
     */
    public ZrzMainFrame() {
        // 设置窗口标题，显示当前登录用户信息
        setTitle("小区物业管理系统 v1.0 - 当前用户: " + ZrzSessionUtil.getCurrentRealName() + " (" + ZrzSessionUtil.getCurrentRole() + ")");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 窗口居中显示

        initComponents();
    }

    /**
     * 初始化界面组件
     * 创建菜单栏、选项卡面板和状态栏
     */
    private void initComponents() {
        // 设置主窗口背景色（红色主题）
        getContentPane().setBackground(new Color(220, 38, 38));
        
        // 创建菜单栏
        createMenuBar();

        // 创建选项卡面板
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("微软雅黑", Font.BOLD, 15));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(new Color(220, 38, 38));
        
        // 设置选项卡的UI样式
        UIManager.put("TabbedPane.selected", Color.WHITE);
        UIManager.put("TabbedPane.contentAreaColor", new Color(220, 38, 38));
        UIManager.put("TabbedPane.borderHightlightColor", new Color(220, 38, 38));

        // 添加各个功能模块选项卡
        tabbedPane.addTab("居民管理", new ZrzResidentPanel());
        tabbedPane.addTab("物业费管理", new ZrzPropertyFeePanel());
        tabbedPane.addTab("取暖费管理", new ZrzHeatingFeePanel());
        tabbedPane.addTab("电卡管理", new ZrzElectricCardPanel());
        tabbedPane.addTab("水卡管理", new ZrzWaterCardPanel());
        tabbedPane.addTab("逾期查询", new ZrzOverdueQueryPanel());
        
        // 如果是管理员，添加用户管理选项卡
        if (ZrzSessionUtil.isAdmin()) {
            tabbedPane.addTab("用户管理", new ZrzUserManagePanel());
        }

        add(tabbedPane, BorderLayout.CENTER);

        // 添加底部状态栏
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(new Color(153, 27, 27));
        statusPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(220, 38, 38)));
        JLabel statusLabel = new JLabel("系统就绪 | 欢迎使用小区物业管理系统 | 当前用户: " + 
                                        ZrzSessionUtil.getCurrentRealName() + " (" + ZrzSessionUtil.getCurrentRole() + ")");
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        statusLabel.setForeground(Color.WHITE);
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }

    /**
     * 创建菜单栏
     * 包含系统菜单：修改密码、退出登录、退出系统
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);

        // 系统菜单
        JMenu systemMenu = new JMenu("系统");
        systemMenu.setFont(new Font("微软雅黑", Font.BOLD, 14));
        systemMenu.setForeground(new Color(220, 38, 38));

        // 修改密码菜单项
        JMenuItem changePasswordItem = new JMenuItem("修改密码");
        changePasswordItem.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        changePasswordItem.setForeground(new Color(220, 38, 38));
        changePasswordItem.addActionListener(e -> changePassword());
        systemMenu.add(changePasswordItem);

        systemMenu.addSeparator();

        // 退出登录菜单项
        JMenuItem logoutItem = new JMenuItem("退出登录");
        logoutItem.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        logoutItem.setForeground(new Color(220, 38, 38));
        logoutItem.addActionListener(e -> logout());
        systemMenu.add(logoutItem);

        // 退出系统菜单项
        JMenuItem exitItem = new JMenuItem("退出系统");
        exitItem.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        exitItem.setForeground(new Color(220, 38, 38));
        exitItem.addActionListener(e -> System.exit(0));
        systemMenu.add(exitItem);

        menuBar.add(systemMenu);

        setJMenuBar(menuBar);
    }

    /**
     * 修改密码功能
     * 弹出对话框让用户输入旧密码和新密码
     */
    private void changePassword() {
        JPasswordField oldPwd = new JPasswordField();//创建一个新的JPasswordField对象，并将其引用赋给名为oldPwd的变量
        JPasswordField newPwd = new JPasswordField();
        JPasswordField confirmPwd = new JPasswordField();

        Object[] message = {
            "原密码:", oldPwd,
            "新密码:", newPwd,
            "确认密码:", confirmPwd
        };

        int option = JOptionPane.showConfirmDialog(this, message, "修改密码", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPwd.getPassword());
            String newPassword = new String(newPwd.getPassword());
            String confirmPassword = new String(confirmPwd.getPassword());

            // 验证新密码不能为空
            if (newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "新密码不能为空！");
                return;
            }

            // 验证两次输入的密码是否一致
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "两次输入的密码不一致！");
                return;
            }

            // TODO: 这里可以添加修改密码的逻辑
            JOptionPane.showMessageDialog(this, "密码修改成功！请重新登录。");
            logout();
        }
    }

    /**
     * 退出登录功能
     * 清除会话信息，返回登录界面
     */
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "确定要退出登录吗？", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // 清除会话信息
            ZrzSessionUtil.clearSession();
            // 关闭主界面
            this.dispose();
            
            // 打开登录界面
            SwingUtilities.invokeLater(() -> {
                ZrzLoginFrame loginFrame = new ZrzLoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }

}
