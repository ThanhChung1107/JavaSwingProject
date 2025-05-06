package model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class HoaDon {
	private String maHD;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private Timestamp ngayLap;
    private double tongTien;
    private double giamGia;
    private double thanhTien;
    private List<ChiTietHoaDon> chiTietHoaDon;
	public HoaDon() {
		super();
	}
	public HoaDon(String maHD, KhachHang khachHang, NhanVien nhanVien, Timestamp ngayLap, double tongTien, double giamGia,
			double thanhTien, List<ChiTietHoaDon> chiTietHoaDon) {
		super();
		this.maHD = maHD;
		this.khachHang = khachHang;
		this.nhanVien = nhanVien;
		this.ngayLap = ngayLap;
		this.tongTien = tongTien;
		this.giamGia = giamGia;
		this.thanhTien = thanhTien;
		this.chiTietHoaDon = chiTietHoaDon;
	}
	public String getMaHD() {
		return maHD;
	}
	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}
	public KhachHang getKhachHang() {
		return khachHang;
	}
	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}
	public NhanVien getNhanVien() {
		return nhanVien;
	}
	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}
	public Timestamp getNgayLap() {
		return ngayLap;
	}
	public void setNgayLap(Timestamp timestamp) {
		this.ngayLap = timestamp;
	}
	public double getTongTien() {
		return tongTien;
	}
	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}
	public double getGiamGia() {
		return giamGia;
	}
	public void setGiamGia(double giamGia) {
		this.giamGia = giamGia;
	}
	public double getThanhTien() {
		return thanhTien;
	}
	public void setThanhTien(double thanhTien) {
		this.thanhTien = thanhTien;
	}
	public List<ChiTietHoaDon> getChiTietHoaDon() {
		return chiTietHoaDon;
	}
	public void setChiTietHoaDon(List<ChiTietHoaDon> chiTietHoaDon) {
		this.chiTietHoaDon = chiTietHoaDon;
	}
    
}
