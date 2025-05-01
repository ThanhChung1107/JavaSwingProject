package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Account;
import model.NhanVien;
import model.TacGia;
import util.ConnectDB;

public class AccountDAO {
	public static AccountDAO getInstance() {
		return new AccountDAO();
	}
	public List<Account> getAll() throws SQLException{
		List<Account> listacc = new ArrayList<Account>();
		String sql = "select a.*, n.TenNV" + 
					" from ACCOUNT a" + 
					" LEFT JOIN NHANVIEN n ON a.MaNV = n.MaNV";
		try (Connection con = ConnectDB.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	            while (rs.next()) {
	                Account acc = new Account();
	                acc.setUsername(rs.getString("Username"));
	                acc.setPassword(rs.getString("Password"));
	                acc.setRole(rs.getString("Role"));
	                NhanVien nv = new NhanVien();
		            nv.setMaNV(rs.getString("MaNV"));
		            nv.setTenNV(rs.getString("TenNV"));
	                acc.setNhanvien(nv); // Có thể null nếu không gán mã nhân viên

	                listacc.add(acc);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return listacc;
	}
}
