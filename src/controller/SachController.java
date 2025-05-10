package controller;

import View.BookManager;
import model.Sach;
import DAO.SachDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class SachController implements ActionListener {
    private BookManager view;
    private SachDAO sachDAO;

    public SachController(BookManager view) {
        this.view = view;
        this.sachDAO = new SachDAO();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Thêm":
			try {
				themSach();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
                break;
            case "Sửa":
                suaSach();
                break;
            case "Xóa":
                xoaSach();
                break;
            case "Làm mới":
                view.refreshFields();
                break;
        }
    }

    private void themSach() throws SQLException {
        Sach sach = view.getSachForAdd();
        if (sach == null) {
            view.showMessage("Giá và số lượng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (sachDAO.insert(sach)) {
                view.showMessage("Thêm sách thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                view.loadSach();
                view.refreshFields();
            } else {
                view.showMessage("Thêm sách thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            view.showMessage("Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaSach() {
        int selectedRow = view.bookTable.getSelectedRow();
        if (selectedRow == -1) {
            view.showMessage("Vui lòng chọn sách cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Sach sach = view.getSachFromInput();
        if (sach == null) {
            view.showMessage("Giá và số lượng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (sachDAO.updateSach(sach)) {
                view.showMessage("Cập nhật sách thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                view.loadSach();
                view.refreshFields();
            } else {
                view.showMessage("Cập nhật sách thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            view.showMessage("Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void xoaSach() {
    	int selectedRow = view.bookTable.getSelectedRow();
    	if(selectedRow == -1) {
    		view.showMessage("vui lòng chọn sách cần xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
    	}
    	int confirm = view.showConfirmDialog("Bạn chắc chắn muốn xóa sách này?", "Xác nhận");
    	if(confirm == JOptionPane.YES_OPTION) {
    		String maSach = view.tfMaSach.getText().trim();
    		try {
				if(sachDAO.deleteSach(maSach)) {
					view.showMessage("Bạn đã xóa thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
					view.loadSach();
					view.refreshFields();
				}
				else {
					view.showMessage("Xóa thất bại!!!!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
				
			} catch (Exception e) {
				view.showMessage("Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
    	}
    }
}      