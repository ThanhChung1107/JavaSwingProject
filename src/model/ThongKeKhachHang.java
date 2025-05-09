package model;

public class ThongKeKhachHang {
    private String maKH;
    private String tenKH;
    private int soHoaDon;
    private double tongTienMua;
    
    // Constructor
    public ThongKeKhachHang(String maKH, String tenKH, int soHoaDon, double tongTienMua) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.soHoaDon = soHoaDon;
        this.tongTienMua = tongTienMua;
    }
    
    // Getters and Setters
    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public int getSoHoaDon() {
        return soHoaDon;
    }

    public void setSoHoaDon(int soHoaDon) {
        this.soHoaDon = soHoaDon;
    }

    public double getTongTienMua() {
        return tongTienMua;
    }

    public void setTongTienMua(double tongTienMua) {
        this.tongTienMua = tongTienMua;
    }
}