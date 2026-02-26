package com.zrz.property;

import com.zrz.property.util.ZrzDBUtil;
import com.zrz.property.view.ZrzLoginFrame;

import javax.swing.*;

/**
 * 小区物业管理系统主程序入口
 * @author zrz
 * @version 1.0
 */
public class ZrzMainApp {
    public static void main(String[] args) {
        // 测试数据库连接
        if (!ZrzDBUtil.testConnection()) {
            JOptionPane.showMessageDialog(null, 
                "数据库连接失败！\n请检查：\n1. MySQL服务是否启动\n2. 数据库配置是否正确\n3. 是否已导入数据库脚本", 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
            //终止JVM运行
            //参数1：非零状态码表示异常退出
            //数据库连接失败时，程序无法正常工作，因此直接退出

            System.exit(1);
        }

        // 设置系统外观并配置按钮样式
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // 强制按钮使用自定义背景色
            UIManager.put("Button.background", new java.awt.Color(0, 0, 0));
            UIManager.put("Button.foreground", java.awt.Color.WHITE);
            UIManager.put("Button.select", new java.awt.Color(40, 40, 40));
        } catch (Exception e) {
            //捕获外观设置过程中的任何异常
            //即使外观设置失败，程序也应继续运行
            //使用printStackTrace()打印异常，方便调试
            e.printStackTrace();
        }

        // 启动登录界面
        SwingUtilities.invokeLater(() -> {
            ZrzLoginFrame loginFrame = new ZrzLoginFrame();
            //设置窗口可见，启动Swing应用程序的主界面，表示应用程序已成功启动
            loginFrame.setVisible(true);
        });
    }
}
