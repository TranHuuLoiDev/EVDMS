-- Database: ev_dealer_management_db

USE `ev_dealer_management_db`;


-- Vô hiệu hóa kiểm tra khóa ngoại tạm thời
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Bảng Dealers
CREATE TABLE IF NOT EXISTS `Dealers` (
    `dealer_id` INT AUTO_INCREMENT PRIMARY KEY,
    `dealer_name` VARCHAR(100) NOT NULL,
    `address` VARCHAR(255),
    `phone` VARCHAR(20),
    `contract_start_date` DATE,
    `sales_quota` DECIMAL(15, 2) DEFAULT 0.00
);

-- 2. Bảng Users
CREATE TABLE IF NOT EXISTS `Users` (
    `user_id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password_hash` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `role` ENUM('Admin', 'EVM Staff', 'Dealer Manager', 'Dealer Staff') NOT NULL,
    `dealer_id` INT NULL,
    `is_active` BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (`dealer_id`) REFERENCES `Dealers`(`dealer_id`)
);

-- 3. Bảng Vehicles
CREATE TABLE IF NOT EXISTS `Vehicles` (
    `vehicle_id` INT AUTO_INCREMENT PRIMARY KEY,
    `model_name` VARCHAR(100) NOT NULL,
    `version` VARCHAR(50) NOT NULL,
    `color` VARCHAR(30) NOT NULL,
    `base_price` DECIMAL(15, 2) NOT NULL,
    `retail_price` DECIMAL(15, 2) NOT NULL,
    `description` TEXT,
    UNIQUE KEY `uk_vehicle_version_color` (`model_name`, `version`, `color`)
);

