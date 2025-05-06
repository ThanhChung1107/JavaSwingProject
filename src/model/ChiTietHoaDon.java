package model;

public class ChiTietHoaDon {
	private String maHD;
    private String maSach;
    private Sach sach;
    private int soLuong;
    private double donGia;
    private double thanhTien;
	public ChiTietHoaDon() {
		super();
	}
	public ChiTietHoaDon(String maHD, String maSach, Sach sach, int soLuong, double donGia, double thanhTien) {
		super();
		this.maHD = maHD;
		this.maSach = maSach;
		this.sach = sach;
		this.soLuong = soLuong;
		this.donGia = donGia;
		this.thanhTien = thanhTien;
	}
	public String getMaHD() {
		return maHD;
	}
	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}
	public String getMaSach() {
		return maSach;
	}
	public void setMaSach(String maSach) {
		this.maSach = maSach;
	}
	public Sach getSach() {
		return sach;
	}
	public void setSach(Sach sach) {
		this.sach = sach;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public double getDonGia() {
		return donGia;
	}
	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}
	public double getThanhTien() {
		return thanhTien;
	}
	public void setThanhTien(double thanhTien) {
		this.thanhTien = thanhTien;
	}
    
}
