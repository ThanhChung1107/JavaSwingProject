package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import DAO.TheLoaiDAO;
import View.CategoryManager;
import model.Sach;
import model.TheLoai;

public class CategoryController implements ActionListener{
	private CategoryManager view;
	private TheLoaiDAO dao;
	public CategoryController(CategoryManager view) {
		this.view = view;
		this.dao = new TheLoaiDAO();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		switch(cmd) {
		case "Thêm":
			try {
				insertCate();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "Sửa":
			updateCate();
			break;
		case "Xóa":
			deleteCate();
			break;
		case "Làm mới":
			view.lamMoiForm();
			break;
		}
	}
	private void insertCate() throws SQLException {
		TheLoai tl = view.getTheLoaiForAdd();
		try {
			for (int i = 0; i < view.tableModel.getRowCount(); i++) {
	            if (tl.getMaTheLoai().equals(view.tableModel.getValueAt(i, 0))) {
	                JOptionPane.showMessageDialog(view, this, "Mã danh mục đã tồn tại!", i);
	                return;
	            }
	        }
            if (dao.insert(tl)) {
                view.showMessage("Thêm danh mục thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                view.taiLaiDuLieu();
                view.lamMoiForm();
            } else {
                view.showMessage("Thêm danh mục thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            view.showMessage("Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
	}
	private void updateCate() {
		int selected = view.table.getSelectedRow();
		 if (selected == -1) {
	            view.showMessage("Vui lòng chọn danh mục cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        TheLoai theloai = view.getTheLoaiForEdit();

	        try {
	            if (dao.update(theloai)) {
	                view.showMessage("Cập nhật danh mục thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	                view.taiLaiDuLieu();
	                view.lamMoiForm();
	            } else {
	                view.showMessage("Cập nhật danh mục thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            }
	        } catch (Exception e) {
	            view.showMessage("Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	}
	private void deleteCate() {
		int selectedRow = view.table.getSelectedRow();
    	if(selectedRow == -1) {
    		view.showMessage("vui lòng chọn danh mục cần xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
    	}
    	int confirm = view.showConfirmDialog("Bạn chắc chắn muốn xóa danh mục này?", "Xác nhận");
    	if(confirm == JOptionPane.YES_OPTION) {
    		String maDM = view.txtMaDanhMuc.getText().trim();
    		try {
				if(dao.delete(maDM)) {
					view.showMessage("Bạn đã xóa thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
					view.taiLaiDuLieu();
					view.lamMoiForm();
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
