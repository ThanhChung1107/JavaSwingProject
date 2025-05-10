package controller;

import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import DAO.HoaDonDAO;
import DAO.KhachHangDAO;
import View.OrderManager;
import model.ChiTietHoaDon;
import model.HoaDon;
import model.KhachHang;
import model.Session;

public class OrderController implements ActionListener{
	private HoaDonDAO dao;
	private OrderManager view;
	public OrderController(OrderManager view) {
		this.view = view;
		this.dao = new HoaDonDAO();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		switch(cmd) {
		case "Lưu hóa đơn":
			luuHoaDon();
			break;
		case "Làm mới":
			view.reset();
			break;
		}
		
	}
	private void luuHoaDon() {
		
	    if (view.modelCart.getRowCount() == 0) {
	        JOptionPane.showMessageDialog(view, "Giỏ hàng trống, không thể lưu hóa đơn", 
	            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    try {
	        // 1. Tạo đối tượng HoaDon
	        HoaDon hoaDon = new HoaDon();
	        String mahd = HoaDonDAO.sinhMaHD();
	        hoaDon.setMaHD(mahd); // Tạo mã hóa đơn
	        hoaDon.setNgayLap(new Timestamp(System.currentTimeMillis()));
	        hoaDon.setNhanVien(Session.currentUser); // Nhân viên đang đăng nhập
	        
	        String phone = view.txtsdt.getText().trim();
	        if (!phone.isEmpty()) {
	            KhachHang kh = KhachHangDAO.findCustomerByPhone(phone);
	            if (kh == null) {
	            	KhachHang khach = view.customerInfo();
	    	        KhachHangDAO.addcusFromOrder(khach);
	    	        hoaDon.setKhachHang(khach);
	            }
	            hoaDon.setKhachHang(kh);
	        }
	
	        hoaDon.setTongTien(view.tongTien);
	        hoaDon.setGiamGia(0);
	        hoaDon.setThanhTien(view.tongTien);
	        // 2. Tạo danh sách chi tiết hóa đơn
	        List<ChiTietHoaDon> chiTietList = new ArrayList<>();
	        for (int i = 0; i < view.modelCart.getRowCount(); i++) {
	            ChiTietHoaDon chiTiet = new ChiTietHoaDon();
	            chiTiet.setMaHD(hoaDon.getMaHD());
	            chiTiet.setMaSach(view.modelCart.getValueAt(i, 0).toString());
	            chiTiet.setSoLuong(Integer.parseInt(view.modelCart.getValueAt(i, 2).toString()));
	            
	            double donGia = Double.parseDouble(
	                view.modelCart.getValueAt(i, 3).toString().replaceAll("[^\\d.]", ""));
	            chiTiet.setDonGia(donGia);
	            
	            double thanhTienCT = Double.parseDouble(
	                view.modelCart.getValueAt(i, 4).toString().replaceAll("[^\\d.]", ""));
	            chiTiet.setThanhTien(thanhTienCT);
	            
	            chiTietList.add(chiTiet);
	        }
	        
	        // 3. Lưu vào database
	        if (dao.saveHoaDon(hoaDon, chiTietList)) {
	            JOptionPane.showMessageDialog(view, 
	                "Lưu hóa đơn thành công!\nMã HD: " + hoaDon.getMaHD() + 
	                "\nTổng tiền: " + String.format("%,.0f VND", view.tongTien),
	                "Thành công", JOptionPane.INFORMATION_MESSAGE);
	            
	            view.reset();
	        }
	        
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(view, 
	            "Lỗi khi lưu hóa đơn: " + ex.getMessage(), 
	            "Lỗi", JOptionPane.ERROR_MESSAGE);
	    }
	}
	public void loadDanhSachHoaDon() {
	    try {
	        List<HoaDon> danhSach = dao.getAllHoaDon(); 
	        view.modelOrders.setRowCount(0);

	        for (HoaDon hd : danhSach) {
	            view.modelOrders.addRow(new Object[]{
	                hd.getMaHD(),
	                new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(hd.getNgayLap()),
	                String.format("%,.0f VND", hd.getThanhTien()),
	                "Đã lập",
	                "Xem"
	            });
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(view, "Lỗi khi tải danh sách hóa đơn: " + e.getMessage());
	    }
	}
	public void xemChiTietHoaDon(String maHD) {
	    try {
	        List<ChiTietHoaDon> chiTietList = dao.getChiTietHoaDon(maHD);
	        HoaDon hoaDon = dao.getAllHoaDon().stream()
	                .filter(hd -> hd.getMaHD().equals(maHD))
	                .findFirst()
	                .orElse(null);

	        if (hoaDon == null) {
	            JOptionPane.showMessageDialog(view, "Không tìm thấy hóa đơn!");
	            return;
	        }

	        String[][] data = new String[chiTietList.size()][5];
	        for (int i = 0; i < chiTietList.size(); i++) {
	            ChiTietHoaDon ct = chiTietList.get(i);
	            data[i][0] = ct.getMaSach();
	            data[i][1] = ct.getSach().getTenSach();
	            data[i][2] = String.valueOf(ct.getSoLuong());
	            data[i][3] = String.format("%,.0f", ct.getDonGia());
	            data[i][4] = String.format("%,.0f", ct.getThanhTien());
	        }

	        view.showOrderDetailDialog(view, 
	            "Nhà sách ABC", 
	            new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(hoaDon.getNgayLap()),
	            hoaDon.getNhanVien().getTenNV(),
	            hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getTenKH() : "Khách lẻ",
	            data, 
	            String.format("%,.0f VND", hoaDon.getThanhTien()),
	            new Font("Segoe UI", Font.PLAIN, 14)
	        );

	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(view, "Lỗi khi xem chi tiết: " + e.getMessage());
	    }
	}



}
