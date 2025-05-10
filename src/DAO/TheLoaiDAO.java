package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.TheLoai;
import util.ConnectDB;

public class TheLoaiDAO {
	public TheLoaiDAO getInstance() {
		return new TheLoaiDAO();
	}
	public static List<TheLoai> getAll(){
		List<TheLoai> list = new ArrayList<>();
		Connection con = ConnectDB.getConnection();
		String sql = "select * from THELOAI";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
	            TheLoai tl = new TheLoai();
	            tl.setMaTheLoai(rs.getString("MaTheLoai"));
	            tl.setTenTheLoai(rs.getString("TenTheLoai"));
	            list.add(tl);

	        }
    		con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}	
	public String generateMaTL() throws SQLException {
        String sql = "SELECT MaTheLoai FROM THELOAI ORDER BY MaTheLoai DESC LIMIT 1";
        Connection connection = ConnectDB.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        String maMoi = "TL001";
        if (rs.next()) {
            String maCu = rs.getString("MaTheLoai");
            int so = Integer.parseInt(maCu.substring(2));
            so++;
            maMoi = String.format("TL%03d", so);
        }
        return maMoi;
    }
	public boolean insert(TheLoai theloai) {
		try {
			Connection con = ConnectDB.getConnection();
			String sql = "insert into THELOAI (MaTheLoai,TenTheLoai) values (?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, theloai.getMaTheLoai());
			ps.setString(2, theloai.getTenTheLoai());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean update(TheLoai loai) {
		String sql = "UPDATE THELOAI SET TenTheLoai=? where MaTheLoai=?";
		Connection con = ConnectDB.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, loai.getTenTheLoai());
			ps.setString(2, loai.getMaTheLoai());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	public boolean delete(String matheLoai) {
		try {
			Connection con = ConnectDB.getConnection();
			String sql = "delete from THELOAI where MaTheLoai=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, matheLoai);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return false;
	}
}
