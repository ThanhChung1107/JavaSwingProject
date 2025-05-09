package model;

public class Account {
	private String username;
	private String password;
	private String role;
	private NhanVien nhanvien;
	public Account() {

	}
	public Account(String username, String password, String role, NhanVien nhanvien) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.nhanvien = nhanvien;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public NhanVien getNhanvien() {
		return nhanvien;
	}
	public void setNhanvien(NhanVien nhanvien) {
		this.nhanvien = nhanvien;
	}
	
}
