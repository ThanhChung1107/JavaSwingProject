package model;

import java.sql.Date;

public class Sach {
	private String maSach;
	private String tenSach;
	private TheLoai maTheLoai;
	private TacGia maTacGia;
	private NhaXuatBan maNxb;
	private int soluong;
	private double giaban;
	private Date ngayXB;
	private String imgpath;
	public Sach() {

	}
	public Sach(String maSach, String tenSach, TheLoai maTheLoai, TacGia maTacGia, NhaXuatBan maNxb, int soluong,
			double giaban, Date ngayXB, String imgpath) {
		super();
		this.maSach = maSach;
		this.tenSach = tenSach;
		this.maTheLoai = maTheLoai;
		this.maTacGia = maTacGia;
		this.maNxb = maNxb;
		this.soluong = soluong;
		this.giaban = giaban;
		this.ngayXB = ngayXB;
		this.imgpath = imgpath;
	}
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
	public TheLoai getMaTheLoai() {
		return maTheLoai;
	}
	public void setMaTheLoai(TheLoai maTheLoai) {
		this.maTheLoai = maTheLoai;
	}
	public TacGia getMaTacGia() {
		return maTacGia;
	}
	public void setMaTacGia(TacGia maTacGia) {
		this.maTacGia = maTacGia;
	}
	public NhaXuatBan getMaNxb() {
		return maNxb;
	}
	public void setMaNxb(NhaXuatBan maNxb) {
		this.maNxb = maNxb;
	}
	public int getSoluong() {
		return soluong;
	}
	public void setSoluong(int soluong) {
		this.soluong = soluong;
	}
	public double getGiaban() {
		return giaban;
	}
	public void setGiaban(double giaban) {
		this.giaban = giaban;
	}
	public Date getNgayXB() {
		return ngayXB;
	}
	public void setNgayXB(Date ngayXB) {
		this.ngayXB = ngayXB;
	}
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	
}
