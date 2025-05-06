package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.HoaDon;
import model.ChiTietHoaDon;
import model.KhachHang;
import model.NhanVien;
import model.Sach;
import util.ConnectDB;

public class HoaDonDAO {
    public static HoaDonDAO getInstance() {
		return new HoaDonDAO();
	}
    // 1. Lấy danh sách hóa đơn
    public List<HoaDon> getAllHoaDon() throws SQLException {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        Connection connection = ConnectDB.getConnection();
        String sql = "SELECT hd.*, kh.TenKH, nv.TenNV FROM HOADON hd " +
                     "LEFT JOIN KHACHHANG kh ON hd.MaKH = kh.MaKH " +
                     "LEFT JOIN NHANVIEN nv ON hd.MaNV = nv.MaNV " +
                     "ORDER BY hd.NgayLap DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                HoaDon hoaDon = new HoaDon();
                hoaDon.setMaHD(rs.getString("MaHD"));
                hoaDon.setNgayLap(rs.getTimestamp("NgayLap"));
                hoaDon.setTongTien(rs.getDouble("TongTien"));
                hoaDon.setGiamGia(rs.getDouble("GiamGia"));
                hoaDon.setThanhTien(rs.getDouble("ThanhTien"));
                
                // Thông tin khách hàng
                if (rs.getString("MaKH") != null) {
                    KhachHang khachHang = new KhachHang();
                    khachHang.setMaKH(rs.getString("MaKH"));
                    khachHang.setTenKH(rs.getString("TenKH"));
                    hoaDon.setKhachHang(khachHang);
                }
                
                // Thông tin nhân viên
                NhanVien nhanVien = new NhanVien();
                nhanVien.setMaNV(rs.getString("MaNV"));
                nhanVien.setTenNV(rs.getString("TenNV"));
                hoaDon.setNhanVien(nhanVien);
                
                danhSachHoaDon.add(hoaDon);
            }
        }
        return danhSachHoaDon;
    }

    // 2. Lấy chi tiết hóa đơn
    public List<ChiTietHoaDon> getChiTietHoaDon(String maHD) throws SQLException {
        List<ChiTietHoaDon> chiTietHoaDon = new ArrayList<>();
        Connection connection = ConnectDB.getConnection();
        String sql = "SELECT ct.*, s.TenSach, s.GiaBan FROM CHITIETHOADON ct " +
                     "JOIN SACH s ON ct.MaSach = s.MaSach " +
                     "WHERE ct.MaHD = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, maHD);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setMaHD(maHD);
                ct.setMaSach(rs.getString("MaSach"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setDonGia(rs.getDouble("DonGia"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                
                Sach sach = new Sach();
                sach.setTenSach(rs.getString("TenSach"));
                sach.setGiaban(rs.getDouble("GiaBan"));
                ct.setSach(sach);
                
                chiTietHoaDon.add(ct);
            }
        }
        return chiTietHoaDon;
    }

    // 3. Thêm hóa đơn mới
    public boolean themHoaDon(HoaDon hoaDon, List<ChiTietHoaDon> chiTietHoaDon) throws SQLException {
    	Connection connection = ConnectDB.getConnection();
        try {
            connection.setAutoCommit(false);
            
            // Thêm hóa đơn
            String sqlHoaDon = "INSERT INTO HOADON(MaHD, MaKH, MaNV, NgayLap, TongTien, GiamGia, ThanhTien) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sqlHoaDon)) {
                stmt.setString(1, hoaDon.getMaHD());
                stmt.setString(2, hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getMaKH() : null);
                stmt.setString(3, hoaDon.getNhanVien().getMaNV());
                stmt.setTimestamp(4, new Timestamp(hoaDon.getNgayLap().getTime()));
                stmt.setDouble(5, hoaDon.getTongTien());
                stmt.setDouble(6, hoaDon.getGiamGia());
                stmt.setDouble(7, hoaDon.getThanhTien());
                stmt.executeUpdate();
            }
            
            // Thêm chi tiết hóa đơn và cập nhật số lượng tồn sách
            String sqlChiTiet = "INSERT INTO CHITIETHOADON(MaHD, MaSach, SoLuong, DonGia, ThanhTien) " +
                                "VALUES (?, ?, ?, ?, ?)";
            String sqlUpdateSach = "UPDATE SACH SET SoLuongTon = SoLuongTon - ? WHERE MaSach = ?";
            
            for (ChiTietHoaDon ct : chiTietHoaDon) {
                // Thêm chi tiết hóa đơn
                try (PreparedStatement stmt = connection.prepareStatement(sqlChiTiet)) {
                    stmt.setString(1, ct.getMaHD());
                    stmt.setString(2, ct.getMaSach());
                    stmt.setInt(3, ct.getSoLuong());
                    stmt.setDouble(4, ct.getDonGia());
                    stmt.setDouble(5, ct.getThanhTien());
                    stmt.executeUpdate();
                }
                
                // Cập nhật số lượng tồn sách
                try (PreparedStatement stmt = connection.prepareStatement(sqlUpdateSach)) {
                    stmt.setInt(1, ct.getSoLuong());
                    stmt.setString(2, ct.getMaSach());
                    stmt.executeUpdate();
                }
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    // 4. Tìm kiếm hóa đơn theo mã hoặc tên khách hàng
    public List<HoaDon> timKiemHoaDon(String keyword) throws SQLException {
        List<HoaDon> ketQua = new ArrayList<>();
        Connection connection = ConnectDB.getConnection();
        String sql = "SELECT hd.*, kh.TenKH, nv.TenNV FROM HOADON hd " +
                     "LEFT JOIN KHACHHANG kh ON hd.MaKH = kh.MaKH " +
                     "LEFT JOIN NHANVIEN nv ON hd.MaNV = nv.MaNV " +
                     "WHERE hd.MaHD LIKE ? OR kh.TenKH LIKE ? " +
                     "ORDER BY hd.NgayLap DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                HoaDon hoaDon = new HoaDon();
                hoaDon.setMaHD(rs.getString("MaHD"));
                hoaDon.setNgayLap(rs.getTimestamp("NgayLap"));
                hoaDon.setTongTien(rs.getDouble("TongTien"));
                hoaDon.setGiamGia(rs.getDouble("GiamGia"));
                hoaDon.setThanhTien(rs.getDouble("ThanhTien"));
                
                if (rs.getString("MaKH") != null) {
                    KhachHang khachHang = new KhachHang();
                    khachHang.setMaKH(rs.getString("MaKH"));
                    khachHang.setTenKH(rs.getString("TenKH"));
                    hoaDon.setKhachHang(khachHang);
                }
                
                NhanVien nhanVien = new NhanVien();
                nhanVien.setMaNV(rs.getString("MaNV"));
                nhanVien.setTenNV(rs.getString("TenNV"));
                hoaDon.setNhanVien(nhanVien);
                
                ketQua.add(hoaDon);
            }
        }
        return ketQua;
    }

    // 5. Lấy thông tin khách hàng từ số điện thoại
    public KhachHang getKhachHangBySdt(String sdt) throws SQLException {
        String sql = "SELECT * FROM KHACHHANG WHERE SoDienThoai = ?";
        Connection connection = ConnectDB.getConnection();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sdt);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                KhachHang khachHang = new KhachHang();
                khachHang.setMaKH(rs.getString("MaKH"));
                khachHang.setTenKH(rs.getString("TenKH"));
                khachHang.setDiaChi(rs.getString("DiaChi"));
                khachHang.setSoDienThoai(rs.getString("SoDienThoai"));
                khachHang.setEmail(rs.getString("Email"));
                khachHang.setNgaySinh(rs.getDate("NgaySinh"));
                return khachHang;
            }
        }
        return null;
    }

    // 6. Thêm khách hàng mới
    public boolean themKhachHang(KhachHang khachHang) throws SQLException {
        String sql = "INSERT INTO KHACHHANG(MaKH, TenKH,SoDienThoai,DiaChi) " +
                     "VALUES (?, ?, ?)";
        Connection connection = ConnectDB.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, khachHang.getMaKH());
            stmt.setString(2, khachHang.getTenKH());
            stmt.setString(3, khachHang.getSoDienThoai());
            stmt.setString(4, khachHang.getDiaChi());
            return stmt.executeUpdate() > 0;
        }
    }
    public static String sinhMaHD() throws SQLException {
        String sql = "SELECT MaHD FROM HOADON ORDER BY MaHD DESC LIMIT 1";
        Connection conn = ConnectDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        String maMoi = "HD001"; 
        if (rs.next()) {
            String maCu = rs.getString("MaHD"); // ví dụ: HD023
            int so = Integer.parseInt(maCu.substring(2)); // 23
            so++;
            maMoi = String.format("HD%03d", so); // HD024
        }
        return maMoi;
    }
    public boolean saveHoaDon(HoaDon hoaDon, List<ChiTietHoaDon> chiTietHoaDonList) throws SQLException {
        Connection conn = null;
        PreparedStatement hoaDonStmt = null;
        PreparedStatement chiTietStmt = null;
        PreparedStatement updateSachStmt = null;
        System.out.println("Mã KH insert: " + hoaDon.getKhachHang().getMaKH());
        try {
            conn = ConnectDB.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction
            NhanVien nv = model.Session.currentUser;
            String hoaDonSql = "INSERT INTO HOADON(MaHD, MaKH, MaNV, NgayLap, TongTien, GiamGia, ThanhTien) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?)";
            hoaDonStmt = conn.prepareStatement(hoaDonSql);
            hoaDonStmt.setString(1, hoaDon.getMaHD());
            hoaDonStmt.setString(2, hoaDon.getKhachHang().getMaKH());
            hoaDonStmt.setString(3, hoaDon.getNhanVien().getMaNV());
            hoaDonStmt.setTimestamp(4, new Timestamp(hoaDon.getNgayLap().getTime()));
            hoaDonStmt.setDouble(5, hoaDon.getTongTien());
            hoaDonStmt.setDouble(6, hoaDon.getGiamGia());
            hoaDonStmt.setDouble(7, hoaDon.getThanhTien());
            hoaDonStmt.executeUpdate();

            // Lưu chi tiết hóa đơn và cập nhật số lượng tồn kho
            String chiTietSql = "INSERT INTO CHITIETHOADON(MaHD, MaSach, SoLuong, DonGia, ThanhTien) " +
                               "VALUES (?, ?, ?, ?, ?)";
            String updateSachSql = "UPDATE SACH SET SoLuongTon = SoLuongTon - ? WHERE MaSach = ?";
            
            chiTietStmt = conn.prepareStatement(chiTietSql);
            updateSachStmt = conn.prepareStatement(updateSachSql);
            
            for (ChiTietHoaDon chiTiet : chiTietHoaDonList) {
                // Lưu chi tiết hóa đơn
                chiTietStmt.setString(1, chiTiet.getMaHD());
                chiTietStmt.setString(2, chiTiet.getMaSach());
                chiTietStmt.setInt(3, chiTiet.getSoLuong());
                chiTietStmt.setDouble(4, chiTiet.getDonGia());
                chiTietStmt.setDouble(5, chiTiet.getThanhTien());
                chiTietStmt.addBatch();
                
                // Cập nhật số lượng tồn kho
                updateSachStmt.setInt(1, chiTiet.getSoLuong());
                updateSachStmt.setString(2, chiTiet.getMaSach());
                updateSachStmt.addBatch();
            }
            
            chiTietStmt.executeBatch();
            updateSachStmt.executeBatch();
            
            conn.commit(); // Commit transaction
            return true;
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback nếu có lỗi
            }
            throw e;
        } catch (IllegalArgumentException e) {
            // Xử lý lỗi nếu hoaDon.getNhanVien() hoặc hoaDon.getKhachHang() là null
            System.err.println("Lỗi: " + e.getMessage());
            return false; // Trả về false nếu gặp lỗi
        } finally {
            // Đóng các resource
            if (updateSachStmt != null) updateSachStmt.close();
            if (chiTietStmt != null) chiTietStmt.close();
            if (hoaDonStmt != null) hoaDonStmt.close();
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}