package model;

import java.util.Date;

public class ThongKeDoanhThu {
    private Date ngay;
    private int soLuongHoaDon;
    private double tongDoanhThu;
    private double loiNhuan;
    
    public ThongKeDoanhThu(Date ngay, int soLuongHoaDon, double tongDoanhThu, double loiNhuan) {
        this.ngay = ngay;
        this.soLuongHoaDon = soLuongHoaDon;
        this.tongDoanhThu = tongDoanhThu;
        this.loiNhuan = loiNhuan;
    }
    
    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    public int getSoLuongHoaDon() {
        return soLuongHoaDon;
    }

    public void setSoLuongHoaDon(int soLuongHoaDon) {
        this.soLuongHoaDon = soLuongHoaDon;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(double tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }

    public double getLoiNhuan() {
        return loiNhuan;
    }

    public void setLoiNhuan(double loiNhuan) {
        this.loiNhuan = loiNhuan;
    }
}