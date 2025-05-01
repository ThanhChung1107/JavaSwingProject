package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.TacGia;
import util.ConnectDB;

public class TacGiaDAO {
	public static List<TacGia> getAll() throws SQLException{
        List<TacGia> list = new ArrayList<>();
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM TACGIA";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TacGia tg = new TacGia();
                tg.setMaTacGia(rs.getString("MaTacGia"));
                tg.setTenTacGia(rs.getString("TenTacGia"));
                tg.setNgaySinh(rs.getDate("NgaySinh"));
                tg.setAnhTacGia(rs.getString("ImgPath"));
                list.add(tg);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
	public boolean insertAuthor(TacGia tacGia) throws SQLException {
	    String sql = "INSERT INTO TACGIA (MaTacGia, TenTacGia, NgaySinh, ImgPath) VALUES (?, ?, ?, ?)";
	    
	    try (Connection conn = ConnectDB.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, tacGia.getMaTacGia());
	        pstmt.setString(2, tacGia.getTenTacGia());
	        pstmt.setDate(3, new java.sql.Date(tacGia.getNgaySinh().getTime()));
	        pstmt.setString(4, tacGia.getAnhTacGia());
	        
	        return pstmt.executeUpdate() > 0;
	    }
	}
	public boolean updateAuthor(TacGia tacGia) throws SQLException {
	    String sql = "UPDATE TACGIA SET TenTacGia = ?, NgaySinh = ?, ImgPath = ? WHERE MaTacGia = ?";
	    
	    try (Connection conn = ConnectDB.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, tacGia.getTenTacGia());
	        pstmt.setDate(2, new java.sql.Date(tacGia.getNgaySinh().getTime()));
	        pstmt.setString(3, tacGia.getAnhTacGia());
	        pstmt.setString(4, tacGia.getMaTacGia());
	        
	        return pstmt.executeUpdate() > 0;
	    }
	}
	public boolean deleteAuthor(String maTacGia) throws SQLException {
	    String sql = "DELETE FROM TACGIA WHERE MaTacGia = ?";
	    
	    try (Connection conn = ConnectDB.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, maTacGia);
	        return pstmt.executeUpdate() > 0;
	    }
	}
}
