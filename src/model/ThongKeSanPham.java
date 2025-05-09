package model;

public class ThongKeSanPham {
    private String maSach;
    private String tenSach;
    private int soLuongBan;
    private double doanhThu;
    
    // Constructor
    public ThongKeSanPham(String maSach, String tenSach, int soLuongBan, double doanhThu) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.soLuongBan = soLuongBan;
        this.doanhThu = doanhThu;
    }
    
    // Getters and Setters
    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public int getSoLuongBan() {
        return soLuongBan;
    }

    public void setSoLuongBan(int soLuongBan) {
        this.soLuongBan = soLuongBan;
    }

    public double getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(double doanhThu) {
        this.doanhThu = doanhThu;
    }
}