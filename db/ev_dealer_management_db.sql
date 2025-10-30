CREATE DATABASE ev_dealer_management_db;
USE ev_dealer_management_db;


-- Vô hiệu hóa kiểm tra khóa ngoại tạm thời
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Bảng Dealers
CREATE TABLE IF NOT EXISTS Dealers (
    dealer_id INT AUTO_INCREMENT PRIMARY KEY,
    dealer_name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20),
    contract_start_date DATE,
    sales_quota DECIMAL(15, 2) DEFAULT 0.00
);
Error Code: 1046. No database selected Select the default DB to be used by double-clicking its name in the SCHEMAS list in the sidebar.

-- 2. Bảng Users
CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('Admin', 'EVM Staff', 'Dealer Manager', 'Dealer Staff') NOT NULL,
    dealer_id INT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (dealer_id) REFERENCES Dealers(dealer_id)
);

-- 3. Bảng Vehicles
CREATE TABLE IF NOT EXISTS Vehicles (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    model_name VARCHAR(100) NOT NULL,
    version VARCHAR(50) NOT NULL,
    color VARCHAR(30) NOT NULL,
    base_price DECIMAL(15, 2) NOT NULL,
    retail_price DECIMAL(15, 2) NOT NULL,
    description TEXT,
    UNIQUE KEY uk_vehicle_version_color (`model_name`, `version`, `color`)
);

-- 4. Bảng Inventory
CREATE TABLE IF NOT EXISTS Inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    dealer_id INT NULL,
    quantity INT NOT NULL DEFAULT 0,
    vin_number VARCHAR(17) UNIQUE NULL,
    location ENUM('EVM_HQ', 'Dealer_Lot', 'In_Transit') NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id),
    FOREIGN KEY (dealer_id) REFERENCES Dealers(dealer_id)
);

-- 5. Bảng Customers
CREATE TABLE IF NOT EXISTS Customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    address VARCHAR(255),
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Bảng SalesOrders
CREATE TABLE IF NOT EXISTS SalesOrders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    customer_id INT NOT NULL,
    dealer_id INT NOT NULL,
    salesperson_id INT NOT NULL,
    total_amount DECIMAL(15, 2) NOT NULL,
    status ENUM('Quotation', 'Pending', 'Confirmed', 'Delivered', 'Cancelled') NOT NULL,
    payment_method ENUM('Cash', 'Installment') NOT NULL,
    delivery_date_expected DATE,
    delivery_date_actual DATE NULL,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id),
    FOREIGN KEY (dealer_id) REFERENCES Dealers(dealer_id),
    FOREIGN KEY (salesperson_id) REFERENCES Users(user_id)
);

-- 7. Bảng OrderItems
CREATE TABLE IF NOT EXISTS OrderItems (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(15, 2) NOT NULL,
    discount_amount DECIMAL(15, 2) DEFAULT 0.00,
    FOREIGN KEY (order_id) REFERENCES SalesOrders(order_id),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- 8. Bảng DealerPayables
CREATE TABLE IF NOT EXISTS DealerPayables (
    payable_id INT AUTO_INCREMENT PRIMARY KEY,
    dealer_id INT NOT NULL,
    invoice_number VARCHAR(50) UNIQUE,
    amount_due DECIMAL(15, 2) NOT NULL,
    due_date DATE NOT NULL,
    status ENUM('Pending', 'Paid', 'Overdue') NOT NULL,
    FOREIGN KEY (dealer_id) REFERENCES Dealers(dealer_id)
);
USE ev_dealer_management_db;

-- Vô hiệu hóa kiểm tra khóa ngoại tạm thời
SET FOREIGN_KEY_CHECKS = 0;

-- =======================================================
-- A. TẠO BẢNG MỚI CHO MODULE KHÁCH HÀNG
-- =======================================================

-- 9. Bảng TestDrives (Lịch Lái Thử)
CREATE TABLE IF NOT EXISTS TestDrives (
    test_drive_id INT AUTO_INCREMENT PRIMARY KEY,
    
    customer_id INT NOT NULL,
    vehicle_id INT NOT NULL, -- Xe muốn lái thử
    dealer_id INT,          -- Đại lý thực hiện
    
    preferred_date DATE NOT NULL,
    preferred_time TIME NOT NULL,
    
    -- Trạng thái: Pending, Confirmed, Completed, Cancelled
    status VARCHAR(50) NOT NULL DEFAULT 'Pending', 
    note TEXT,
    
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id),
    FOREIGN KEY (dealer_id) REFERENCES Dealers(dealer_id)
);

