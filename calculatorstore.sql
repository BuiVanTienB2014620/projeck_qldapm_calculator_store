-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 24, 2023 lúc 05:16 PM
-- Phiên bản máy phục vụ: 10.4.27-MariaDB
-- Phiên bản PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `calculatorstore`
--

DELIMITER $$
--
-- Thủ tục
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `suaSanpham` (`idSanpham` INT, `tenSanpham` VARCHAR(255), `mota` VARCHAR(255), `gia` VARCHAR(255), `soluong` INT, `idLoai` INT, OUT `kq` INT)   BEGIN
    DECLARE soSanpham INT;
    DECLARE soLoai INT;

    SELECT COUNT(*) INTO soSanpham FROM sanpham WHERE idSanpham = idSanpham;

    IF soSanpham > 0 THEN

        SELECT COUNT(*) INTO soLoai FROM loai WHERE idLoai = idLoai;

        IF soLoai > 0 THEN
            UPDATE sanpham
            SET
                name = tenSanpham,
                mota = mota,
                gia = gia,
                soluong = soluong,
                idLoai = idLoai
            WHERE
                idSanpham = idSanpham;

            SET kq = 1; -- Success
        ELSE
            SET kq = 0; -- Category not found
        END IF;
    ELSE
        SET kq = -1; -- Product not found
    END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `taoDathang` (`idSanpham` INT, `sdt` CHAR(64), `diachi` CHAR(64), `soluong` INT, `idNguoidung` INT, OUT `kq` INT)   BEGIN
    DECLARE soSanpham INT;
    DECLARE soDathang INT;
    DECLARE soluongdat INT;
    DECLARE gia INT;

    SELECT c.soluong, c.gia INTO soSanpham, gia FROM sanpham c WHERE c.idSanpham = idSanpham;

    IF soSanpham is not null THEN
        SELECT sum(o.soluong) INTO soDathang FROM dathang o WHERE o.idSanpham = idSanpham;

        IF soSanpham < (soDathang + soluong) THEN
            SET kq = -1;
        ELSE
            SET soluongdat = soluong * gia;
            INSERT INTO dathang(soluongdat, sdt, diachi, idSanpham, soluong, idNguoidung) VALUES (soluongdat, sdt, diachi, idSanpham, soluong, idNguoidung);
            SET kq = 1;
        END IF;
    ELSE
        SET kq = 0;
    END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `taoSanpham` (`tenSanpham` VARCHAR(255), `mota` VARCHAR(255), `gia` VARCHAR(255), `soluong` INT, `idLoai` INT, OUT `kq` INT)   BEGIN
    DECLARE soLoai INT;
    
    SELECT COUNT(*) INTO soLoai FROM loai WHERE idLoai = idLoai;
    
    IF soLoai > 0 THEN
        INSERT INTO sanpham(name, mota, gia, soluong, idLoai) 
        VALUES (tenSanpham, mota, gia, soluong, idLoai);
        
        SET kq = 1; -- Success
    ELSE
        SET kq = 0; -- Category not found
    END IF;
END$$

--
-- Các hàm
--
CREATE DEFINER=`root`@`localhost` FUNCTION `Xacthuc` (`sdt` CHAR(10), `password` VARCHAR(64)) RETURNS INT(11)  begin
		
		declare nguoidungExist int;
		select u.idNguoidung into nguoidungExist from nguoidung u where u.sdt = sdt and u.password = password;
		if nguoidungExist is null then
			return -1;
		else
			return nguoidungExist;
		end if;
	end$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `dathang`
--

