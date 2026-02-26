-- 小区物业管理系统数据库设计
-- 创建数据库
CREATE DATABASE IF NOT EXISTS zrz_property_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE zrz_property_management;

-- 0. 系统用户表
CREATE TABLE zrz_user (
    user_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    role ENUM('管理员', '普通用户') DEFAULT '普通用户' COMMENT '角色',
    phone VARCHAR(20) COMMENT '联系电话',
    status ENUM('启用', '禁用') DEFAULT '启用' COMMENT '状态',
    last_login DATETIME COMMENT '最后登录时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='系统用户表';

-- 1. 用户信息表
CREATE TABLE zrz_resident (
    resident_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    room_number VARCHAR(20) NOT NULL UNIQUE COMMENT '房号',
    resident_name VARCHAR(50) NOT NULL COMMENT '姓名',
    phone VARCHAR(20) COMMENT '联系电话',
    id_card VARCHAR(18) COMMENT '身份证号',
    family_members INT DEFAULT 1 COMMENT '家庭人数',
    register_date DATE COMMENT '入住日期',
    status ENUM('正常', '欠费', '已搬离') DEFAULT '正常' COMMENT '状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='小区居民信息表';

-- 2. 物业费记录表
CREATE TABLE zrz_property_fee (
    fee_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '费用ID',
    resident_id INT NOT NULL COMMENT '用户ID',
    fee_year INT NOT NULL COMMENT '费用年份',
    fee_month INT NOT NULL COMMENT '费用月份',
    amount DECIMAL(10,2) NOT NULL COMMENT '应缴金额',
    paid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '已缴金额',
    payment_status ENUM('未缴', '已缴', '部分缴纳') DEFAULT '未缴' COMMENT '缴费状态',
    due_date DATE COMMENT '应缴日期',
    payment_date DATE COMMENT '实际缴费日期',
    overdue_days INT DEFAULT 0 COMMENT '逾期天数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (resident_id) REFERENCES zrz_resident(resident_id) ON DELETE CASCADE,
    UNIQUE KEY uk_resident_period (resident_id, fee_year, fee_month)
) COMMENT='物业费记录表';

-- 3. 取暖费记录表
CREATE TABLE zrz_heating_fee (
    heating_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '取暖费ID',
    resident_id INT NOT NULL COMMENT '用户ID',
    fee_year INT NOT NULL COMMENT '费用年份',
    amount DECIMAL(10,2) NOT NULL COMMENT '应缴金额',
    paid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '已缴金额',
    payment_status ENUM('未缴', '已缴', '部分缴纳') DEFAULT '未缴' COMMENT '缴费状态',
    due_date DATE COMMENT '应缴日期',
    payment_date DATE COMMENT '实际缴费日期',
    overdue_days INT DEFAULT 0 COMMENT '逾期天数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (resident_id) REFERENCES zrz_resident(resident_id) ON DELETE CASCADE,
    UNIQUE KEY uk_resident_year (resident_id, fee_year)
) COMMENT='取暖费记录表';

-- 4. 电卡购买记录表
CREATE TABLE zrz_electric_card (
    electric_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '电卡记录ID',
    resident_id INT NOT NULL COMMENT '用户ID',
    purchase_amount DECIMAL(10,2) NOT NULL COMMENT '购买金额',
    purchase_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '购买日期',
    kwh_amount DECIMAL(10,2) COMMENT '电量（度）',
    operator VARCHAR(50) COMMENT '操作员',
    FOREIGN KEY (resident_id) REFERENCES zrz_resident(resident_id) ON DELETE CASCADE
) COMMENT='电卡购买记录表';

-- 5. 水卡购买记录表
CREATE TABLE zrz_water_card (
    water_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '水卡记录ID',
    resident_id INT NOT NULL COMMENT '用户ID',
    purchase_amount DECIMAL(10,2) NOT NULL COMMENT '购买金额',
    purchase_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '购买日期',
    cubic_amount DECIMAL(10,2) COMMENT '水量（立方米）',
    operator VARCHAR(50) COMMENT '操作员',
    FOREIGN KEY (resident_id) REFERENCES zrz_resident(resident_id) ON DELETE CASCADE
) COMMENT='水卡购买记录表';

-- 插入测试数据
-- 插入系统用户（密码都是123456）
INSERT INTO zrz_user (username, password, real_name, role, phone, status) VALUES
('admin', '123456', '系统管理员', '管理员', '13900000000', '启用'),
('user1', '123456', '张三', '普通用户', '13900000001', '启用'),
('user2', '123456', '李四', '普通用户', '13900000002', '启用');

INSERT INTO zrz_resident (room_number, resident_name, phone, id_card, family_members, register_date, status) VALUES
('1-101', '张三', '13800138001', '110101199001011234', 3, '2020-01-15', '正常'),
('1-102', '李四', '13800138002', '110101199002021234', 2, '2020-02-20', '欠费'),
('1-201', '王五', '13800138003', '110101199003031234', 4, '2020-03-10', '正常'),
('2-101', '赵六', '13800138004', '110101199004041234', 2, '2020-04-05', '欠费'),
('2-102', '钱七', '13800138005', '110101199005051234', 3, '2020-05-12', '正常');

INSERT INTO zrz_property_fee (resident_id, fee_year, fee_month, amount, paid_amount, payment_status, due_date, payment_date, overdue_days) VALUES
(1, 2025, 12, 500.00, 500.00, '已缴', '2025-12-10', '2025-12-08', 0),
(1, 2025, 11, 500.00, 500.00, '已缴', '2025-11-10', '2025-11-05', 0),
(2, 2025, 12, 500.00, 0.00, '未缴', '2025-12-10', NULL, 17),
(2, 2025, 11, 500.00, 0.00, '未缴', '2025-11-10', NULL, 47),
(3, 2025, 12, 600.00, 600.00, '已缴', '2025-12-10', '2025-12-09', 0),
(4, 2025, 12, 500.00, 0.00, '未缴', '2025-12-10', NULL, 17),
(5, 2025, 12, 550.00, 550.00, '已缴', '2025-12-10', '2025-12-07', 0);

INSERT INTO zrz_heating_fee (resident_id, fee_year, amount, paid_amount, payment_status, due_date, payment_date, overdue_days) VALUES
(1, 2025, 2000.00, 2000.00, '已缴', '2025-10-31', '2025-10-20', 0),
(2, 2025, 2000.00, 0.00, '未缴', '2025-10-31', NULL, 57),
(3, 2025, 2500.00, 2500.00, '已缴', '2025-10-31', '2025-10-25', 0),
(4, 2025, 2000.00, 0.00, '未缴', '2025-10-31', NULL, 57),
(5, 2025, 2200.00, 2200.00, '已缴', '2025-10-31', '2025-10-28', 0);

INSERT INTO zrz_electric_card (resident_id, purchase_amount, purchase_date, kwh_amount, operator) VALUES
(1, 200.00, '2025-12-01 10:30:00', 333.33, '管理员'),
(1, 150.00, '2025-12-15 14:20:00', 250.00, '管理员'),
(3, 300.00, '2025-12-05 09:15:00', 500.00, '管理员'),
(5, 180.00, '2025-12-10 16:45:00', 300.00, '管理员');

INSERT INTO zrz_water_card (resident_id, purchase_amount, purchase_date, cubic_amount, operator) VALUES
(1, 100.00, '2025-12-01 10:35:00', 20.00, '管理员'),
(1, 80.00, '2025-12-20 11:20:00', 16.00, '管理员'),
(3, 120.00, '2025-12-05 09:20:00', 24.00, '管理员'),
(5, 90.00, '2025-12-10 16:50:00', 18.00, '管理员');
