package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import DAO.TacGiaDAO;
import DAO.TheLoaiDAO;
import View.AuthorManager;
import View.CategoryManager;
import model.TacGia;
import model.TheLoai;

public class AuthorController implements ActionListener{
	private AuthorManager view;
	private TacGiaDAO dao;
	public AuthorController(AuthorManager view) {
		this.view = view;
		this.dao = new TacGiaDAO();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		switch(cmd) {
		case "Thêm":
			insertAuthor();
			break;
		case "Sửa":
			updateAuthor();
			break;
		case "Xóa":
			deleteAuthor();
			break;
		case "Làm mới":
			view.lamMoiForm();
			break;
		}
	}
	private void insertAuthor() {
		TacGia tg = view.getAuthorFromInput();
		try {
			for (int i = 0; i < view.tableModel.getRowCount(); i++) {
	            if (tg.getMaTacGia().equals(view.tableModel.getValueAt(i, 0))) {
	                JOptionPane.showMessageDialog(view, this, "Mã tác giả đã tồn tại!", i);
	                return;
	            }
	        }
            if (dao.insertAuthor(tg)) {
                view.showMessage("Thêm tác giả thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                view.loadDataFromDAO();
            } else {
                view.showMessage("Thêm tác giả thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            view.showMessage("Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
	}
	private void updateAuthor() {
		int selected = view.table.getSelectedRow();
		 if (selected == -1) {
	            view.showMessage("Vui lòng chọn Sách cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        TacGia tg = view.getAuthorFromInput();

	        try {
	            if (dao.updateAuthor(tg)) {
	                view.showMessage("Cập nhật tác giả thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	                view.loadDataFromDAO();
	                view.lamMoiForm();
	            } else {
	                view.showMessage("Cập nhật tác giả thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            }
	        } catch (Exception e) {
	            view.showMessage("Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	}
	private void deleteAuthor() {
		int selectedRow = view.table.getSelectedRow();
    	if(selectedRow == -1) {
    		view.showMessage("vui lòng chọn tác giả cần xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
    	}
    	int confirm = view.showConfirmDialog("Bạn chắc chắn muốn xóa Tác giả này?", "Xác nhận");
    	if(confirm == JOptionPane.YES_OPTION) {
    		String maDM = view.txtMaTacGia.getText().trim();
    		try {
				if(dao.deleteAuthor(maDM)) {
					view.showMessage("Bạn đã xóa thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
					view.loadDataFromDAO();
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
