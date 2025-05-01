package model;

import java.sql.Date;

public class TacGia {
	private String maTacGia;
	private String tenTacGia;
	private String AnhTacGia;
	private Date ngaySinh;
	public TacGia() {

	}
	public TacGia(String maTacGia, String tenTacGia, String anhTacGia, Date ngaySinh) {
		super();
		this.maTacGia = maTacGia;
		this.tenTacGia = tenTacGia;
		AnhTacGia = anhTacGia;
		this.ngaySinh = ngaySinh;
	}
	public String getMaTacGia() {
		return maTacGia;
	}
	public void setMaTacGia(String maTacGia) {
		this.maTacGia = maTacGia;
	}
	public String getTenTacGia() {
		return tenTacGia;
	}
	public void setTenTacGia(String tenTacGia) {
		this.tenTacGia = tenTacGia;
	}
	public String getAnhTacGia() {
		return AnhTacGia;
	}
	public void setAnhTacGia(String anhTacGia) {
		AnhTacGia = anhTacGia;
	}
	public Date getNgaySinh() {
		return ngaySinh;
	}
	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
	}
	
	
}