CREATE TABLE `dathang` (
  `idDathang` int(11) NOT NULL,
  `soluongdat` int(11) DEFAULT NULL,
  `date` date DEFAULT current_timestamp(),
  `sdt` char(64) DEFAULT NULL,
  `diachi` char(64) DEFAULT NULL,
  `idSanpham` int(11) DEFAULT NULL,
  `soluong` int(11) DEFAULT 1,
  `idNguoidung` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `dathang`
--

INSERT INTO `dathang` (`idDathang`, `soluongdat`, `date`, `sdt`, `diachi`, `idSanpham`, `soluong`, `idNguoidung`) VALUES
(4, 800, '2023-11-16', '012345678', 'an giang', 6, 4, 2),
(6, 800, '2023-11-16', '12345', 'soc trang', 6, 4, 4),
(7, 2800, '2023-11-17', '012345678', 'kien giang', 8, 5, 2),
(8, 1950, '2023-11-24', '12345', 'Can Tho', 15, 3, 4);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loai`
--

CREATE TABLE `loai` (
  `idLoai` int(11) NOT NULL,
  `name` char(64) DEFAULT NULL,
  `mota` char(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `loai`
--

INSERT INTO `loai` (`idLoai`, `name`, `mota`) VALUES
(1, 'Dong moi nhat', 'Dong mat tinh moi nhat tren thi truong hien nay'),
(4, 'Dong may duoc ua chong', 'dong may duoc nhieu nguoi tin dung');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nguoidung`
--

CREATE TABLE `nguoidung` (
  `idNguoidung` int(11) NOT NULL,
  `Hoten` char(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `diachi` char(128) DEFAULT NULL,
  `sdt` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nguoidung`
--

INSERT INTO `nguoidung` (`idNguoidung`, `Hoten`, `password`, `diachi`, `sdt`) VALUES
(1, 'ngoc', '12345', 'Can tho', '0123456'),
(2, 'phan dai cat', '45678', 'an gian', '012345678'),
(4, 'bui van tien', '12345', 'an giang', '12345');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham`
--

CREATE TABLE `sanpham` (
  `idSanpham` int(11) NOT NULL,
  `name` char(64) DEFAULT NULL,
  `mota` char(128) DEFAULT NULL,
  `gia` float DEFAULT NULL,
  `soluong` int(11) DEFAULT NULL,
  `idLoai` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`idSanpham`, `name`, `mota`, `gia`, `soluong`, `idLoai`) VALUES
(6, '580 vn', 'Duoc nhiwu hoc sinh , sinh vien ua chuong', 650, 26, 1),
(8, '580 vn', 'Duoc nhiwu hoc sinh , sinh vien ua chuong', 650, 26, 1),
(9, '580 vn', 'Duoc nhiwu hoc sinh , sinh vien ua chuong', 650, 26, 1),
(10, '580 vn', 'Duoc nhiwu hoc sinh , sinh vien ua chuong', 650, 26, 1),
(11, '580 vn', 'Duoc nhiwu hoc sinh , sinh vien ua chuong', 650, 26, 1),
(12, '580 vn', 'Duoc nhiwu hoc sinh , sinh vien ua chuong', 650, 26, 1),
(13, '580 vn', 'Duoc nhiwu hoc sinh , sinh vien ua chuong', 650, 26, 1),
(14, '580 vn', 'Duoc nhiwu hoc sinh , sinh vien ua chuong', 650, 26, 1),
(15, '580 vn', 'Duoc nhiwu hoc sinh , sinh vien ua chuong', 650, 26, 1);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `dathang`
--
ALTER TABLE `dathang`
  ADD PRIMARY KEY (`idDathang`),
  ADD KEY `idSanpham` (`idSanpham`),
  ADD KEY `idNguoidung` (`idNguoidung`);

--
-- Chỉ mục cho bảng `loai`
--
ALTER TABLE `loai`
  ADD PRIMARY KEY (`idLoai`);

--
-- Chỉ mục cho bảng `nguoidung`
--
ALTER TABLE `nguoidung`
  ADD PRIMARY KEY (`idNguoidung`);

--
-- Chỉ mục cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`idSanpham`),
  ADD KEY `idLoai` (`idLoai`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `dathang`
--
ALTER TABLE `dathang`
  MODIFY `idDathang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `loai`
--
ALTER TABLE `loai`
  MODIFY `idLoai` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `nguoidung`
--
ALTER TABLE `nguoidung`
  MODIFY `idNguoidung` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  MODIFY `idSanpham` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `dathang`
--
ALTER TABLE `dathang`
  ADD CONSTRAINT `dathang_ibfk_1` FOREIGN KEY (`idSanpham`) REFERENCES `sanpham` (`idSanpham`),
  ADD CONSTRAINT `dathang_ibfk_2` FOREIGN KEY (`idNguoidung`) REFERENCES `nguoidung` (`idNguoidung`);

--
-- Các ràng buộc cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD CONSTRAINT `sanpham_ibfk_1` FOREIGN KEY (`idLoai`) REFERENCES `loai` (`idLoai`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
