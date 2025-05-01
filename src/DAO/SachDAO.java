package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.NhaXuatBan;
import model.Sach;
import model.TacGia;
import model.TheLoai;
import util.ConnectDB;
public class SachDAO {
	public static SachDAO getInstance() {
		return new SachDAO();
	}
	public List<Sach> getAll() throws SQLException {
	    List<Sach> dsSach = new ArrayList<>();
	    String sql = "SELECT s.*, tl.tenTheLoai, tg.tenTacGia, nxb.tenNXB " +
	                 "FROM SACH s " +
	                 "LEFT JOIN THELOAI tl ON s.maTheLoai = tl.maTheLoai " +
	                 "LEFT JOIN TACGIA tg ON s.maTacGia = tg.maTacGia " +
	                 "LEFT JOIN NHAXUATBAN nxb ON s.maNXB = nxb.maNXB";
	    
	    try (Connection conn = ConnectDB.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        
	        while (rs.next()) {
	            Sach s = new Sach();
	            s.setMaSach(rs.getString("maSach"));
	            s.setTenSach(rs.getString("tenSach"));
	            TheLoai tl = new TheLoai();
	            tl.setMaTheLoai(rs.getString("maTheLoai"));
	            tl.setTenTheLoai(rs.getString("tenTheLoai"));
	            s.setMaTheLoai(tl);
	            
	            // Thiết lập thông tin Tác giả
	            TacGia tg = new TacGia();
	            tg.setMaTacGia(rs.getString("maTacGia"));
	            tg.setTenTacGia(rs.getString("tenTacGia"));
	            s.setMaTacGia(tg);
	            
	            // Thiết lập thông tin Nhà xuất bản
	            NhaXuatBan nxb = new NhaXuatBan();
	            nxb.setMaNXB(rs.getString("maNXB"));
	            nxb.setTenNXB(rs.getString("tenNXB"));
	            s.setMaNxb(nxb);
	            
	            s.setGiaban(rs.getInt("giaban"));
	            s.setSoluong(rs.getInt("soluongton"));
	            s.setImgpath(rs.getString("imgpath"));
	            dsSach.add(s);
	        }
	    }
	    return dsSach;
	}
	public List<Object[]> getAllSachForDisplay() throws SQLException {
	    List<Object[]> result = new ArrayList<>();
	    String sql = "SELECT s.TenSach, tg.TenTacGia, tl.TenTheLoai, s.GiaBan, s.imgpath "
	               + "FROM SACH s "
	               + "JOIN TACGIA tg ON s.MaTacGia = tg.MaTacGia "
	               + "JOIN THELOAI tl ON s.MaTheLoai = tl.MaTheLoai";
	    
	    try (Connection conn = ConnectDB.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        
	        while (rs.next()) {
	            Object[] book = new Object[5]; // Tăng lên 5 phần tử
	            book[0] = rs.getString("TenSach");
	            book[1] = rs.getString("TenTacGia");
	            book[2] = rs.getString("TenTheLoai");
	            book[3] = formatCurrency(rs.getDouble("GiaBan"));
	            book[4] = rs.getString("ImgPath"); // Lấy đường dẫn ảnh
	            result.add(book);
	        }
	    }
	    return result;
	}

	private String formatCurrency(double amount) {
	    return String.format("%,.0fđ", amount);
	}
	public boolean insert(Sach sach) {
		try {
			Connection con = ConnectDB.getConnection();
			String sql = "INSERT INTO SACH (MaSach, TenSach, MaTheLoai, MaTacGia, MaNXB, SoLuongTon, GiaBan, NgayXuatBan,imgPath) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, sach.getMaSach());
            ps.setString(2, sach.getTenSach());
            ps.setString(3, sach.getMaTheLoai().getMaTheLoai());
            ps.setString(4, sach.getMaTacGia().getMaTacGia());
            ps.setString(5, sach.getMaNxb().getMaNXB());
            ps.setInt(6, sach.getSoluong());
            ps.setDouble(7, sach.getGiaban());
            ps.setDate(8, sach.getNgayXB());
            ps.setString(9, sach.getImgpath());
            return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean updateSach(Sach sach) {
	    String sql = "UPDATE SACH SET TenSach=?, MaTheLoai=?, MaTacGia=?, MaNXB=?, SoLuongTon=?, GiaBan=?, imgpath=? WHERE MaSach=?";
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        
	        ps.setString(1, sach.getTenSach());
	        ps.setString(2, sach.getMaTheLoai().getMaTheLoai());
	        ps.setString(3, sach.getMaTacGia().getMaTacGia());
	        ps.setString(4, sach.getMaNxb().getMaNXB());
	        ps.setInt(5, sach.getSoluong());
	        ps.setDouble(6, sach.getGiaban());
	        ps.setString(7, sach.getImgpath());
	        ps.setString(8, sach.getMaSach());

	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean deleteSach(String masach) {
		try {
			Connection con = ConnectDB.getConnection();
			String sql = "delete from SACH where masach=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, masach);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
