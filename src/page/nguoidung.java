package page;

public class nguoidung {
    private int idNguoidung;
    private String Hoten;
    private String sdt;
    private String password;
    private String diachi;

    public nguoidung() {
        this.Hoten = new String();
        this.sdt = new String();
        this.diachi = new String();
        this.password = new String();
    }

    public nguoidung(String Hoten, String sdt, String diachi, String password) {
        this.Hoten = new String(Hoten);
        this.sdt = new String(sdt);
        this.diachi = new String(diachi);
        this.password = new String(password);

    }

    public String getdiachi() {
        return diachi;
    }

    public String getHoten() {
        return Hoten;
    }

    public String getPassword() {
        return password;
    }

    public String getsdt() {
        return sdt;
    }

    public String toString() {
        return "[Hoten: " + Hoten + ", sdt: " + sdt + ", diachi: " + diachi + ", password" + password
                + " ]";
    }

}
