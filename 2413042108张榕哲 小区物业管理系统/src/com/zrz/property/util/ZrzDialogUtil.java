package com.zrz.property.util;

import javax.swing.*;
import java.awt.*;

/**
 * 对话框工具类 - 统一对话框样式
 */
public class ZrzDialogUtil {
    
    /**
     * 显示输入对话框（深色主题）
     */
    public static String showInputDialog(Component parent, String message, String title, String initialValue) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel(message);
        label.setFont(new Font("微软雅黑", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        
        JTextField textField = new JTextField(initialValue);
        textField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(300, 35));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        final String[] result = {null};
        
        JButton okButton = createStyledButton("确定");
        okButton.addActionListener(e -> {
            result[0] = textField.getText();
            Window window = SwingUtilities.getWindowAncestor(okButton);
            if (window != null) {
                window.dispose();
            }
        });
        
        JButton cancelButton = createStyledButton("取消");
        cancelButton.addActionListener(e -> {
            result[0] = null;
            Window window = SwingUtilities.getWindowAncestor(cancelButton);
            if (window != null) {
                window.dispose();
            }
        });
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // 回车键确认
        textField.addActionListener(e -> okButton.doClick());
        
        dialog.setVisible(true);
        
        return result[0];
    }
    
    /**
     * 显示确认对话框（深色主题）
     */
    public static int showConfirmDialog(Component parent, String message, String title) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("<html><div style='text-align: center; width: 300px;'>" + message + "</div></html>");
        label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(label, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        final int[] result = {JOptionPane.NO_OPTION};
        
        JButton yesButton = createStyledButton("是");
        yesButton.addActionListener(e -> {
            result[0] = JOptionPane.YES_OPTION;
            Window window = SwingUtilities.getWindowAncestor(yesButton);
            if (window != null) {
                window.dispose();
            }
        });
        
        JButton noButton = createStyledButton("否");
        noButton.addActionListener(e -> {
            result[0] = JOptionPane.NO_OPTION;
            Window window = SwingUtilities.getWindowAncestor(noButton);
            if (window != null) {
                window.dispose();
            }
        });
        
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        
        return result[0];
    }
    
    /**
     * 显示消息对话框（深色主题）
     */
    public static void showMessageDialog(Component parent, String message, String title, int messageType) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 图标
        JLabel iconLabel = new JLabel();
        if (messageType == JOptionPane.INFORMATION_MESSAGE) {
            iconLabel.setText("ℹ");
            iconLabel.setForeground(new Color(59, 130, 246));
        } else if (messageType == JOptionPane.WARNING_MESSAGE) {
            iconLabel.setText("⚠");
            iconLabel.setForeground(new Color(245, 158, 11));
        } else if (messageType == JOptionPane.ERROR_MESSAGE) {
            iconLabel.setText("✖");
            iconLabel.setForeground(new Color(239, 68, 68));
        } else {
            iconLabel.setText("✓");
            iconLabel.setForeground(new Color(34, 197, 94));
        }
        iconLabel.setFont(new Font("Arial", Font.BOLD, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(60, 60));
        
        JLabel label = new JLabel("<html><div style='text-align: center; width: 300px;'>" + message + "</div></html>");
        label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(iconLabel, BorderLayout.WEST);
        centerPanel.add(label, BorderLayout.CENTER);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton okButton = createStyledButton("确定");
        okButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(okButton);
            if (window != null) {
                window.dispose();
            }
        });
        
        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
    
    /**
     * 显示消息对话框（简化版）
     */
    public static void showMessageDialog(Component parent, String message) {
        showMessageDialog(parent, message, "提示", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 创建统一样式的按钮
     */
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setUI(new ZrzButtonUI());
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setBackground(new Color(0, 0, 0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(100, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        return button;
    }
}
