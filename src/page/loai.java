package page;

public class loai {
    private int idLoai;
    private String name;
    private String mota;

    public loai() {
        this.name = new String();
        this.mota = new String();
    }

    public loai(String name, String mota) {
        this.name = new String(name);
        this.mota = new String(mota);
    }

    public String getName() {
        return name;
    }

    public String getmota() {
        return mota;
    }

    public String toString() {
        return "[name: " + name + ", mota: " + mota +
                " ]";
    }

}
