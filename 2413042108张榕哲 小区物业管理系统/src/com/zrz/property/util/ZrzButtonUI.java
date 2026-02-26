package com.zrz.property.util;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

/**
 * 自定义按钮UI类
 * 继承BasicButtonUI，实现自定义按钮样式
 * 特点：黑色背景、白色文字、白色边框
 */
public class ZrzButtonUI extends BasicButtonUI {
    
    /**
     * 绘制按钮
     * 重写paint方法实现自定义样式
     * @param g 图形上下文
     * @param c 组件对象
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton) c;
        Graphics2D g2d = (Graphics2D) g.create();
        
        // 开启抗锯齿，使边缘更平滑
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 绘制按钮背景（使用按钮的背景色）
        g2d.setColor(button.getBackground());
        g2d.fillRect(0, 0, button.getWidth(), button.getHeight());
        
        // 绘制白色边框（2像素宽）
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(1, 1, button.getWidth() - 2, button.getHeight() - 2);
        
        // 绘制按钮文字（居中显示）
        g2d.setColor(button.getForeground());
        g2d.setFont(button.getFont());
        FontMetrics fm = g2d.getFontMetrics();
        String text = button.getText();
        int x = (button.getWidth() - fm.stringWidth(text)) / 2;
        int y = (button.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(text, x, y);
        
        g2d.dispose();
    }
    
    /**
     * 绘制按钮按下状态
     * 按下时背景色变深
     * @param g 图形上下文
     * @param b 按钮对象
     */
    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillRect(0, 0, b.getWidth(), b.getHeight());
        g2d.dispose();
    }
}