-- 4. Bảng Inventory
CREATE TABLE IF NOT EXISTS `Inventory` (
    `inventory_id` INT AUTO_INCREMENT PRIMARY KEY,
    `vehicle_id` INT NOT NULL,
    `dealer_id` INT NULL,
    `quantity` INT NOT NULL DEFAULT 0,
    `vin_number` VARCHAR(17) UNIQUE NULL,
    `location` ENUM('EVM_HQ', 'Dealer_Lot', 'In_Transit') NOT NULL,
    FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles`(`vehicle_id`),
    FOREIGN KEY (`dealer_id`) REFERENCES `Dealers`(`dealer_id`)
);

-- 5. Bảng Customers
CREATE TABLE IF NOT EXISTS `Customers` (
    `customer_id` INT AUTO_INCREMENT PRIMARY KEY,
    `full_name` VARCHAR(100) NOT NULL,
    `phone` VARCHAR(20) UNIQUE,
    `email` VARCHAR(100) UNIQUE,
    `address` VARCHAR(255),
    `date_created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Bảng SalesOrders
CREATE TABLE IF NOT EXISTS `SalesOrders` (
    `order_id` INT AUTO_INCREMENT PRIMARY KEY,
    `order_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `customer_id` INT NOT NULL,
    `dealer_id` INT NOT NULL,
    `salesperson_id` INT NOT NULL,
    `total_amount` DECIMAL(15, 2) NOT NULL,
    `status` ENUM('Quotation', 'Pending', 'Confirmed', 'Delivered', 'Cancelled') NOT NULL,
    `payment_method` ENUM('Cash', 'Installment') NOT NULL,
    `delivery_date_expected` DATE,
    `delivery_date_actual` DATE NULL,
    FOREIGN KEY (`customer_id`) REFERENCES `Customers`(`customer_id`),
    FOREIGN KEY (`dealer_id`) REFERENCES `Dealers`(`dealer_id`),
    FOREIGN KEY (`salesperson_id`) REFERENCES `Users`(`user_id`)
);

-- 7. Bảng OrderItems
CREATE TABLE IF NOT EXISTS `OrderItems` (
    `order_item_id` INT AUTO_INCREMENT PRIMARY KEY,
    `order_id` INT NOT NULL,
    `vehicle_id` INT NOT NULL,
    `quantity` INT NOT NULL DEFAULT 1,
    `unit_price` DECIMAL(15, 2) NOT NULL,
    `discount_amount` DECIMAL(15, 2) DEFAULT 0.00,
    FOREIGN KEY (`order_id`) REFERENCES `SalesOrders`(`order_id`),
    FOREIGN KEY (`vehicle_id`) REFERENCES `Vehicles`(`vehicle_id`)
);

-- 8. Bảng DealerPayables
CREATE TABLE IF NOT EXISTS `DealerPayables` (
    `payable_id` INT AUTO_INCREMENT PRIMARY KEY,
    `dealer_id` INT NOT NULL,
    `invoice_number` VARCHAR(50) UNIQUE,
    `amount_due` DECIMAL(15, 2) NOT NULL,
    `due_date` DATE NOT NULL,
    `status` ENUM('Pending', 'Paid', 'Overdue') NOT NULL,
    FOREIGN KEY (`dealer_id`) REFERENCES `Dealers`(`dealer_id`)
);

-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;

-- B. DỮ LIỆU MẪU (SEED DATA)

-- 1. Dealers
INSERT INTO `Dealers` (`dealer_name`, `address`, `phone`, `contract_start_date`, `sales_quota`) VALUES
('Đại lý Miền Bắc - HN', 'Hà Nội', '0912345678', '2023-01-15', 50.00),
('Đại lý Miền Nam - HCM', 'TP. Hồ Chí Minh', '0987654321', '2023-02-20', 75.00);

-- 2. Users 
INSERT INTO `Users` (`username`, `password_hash`, `full_name`, `role`, `dealer_id`) VALUES
('admin', 'admin_hash', 'Nguyễn Văn A', 'Admin', NULL),
('evm_staff', 'evm_hash', 'Trần Thị B', 'EVM Staff', NULL),
('d1_manager', 'd1m_hash', 'Lê Văn C', 'Dealer Manager', 1),
('sales_hn_01', 'sales1_hash', 'Phạm Thu D', 'Dealer Staff', 1),
('sales_hcm_02', 'sales2_hash', 'Hoàng Văn E', 'Dealer Staff', 2);

-- 3. Vehicles
INSERT INTO `Vehicles` (`model_name`, `version`, `color`, `base_price`, `retail_price`, `description`) VALUES
('VF 5', 'Plus', 'Trắng', 450000000.00, 500000000.00, 'Xe điện cỡ nhỏ'),
('VF e34', 'Plus', 'Đỏ', 600000000.00, 700000000.00, 'Xe điện SUV tầm trung'),
('VF 8', 'Eco', 'Đen', 1000000000.00, 1100000000.00, 'Xe điện SUV cỡ lớn');

-- 4. Customers
INSERT INTO `Customers` (`full_name`, `phone`, `email`, `address`) VALUES
('Nguyễn Văn Khách', '0901112223', 'khachnv@mail.com', 'Quận 1, TP.HCM'),
('Trần Thị Hàng', '0904445556', 'hangtt@mail.com', 'Đống Đa, Hà Nội');

-- 5. SalesOrders (Đơn hàng mẫu)
INSERT INTO `SalesOrders` (`customer_id`, `dealer_id`, `salesperson_id`, `total_amount`, `status`, `payment_method`, `delivery_date_actual`) VALUES
(2, 1, 4, 700000000.00, 'Delivered', 'Cash', '2025-10-20');
INSERT INTO `SalesOrders` (`customer_id`, `dealer_id`, `salesperson_id`, `total_amount`, `status`, `payment_method`, `delivery_date_expected`) VALUES
(1, 2, 5, 1100000000.00, 'Confirmed', 'Installment', '2025-11-15');
INSERT INTO `SalesOrders` (`customer_id`, `dealer_id`, `salesperson_id`, `total_amount`, `status`, `payment_method`, `delivery_date_actual`) VALUES
(1, 2, 5, 500000000.00, 'Delivered', 'Cash', '2025-09-01');

-- 6. OrderItems
INSERT INTO `OrderItems` (`order_id`, `vehicle_id`, `quantity`, `unit_price`, `discount_amount`) VALUES
(1, 2, 1, 700000000.00, 0.00),
(2, 3, 1, 1100000000.00, 0.00),
(3, 1, 1, 500000000.00, 0.00);

-- 7. Inventory (Tồn kho)
INSERT INTO `Inventory` (`vehicle_id`, `dealer_id`, `quantity`, `location`) VALUES
(1, NULL, 50, 'EVM_HQ'),
(2, NULL, 40, 'EVM_HQ'),
(3, 1, 5, 'Dealer_Lot'),
(2, 2, 3, 'Dealer_Lot');

-- 8. DealerPayables (Công nợ mẫu)
INSERT INTO `DealerPayables` (`dealer_id`, `invoice_number`, `amount_due`, `due_date`, `status`) VALUES
(1, 'INV20251001', 5000000000.00, '2025-11-30', 'Pending'),
(2, 'INV20251002', 7500000000.00, '2025-11-20', 'Pending');

-- C. TRUY VẤN BÁO CÁO MẪU

SELECT
    U.full_name AS Ten_Nhan_Vien_Ban_Hang,
    D.dealer_name AS Dai_Ly,
    COUNT(SO.order_id) AS Tong_Don_Hang_Da_Giao,
    SUM(SO.total_amount) AS Tong_Doanh_So_VND,
    SUM(OI.quantity) AS Tong_So_Xe_Ban_Duoc
FROM
    SalesOrders SO
INNER JOIN
    Users U ON SO.salesperson_id = U.user_id
INNER JOIN
    Dealers D ON SO.dealer_id = D.dealer_id
INNER JOIN
    OrderItems OI ON SO.order_id = OI.order_id
WHERE
    SO.status = 'Delivered'
GROUP BY
    U.user_id, U.full_name, D.dealer_name
ORDER BY
    Tong_Doanh_So_VND DESC;