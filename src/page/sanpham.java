package page;

public class sanpham {
    private int idSanpham;
    private String name;
    private String mota;
    private String gia;
    private int soluong;
    private int idLoai;

    public sanpham(String name, String mota, String gia, int soluong, int idLoai) {
        this.name = name;
        this.mota = mota;
        this.gia = gia;
        this.soluong = soluong;

        this.idLoai = idLoai;
    }

    public sanpham() {
        this.name = new String();
        this.mota = new String();
        this.gia = new String();
        this.soluong = 0;

    }

    public String getName() {
        return name;
    }

    public Integer getsoluong() {
        return soluong;
    }

    public String getmota() {
        return mota;
    }

    public String getgia() {
        return gia;
    }

    public int getiIdLoai() {
        return idLoai;
    }

    public String toString() {
        return "[name: " + name + ", mota: " + mota + ", gia: " + gia + ", soluong: " + soluong + ", idLoai: "
                + idLoai + " ]\n";
    }

}
