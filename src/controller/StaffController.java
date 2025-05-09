package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import DAO.NhanVienDAO;
import View.StaffManager;
import model.NhanVien;
import model.TheLoai;

public class StaffController implements ActionListener{
	private StaffManager view;
	private NhanVienDAO dao;
	public StaffController(StaffManager view) {
		dao = new NhanVienDAO();
		this.view = view;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		switch(cmd) {
		case "Thêm":
			try {
				insert();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			break;
		case "Sửa":
			try {
				update();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "Xóa":
			delete();
			break;
		case "Làm mới":
			view.lamMoiForm();
		}
		
			
	}
	public void insert() throws SQLException {
		NhanVien nv = view.getNhanVien();
		try {
			for (int i = 0; i < view.tableModel.getRowCount(); i++) {
	            if (nv.getMaNV().equals(view.tableModel.getValueAt(i, 0))) {
	                JOptionPane.showMessageDialog(view, this, "Mã danh mục đã tồn tại!", i);
	                return;
	            }
	        }
            if (dao.insert(nv)) {
                view.showMessage("Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                view.taiLaiDuLieu();
                view.lamMoiForm();
            } else {
                view.showMessage("Thêm nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            view.showMessage("Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
	}
	private void update() throws SQLException {
		int selected = view.table.getSelectedRow();
		 if (selected == -1) {
	            view.showMessage("Vui lòng chọn nhân viên cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        NhanVien theloai = view.getNhanVien();

	        try {
	            if (dao.update(theloai)) {
	                view.showMessage("Cập nhật nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	                view.taiLaiDuLieu();
	                view.lamMoiForm();
	            } else {
	                view.showMessage("Cập nhật nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            }
	        } catch (Exception e) {
	            view.showMessage("Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	}
	private void delete() {
		int selectedRow = view.table.getSelectedRow();
    	if(selectedRow == -1) {
    		view.showMessage("vui lòng chọn nhân viên cần xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
    	}
    	int confirm = view.showConfirmDialog("Bạn chắc chắn muốn xóa nhân viên này?", "Xác nhận");
    	if(confirm == JOptionPane.YES_OPTION) {
    		String maDM = view.txtMaNV.getText().trim();
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
	
