package com.zrz.property.view;

import com.zrz.property.model.ZrzUser;
import com.zrz.property.service.ZrzUserService;
import com.zrz.property.util.ZrzSessionUtil;
import com.zrz.property.util.ZrzButtonUI;
import com.zrz.property.util.ZrzDialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 用户登录界面
 * 系统的入口界面，提供用户登录功能
 * 采用红色渐变背景，现代化设计风格
 */
public class ZrzLoginFrame extends JFrame { //继承JFrame，表示这是一个窗口
    private ZrzUserService userService = new ZrzUserService();  // 创建用户服务对象
    private JTextField txtUsername;      // 用户名输入框
    private JPasswordField txtPassword;  // 密码输入框
    private JButton btnLogin, btnReset;  // 登录和重置按钮

    /**
     * 构造方法
     * 初始化登录界面
     */
    public ZrzLoginFrame() {// 构造方法，创建对象时自动调用
        setTitle("小区物业管理系统 - 用户登录");// 设置窗口标题
        setSize(500, 400);// 设置窗口大小：宽500，高400像素
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置关闭窗口时退出程序
        setLocationRelativeTo(null);  // 窗口居中
        setResizable(false);          // 禁止调整窗口大小

        initComponents();// 调用初始化组件方法
    }

    /**
     * 初始化界面组件
     * 创建登录表单、按钮等UI元素
     */
    private void initComponents() {
        // 主面板 - 红色渐变背景
        // 主面板 - 使用BorderLayout布局，水平和垂直间距10像素
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(220, 38, 38), 0, h, new Color(153, 27, 27));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // 标题面板
        JPanel titlePanel = new JPanel();// 创建面板
        titlePanel.setOpaque(false);// 设置为透明
        JLabel titleLabel = new JLabel("小区物业管理系统");// 创建标签
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 32));// 设置字体
        titleLabel.setForeground(Color.WHITE);// 设置前景色为白色
        titlePanel.add(titleLabel);// 将标签添加到面板
        mainPanel.add(titlePanel, BorderLayout.NORTH);// 将面板添加到主面板的北部

        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 用户名
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblUsername = new JLabel("用户名:");
        lblUsername.setFont(new Font("微软雅黑", Font.BOLD, 16));
        lblUsername.setForeground(Color.WHITE);
        formPanel.add(lblUsername, gbc);// 将标签添加到表单面板

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        txtUsername.setPreferredSize(new Dimension(220, 40));// 设置首选大小
        txtUsername.setBorder(BorderFactory.createCompoundBorder(// 设置边框
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        txtUsername.setBackground(new Color(255, 255, 255, 230));
        formPanel.add(txtUsername, gbc);// 将文本框添加到表单面板

        // 密码
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel lblPassword = new JLabel("密码:");
        lblPassword.setFont(new Font("微软雅黑", Font.BOLD, 16));
        lblPassword.setForeground(Color.WHITE);
        formPanel.add(lblPassword, gbc);// 将密码标签添加到表单面板，使用gbc布局约束

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        txtPassword.setPreferredSize(new Dimension(220, 40));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        txtPassword.setBackground(new Color(255, 255, 255, 230));
        formPanel.add(txtPassword, gbc);// 将密码框添加到表单面板

        // 将表单面板添加到主面板的中央区域
// BorderLayout.CENTER表示占据剩余空间
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // 底部面板（包含按钮和提示）
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        // 按钮面板
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        btnLogin = new JButton("登录");
        // 设置按钮的自定义UI
        btnLogin.setUI(new ZrzButtonUI());
        btnLogin.setFont(new Font("微软雅黑", Font.BOLD, 20));
        btnLogin.setPreferredSize(new Dimension(140, 50));
        btnLogin.setBackground(new Color(0, 0, 0));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setOpaque(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> login());
        btnPanel.add(btnLogin);

        btnReset = new JButton("重置");
        btnReset.setUI(new ZrzButtonUI());
        btnReset.setFont(new Font("微软雅黑", Font.BOLD, 20));
        btnReset.setPreferredSize(new Dimension(140, 50));
        btnReset.setBackground(new Color(40, 40, 40));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFocusPainted(false);
        btnReset.setBorderPainted(false);
        btnReset.setContentAreaFilled(false);
        btnReset.setOpaque(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(e -> reset());
        btnPanel.add(btnReset);

        bottomPanel.add(btnPanel, BorderLayout.CENTER);

        // 提示信息
        JPanel tipPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tipPanel.setOpaque(false);
        JLabel tipLabel = new JLabel("2413042108 张榕哲");
        tipLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        tipLabel.setForeground(new Color(255, 255, 255, 220));
        tipPanel.add(tipLabel);
        bottomPanel.add(tipPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 将主面板添加到窗口（JFrame）的内容面板中
        // 这是Swing中向窗口添加组件的关键步骤
        add(mainPanel);

        // 回车登录
        // 为密码框添加键盘监听器，实现回车登录功能
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // 检查按下的键是否是回车键（KeyEvent.VK_ENTER）
                    login();
                }
            }
        });

        // 为用户名框添加键盘监听器，实现回车跳转到密码框功能
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtPassword.requestFocus();
                }
            }
        });
    }

    /**
     * 登录功能
     * 验证用户名和密码，登录成功后跳转到主界面
     */
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // 验证用户名不能为空
        if (username.isEmpty()) {
            // 显示提示对话框
            ZrzDialogUtil.showMessageDialog(this, "请输入用户名！", "提示", JOptionPane.WARNING_MESSAGE);
            // 将焦点设回用户名框
            txtUsername.requestFocus();
            // 退出方法
            return;
        }

        // 验证密码不能为空
        if (password.isEmpty()) {
            ZrzDialogUtil.showMessageDialog(this, "请输入密码！", "提示", JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        // 调用服务层进行登录验证
        // 调用userService的login方法，传入用户名和密码
        ZrzUser user = userService.login(username, password);
        // 判断返回的用户对象是否不为null
        if (user != null) {
            // 登录成功：保存用户信息到会话
            // 将用户对象保存到会话工具类中
            ZrzSessionUtil.setCurrentUser(user);
            
            // 关闭登录窗口
            // this.dispose()会释放窗口的所有资源
            this.dispose();
            
            // 打开主界面
            // SwingUtilities.invokeLater()确保在主线程（事件分发线程）中执行GUI操作
            SwingUtilities.invokeLater(() -> {
                ZrzMainFrame mainFrame = new ZrzMainFrame();
                // 显示主界面窗口
                mainFrame.setVisible(true);
            });
        } else {
            // 登录失败：显示错误提示
            ZrzDialogUtil.showMessageDialog(this, "用户名或密码错误，或账号已被禁用！", "登录失败", JOptionPane.ERROR_MESSAGE);
            // 清空密码框
            txtPassword.setText("");
            // 将焦点设回密码框
            txtPassword.requestFocus();
        }
    }

    /**
     * 重置功能
     * 清空输入框内容
     */
    private void reset() {
        // 清空用户名文本框
        txtUsername.setText("");
        // 清空密码框
        txtPassword.setText("");
        // 将焦点设回用户名框
        txtUsername.requestFocus();
    }
}