-- 10. Bảng Feedbacks (Phản hồi/Góp ý)
CREATE TABLE IF NOT EXISTS Feedbacks (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    
    customer_id INT NOT NULL,
    
    -- Loại phản hồi: Service, Vehicle, Dealer, General
    feedback_type VARCHAR(50), 
    subject VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    rating TINYINT CHECK (rating BETWEEN 1 AND 5), -- Đánh giá từ 1 đến 5 sao
    
    submission_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    -- Trạng thái xử lý: New, In Progress, Resolved
    status VARCHAR(50) DEFAULT 'New',
    
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
);

-- Bảng Customers trong file gốc đã thiếu user_id, 
-- để đảm bảo tính toàn vẹn, ta bỏ qua trường user_id trong Customers ở đây.

-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;

-- =======================================================
-- B. CHÈN DỮ LIỆU MẪU CHO MODULE KHÁCH HÀNG
-- =======================================================

-- Dữ liệu mẫu cho bảng TestDrives (customer_id 1 và 2 đã có)
-- Giả định: vehicle_id=2 (VF e34), vehicle_id=1 (VF 5)
-- Giả định: dealer_id=1 (Miền Bắc), dealer_id=2 (Miền Nam)
INSERT INTO TestDrives (customer_id, vehicle_id, dealer_id, preferred_date, preferred_time, status, note) VALUES
(1, 2, 1, '2025-12-01', '10:00:00', 'Confirmed', 'Khách hàng muốn lái thử VF e34 tại HN.'),
(2, 1, 2, '2025-12-05', '15:30:00', 'Pending', 'Xin dời lịch nếu có thể.'),
(1, 3, 1, '2025-12-10', '14:00:00', 'Pending', NULL); -- Thêm 1 lịch nữa cho customer 1


-- Dữ liệu mẫu cho bảng Feedbacks
INSERT INTO Feedbacks (customer_id, feedback_type, subject, content, rating, status) VALUES
(1, 'Vehicle', 'Chất lượng VF 8', 'Tôi rất hài lòng với trải nghiệm lái VF 8, rất êm và mạnh.', 5, 'New'),
(2, 'Dealer', 'Dịch vụ tại Đại lý Miền Nam', 'Nhân viên tư vấn nhiệt tình, nhưng quá trình chờ đợi hơi lâu.', 4, 'In Progress'),
(1, 'Service', 'Góp ý ứng dụng di động', 'Ứng dụng mobile cần cải thiện tính năng đặt lịch bảo dưỡng.', 3, 'New');

-- =======================================================
-- C. TRUY VẤN BÁO CÁO MẪU MỚI (Về Khách hàng)
-- =======================================================

-- Truy vấn 1: Thống kê số lượng Lịch Lái Thử theo trạng thái
SELECT
    status AS Trang_Thai_Lai_Thu,
    COUNT(test_drive_id) AS So_Luong
FROM
    TestDrives
GROUP BY
    status;

-- Truy vấn 2: Xem chi tiết Phản hồi của Khách hàng
SELECT
    C.full_name AS Ten_Khach_Hang,
    F.submission_date AS Ngay_Gui,
    F.feedback_type AS Loai_Phan_Hoi,
    F.subject AS Tieu_De,
    F.rating AS Danh_Gia_Sao,
    F.status AS Trang_Thai_Xu_Ly
FROM
    Feedbacks F
INNER JOIN
    Customers C ON F.customer_id = C.customer_id
ORDER BY
    Ngay_Gui DESC;

-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;

-- B. DỮ LIỆU MẪU (SEED DATA)

-- 1. Dealers
INSERT INTO Dealers (`dealer_name`, `address`, `phone`, `contract_start_date`, `sales_quota`) VALUES
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