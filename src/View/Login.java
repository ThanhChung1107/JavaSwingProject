package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import controller.AccountController;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JButton;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField textfieldusername;
	public JPasswordField textFieldpass;
	public JLabel messageLabel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 688, 416);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(200, 255, 200));
		panel.setBounds(0, 0, 320, 379);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblicon = new JLabel("");
		lblicon.setFont(new Font("Tahoma", Font.PLAIN, 30));
		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/image/book.png"));
		Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);

		lblicon.setIcon(new ImageIcon(img));
		lblicon.setBounds(50, 60, 299, 220);

		panel.add(lblicon);
		
		JLabel lbllogin = new JLabel("ĐĂNG NHẬP");
		lbllogin.setFont(new Font("Arial", Font.PLAIN, 24));
		lbllogin.setBounds(408, 29, 177, 38);
		contentPane.add(lbllogin);
		
		JLabel lbliconAcc = new JLabel("");
		lbliconAcc.setBackground(Color.WHITE);
		lbliconAcc.setIcon(new ImageIcon(Login.class.getResource("/resources/icons/user (1).png")));
		lbliconAcc.setBounds(327, 100, 32, 38);
		contentPane.add(lbliconAcc);
		
		textfieldusername = new JTextField();
		textfieldusername.setFont(new Font("Arial", Font.PLAIN, 18));
		textfieldusername.setBounds(408, 100, 194, 38);
		textfieldusername.setBorder(new MatteBorder(0, 0, 2, 0,Color.BLACK));
		contentPane.add(textfieldusername);
		textfieldusername.setColumns(10);
		
		JLabel lbliconPass = new JLabel("");
		lbliconPass.setIcon(new ImageIcon(Login.class.getResource("/resources/icons/padlock.png")));
		lbliconPass.setBounds(327, 173, 32, 38);
		contentPane.add(lbliconPass);
		
		textFieldpass = new JPasswordField();
		textFieldpass.setEchoChar('*');
		textFieldpass.setFont(new Font("Arial", Font.PLAIN, 18));
		textFieldpass.setBorder(new MatteBorder(0, 0, 2, 0,Color.BLACK));
		textFieldpass.setColumns(10);
		textFieldpass.setBounds(408, 173, 194, 38);
		contentPane.add(textFieldpass);
		ActionListener ac = (ActionListener) new AccountController(this);
		JButton btnLogin = new JButton("Đăng Nhập");
		btnLogin.setIcon(new ImageIcon(Login.class.getResource("/resources/icons/enter.png")));
		btnLogin.setFont(new Font("Arial", Font.PLAIN, 18));
		btnLogin.setBounds(309, 261, 159, 38);
		contentPane.add(btnLogin);
		
		messageLabel = new JLabel("");
		messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		messageLabel.setForeground(Color.RED);
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageLabel.setBounds(309, 310, 340, 30);
		contentPane.add(messageLabel);
		btnLogin.addActionListener(ac);
		
		
		JButton btnexit = new JButton("Thoát");
		btnexit.setIcon(new ImageIcon(Login.class.getResource("/resources/icons/reject.png")));
		btnexit.setFont(new Font("Arial", Font.PLAIN, 18));
		btnexit.setBounds(490, 261, 159, 38);
		contentPane.add(btnexit);
		btnexit.addActionListener(ac);
	}
	

}