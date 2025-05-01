package controller;
	
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import DAO.AccountDAO;
import View.Login;
import View.TrangChu;
import model.Account;

public class AccountController implements ActionListener{
	private Login view;
	private AccountDAO dao;
	public AccountController(Login view) {
		this.view = view;
		this.dao = new AccountDAO();
	}
	public void actionPerformed(ActionEvent e) {
		String cm = e.getActionCommand();
		if(cm.equals("Đăng Nhập")) {
			try {
				login();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if(cm.equals("Thoát")) {
			view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			view.setVisible(false);
		}
	}
	private void login() throws SQLException {
		List<Account> list = dao.getAll();
		String username = view.textfieldusername.getText();
	    String password = view.textFieldpass.getText();
		for (Account account : list) {
			if(username.equals(account.getUsername())&&password.equals(account.getPassword())) {
				view.messageLabel.setText("Đăng nhập thành công!");
		        view.messageLabel.setForeground(Color.GREEN);
		        
		        view.dispose();
		        SwingUtilities.invokeLater(() -> {
		            TrangChu trangChu = new TrangChu(); 
		            trangChu.setVisible(true);          
		        });
			}
			else {
		        view.messageLabel.setText("Sai tài khoản hoặc mật khẩu!");
		        view.messageLabel.setForeground(Color.RED);
		    }
		}
	}
}
