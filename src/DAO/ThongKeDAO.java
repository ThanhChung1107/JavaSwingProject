package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.ThongKeDoanhThu;
import model.ThongKeKhachHang;
import model.ThongKeSanPham;
import util.ConnectDB;

public class ThongKeDAO {
	public static ThongKeDAO getInstance() {
		return new ThongKeDAO();
	}
    
    // 1. Thống kê doanh thu theo ngày
    public List<ThongKeDoanhThu> thongKeDoanhThu(Date fromDate, Date toDate) throws SQLException {
        List<ThongKeDoanhThu> result = new ArrayList<>();
        Connection connection = ConnectDB.getConnection();
        String sql = "SELECT DATE(hd.NgayLap) as Ngay, " +
                     "COUNT(hd.MaHD) as SoLuongHoaDon, " +
                     "SUM(hd.ThanhTien) as TongDoanhThu " +
                     "FROM HOADON hd " +
                     "JOIN CHITIETHOADON ct ON hd.MaHD = ct.MaHD " +
                     "JOIN SACH s ON ct.MaSach = s.MaSach " +
                     "WHERE hd.NgayLap BETWEEN ? AND ? " +
                     "GROUP BY DATE(hd.NgayLap) " +
                     "ORDER BY Ngay DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(fromDate.getTime()));
            stmt.setDate(2, new java.sql.Date(toDate.getTime()));
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ThongKeDoanhThu tk = new ThongKeDoanhThu(
                    rs.getDate("Ngay"),
                    rs.getInt("SoLuongHoaDon"),
                    rs.getDouble("TongDoanhThu"), 0
                );
                result.add(tk);
            }
            connection.close();
        }
        return result;
    }
    public int OrderSum() {
        int tongHoaDon = 0;
        String sql = "SELECT COUNT(MaHD) AS TongSoHoaDon FROM HOADON";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                tongHoaDon = rs.getInt("TongSoHoaDon");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tongHoaDon;
    }
    public double RevueSum() {
        double tongDoanhThu = 0;
        String sql = "SELECT SUM(ThanhTien) AS TongDoanhThu FROM HOADON";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                tongDoanhThu = rs.getDouble("TongDoanhThu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tongDoanhThu;
    }

    
    // 2. Thống kê sản phẩm bán chạy
    public List<ThongKeSanPham> thongKeSanPham(Date fromDate, Date toDate, int limit) throws SQLException {
        List<ThongKeSanPham> result = new ArrayList<>();
        Connection connection = ConnectDB.getConnection();
        String sql = "SELECT s.MaSach, s.TenSach, " +
                     "SUM(ct.SoLuong) as SoLuongBan, " +
                     "SUM(ct.ThanhTien) as DoanhThu " +
                     "FROM CHITIETHOADON ct " +
                     "JOIN HOADON hd ON ct.MaHD = hd.MaHD " +
                     "JOIN SACH s ON ct.MaSach = s.MaSach " +
                     "WHERE hd.NgayLap BETWEEN ? AND ? " +
                     "GROUP BY s.MaSach, s.TenSach " +
                     "ORDER BY SoLuongBan DESC " +
                     "LIMIT ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(fromDate.getTime()));
            stmt.setDate(2, new java.sql.Date(toDate.getTime()));
            stmt.setInt(3, limit);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ThongKeSanPham tk = new ThongKeSanPham(
                    rs.getString("MaSach"),
                    rs.getString("TenSach"),
                    rs.getInt("SoLuongBan"),
                    rs.getDouble("DoanhThu")
                );
                result.add(tk);
            }
        }
        return result;
    }
    
    public List<ThongKeKhachHang> thongKeKhachHang(Date fromDate, Date toDate, int limit) throws SQLException {
        List<ThongKeKhachHang> result = new ArrayList<>();
        Connection connection = ConnectDB.getConnection();
        String sql = "SELECT kh.MaKH, kh.TenKH, " +
                     "COUNT(hd.MaHD) as SoHoaDon, " +
                     "SUM(hd.ThanhTien) as TongTienMua " +
                     "FROM HOADON hd " +
                     "JOIN KHACHHANG kh ON hd.MaKH = kh.MaKH " +
                     "WHERE hd.NgayLap BETWEEN ? AND ? " +
                     "GROUP BY kh.MaKH, kh.TenKH " +
                     "ORDER BY TongTienMua DESC " +
                     "LIMIT ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(fromDate.getTime()));
            stmt.setDate(2, new java.sql.Date(toDate.getTime()));
            stmt.setInt(3, limit);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ThongKeKhachHang tk = new ThongKeKhachHang(
                    rs.getString("MaKH"),
                    rs.getString("TenKH"),
                    rs.getInt("SoHoaDon"),
                    rs.getDouble("TongTienMua")
                );
                result.add(tk);
            }
        }
        return result;
    }
    
    
}