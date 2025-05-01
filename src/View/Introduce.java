package View;

import javax.swing.*;

import DAO.SachDAO;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class Introduce extends JPanel {
	public Introduce() {
	    setLayout(new BorderLayout());
	    
	    JTabbedPane tabbedPane = new JTabbedPane();
	    
	    // Tab 1: Giới thiệu dự án (giữ nguyên)
	    JPanel projectPanel = new JPanel(new BorderLayout());
	    projectPanel.add(createProjectContent(), BorderLayout.CENTER);
	    tabbedPane.addTab("Dự án", null, projectPanel, "Thông tin về dự án");
	    
	    // Tab 2: Giới thiệu thành viên (giữ nguyên)
	    JPanel memberPanel = new JPanel(new BorderLayout());
	    memberPanel.add(createMemberContent(), BorderLayout.CENTER);
	    tabbedPane.addTab("Thành viên", null, memberPanel, "Thông tin các thành viên");
	    
	    // Tab 3: Thêm tab hiển thị sách mới
	    JPanel bookPanel = new JPanel(new BorderLayout());
	    bookPanel.add(createBookShopContent(), BorderLayout.CENTER); // Sử dụng phương thức đã tạo
	    tabbedPane.addTab("Cửa hàng sách", null, bookPanel, "Xem các sách hiện có");
	    
	    // Tùy chỉnh giao diện tab
	    tabbedPane.setBackgroundAt(0, new Color(240, 240, 240));
	    tabbedPane.setBackgroundAt(1, new Color(240, 240, 240));
	    tabbedPane.setBackgroundAt(2, new Color(240, 240, 240)); // Thêm màu cho tab mới
	    tabbedPane.setForeground(Color.BLACK);
	    tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
	    
	    add(tabbedPane, BorderLayout.CENTER);
	}
    
    private JComponent createProjectContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBackground(new Color(200, 255, 200));
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/image/book.png")); 
        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel titleLabel = new JLabel("Quản lý nhà sách");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(30, 136, 229));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        JLabel subjectLabel = new JLabel("Học phần: JAVA");
        subjectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subjectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subjectLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel techLabel = new JLabel("Công nghệ sử dụng: Java Swing, MySQL");
        techLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        techLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        techLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        panel.add(Box.createVerticalGlue());
        panel.add(imageLabel);
        panel.add(titleLabel);
        panel.add(subjectLabel);
        panel.add(techLabel);
        panel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null); 
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }
    
    private JComponent createMemberContent() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Thêm thông tin thành viên 1
        panel.add(createMemberCard("Nguyễn Văn A", "Trưởng nhóm", "a.nguyen@email.com"));
        
        // Thêm thông tin thành viên 2
        panel.add(createMemberCard("Trần Thị B", "Phát triển backend", "b.tran@email.com"));
        
        // Thêm thông tin thành viên 3
        panel.add(createMemberCard("Lê Văn C", "Phát triển frontend", "c.le@email.com"));
        
        return new JScrollPane(panel);
    }
    
    private JPanel createMemberCard(String name, String role, String email) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(new Color(100, 100, 100));
        
        JLabel emailLabel = new JLabel(email);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailLabel.setForeground(new Color(0, 100, 200));
        
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.setOpaque(false);
        infoPanel.add(nameLabel);
        infoPanel.add(roleLabel);
        infoPanel.add(emailLabel);
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
 // Thêm vào class Introduce

    private JComponent createBookShopContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        
        // Panel tiêu đề
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 100, 200));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel titleLabel = new JLabel("DANH SÁCH SÁCH NỔI BẬT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Panel lưới hiển thị sách
        JPanel bookGridPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        bookGridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        bookGridPanel.setBackground(new Color(240, 240, 240));
        
        try {
            // Lấy dữ liệu từ CSDL thay vì dùng dữ liệu mẫu
            List<Object[]> bookData = new SachDAO().getAllSachForDisplay();
            
            // Tạo card cho mỗi cuốn sách
            for (Object[] book : bookData) {
                bookGridPanel.add(createSimpleBookCard(
                    (String) book[0], // Tên sách
                    (String) book[1], // Tác giả
                    (String) book[2], // Thể loại
                    (String) book[3],
                    (String) book[4]
                ));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu sách: " + ex.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        JScrollPane scrollPane = new JScrollPane(bookGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }

    private JPanel createSimpleBookCard(String title, String author, String category, String price, String imagePath) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(200, 280));
        
        // Phần hình ảnh sách
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Load ảnh từ đường dẫn trong CSDL
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                // Nếu đường dẫn là tuyệt đối (bắt đầu bằng http:// hoặc https://)
                if (imagePath.startsWith("http")) {
                    // Load ảnh từ URL
                    ImageIcon icon = new ImageIcon(new String(imagePath));
                    Image img = icon.getImage().getScaledInstance(150, 180, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                } else {
                    ImageIcon icon = new ImageIcon(imagePath);
                    Image img = icon.getImage().getScaledInstance(150, 180, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                }
            } else {

                imageLabel.setIcon(new ImageIcon(getClass().getResource("/resources/image/book.png")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageLabel.setIcon(new ImageIcon(getClass().getResource("/resources/image/book.png")));
        }
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Panel thông tin
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        infoPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("<html><div style='text-align:center;'>" + title + "</div></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel authorLabel = new JLabel("Tác giả: " + author);
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel categoryLabel = new JLabel("Thể loại: " + category);
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(new Color(200, 0, 0));
        priceLabel.setHorizontalAlignment(JLabel.CENTER);
        
        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(categoryLabel);
        infoPanel.add(priceLabel);
        
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);
        
        // Hiệu ứng hover đơn giản
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 150, 255)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        });
        
        return card;
    }
    // Phương thức để test riêng panel này
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Panel Giới Thiệu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            
            frame.add(new Introduce());
            frame.setVisible(true);
        });
    }
}
