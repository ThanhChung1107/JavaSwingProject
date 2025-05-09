package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.NhanVien;
import util.ConnectDB;

public class NhanVienDAO {
	private NhanVienDAO getInstance() {
		return new NhanVienDAO();
	}
	public String sinhMaNV() throws SQLException {
        String sql = "SELECT MaNV FROM NHANVIEN ORDER BY MaNV DESC LIMIT 1";
        Connection conn = ConnectDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        String maMoi = "NV001"; 
        if (rs.next()) {
            String maCu = rs.getString("MaNV"); 
            int so = Integer.parseInt(maCu.substring(2)); 
            so++;
            maMoi = String.format("NV%03d", so); 
        }
        return maMoi;
    }
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NHANVIEN";

        try (Connection conn = ConnectDB.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getString("MaNV"),
                    rs.getString("TenNV"),
                    rs.getString("DiaChi"),
                    rs.getString("SoDienThoai"),
                    rs.getString("Email"),
                    rs.getDate("NgaySinh"),
                    rs.getDouble("Luong")
                );
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(NhanVien nv) {
        String sql = "INSERT INTO NHANVIEN (MaNV, TenNV, DiaChi, SoDienThoai, Email, NgaySinh,Luong) VALUES (?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = ConnectDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nv.getMaNV());
            pstmt.setString(2, nv.getTenNV());
            pstmt.setString(3, nv.getDiaChi());
            pstmt.setString(4, nv.getSoDienThoai());
            pstmt.setString(5, nv.getEmail());
            pstmt.setDate(6, (Date) nv.getNgaySinh());
            pstmt.setDouble(7, nv.getLuong());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(NhanVien nv) {
        String sql = "UPDATE NHANVIEN SET TenNV=?, DiaChi=?, SoDienThoai=?, Email=?, NgaySinh=?, Luong=? WHERE MaNV=?";

        try (Connection conn = ConnectDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nv.getTenNV());
            pstmt.setString(2, nv.getDiaChi());
            pstmt.setString(3, nv.getSoDienThoai());
            pstmt.setString(4, nv.getEmail());
            pstmt.setDate(5, (Date) nv.getNgaySinh());
            pstmt.setDouble(6, nv.getLuong());
            pstmt.setString(7, nv.getMaNV());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maNV) {
        String sql = "DELETE FROM NHANVIEN WHERE MaNV=?";

        try (Connection conn = ConnectDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
