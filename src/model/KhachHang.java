package model;

import java.sql.Date;

public class KhachHang {
	private String maKH;
	private String tenKH;
	private String diaChi;
	private String SDT;
	private String email;
	private Date NS;
	public KhachHang() {
	}
	public KhachHang(String maKH, String tenKH, String diaChi, String sDT, String email, Date nS) {
		this.maKH = maKH;
		this.tenKH = tenKH;
		this.diaChi = diaChi;
		SDT = sDT;
		this.email = email;
		NS = nS;
	}
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
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public String getSDT() {
		return SDT;
	}
	public void setSDT(String sDT) {
		SDT = sDT;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getNS() {
		return NS;
	}
	public void setNS(Date nS) {
		NS = nS;
	}
	
	
}
