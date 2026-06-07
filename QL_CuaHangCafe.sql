CREATE DATABASE QL_CuaHangCafe_Java;
USE QL_CuaHangCafe_Java;

-- =========================================
-- BẢNG DANH MỤC
-- =========================================
CREATE TABLE DanhMuc (
    IdDanhMuc INT AUTO_INCREMENT PRIMARY KEY,
    TenDanhMuc VARCHAR(100) NOT NULL,
    MoTa TEXT,
    HinhAnh VARCHAR(255),
    TrangThai BOOLEAN DEFAULT TRUE
);

-- =========================================
-- BẢNG NGƯỜI DÙNG
-- =========================================
CREATE TABLE NguoiDung (
    IdNguoiDung INT AUTO_INCREMENT PRIMARY KEY,

    HoTen VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    MatKhau VARCHAR(255) NOT NULL,

    SoDienThoai VARCHAR(20),
    DiaChi TEXT,

    AnhDaiDien VARCHAR(255),

    VaiTro ENUM('ADMIN','USER') DEFAULT 'USER',

    TrangThai BOOLEAN DEFAULT TRUE,

    NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- =========================================
-- BẢNG SẢN PHẨM
-- =========================================
CREATE TABLE SanPham (
    IdSanPham INT AUTO_INCREMENT PRIMARY KEY,

    IdDanhMuc INT NOT NULL,

    TenSanPham VARCHAR(200) NOT NULL,

    Gia DECIMAL(18,2) NOT NULL,

    GiaKhuyenMai DECIMAL(18,2),

    MoTaNgan TEXT,

    MoTaChiTiet LONGTEXT,

    HinhAnh VARCHAR(255),

    SoLuongTon INT DEFAULT 0,

    DaBan INT DEFAULT 0,

    DanhGia DOUBLE DEFAULT 5,

    LaNoiBat BOOLEAN DEFAULT FALSE,

    LaBanChay BOOLEAN DEFAULT FALSE,

    LaSanPhamMoi BOOLEAN DEFAULT TRUE,

    TrangThai BOOLEAN DEFAULT TRUE,

    NgayThem DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_SanPham_DanhMuc
        FOREIGN KEY (IdDanhMuc)
        REFERENCES DanhMuc(IdDanhMuc)
);

-- =========================================
-- BẢNG ẢNH SẢN PHẨM
-- =========================================
CREATE TABLE HinhAnhSanPham (
    IdHinhAnh INT AUTO_INCREMENT PRIMARY KEY,

    IdSanPham INT NOT NULL,

    HinhAnh VARCHAR(255),

    CONSTRAINT FK_HinhAnh_SanPham
        FOREIGN KEY (IdSanPham)
        REFERENCES SanPham(IdSanPham)
);

-- =========================================
-- BẢNG SIZE
-- =========================================
CREATE TABLE SizeSanPham (
    IdSize INT AUTO_INCREMENT PRIMARY KEY,

    TenSize VARCHAR(20),

    GiaThem DECIMAL(18,2)
);

-- =========================================
-- BẢNG TOPPING
-- =========================================
CREATE TABLE Topping (
    IdTopping INT AUTO_INCREMENT PRIMARY KEY,

    TenTopping VARCHAR(100),

    Gia DECIMAL(18,2),

    HinhAnh VARCHAR(255),

    TrangThai BOOLEAN DEFAULT TRUE
);

-- =========================================
-- BẢNG BIẾN THỂ SẢN PHẨM
-- =========================================
CREATE TABLE BienTheSanPham (
    IdBienThe INT AUTO_INCREMENT PRIMARY KEY,

    IdSanPham INT NOT NULL,

    IdSize INT NOT NULL,

    SoLuongTon INT DEFAULT 0,

    CONSTRAINT FK_BienThe_SanPham
        FOREIGN KEY (IdSanPham)
        REFERENCES SanPham(IdSanPham),

    CONSTRAINT FK_BienThe_Size
        FOREIGN KEY (IdSize)
        REFERENCES SizeSanPham(IdSize)
);

-- =========================================
-- BẢNG GIỎ HÀNG
-- =========================================
CREATE TABLE GioHang (
    IdGioHang INT AUTO_INCREMENT PRIMARY KEY,

    IdNguoiDung INT,

    NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_GioHang_NguoiDung
        FOREIGN KEY (IdNguoiDung)
        REFERENCES NguoiDung(IdNguoiDung)
);

-- =========================================
-- CHI TIẾT GIỎ HÀNG
-- =========================================
CREATE TABLE ChiTietGioHang (
    IdChiTietGioHang INT AUTO_INCREMENT PRIMARY KEY,

    IdGioHang INT NOT NULL,

    IdSanPham INT NOT NULL,

    IdSize INT,

    SoLuong INT DEFAULT 1,

    GhiChu TEXT,

    DonGia DECIMAL(18,2),

    CONSTRAINT FK_CTGH_GioHang
        FOREIGN KEY (IdGioHang)
        REFERENCES GioHang(IdGioHang),

    CONSTRAINT FK_CTGH_SanPham
        FOREIGN KEY (IdSanPham)
        REFERENCES SanPham(IdSanPham),

    CONSTRAINT FK_CTGH_Size
        FOREIGN KEY (IdSize)
        REFERENCES SizeSanPham(IdSize)
);

-- =========================================
-- TOPPING TRONG GIỎ HÀNG
-- =========================================
CREATE TABLE ChiTietGioHang_Topping (
    Id INT AUTO_INCREMENT PRIMARY KEY,

    IdChiTietGioHang INT NOT NULL,

    IdTopping INT NOT NULL,

    CONSTRAINT FK_GioHangTopping_CTGH
        FOREIGN KEY (IdChiTietGioHang)
        REFERENCES ChiTietGioHang(IdChiTietGioHang),

    CONSTRAINT FK_GioHangTopping_Topping
        FOREIGN KEY (IdTopping)
        REFERENCES Topping(IdTopping)
);

-- =========================================
-- BẢNG ĐƠN HÀNG
-- =========================================
CREATE TABLE DonHang (
    IdDonHang INT AUTO_INCREMENT PRIMARY KEY,

    IdNguoiDung INT,

    HoTenNguoiNhan VARCHAR(100),

    SoDienThoai VARCHAR(20),

    DiaChiGiaoHang TEXT,

    GhiChu TEXT,

    TongTien DECIMAL(18,2),

    PhiShip DECIMAL(18,2) DEFAULT 0,

    TienGiam DECIMAL(18,2) DEFAULT 0,

    PhuongThucThanhToan ENUM('COD','BANKING'),

    TrangThai ENUM(
        'CHO_XAC_NHAN',
        'DA_XAC_NHAN',
        'DANG_PHA_CHE',
        'DANG_GIAO',
        'HOAN_THANH',
        'DA_HUY'
    ) DEFAULT 'CHO_XAC_NHAN',

    NgayDat DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_DonHang_NguoiDung
        FOREIGN KEY (IdNguoiDung)
        REFERENCES NguoiDung(IdNguoiDung)
);

-- =========================================
-- CHI TIẾT ĐƠN HÀNG
-- =========================================
CREATE TABLE ChiTietDonHang (
    IdChiTietDonHang INT AUTO_INCREMENT PRIMARY KEY,

    IdDonHang INT NOT NULL,

    IdSanPham INT NOT NULL,

    IdSize INT,

    SoLuong INT,

    DonGia DECIMAL(18,2),

    CONSTRAINT FK_CTDH_DonHang
        FOREIGN KEY (IdDonHang)
        REFERENCES DonHang(IdDonHang),

    CONSTRAINT FK_CTDH_SanPham
        FOREIGN KEY (IdSanPham)
        REFERENCES SanPham(IdSanPham),

    CONSTRAINT FK_CTDH_Size
        FOREIGN KEY (IdSize)
        REFERENCES SizeSanPham(IdSize)
);

-- =========================================
-- TOPPING TRONG ĐƠN HÀNG
-- =========================================
CREATE TABLE ChiTietDonHang_Topping (
    Id INT AUTO_INCREMENT PRIMARY KEY,

    IdChiTietDonHang INT NOT NULL,

    IdTopping INT NOT NULL,

    CONSTRAINT FK_DonHangTopping_CTDH
        FOREIGN KEY (IdChiTietDonHang)
        REFERENCES ChiTietDonHang(IdChiTietDonHang),

    CONSTRAINT FK_DonHangTopping_Topping
        FOREIGN KEY (IdTopping)
        REFERENCES Topping(IdTopping)
);

-- =========================================
-- ĐÁNH GIÁ
-- =========================================
CREATE TABLE DanhGia (
    IdDanhGia INT AUTO_INCREMENT PRIMARY KEY,

    IdNguoiDung INT NOT NULL,

    IdSanPham INT NOT NULL,

    SoSao INT NOT NULL,

    NoiDung TEXT,

    HinhAnh VARCHAR(255),

    PhanHoiAdmin TEXT,

    DaDuyet BOOLEAN DEFAULT TRUE,

    NgayDanhGia DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_DanhGia_NguoiDung
        FOREIGN KEY (IdNguoiDung)
        REFERENCES NguoiDung(IdNguoiDung),

    CONSTRAINT FK_DanhGia_SanPham
        FOREIGN KEY (IdSanPham)
        REFERENCES SanPham(IdSanPham)
);

-- =========================================
-- MÃ GIẢM GIÁ
-- =========================================
CREATE TABLE MaGiamGia (
    IdMaGiamGia INT AUTO_INCREMENT PRIMARY KEY,

    MaCode VARCHAR(50) UNIQUE,

    TenMaGiamGia VARCHAR(100),

    LoaiGiamGia ENUM('PHAN_TRAM','TIEN_MAT'),

    GiaTriGiam DECIMAL(18,2),

    DonToiThieu DECIMAL(18,2),

    SoLuong INT,

    DaSuDung INT DEFAULT 0,

    NgayBatDau DATETIME,

    NgayKetThuc DATETIME,

    KichHoat BOOLEAN DEFAULT TRUE
);

-- =========================================
-- ĐƠN HÀNG - MÃ GIẢM GIÁ
-- =========================================
CREATE TABLE DonHang_MaGiamGia (
    Id INT AUTO_INCREMENT PRIMARY KEY,

    IdDonHang INT,

    IdMaGiamGia INT,

    CONSTRAINT FK_DH_MGG_DonHang
        FOREIGN KEY (IdDonHang)
        REFERENCES DonHang(IdDonHang),

    CONSTRAINT FK_DH_MGG_MaGiamGia
        FOREIGN KEY (IdMaGiamGia)
        REFERENCES MaGiamGia(IdMaGiamGia)
);

-- =========================================
-- YÊU THÍCH
-- =========================================
CREATE TABLE YeuThich (
    Id INT AUTO_INCREMENT PRIMARY KEY,

    IdNguoiDung INT,

    IdSanPham INT,

    CONSTRAINT FK_YeuThich_NguoiDung
        FOREIGN KEY (IdNguoiDung)
        REFERENCES NguoiDung(IdNguoiDung),

    CONSTRAINT FK_YeuThich_SanPham
        FOREIGN KEY (IdSanPham)
        REFERENCES SanPham(IdSanPham)
);

-- =========================================
-- YÊU CẦU TƯ VẤN
-- =========================================
CREATE TABLE YeuCauTuVan (
    IdTuVan INT AUTO_INCREMENT PRIMARY KEY,

    IdNguoiDung INT,

    NoiDung TEXT,

    PhanHoiAdmin TEXT,

    TrangThai ENUM('CHO_PHAN_HOI','DA_PHAN_HOI')
        DEFAULT 'CHO_PHAN_HOI',

    NgayGui DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_TuVan_NguoiDung
        FOREIGN KEY (IdNguoiDung)
        REFERENCES NguoiDung(IdNguoiDung)
);

-- =========================================
-- THÔNG BÁO
-- =========================================
CREATE TABLE ThongBao (
    IdThongBao INT AUTO_INCREMENT PRIMARY KEY,

    IdNguoiDung INT,

    TieuDe VARCHAR(255),

    NoiDung TEXT,

    DaDoc BOOLEAN DEFAULT FALSE,

    NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_ThongBao_NguoiDung
        FOREIGN KEY (IdNguoiDung)
        REFERENCES NguoiDung(IdNguoiDung)
);

-- =========================================
-- NHẬT KÝ HOẠT ĐỘNG
-- =========================================
CREATE TABLE NhatKyHoatDong (
    IdNhatKy INT AUTO_INCREMENT PRIMARY KEY,

    IdNguoiDung INT,

    HanhDong VARCHAR(255),

    MoTa TEXT,

    ThoiGian DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_NhatKy_NguoiDung
        FOREIGN KEY (IdNguoiDung)
        REFERENCES NguoiDung(IdNguoiDung)
);

-- =========================================
-- NHÀ CUNG CẤP
-- =========================================
CREATE TABLE NhaCungCap (
    IdNhaCungCap INT AUTO_INCREMENT PRIMARY KEY,

    TenNhaCungCap VARCHAR(255),

    SoDienThoai VARCHAR(20),

    DiaChi TEXT,

    Email VARCHAR(100)
);

-- =========================================
-- PHIẾU NHẬP KHO
-- =========================================
CREATE TABLE PhieuNhapKho (
    IdPhieuNhap INT AUTO_INCREMENT PRIMARY KEY,

    IdNhaCungCap INT,

    TongTien DECIMAL(18,2),

    GhiChu TEXT,

    NgayNhap DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_PhieuNhap_NhaCungCap
        FOREIGN KEY (IdNhaCungCap)
        REFERENCES NhaCungCap(IdNhaCungCap)
);

-- =========================================
-- CHI TIẾT NHẬP KHO
-- =========================================
CREATE TABLE ChiTietNhapKho (
    IdChiTietNhap INT AUTO_INCREMENT PRIMARY KEY,

    IdPhieuNhap INT,

    IdSanPham INT,

    SoLuongNhap INT,

    GiaNhap DECIMAL(18,2),

    CONSTRAINT FK_CTNK_PhieuNhap
        FOREIGN KEY (IdPhieuNhap)
        REFERENCES PhieuNhapKho(IdPhieuNhap),

    CONSTRAINT FK_CTNK_SanPham
        FOREIGN KEY (IdSanPham)
        REFERENCES SanPham(IdSanPham)
);

-- =========================================
-- TRIGGER TĂNG TỒN KHO
-- =========================================
DELIMITER $$

CREATE TRIGGER TRG_CapNhatTonKho
AFTER INSERT
ON ChiTietNhapKho
FOR EACH ROW
BEGIN

    UPDATE SanPham
    SET SoLuongTon = SoLuongTon + NEW.SoLuongNhap
    WHERE IdSanPham = NEW.IdSanPham;

END $$

DELIMITER ;

-- =========================================
-- TÀI KHOẢN ADMIN MẶC ĐỊNH
-- =========================================
-- Ứng dụng xác thực admin thông qua bảng NguoiDung với VaiTro = 'ADMIN'
INSERT INTO NguoiDung (HoTen, Email, MatKhau, SoDienThoai, DiaChi, VaiTro)
VALUES ('Tài Khoản Quản Trị Hệ Thống', 'admin@gmail.com', '123456', '0987654321', 'TP.HCM', 'ADMIN');

-- =========================================
-- DỮ LIỆU DANH MỤC MẪU
-- =========================================
INSERT INTO DanhMuc (TenDanhMuc, MoTa, HinhAnh, TrangThai) VALUES
('Cà phê', 'Các loại cà phê thơm ngon, đậm đà truyền thống và hiện đại.', 'cafe.jpg', TRUE),
('Trà sữa', 'Trà sữa trân châu, trà sữa các vị béo ngậy, hấp dẫn.', 'tra_sua.jpg', TRUE),
('Trà trái cây', 'Trà trái cây thanh mát, giải nhiệt cho ngày hè.', 'tra_trai_cay.jpg', TRUE),
('Đá xay', 'Thức uống đá xay mát lạnh kết hợp lớp kem mịn màng.', 'da_xay.jpg', TRUE),
('Bánh ngọt', 'Các loại bánh ngọt ăn kèm, thơm ngon bổ dưỡng.', 'banh_ngot.jpg', TRUE);

-- =========================================
-- DỮ LIỆU SẢN PHẨM MẪU (Cho Đánh Giá)
-- =========================================
INSERT INTO SanPham (IdDanhMuc, TenSanPham, Gia, GiaKhuyenMai, MoTaNgan, SoLuongTon, DaBan, DanhGia) VALUES
(1, 'Cà phê Đen Đá', 25000, NULL, 'Cà phê nguyên chất đậm vị', 100, 50, 4.5),
(1, 'Cà phê Sữa Đá', 30000, 25000, 'Cà phê sữa đá Sài Gòn', 200, 150, 4.8),
(2, 'Trà Sữa Trân Châu', 35000, NULL, 'Trà sữa truyền thống', 150, 120, 4.0);

-- =========================================
-- DỮ LIỆU NGƯỜI DÙNG MẪU (Cho Đánh Giá)
-- =========================================
INSERT INTO NguoiDung (HoTen, Email, MatKhau, VaiTro) VALUES
('Nguyễn Văn Khách', 'khach1@gmail.com', '123456', 'USER'),
('Trần Thị Khách', 'khach2@gmail.com', '123456', 'USER');

-- =========================================
-- DỮ LIỆU ĐÁNH GIÁ MẪU
-- =========================================
-- Giả sử Admin = IdNguoiDung 1, Khách 1 = IdNguoiDung 2, Khách 2 = IdNguoiDung 3
INSERT INTO DanhGia (IdNguoiDung, IdSanPham, SoSao, NoiDung, DaDuyet, NgayDanhGia) VALUES
(2, 1, 5, 'Cà phê rất ngon, đậm vị, giao hàng nhanh chóng.', FALSE, '2026-05-23 10:15:00'),
(3, 2, 3, 'Trà sữa hơi ngọt so với khẩu vị của mình. Trân châu cứng.', TRUE, '2026-05-24 15:30:00'),
(2, 3, 4, 'Bình thường, không có gì đặc sắc lắm. Đóng gói cẩn thận', FALSE, '2026-05-25 08:20:00');

-- =========================================
-- DỮ LIỆU ĐƠN HÀNG MẪU (Cho Dashboard Thống Kê)
-- =========================================
-- Đơn hàng mới (CHO_XAC_NHAN)
INSERT INTO DonHang (IdNguoiDung, HoTenNguoiNhan, SoDienThoai, TongTien, TrangThai, NgayDat)
VALUES 
((SELECT IdNguoiDung FROM NguoiDung ORDER BY IdNguoiDung ASC LIMIT 1), 'Nguyễn Văn A', '0901234567', 150000, 'CHO_XAC_NHAN', NOW()),
((SELECT IdNguoiDung FROM NguoiDung ORDER BY IdNguoiDung ASC LIMIT 1), 'Trần Thị B', '0912345678', 85000, 'CHO_XAC_NHAN', NOW());

-- Đơn hàng hoàn thành hôm nay
INSERT INTO DonHang (IdNguoiDung, HoTenNguoiNhan, SoDienThoai, TongTien, TrangThai, NgayDat)
VALUES 
((SELECT IdNguoiDung FROM NguoiDung ORDER BY IdNguoiDung ASC LIMIT 1), 'Lê Văn C', '0923456789', 200000, 'HOAN_THANH', NOW());

-- Đơn hàng hoàn thành các ngày trước (Cho biểu đồ doanh thu 7 ngày)
INSERT INTO DonHang (IdNguoiDung, HoTenNguoiNhan, SoDienThoai, TongTien, TrangThai, NgayDat)
VALUES 
((SELECT IdNguoiDung FROM NguoiDung ORDER BY IdNguoiDung ASC LIMIT 1), 'Khách 1', '0900000001', 350000, 'HOAN_THANH', DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
((SELECT IdNguoiDung FROM NguoiDung ORDER BY IdNguoiDung ASC LIMIT 1), 'Khách 2', '0900000002', 450000, 'HOAN_THANH', DATE_SUB(CURDATE(), INTERVAL 2 DAY)),
((SELECT IdNguoiDung FROM NguoiDung ORDER BY IdNguoiDung ASC LIMIT 1), 'Khách 3', '0900000003', 120000, 'HOAN_THANH', DATE_SUB(CURDATE(), INTERVAL 3 DAY)),
((SELECT IdNguoiDung FROM NguoiDung ORDER BY IdNguoiDung ASC LIMIT 1), 'Khách 4', '0900000004', 500000, 'HOAN_THANH', DATE_SUB(CURDATE(), INTERVAL 4 DAY)),
((SELECT IdNguoiDung FROM NguoiDung ORDER BY IdNguoiDung ASC LIMIT 1), 'Khách 5', '0900000005', 250000, 'HOAN_THANH', DATE_SUB(CURDATE(), INTERVAL 5 DAY)),
((SELECT IdNguoiDung FROM NguoiDung ORDER BY IdNguoiDung ASC LIMIT 1), 'Khách 6', '0900000006', 700000, 'HOAN_THANH', DATE_SUB(CURDATE(), INTERVAL 6 DAY));

-- Cập nhật dữ liệu Sản phẩm để test cảnh báo "Sắp hết hàng" (SoLuongTon <= 10) và Top bán chạy
SET SQL_SAFE_UPDATES = 0;
UPDATE SanPham SET SoLuongTon = 5, DaBan = 300 WHERE TenSanPham LIKE '%Cà phê Đen Đá%';
UPDATE SanPham SET SoLuongTon = 2, DaBan = 200 WHERE TenSanPham LIKE '%Cà phê Sữa Đá%';
UPDATE SanPham SET SoLuongTon = 10, DaBan = 150 WHERE TenSanPham LIKE '%Trà Sữa Trân Châu%';
SET SQL_SAFE_UPDATES = 1;