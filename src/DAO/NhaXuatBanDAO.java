package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.NhaXuatBan;
import util.ConnectDB;

public class NhaXuatBanDAO {
	public List<NhaXuatBan> getAll() {
        List<NhaXuatBan> list = new ArrayList<>();
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM NHAXUATBAN";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhaXuatBan nxb = new NhaXuatBan();
                nxb.setMaNXB(rs.getString("MaNXB"));
                nxb.setTenNXB(rs.getString("TenNXB"));
                nxb.setDiaChi(rs.getString("DiaChi"));
                nxb.setSDT(rs.getString("SoDienThoai"));
                nxb.setEmail(rs.getString("Email"));
                list.add(nxb);
            }
    		con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
