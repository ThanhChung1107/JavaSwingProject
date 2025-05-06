package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.JOptionPane;
import DAO.KhachHangDAO;
import View.CustomerManager;
import model.KhachHang;
import java.util.List;
public class KhachHangController implements ActionListener {
    private CustomerManager view;
    private KhachHangDAO dao;

    public KhachHangController(CustomerManager view) {
        this.view = view;
        this.dao = new KhachHangDAO();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "Thêm":
            	themKhachHang();
                break;
            case "Sửa":
            	suaKhachHang();
                break;
            case "Xóa":
                xoaKhachHang();
                break;
            case "Làm mới":
                view.clearFields();
                break;
//            case "Tìm kiếm":
//                view.searchCustomer();
//                break;
        }
    }

    public void loadAllKhachHang() {
        List<KhachHang> list = dao.getAllKhachHang();
        view.loadTable(list);
    }

    public void themKhachHang() {
        try {
            KhachHang kh = getKhachHangFromView();
            
            if (dao.themKhachHang(kh)) {
                JOptionPane.showMessageDialog(view, "Thêm khách hàng thành công!");
                loadAllKhachHang();
                view.clearFields();
            } else {
                JOptionPane.showMessageDialog(view, "Thêm khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void suaKhachHang() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn khách hàng cần sửa", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            KhachHang kh = getKhachHangFromView();
            
            if (dao.suaKhachHang(kh)) {
                JOptionPane.showMessageDialog(view, "Sửa thông tin khách hàng thành công!");
                loadAllKhachHang();
                view.clearFields();
            } else {
                JOptionPane.showMessageDialog(view, "Sửa thông tin khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void xoaKhachHang() {
        try {
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn khách hàng cần xóa", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String maKH = view.getMaKH();
            int confirm = JOptionPane.showConfirmDialog(view, 
                    "Bạn có chắc chắn muốn xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.xoaKhachHang(maKH)) {
                    JOptionPane.showMessageDialog(view, "Xóa khách hàng thành công!");
                    loadAllKhachHang();
                    view.clearFields();
                } else {
                    JOptionPane.showMessageDialog(view, "Xóa khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void timKiemKhachHang() {
        try {
            String keyword = view.getTimKiem();
            List<KhachHang> result = dao.timKiemKhachHang(keyword);
            view.loadTable(result);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void lamMoi() {
        view.clearFields();
        loadAllKhachHang();
    }

    private KhachHang getKhachHangFromView() throws Exception {
        String maKH = view.getMaKH();
        String tenKH = view.getTenKH();
        String sdt = view.getSDT();
        String email = view.getEmail();
        String diaChi = view.getDiaChi();
        Date ngaySinh = Date.valueOf(view.getNgaySinh());

        if (maKH.isEmpty() || tenKH.isEmpty()) {
            throw new Exception("Mã KH và Tên KH không được để trống");
        }

        return new KhachHang(maKH, tenKH, diaChi, sdt, email, ngaySinh);
    }
} 