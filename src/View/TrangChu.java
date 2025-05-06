package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class TrangChu extends JFrame {
    private JLabel lblLogo;
    private JButton btnDashboard, btnBooks, btnCategories, btnAuthors, btnCustomers, btnOrders, btnReports, btnSettings;
    private JButton currentActiveButton;
    private JPanel sidebarPanel;
    private JPanel contentPanel;

    public TrangChu() {
        // Thiết lập cho JFrame
        setTitle("Quản lý nhà sách");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null); // Căn giữa màn hình
        setLayout(new BorderLayout());

        // Sidebar
        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(250, Integer.MAX_VALUE));
        sidebarPanel.setBackground(new Color(51, 51, 76));
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel header với logo
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        lblLogo = new JLabel("NHÀ SÁCH ABC", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(Color.WHITE);
        headerPanel.add(lblLogo, BorderLayout.CENTER);
        
        // Panel chứa các nút chức năng
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new GridLayout(8, 1, 0, 10));
        
        // Tạo các nút menu
        btnDashboard = createMenuButton("Trang chủ", "/resources/icons/house.png");
        btnBooks = createMenuButton("Quản lý sách", "/resources/icons/stack-of-books.png");
        btnCategories = createMenuButton("Danh mục", "/resources/icons/category.png");
        btnAuthors = createMenuButton("Tác giả", "/resources/icons/author.png");
        btnCustomers = createMenuButton("Khách hàng", "/resources/icons/client.png");
        btnOrders = createMenuButton("Đơn hàng", "/resources/icons/bill.png");
        btnReports = createMenuButton("Thống kê", "/resources/icons/analytics.png");
        btnSettings = createMenuButton("Nhân viên", "/resources/icons/grouping.png");
        
        // Thêm sự kiện cho các nút
        addButtonAction(btnDashboard);
        addButtonAction(btnBooks);
        addButtonAction(btnCategories);
        addButtonAction(btnAuthors);
        addButtonAction(btnCustomers);
        addButtonAction(btnOrders);
        addButtonAction(btnReports);
        addButtonAction(btnSettings);
        
        // Thêm các nút vào panel
        menuPanel.add(btnDashboard);
        menuPanel.add(btnBooks);
        menuPanel.add(btnCategories);
        menuPanel.add(btnAuthors);
        menuPanel.add(btnCustomers);
        menuPanel.add(btnOrders);
        menuPanel.add(btnReports);
        menuPanel.add(btnSettings);
        
        // Panel footer
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JButton btnLogout = new JButton("Đăng xuất");
        customizeButton(btnLogout, new Color(244, 67, 54));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setOpaque(true);
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new Login().setVisible(true);
            }
        });

        footerPanel.add(btnLogout, BorderLayout.CENTER);
        
        // Thêm các panel vào sidebar
        sidebarPanel.add(headerPanel, BorderLayout.NORTH);
        sidebarPanel.add(menuPanel, BorderLayout.CENTER);
        sidebarPanel.add(footerPanel, BorderLayout.SOUTH);

        // Content Panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(new Introduce(), BorderLayout.CENTER);

        // Thêm sidebar và content vào JFrame
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Mặc định chọn trang chủ
        setActiveButton(btnDashboard);
    }

    private JButton createMenuButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(51, 51, 76));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            button.setIcon(icon);
            button.setIconTextGap(15);
        } catch (Exception e) {
            System.err.println("Không tìm thấy icon: " + iconPath);
            e.printStackTrace();
        }	
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != currentActiveButton) {
                    button.setBackground(new Color(70, 70, 100));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (button != currentActiveButton) {
                    button.setBackground(new Color(51, 51, 76));
                }
            }
        });

        return button;
    }

    private void customizeButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }

    private void addButtonAction(JButton button) {
        button.addActionListener(e -> {
            setActiveButton(button);
            contentPanel.removeAll();
            String command = button.getText();
            switch (command) {
            case "Trang chủ":
                contentPanel.add(new Introduce(), BorderLayout.CENTER);
                break;
            case "Quản lý sách":
            	try {
            	    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); // hoặc WindowsLookAndFeel
            	} catch (Exception ex) {
            	    ex.printStackTrace();
            	}

                contentPanel.add(new BookManager(),BorderLayout.CENTER);
                break;
            case "Danh mục":
            	try {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
					contentPanel.add(new CategoryManager(),BorderLayout.CENTER);
	                break;
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
            case "Tác giả":
                contentPanel.add(new AuthorManager());
                break;
            case "Khách hàng":
                try {
                	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                	contentPanel.add(new CustomerManager(),BorderLayout.CENTER);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
                break;
            case "Đơn hàng":
                try {
                	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                	contentPanel.add(new OrderManager(),BorderLayout.CENTER);
				} catch (Exception e3) {
					// TODO: handle exception
				}
                break;
            case "Báo cáo":
                System.out.println("Mở báo cáo");
                break;
            case "Cài đặt":
                System.out.println("Mở cài đặt");
                break;
        }
            contentPanel.revalidate(); 
            contentPanel.repaint(); 
        });
    }

    private void setActiveButton(JButton button) {
        if (currentActiveButton != null) {
            currentActiveButton.setBackground(new Color(51, 51, 76));
        }
        currentActiveButton = button;
        button.setBackground(new Color(30, 136, 229));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TrangChu trangChu = new TrangChu();
            trangChu.setVisible(true);
        });
    }
}
