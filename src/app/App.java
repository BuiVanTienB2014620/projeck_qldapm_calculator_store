package app;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Scanner;

import page.loai;
import page.nguoidung;
import page.sanpham;
import sql.MySQLConnect;

public class App {
    static int userIdLogin;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection conn = null;
        conn = MySQLConnect.getConnection();

        try {
            System.out.println();
            System.out.println("----------------------Welcome to shop SuperSports!----------------");
            int chosse = 0;
            do {
                switch (chosse) {
                    case 1:
                        handleLogin(conn);
                        showMenu();

                        break;
                    case 2:
                        handleRegister(conn);
                        showMenu();

                        break;
                    default:
                        showMenu();
                        break;
                }
                chosse = sc.nextInt();
            } while (chosse != 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            sc.close();
        }
    }

    public static void handleLogin(Connection conn) {
        CallableStatement cStmt = null;
        Scanner sc = new Scanner(System.in);
        // kiem tra
        System.out.println("----------------------Dang nhap vao tai khoan----------------");
        System.out.print("Xin moi nhap so dien thoai: ");
        String sdt = sc.nextLine();

        System.out.print("Xin moi nhap mat khau: ");
        String password = sc.nextLine();

        try {

            cStmt = conn.prepareCall("{? = call Xacthuc(?, ?)}");
            cStmt.setString(2, sdt);
            cStmt.setString(3, password);
            cStmt.registerOutParameter(1, Types.INTEGER);
            cStmt.execute();
            int balance = cStmt.getInt(1);
            if (balance != -1) {
                if (balance == 1) {
                    System.out.println();
                    System.out.println("Ban la quan tri vien shop SuperSports id = " + balance);
                    homeAdminPage(conn);
                } else {
                    System.out.println();
                    userIdLogin = balance;
                    System.out.println("Dang nhap thanh cong. id nguoi dung = " + balance);
                    homePage(conn);
                }
                // giao dien trang chu
            } else {
                System.out.println("SDT và mat khau sai!");
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void handleRegister(Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------Can nhap thong tin--------------------");
        System.out.print("Ho ten: ");
        String Hoten = sc.nextLine();

        System.out.print("password: ");
        String password = sc.nextLine();

        System.out.print("sdt: ");
        String sdt = sc.nextLine();

        System.out.print("Dia chi: ");
        String diachi = sc.nextLine();

        nguoidung nguoidung = new nguoidung(Hoten, sdt, diachi, password);
        System.out.println(nguoidung.toString());
        try {

            PreparedStatement pStmt = null;

            pStmt = conn.prepareStatement("insert into nguoidung(Hoten, password, diachi, sdt) values(?,?,?,?);");
            pStmt.setString(1, nguoidung.getHoten());
            pStmt.setString(2, nguoidung.getPassword());
            pStmt.setString(3, nguoidung.getdiachi());
            pStmt.setString(4, nguoidung.getsdt());
            pStmt.executeUpdate();

            System.out.println("Tai khoan dang ky thanh cong!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void showMenu() {
        System.out.println("1. Dang nhap");
        System.out.println("2. Dang ky ");
        System.out.println("0. Thoat");

    }

    public static void homeAdminPage(Connection conn) {
        System.out.println("----------------------Trang quan tri vien--------------------");
        Scanner sc = new Scanner(System.in);
        int chosse = 0;
        do {
            switch (chosse) {
                case 1:
                    showMenuAdmin();
                    handleViewProduct(conn);
                    showMenuAdmin();
                    break;
                case 2:
                    showMenuAdmin();
                    handleCreateProduct(conn);
                    showMenuAdmin();

                    break;

                case 3:
                    showMenuAdmin();
                    handleDeleteProduct(conn);
                    showMenuAdmin();

                    break;
                case 4:
                    showMenuAdmin();
                    handleViewCategory(conn);
                    showMenuAdmin();
                    break;

                case 5:
                    handleCreateCategory(conn);
                    showMenuAdmin();
                    break;

                case 6:
                    showMenuAdmin();
                    handleViewOrder(conn, 1);
                    showMenuAdmin();
                    break;
                case 7:
                    showMenuAdmin();
                    handleUpdateProduct(conn);
                    showMenuAdmin();
                    break;

                default:
                    showMenuAdmin();
                    break;
            }
            System.out.print("Chon mot so de sang trang: ");
            chosse = sc.nextInt();
        } while (chosse != 0);
    }

    public static void homePage(Connection conn) {
        int chosse = 0;
        do {
            System.out.println("Trang chu!");
            Scanner sc = new Scanner(System.in);
            switch (chosse) {
                case 1:
                    showMenuHome();
                    handleViewProduct(conn);
                    showMenuHome();
                    break;
                case 2:
                    showMenuHome();
                    handleCreateOrder(conn);
                    showMenuHome();

                    break;

                case 3:
                    showMenuHome();
                    handleViewOrder(conn, 1);
                    showMenuHome();

                    break;
                default:
                    showMenuHome();
                    break;
            }
            System.out.print("chon mot so de sang trang: ");
            chosse = sc.nextInt();
        } while (chosse != 0);

    }

    public static void showMenuHome() {
        System.out.println("1. Danh sach san pham");
        System.out.println("2. Dat hang");
        System.out.println("3. Xem danh sach dat hang");

        System.out.println("0. Dang xuat");
    }

    public static void showMenuAdmin() {
        System.out.println("1. Danh sach san pham");
        System.out.println("2. Them san pham");
        System.out.println("3. Xoa san pham");
        System.out.println("4. Xem danh muc");
        System.out.println("5. Them danh muc");
        System.out.println("6. Xem danh sach dat hang");
        System.out.println("7. Chinh sua san pham");

        System.out.println("0. Dang xuat");
    }

    public static void handleCreateOrder(Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.println("------------------- Nhap thong tin dat hang --------------------");
        System.out.print("Id san pham: ");
        String idSanpham = sc.nextLine();

        System.out.print("So luong: ");
        int soluong = sc.nextInt();
        sc.nextLine();

        System.out.print("SDT khach hang: ");
        String sdt = sc.nextLine();

        System.out.print("Dia chi nhan: ");
        String diachi = sc.nextLine();

        try {
            CallableStatement cStmt = null;
            cStmt = conn.prepareCall("{call taoDathang(?, ?, ?, ?, ?, ?)}");

            cStmt.setString(1, idSanpham);
            cStmt.setString(2, sdt);
            cStmt.setString(3, diachi);
            cStmt.setInt(4, soluong);
            cStmt.setInt(5, userIdLogin);

            cStmt.registerOutParameter(6, Types.INTEGER);
            cStmt.executeUpdate();

            int result = cStmt.getInt(6);
            if (result == 1) {
                System.out.println("Dat hang thanh cong!");
            } else if (result == 0) {
                System.out.println("Khong tim thay san pham!");
            } else {
                System.out.println("so luong vuot qua  trong kho!");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void handleViewCategory(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = MySQLConnect.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM loai");

            System.out.println("-----------------------Danh sach danh muc:----------------");
            System.out.println("Id" + "\t " + "Ten danh muc" + "\t " + "\t " + "Mo ta");

            while (rs.next()) {
                String name = rs.getString("name"); // resultset trả về gồm 2 trường là
                String mota = rs.getString("mota");
                String id = rs.getString("idLoai"); // MSSV và họ tên
                // MSSV và họ tên
                System.out.println(id + "\t " + name + "\t " + mota);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void handleViewProduct(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = MySQLConnect.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(
                    "SELECT p.idSanpham, p.name, p.mota, p.gia, p.soluong, c.name nameLoai FROM sanpham p left join loai c on p.idLoai = c.idLoai");

            System.out.println(
                    "---------------------------------------------- Danh sach san pham ------------------------------------------------------------------------------------");
            System.out.println(
                    "------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.format("%-3s %-30s %-55s %-22s %-18s  %-18s\n", "Id", "Ten san pham", "Mo ta", "Gia",
                    "So luong", "Danh muc");

            while (rs.next()) {
                String name = rs.getString("name");
                String mota = rs.getString("mota");
                String gia = rs.getString("gia");
                String nameLoai = rs.getString("nameLoai");
                Integer soluong = rs.getInt("soluong");
                String id = rs.getString("idSanpham");
                System.out.format("%-3s %-23s %-60s %-25s %-18d  %-18s\n", id, name, mota, gia, soluong,
                        nameLoai);
            }
            System.out.println(
                    "------------------------------------------------------------------------------------------------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void handleViewOrder(Connection conn, int isNguoidung) {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            String sql;
            if (isNguoidung == 1) {
                sql = "SELECT *, p.name tenSanpham  FROM dathang,sanpham p WHERE idNguoidung = ? and p.idSanpham = dathang.idSanpham; ";
                pStmt = conn.prepareStatement(sql);

                System.out.println(
                        "userIdLogin ----------------------------------------------------------------" + userIdLogin);
                pStmt.setInt(1, userIdLogin);
                rs = pStmt.executeQuery();
                System.out.println(
                        "----------------------------------------------------------------------------------------------------------------------------------");
                System.out.format("%-3s %-18s %-18s %-18s %-18s %-18s %-18s %-18s\n", "Id", "Ten san pham", "So luong",
                        "Don gia",
                        "SDT cua ban", "Dia chi",
                        "Ngay dat", "Tong gia");
                while (rs.next()) {
                    int id = rs.getInt("idDathang");
                    int total = rs.getInt("soluongdat");
                    Date date = rs.getDate("date");
                    String sdt = rs.getString("sdt");
                    String diachi = rs.getString("diachi");
                    String tenSanpham = rs.getString("tenSanpham");
                    int soluong = rs.getInt("soluong");
                    int gia = rs.getInt("gia");

                    System.out.format("%-3s %-18s %-18d %-18s %-18s %-18s %-18s %-18d\n", id, tenSanpham, soluong, gia,
                            sdt,
                            diachi,
                            date.toString(), total);
                }
            } else {
                sql = "SELECT *, nguoidung.Hoten userName, p.name tenSanpham  FROM dathang,nguoidung, sanpham p \n" + //
                        "WHERE nguoidung.idNguoidung = dathang.idNguoidung and p.idSanpham = dathang.idSanpham;";
                pStmt = conn.prepareStatement(sql);

                rs = pStmt.executeQuery();
                System.out.println(
                        "----------------------------------------------------------------------------------------------------------------------------------");
                System.out.format("%-3s %-18s %-25s %-18s %-18s %-18s %-18s %-18s\n", "Id", "Ten san pham", "So luong",
                        "SDT :", "Dia chi",
                        "Ngay dat:", "Khach hang :", "Tong tien :");
                while (rs.next()) {

                    int id = rs.getInt("idDathang");
                    int total = rs.getInt("soluongdat");
                    Date date = rs.getDate("date");
                    String sdt = rs.getString("sdt");
                    String diachi = rs.getString("diachi");
                    String tenSanpham = rs.getString("tenSanpham");
                    int userName = rs.getInt("userName");
                    int soluong = rs.getInt("soluong");

                    System.out.format("%-3s %-18s %-25d %-18s %-18s %-18s %-18d %-18d\n", id, tenSanpham, soluong,
                            sdt,
                            diachi,
                            date.toString(), userName, total);
                }
            }

            System.out.println(
                    "----------------------------------------------------------------------------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void handleCreateCategory(Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.println("------------------- Nhap thong tin danh muc--------------------");
        System.out.print("Ten danh muc: ");
        String name = sc.nextLine();

        System.out.print("Mo ta: ");
        String mota = sc.nextLine();

        loai loai = new loai(name, mota);
        System.out.println(loai.toString());
        try {
            PreparedStatement pStmt = null;
            pStmt = conn.prepareStatement("insert into loai(name, mota) values(?,?);");
            pStmt.setString(1, loai.getName());
            pStmt.setString(2, loai.getmota());
            pStmt.executeUpdate();

            System.out.println("Tao danh muc thanh cong!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void handleDeleteProduct(Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------Nhap id san pham muon xoa--------------------");
        System.out.print("idSanpham: ");
        String id = sc.nextLine();

        try {
            // Delete associated records in the "dathang" table
            PreparedStatement deleteOrderStmt = conn.prepareStatement("DELETE FROM dathang WHERE idSanpham = ?");
            deleteOrderStmt.setString(1, id);
            deleteOrderStmt.executeUpdate();

            // Delete the product from the "sanpham" table
            PreparedStatement deleteProductStmt = conn.prepareStatement("DELETE FROM sanpham WHERE idSanpham = ?");
            deleteProductStmt.setString(1, id);
            deleteProductStmt.executeUpdate();

            System.out.println("Xoa san pham thanh cong!");
            System.out.println();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void handleCreateProduct(Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.println("------------------- Nhap thong tin san pham-------------------");
        System.out.print("Ten san pham: ");
        String name = sc.nextLine();

        System.out.print("Mo ta: ");
        String mota = sc.nextLine();

        System.out.print("Gia: ");
        String gia = sc.nextLine();

        System.out.print("So luong: ");
        Integer soluong = sc.nextInt();

        System.out.println("Danh muc: ");
        handleViewCategory(conn);
        System.out.print("Lua chon: ");
        int idLoai = sc.nextInt();

        // Create sanpham object with the correct constructor
        sanpham sanpham = new sanpham(name, mota, gia, soluong, idLoai);

        System.out.println(sanpham.toString());
        try {
            CallableStatement cStmt = null;
            cStmt = conn.prepareCall("{call taoSanpham(?, ?, ?, ?, ?, ?)}");

            cStmt.setString(1, sanpham.getName());
            cStmt.setString(2, sanpham.getmota());
            cStmt.setString(3, sanpham.getgia());
            cStmt.setInt(4, sanpham.getsoluong());
            cStmt.setInt(5, sanpham.getiIdLoai());
            cStmt.registerOutParameter(6, Types.INTEGER);
            cStmt.executeUpdate();

            int result = cStmt.getInt(6);
            if (result == 1) {
                System.out.println("Tao san pham thanh cong !");
            } else {
                System.out.println("Khong tim thay san pham hoac co loi khi tao san pham!");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void handleUpdateProduct(Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------Nhap id san pham muon sua--------------------");
        System.out.print("idSanpham: ");
        String id = sc.nextLine();

        // Hiển thị thông tin sản phẩm hiện tại để người dùng có thể chỉnh sửa
        System.out.println("Thông tin sản phẩm hiện tại:");
        handleViewProduct(conn);

        System.out.println("-------------------Nhap thong tin moi cho san pham-------------------");
        System.out.print("Ten san pham: ");
        String name = sc.nextLine();

        System.out.print("Mo ta: ");
        String mota = sc.nextLine();

        System.out.print("Gia: ");
        String gia = sc.nextLine();

        System.out.print("So luong: ");
        Integer soluong = sc.nextInt();

        System.out.println("Danh muc: ");
        handleViewCategory(conn);
        System.out.print("Lua chon danh muc moi: ");
        int idLoai = sc.nextInt();

        try {
            CallableStatement cStmt = null;
            cStmt = conn.prepareCall("{call suaSanpham(?, ?, ?, ?, ?, ?, ?)}");

            cStmt.setString(1, id);
            cStmt.setString(2, name);
            cStmt.setString(3, mota);
            cStmt.setString(4, gia);
            cStmt.setInt(5, soluong);
            cStmt.setInt(6, idLoai);
            cStmt.registerOutParameter(7, Types.INTEGER);
            cStmt.executeUpdate();

            int result = cStmt.getInt(7);
            if (result == 1) {
                System.out.println("Cap nhat san pham thanh cong!");
            } else if (result == 0) {
                System.out.println("Khong tim thay san pham hoac danh muc!");
            } else {
                System.out.println("Loi khi cap nhat san pham!");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
