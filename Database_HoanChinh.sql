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
-- DỮ LIỆU SẢN PHẨM VÀ THUỘC TÍNH (Đã gộp)
-- =========================================
INSERT INTO DanhMuc (IdDanhMuc, TenDanhMuc, MoTa, TrangThai) VALUES (1, 'Cafe', 'Danh mục Cafe thơm ngon', 1);
INSERT INTO DanhMuc (IdDanhMuc, TenDanhMuc, MoTa, TrangThai) VALUES (2, 'Kem', 'Danh mục Kem thơm ngon', 1);
INSERT INTO DanhMuc (IdDanhMuc, TenDanhMuc, MoTa, TrangThai) VALUES (3, 'Nước giải nhiệt', 'Danh mục Nước giải nhiệt thơm ngon', 1);
INSERT INTO DanhMuc (IdDanhMuc, TenDanhMuc, MoTa, TrangThai) VALUES (4, 'Nước ép', 'Danh mục Nước ép thơm ngon', 1);
INSERT INTO DanhMuc (IdDanhMuc, TenDanhMuc, MoTa, TrangThai) VALUES (5, 'Trà sữa', 'Danh mục Trà sữa thơm ngon', 1);

INSERT INTO SizeSanPham (IdSize, TenSize, GiaThem) VALUES (1, 'S', 0);
INSERT INTO SizeSanPham (IdSize, TenSize, GiaThem) VALUES (2, 'M', 5000);
INSERT INTO SizeSanPham (IdSize, TenSize, GiaThem) VALUES (3, 'L', 10000);

INSERT INTO Topping (IdTopping, TenTopping, Gia, TrangThai) VALUES (1, 'Trân châu đen', 5000, 1);
INSERT INTO Topping (IdTopping, TenTopping, Gia, TrangThai) VALUES (2, 'Trân châu trắng', 5000, 1);
INSERT INTO Topping (IdTopping, TenTopping, Gia, TrangThai) VALUES (3, 'Thạch nha đam', 5000, 1);
INSERT INTO Topping (IdTopping, TenTopping, Gia, TrangThai) VALUES (4, 'Thạch dừa', 5000, 1);
INSERT INTO Topping (IdTopping, TenTopping, Gia, TrangThai) VALUES (5, 'Bánh flan', 5000, 1);
INSERT INTO Topping (IdTopping, TenTopping, Gia, TrangThai) VALUES (6, 'Pudding trứng', 5000, 1);
INSERT INTO Topping (IdTopping, TenTopping, Gia, TrangThai) VALUES (7, 'Kem phô mai', 5000, 1);
INSERT INTO Topping (IdTopping, TenTopping, Gia, TrangThai) VALUES (8, 'Hạt sen', 5000, 1);

INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (1, 1, 'Cà phê đen đá', 25000, 'Cà phê đen đá mát lạnh thơm ngon', 'Cà phê đen đá mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w--3RzIcg3NyA.webp', 100, 1, 1, 1, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (1, 1, '/images/Cafe/1600w--3RzIcg3NyA.webp');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (1, 1, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (2, 1, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (3, 1, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (2, 1, 'Cà phê sữa đá', 27000, 'Cà phê sữa đá mát lạnh thơm ngon', 'Cà phê sữa đá mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-b17orv7WGG8.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (2, 2, '/images/Cafe/1600w-b17orv7WGG8.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (4, 2, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (5, 2, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (6, 2, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (3, 1, 'Bạc xỉu', 29000, 'Bạc xỉu mát lạnh thơm ngon', 'Bạc xỉu mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-d5QwtsZUBvI.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (3, 3, '/images/Cafe/1600w-d5QwtsZUBvI.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (7, 3, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (8, 3, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (9, 3, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (4, 1, 'Latte đá', 31000, 'Latte đá mát lạnh thơm ngon', 'Latte đá mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-mgYBFHJUaVU.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (4, 4, '/images/Cafe/1600w-mgYBFHJUaVU.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (10, 4, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (11, 4, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (12, 4, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (5, 1, 'Cappuccino', 33000, 'Cappuccino mát lạnh thơm ngon', 'Cappuccino mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-mMIKy6Iat0c.jpg', 100, 1, 1, 0, 1, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (5, 5, '/images/Cafe/1600w-mMIKy6Iat0c.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (13, 5, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (14, 5, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (15, 5, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (6, 1, 'Mocha', 35000, 'Mocha mát lạnh thơm ngon', 'Mocha mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-nU74mnourcA.jpg', 100, 1, 0, 0, 1, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (6, 6, '/images/Cafe/1600w-nU74mnourcA.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (16, 6, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (17, 6, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (18, 6, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (7, 1, 'Americano', 37000, 'Americano mát lạnh thơm ngon', 'Americano mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-pLqyHz12xDM.jpg', 100, 1, 1, 1, 1, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (7, 7, '/images/Cafe/1600w-pLqyHz12xDM.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (19, 7, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (20, 7, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (21, 7, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (8, 1, 'Espresso', 39000, 'Espresso mát lạnh thơm ngon', 'Espresso mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-SLeHBJSeDh4.webp', 100, 1, 0, 1, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (8, 8, '/images/Cafe/1600w-SLeHBJSeDh4.webp');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (22, 8, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (23, 8, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (24, 8, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (9, 1, 'Cold Brew', 41000, 'Cold Brew mát lạnh thơm ngon', 'Cold Brew mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-SVgiYTuWeTo.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (9, 9, '/images/Cafe/1600w-SVgiYTuWeTo.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (25, 9, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (26, 9, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (27, 9, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (10, 1, 'Cà phê cốt dừa', 43000, 'Cà phê cốt dừa mát lạnh thơm ngon', 'Cà phê cốt dừa mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-THCwGnYr02g.jpg', 100, 1, 1, 0, 1, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (10, 10, '/images/Cafe/1600w-THCwGnYr02g.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (28, 10, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (29, 10, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (30, 10, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (11, 1, 'Cà phê trứng', 45000, 'Cà phê trứng mát lạnh thơm ngon', 'Cà phê trứng mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-vD0pm3tQs5E.jpg', 100, 1, 1, 0, 1, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (11, 11, '/images/Cafe/1600w-vD0pm3tQs5E.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (31, 11, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (32, 11, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (33, 11, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (12, 1, 'Cà phê muối', 47000, 'Cà phê muối mát lạnh thơm ngon', 'Cà phê muối mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-voWg934NwBk.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (12, 12, '/images/Cafe/1600w-voWg934NwBk.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (34, 12, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (35, 12, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (36, 12, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (13, 1, 'Cà phê hạnh nhân', 49000, 'Cà phê hạnh nhân mát lạnh thơm ngon', 'Cà phê hạnh nhân mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-wSO26EmaKTs.jpg', 100, 1, 1, 1, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (13, 13, '/images/Cafe/1600w-wSO26EmaKTs.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (37, 13, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (38, 13, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (39, 13, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (14, 1, 'Cà phê đen nóng', 51000, 'Cà phê đen nóng mát lạnh thơm ngon', 'Cà phê đen nóng mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/1600w-xblE7LhrG-4.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (14, 14, '/images/Cafe/1600w-xblE7LhrG-4.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (40, 14, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (41, 14, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (42, 14, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (15, 1, 'Cà phê sữa nóng', 53000, 'Cà phê sữa nóng mát lạnh thơm ngon', 'Cà phê sữa nóng mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Cafe/coffee1.jpg', 100, 1, 0, 0, 1, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (15, 15, '/images/Cafe/coffee1.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (43, 15, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (44, 15, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (45, 15, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (16, 2, 'Kem Vani', 25000, 'Kem Vani mát lạnh thơm ngon', 'Kem Vani mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Kem/1600w-fgN7RHv5ir4.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (16, 16, '/images/Kem/1600w-fgN7RHv5ir4.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (46, 16, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (47, 16, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (48, 16, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (17, 2, 'Kem Chocolate', 27000, 'Kem Chocolate mát lạnh thơm ngon', 'Kem Chocolate mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Kem/1600w-HqTL2I7A0F4.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (17, 17, '/images/Kem/1600w-HqTL2I7A0F4.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (49, 17, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (50, 17, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (51, 17, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (18, 2, 'Kem Dâu', 29000, 'Kem Dâu mát lạnh thơm ngon', 'Kem Dâu mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Kem/1600w-Lu6yqDna6eM.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (18, 18, '/images/Kem/1600w-Lu6yqDna6eM.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (52, 18, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (53, 18, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (54, 18, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (19, 2, 'Kem Trà Xanh', 31000, 'Kem Trà Xanh mát lạnh thơm ngon', 'Kem Trà Xanh mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Kem/1600w-PG7_O7UaiqU.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (19, 19, '/images/Kem/1600w-PG7_O7UaiqU.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (55, 19, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (56, 19, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (57, 19, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (20, 2, 'Kem Dừa', 33000, 'Kem Dừa mát lạnh thơm ngon', 'Kem Dừa mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Kem/1600w-uowJyEj4Wyc.jpg', 100, 1, 1, 0, 1, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (20, 20, '/images/Kem/1600w-uowJyEj4Wyc.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (58, 20, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (59, 20, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (60, 20, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (21, 2, 'Kem Bơ', 35000, 'Kem Bơ mát lạnh thơm ngon', 'Kem Bơ mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Kem/1600w-Zq9V2brrPBM.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (21, 21, '/images/Kem/1600w-Zq9V2brrPBM.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (61, 21, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (62, 21, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (63, 21, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (22, 3, 'Trà đào cam sả', 25000, 'Trà đào cam sả mát lạnh thơm ngon', 'Trà đào cam sả mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Nước giải nhiệt/1600w-HpfxIuWjOWE.jpg', 100, 1, 0, 1, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (22, 22, '/images/Nước giải nhiệt/1600w-HpfxIuWjOWE.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (64, 22, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (65, 22, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (66, 22, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (23, 3, 'Trà vải', 27000, 'Trà vải mát lạnh thơm ngon', 'Trà vải mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Nước giải nhiệt/1600w-jTE6xPHtSeI.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (23, 23, '/images/Nước giải nhiệt/1600w-jTE6xPHtSeI.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (67, 23, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (68, 23, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (69, 23, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (24, 3, 'Nước sâm', 29000, 'Nước sâm mát lạnh thơm ngon', 'Nước sâm mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Nước giải nhiệt/1600w-KmwbQ_iaAXk.jpg', 100, 1, 0, 1, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (24, 24, '/images/Nước giải nhiệt/1600w-KmwbQ_iaAXk.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (70, 24, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (71, 24, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (72, 24, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (25, 3, 'Nha đam đường phèn', 31000, 'Nha đam đường phèn mát lạnh thơm ngon', 'Nha đam đường phèn mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Nước giải nhiệt/1600w-NdOFw8tm2Bo.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (25, 25, '/images/Nước giải nhiệt/1600w-NdOFw8tm2Bo.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (73, 25, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (74, 25, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (75, 25, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (26, 3, 'Sâm bí đao', 33000, 'Sâm bí đao mát lạnh thơm ngon', 'Sâm bí đao mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Nước giải nhiệt/1600w-PLIuo75S8e8.jpg', 100, 1, 0, 0, 1, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (26, 26, '/images/Nước giải nhiệt/1600w-PLIuo75S8e8.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (76, 26, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (77, 26, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (78, 26, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (27, 3, 'Nước sâm dứa', 35000, 'Nước sâm dứa mát lạnh thơm ngon', 'Nước sâm dứa mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Nước giải nhiệt/1600w-SHsKvaRhlRw.jpg', 100, 1, 1, 1, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (27, 27, '/images/Nước giải nhiệt/1600w-SHsKvaRhlRw.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (79, 27, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (80, 27, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (81, 27, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (28, 3, 'Trà chanh', 37000, 'Trà chanh mát lạnh thơm ngon', 'Trà chanh mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Nước giải nhiệt/1600w-ZnRIcbm94Rg.jpg', 100, 1, 0, 0, 1, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (28, 28, '/images/Nước giải nhiệt/1600w-ZnRIcbm94Rg.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (82, 28, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (83, 28, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (84, 28, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (29, 4, 'Nước ép cam', 25000, 'Nước ép cam mát lạnh thơm ngon', 'Nước ép cam mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Nước ép/1600w-VafvOcpQPg4.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (29, 29, '/images/Nước ép/1600w-VafvOcpQPg4.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (85, 29, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (86, 29, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (87, 29, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (30, 4, 'Sinh tố dâu', 27000, 'Sinh tố dâu mát lạnh thơm ngon', 'Sinh tố dâu mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Nước ép/1600w-vF9lTI78WMQ.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (30, 30, '/images/Nước ép/1600w-vF9lTI78WMQ.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (88, 30, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (89, 30, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (90, 30, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (31, 4, 'Nước ép táo', 29000, 'Nước ép táo mát lạnh thơm ngon', 'Nước ép táo mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Nước ép/Screenshot 2026-05-07 175305.png', 100, 1, 1, 0, 1, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (31, 31, '/images/Nước ép/Screenshot 2026-05-07 175305.png');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (91, 31, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (92, 31, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (93, 31, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (32, 4, 'Nước ép dứa', 31000, 'Nước ép dứa mát lạnh thơm ngon', 'Nước ép dứa mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Nước ép/Screenshot 2026-05-07 175344.png', 100, 1, 0, 0, 1, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (32, 32, '/images/Nước ép/Screenshot 2026-05-07 175344.png');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (94, 32, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (95, 32, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (96, 32, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (33, 4, 'Nước ép ổi', 33000, 'Nước ép ổi mát lạnh thơm ngon', 'Nước ép ổi mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Nước ép/Screenshot 2026-05-07 175432.png', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (33, 33, '/images/Nước ép/Screenshot 2026-05-07 175432.png');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (97, 33, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (98, 33, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (99, 33, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (34, 5, 'Trà sữa truyền thống', 25000, 'Trà sữa truyền thống mát lạnh thơm ngon', 'Trà sữa truyền thống mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w--qxsZBqgTRM.jpg', 100, 1, 0, 1, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (34, 34, '/images/Trà sữa/1600w--qxsZBqgTRM.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (100, 34, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (101, 34, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (102, 34, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (35, 5, 'Trà sữa Thái xanh', 27000, 'Trà sữa Thái xanh mát lạnh thơm ngon', 'Trà sữa Thái xanh mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w-9kuKD7AaOXc.jpg', 100, 1, 0, 0, 1, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (35, 35, '/images/Trà sữa/1600w-9kuKD7AaOXc.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (103, 35, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (104, 35, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (105, 35, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (36, 5, 'Trà sữa Thái đỏ', 29000, 'Trà sữa Thái đỏ mát lạnh thơm ngon', 'Trà sữa Thái đỏ mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w-aXYnsjPoNlw.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (36, 36, '/images/Trà sữa/1600w-aXYnsjPoNlw.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (106, 36, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (107, 36, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (108, 36, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (37, 5, 'Trà sữa socola', 31000, 'Trà sữa socola mát lạnh thơm ngon', 'Trà sữa socola mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w-B12PpGQJcNE.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (37, 37, '/images/Trà sữa/1600w-B12PpGQJcNE.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (109, 37, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (110, 37, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (111, 37, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (38, 5, 'Trà sữa matcha', 33000, 'Trà sữa matcha mát lạnh thơm ngon', 'Trà sữa matcha mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w-dUSRfIAPg_A.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (38, 38, '/images/Trà sữa/1600w-dUSRfIAPg_A.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (112, 38, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (113, 38, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (114, 38, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (39, 5, 'Trà sữa khoai môn', 35000, 'Trà sữa khoai môn mát lạnh thơm ngon', 'Trà sữa khoai môn mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w-EFFN90ObFiQ.webp', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (39, 39, '/images/Trà sữa/1600w-EFFN90ObFiQ.webp');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (115, 39, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (116, 39, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (117, 39, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (40, 5, 'Hồng trà sữa', 37000, 'Hồng trà sữa mát lạnh thơm ngon', 'Hồng trà sữa mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w-fqSY7wnQLRM.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (40, 40, '/images/Trà sữa/1600w-fqSY7wnQLRM.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (118, 40, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (119, 40, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (120, 40, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (41, 5, 'Lục trà sữa', 39000, 'Lục trà sữa mát lạnh thơm ngon', 'Lục trà sữa mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w-GpH9nPgMS4Y.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (41, 41, '/images/Trà sữa/1600w-GpH9nPgMS4Y.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (121, 41, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (122, 41, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (123, 41, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (42, 5, 'Trà sữa nướng', 41000, 'Trà sữa nướng mát lạnh thơm ngon', 'Trà sữa nướng mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w-IAxf4Gkzd9c.webp', 100, 1, 0, 1, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (42, 42, '/images/Trà sữa/1600w-IAxf4Gkzd9c.webp');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (124, 42, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (125, 42, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (126, 42, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (43, 5, 'Trà sữa than tre', 43000, 'Trà sữa than tre mát lạnh thơm ngon', 'Trà sữa than tre mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w-mgYBFHJUaVU.webp', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (43, 43, '/images/Trà sữa/1600w-mgYBFHJUaVU.webp');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (127, 43, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (128, 43, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (129, 43, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (44, 5, 'Trà sữa hoa nhài', 45000, 'Trà sữa hoa nhài mát lạnh thơm ngon', 'Trà sữa hoa nhài mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w-sjUKrINnHQI.jpg', 100, 1, 1, 1, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (44, 44, '/images/Trà sữa/1600w-sjUKrINnHQI.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (130, 44, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (131, 44, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (132, 44, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (45, 5, 'Trà sữa lài', 47000, 'Trà sữa lài mát lạnh thơm ngon', 'Trà sữa lài mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w-vMyZb5Lr2tA.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (45, 45, '/images/Trà sữa/1600w-vMyZb5Lr2tA.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (133, 45, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (134, 45, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (135, 45, 3, 50);
INSERT INTO SanPham (IdSanPham, IdDanhMuc, TenSanPham, Gia, MoTaNgan, MoTaChiTiet, HinhAnh, SoLuongTon, TrangThai, LaSanPhamMoi, LaNoiBat, LaBanChay, DanhGia) VALUES (46, 5, 'Trà sữa Hokkaido', 49000, 'Trà sữa Hokkaido mát lạnh thơm ngon', 'Trà sữa Hokkaido mát lạnh thơm ngon và được chế biến từ những nguyên liệu tươi ngon nhất, mang lại cảm giác sảng khoái tuyệt vời cho ngày dài năng động.', '/images/Trà sữa/1600w-wQJOMozApfM.jpg', 100, 1, 1, 0, 0, 5);
INSERT INTO HinhAnhSanPham (IdHinhAnh, IdSanPham, HinhAnh) VALUES (46, 46, '/images/Trà sữa/1600w-wQJOMozApfM.jpg');
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (136, 46, 1, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (137, 46, 2, 50);
INSERT INTO BienTheSanPham (IdBienThe, IdSanPham, IdSize, SoLuongTon) VALUES (138, 46, 3, 50);

-- Thêm dữ liệu Mã Giảm Giá
INSERT INTO MaGiamGia (MaCode, TenMaGiamGia, LoaiGiamGia, GiaTriGiam, DonToiThieu, SoLuong, DaSuDung, NgayBatDau, NgayKetThuc, KichHoat) VALUES 
('GIAM10K', 'Giảm 10.000đ', 'TIEN_MAT', 10000.00, 20000.00, 100, 0, '2023-01-01 00:00:00', '2030-12-31 23:59:59', 1),
('GIAM10PT', 'Giảm 10%', 'PHAN_TRAM', 10.00, 30000.00, 100, 0, '2023-01-01 00:00:00', '2030-12-31 23:59:59', 1);

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

-- =========================================
-- DỮ LIỆU TEST CHO ADMIN (Thêm vào cuối)
-- =========================================

-- Thêm Khách hàng
INSERT INTO NguoiDung (HoTen, Email, MatKhau, SoDienThoai, DiaChi, VaiTro, TrangThai, NgayTao) VALUES
('Nguyễn Khách Mới', 'khach_test1@gmail.com', '123456', '0981111111', 'Quận 1, TP.HCM', 'USER', TRUE, '2026-01-10 10:00:00'),
('Trần Khách VIP', 'khach_test2@gmail.com', '123456', '0982222222', 'Quận 2, TP.HCM', 'USER', TRUE, '2026-02-15 14:00:00'),
('Lê Khách Khóa', 'khach_test3@gmail.com', '123456', '0983333333', 'Quận 3, TP.HCM', 'USER', FALSE, '2026-03-20 09:30:00');

-- Thêm Đơn hàng (giả định có các User id=1, 2, 3...)
INSERT INTO DonHang (IdNguoiDung, HoTenNguoiNhan, SoDienThoai, DiaChiGiaoHang, TongTien, PhuongThucThanhToan, TrangThai, NgayDat) VALUES
(2, 'Khách hàng 2', '0982222222', 'Q2, HCM', 150000, 'COD', 'CHO_XAC_NHAN', '2026-06-01 10:00:00'),
(2, 'Khách hàng 2', '0982222222', 'Q2, HCM', 250000, 'BANKING', 'DA_XAC_NHAN', '2026-06-02 11:00:00'),
(2, 'Khách hàng 2', '0982222222', 'Q2, HCM', 85000, 'COD', 'DANG_PHA_CHE', '2026-06-03 12:00:00'),
(2, 'Khách hàng 2', '0982222222', 'Q2, HCM', 320000, 'BANKING', 'DANG_GIAO', '2026-06-04 13:00:00'),
(2, 'Khách hàng 2', '0982222222', 'Q2, HCM', 180000, 'COD', 'DA_HUY', '2026-06-05 14:00:00'),
(2, 'Khách hàng 2', '0982222222', 'Q2, HCM', 120000, 'COD', 'HOAN_THANH', '2026-01-15 08:00:00'),
(2, 'Khách hàng 2', '0982222222', 'Q2, HCM', 450000, 'BANKING', 'HOAN_THANH', '2026-02-20 09:00:00'),
(2, 'Khách hàng 2', '0982222222', 'Q2, HCM', 60000, 'COD', 'HOAN_THANH', '2026-03-05 10:00:00'),
(2, 'Khách hàng 2', '0982222222', 'Q2, HCM', 210000, 'BANKING', 'HOAN_THANH', '2026-04-18 11:00:00'),
(2, 'Khách hàng 2', '0982222222', 'Q2, HCM', 300000, 'COD', 'HOAN_THANH', '2026-05-10 12:00:00');

-- Thêm Yêu cầu Tư vấn
INSERT INTO YeuCauTuVan (IdNguoiDung, NoiDung, TrangThai, NgayGui) VALUES
(2, 'Tư vấn mua sỉ: Tôi muốn mua số lượng lớn cho quán cafe của mình thì có được chiết khấu không?', 'CHO_PHAN_HOI', '2026-06-06 08:30:00'),
(2, 'Hỏi về thành phần: Sản phẩm Cà phê đen đá có cho thêm đường không shop?', 'CHO_PHAN_HOI', '2026-06-07 09:00:00'),
(2, 'Thời gian giao hàng: Giao nội thành HCM thường mất bao lâu ạ?', 'DA_PHAN_HOI', '2026-06-01 14:00:00');

-- Cập nhật phản hồi cho yêu cầu tư vấn đã phản hồi
SET SQL_SAFE_UPDATES = 0;
UPDATE YeuCauTuVan SET PhanHoiAdmin = 'Dạ giao nội thành thường trong vòng 1-2h ạ.' WHERE TrangThai = 'DA_PHAN_HOI';
SET SQL_SAFE_UPDATES = 1;

