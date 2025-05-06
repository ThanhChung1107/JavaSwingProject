package DAO;

import model.KhachHang;
import util.ConnectDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {
    private Connection connection;

    public KhachHangDAO() {
        this.connection = ConnectDB.getConnection();
    }
    public String generateMaKH() throws SQLException {
        String sql = "SELECT MaKH FROM KHACHHANG ORDER BY MaKH DESC LIMIT 1";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        String maMoi = "KH001";
        if (rs.next()) {
            String maCu = rs.getString("MaKH");
            int so = Integer.parseInt(maCu.substring(2));
            so++;
            maMoi = String.format("KH%03d", so);
        }
        return maMoi;
    }
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KHACHHANG";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("MaKH"),
                    rs.getString("TenKH"),
                    rs.getString("DiaChi"),
                    rs.getString("SoDienThoai"),
                    rs.getString("Email"),
                    rs.getDate("NgaySinh")
                );
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
 // Trong CustomerDAO
    public static KhachHang findCustomerByPhone(String phone) throws SQLException {
        String sql = "SELECT * FROM KHACHHANG WHERE SoDienThoai = ?";
        Connection connection = ConnectDB.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setDiaChi(rs.getString("DiaChi"));
                kh.setEmail(rs.getString("Email"));
                return kh;
            }
        }
        return null;
    }
    public static boolean addcusFromOrder(KhachHang kh) {
    	String sql = "INSERT INTO KHACHHANG (MaKH, TenKH, SoDienThoai,DiaChi) VALUES (?, ?, ?,?)";
    	Connection connection = ConnectDB.getConnection();
    	try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kh.getMaKH());
            stmt.setString(2, kh.getTenKH());
            stmt.setString(3, kh.getSoDienThoai());
            stmt.setString(4, kh.getDiaChi());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KHACHHANG (MaKH, TenKH, DiaChi, SoDienThoai, Email, NgaySinh) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kh.getMaKH());
            stmt.setString(2, kh.getTenKH());
            stmt.setString(3, kh.getDiaChi());
            stmt.setString(4, kh.getSoDienThoai());
            stmt.setString(5, kh.getEmail());
            stmt.setDate(6, kh.getNgaySinh());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaKhachHang(KhachHang kh) {
        String sql = "UPDATE KHACHHANG SET TenKH=?, DiaChi=?, SoDienThoai=?, Email=?, NgaySinh=? WHERE MaKH=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kh.getTenKH());
            stmt.setString(2, kh.getDiaChi());
            stmt.setString(3, kh.getSoDienThoai());
            stmt.setString(4, kh.getEmail());
            stmt.setDate(5, kh.getNgaySinh());
            stmt.setString(6, kh.getMaKH());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhachHang(String maKH) {
        String sql = "DELETE FROM KHACHHANG WHERE MaKH=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, maKH);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<KhachHang> timKiemKhachHang(String keyword) {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KHACHHANG WHERE MaKH LIKE ? OR TenKH LIKE ? OR SoDienThoai LIKE ? OR Email LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("MaKH"),
                    rs.getString("TenKH"),
                    rs.getString("DiaChi"),
                    rs.getString("SoDienThoai"),
                    rs.getString("Email"),
                    rs.getDate("NgaySinh")
                );
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}