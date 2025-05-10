package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.table.*;
import com.toedter.calendar.JDateChooser;

import DAO.TacGiaDAO;
import DAO.TheLoaiDAO;
import controller.AuthorController;
import model.TacGia;
import model.TheLoai;

import java.util.ArrayList;
import java.util.List;

public class AuthorManager extends JPanel {
    // Các thành phần giao diện
    public JTextField txtMaTacGia, txtTenTacGia, txtTimKiem;
    private JLabel lblAnhTacGia;
    public JTable table;
    public DefaultTableModel tableModel;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnChonAnh, btnTimKiem;
    private JDateChooser dateChooser;
    public String currentImagePath;
    private JList<String> listGoiY;
    private DefaultListModel<String> listModel;
    private JPopupMenu popupMenu;
    
    public AuthorManager() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        Color bgColor = new Color(200, 255, 200);
        Color panelColor = new Color(200, 255, 200);
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
        
        setBackground(bgColor);

        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        infoPanel.setBorder(new TitledBorder("THÔNG TIN TÁC GIẢ"));
        infoPanel.setBackground(panelColor);
        
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(panelColor);
        
        JLabel lblMa = new JLabel("Mã tác giả:");
        lblMa.setFont(font);
        txtMaTacGia = new JTextField();
        txtMaTacGia.setFont(font);
        txtMaTacGia.setEditable(false);
        
        JLabel lblTen = new JLabel("Tên tác giả:");
        lblTen.setFont(font);
        txtTenTacGia = new JTextField();
        txtTenTacGia.setFont(font);
        
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(font);
        dateChooser = new JDateChooser();
        dateChooser.setFont(font);
        
        inputPanel.add(lblMa);
        inputPanel.add(txtMaTacGia);
        inputPanel.add(lblTen);
        inputPanel.add(txtTenTacGia);
        inputPanel.add(lblNgaySinh);
        inputPanel.add(dateChooser);
        
        
        // Panel hình ảnh
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(new TitledBorder("HÌNH ẢNH TÁC GIẢ"));
        imagePanel.setBackground(panelColor);
        imagePanel.setPreferredSize(new Dimension(140, 150));
        
        lblAnhTacGia = new JLabel("", JLabel.CENTER);
        lblAnhTacGia.setPreferredSize(new Dimension(180, 300));
        lblAnhTacGia.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        btnChonAnh = new JButton("Chọn ảnh");
        btnChonAnh.setFont(font);
        btnChonAnh.addActionListener(e -> browseImage());
        
        imagePanel.add(lblAnhTacGia, BorderLayout.CENTER);
        imagePanel.add(btnChonAnh, BorderLayout.SOUTH);
        
        infoPanel.add(inputPanel, BorderLayout.CENTER);
        infoPanel.add(imagePanel, BorderLayout.EAST);
        
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBorder(new TitledBorder("TÌM KIẾM TÁC GIẢ"));
        searchPanel.setBackground(panelColor);
        
        txtTimKiem = new JTextField();
        txtTimKiem.setFont(font);
        listModel = new DefaultListModel<>();
        listGoiY = new JList<>(listModel);
        listGoiY.setFont(font);
        popupMenu = new JPopupMenu();
        popupMenu.add(new JScrollPane(listGoiY));

        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateGoiY(); }
            public void removeUpdate(DocumentEvent e) { updateGoiY(); }
            public void insertUpdate(DocumentEvent e) { updateGoiY(); }
            private void updateGoiY() {
                String input = txtTimKiem.getText().trim();
                if (!input.isEmpty()) {
                    capNhatDanhSachGoiY(input);
                    popupMenu.setFocusable(false);
                    popupMenu.show(txtTimKiem, 0, txtTimKiem.getHeight());
                    txtTimKiem.requestFocusInWindow();
                } else {
                    popupMenu.setVisible(false);
                }
            }

        });

        listGoiY.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selected = listGoiY.getSelectedValue();
                if (selected != null) {
                    txtTimKiem.setText(selected);
                    popupMenu.setVisible(false);
                    timKiemTG(selected);
                }
            }
        });
        
        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(font);
        btnTimKiem.setBackground(new Color(70, 130, 180));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.addActionListener(e -> timKiemTG(txtTimKiem.getText()));
        
        searchPanel.add(txtTimKiem, BorderLayout.CENTER);
        searchPanel.add(btnTimKiem, BorderLayout.EAST);
        
        // ========== PHẦN BẢNG HIỂN THỊ ==========
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new TitledBorder("DANH SÁCH TÁC GIẢ"));
        tablePanel.setBackground(panelColor);
        
        tableModel = new DefaultTableModel(new Object[]{"Mã TG", "Tên tác giả", "Ngày sinh", "Ảnh"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(font);
        table.setRowHeight(30);
        table.getTableHeader().setFont(titleFont);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(200, 200, 200));
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedItem();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        ActionListener ac = (ActionListener) new AuthorController(this);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(bgColor);
        
        btnThem = createButton("Thêm", new Color(46, 125, 50));
        btnThem.addActionListener(ac);
        btnSua = createButton("Sửa", new Color(30, 144, 255));
        btnSua.addActionListener(ac);
        btnXoa = createButton("Xóa", new Color(220, 53, 69));
        btnXoa.addActionListener(ac);
        btnLamMoi = createButton("Làm mới", new Color(108, 117, 125));
        btnLamMoi.addActionListener(ac);
        
        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);
        
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(bgColor);
        topPanel.add(infoPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        loadDataFromDAO();
    }
    private void capNhatDanhSachGoiY(String input) {
        listModel.clear();
        List<String> dsTg = new ArrayList<String>();
        List<TacGia> tg = null;
		try {
			tg = TacGiaDAO.getAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        for (TacGia tacgia : tg) {
        	dsTg.add(tacgia.getTenTacGia());
        }
        
        for (String tenDM : dsTg) {
            if (tenDM.toLowerCase().contains(input.toLowerCase())) {
                listModel.addElement(tenDM);
            }
        }
        listGoiY.setVisibleRowCount(Math.min(listModel.size(), 5));
    }
    private void timKiemTG(String keyword) {
        tableModel.setRowCount(0);
        List<TacGia> tg = null;
		try {
			tg = TacGiaDAO.getAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        for (TacGia tacgia : tg) {
            if(keyword.equals(tacgia.getTenTacGia())) {
                tableModel.addRow(new Object[] {
                		tacgia.getMaTacGia(),
                		tacgia.getTenTacGia(),
                		tacgia.getNgaySinh(),
                		tacgia.getAnhTacGia()
                		
                });
            }
        }
    }
    private void showSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {

            txtMaTacGia.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenTacGia.setText(tableModel.getValueAt(selectedRow, 1).toString());
            
            Object ngaySinhObj = tableModel.getValueAt(selectedRow, 2);
            if (ngaySinhObj != null) {
                if (ngaySinhObj instanceof java.sql.Date) {
                    dateChooser.setDate(new java.util.Date(((java.sql.Date)ngaySinhObj).getTime()));
                } else if (ngaySinhObj instanceof String) {
                    // Xử lý nếu ngày sinh được lưu dưới dạng chuỗi
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = (Date) sdf.parse(ngaySinhObj.toString());
                        dateChooser.setDate(date);
                    } catch (ParseException e) {
                        dateChooser.setDate(null);
                    }
                }
            } else {
                dateChooser.setDate(null);
            }
           
            Object imgPathObj = tableModel.getValueAt(selectedRow, 3);
            if (imgPathObj != null && !imgPathObj.toString().isEmpty()) {
                displayImage(imgPathObj.toString());
                currentImagePath = imgPathObj.toString();
            } else {
                lblAnhTacGia.setIcon(null);
                currentImagePath = null;
            }
        }
    }
    public void loadDataFromDAO() {
        try {
            tableModel.setRowCount(0);
            
            List<TacGia> dsTacGia = new TacGiaDAO().getAll();
            
            for (TacGia tg : dsTacGia) {
                Object[] rowData = new Object[]{
                    tg.getMaTacGia(),
                    tg.getTenTacGia(),
                    tg.getNgaySinh(),
                    tg.getAnhTacGia()
                };
                tableModel.addRow(rowData);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Lỗi khi tải dữ liệu từ CSDL: " + ex.getMessage(), 
                       "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    public TacGia getAuthorFromInput() {
    	TacGia tacGia = new TacGia();
    	tacGia.setMaTacGia(txtMaTacGia.getText().trim());
    	tacGia.setTenTacGia(txtTenTacGia.getText().trim());
    	if (dateChooser.getDate() != null) {
            tacGia.setNgaySinh(new java.sql.Date(dateChooser.getDate().getTime()));
        } else {
            tacGia.setNgaySinh(null);
        }
    	tacGia.setAnhTacGia(currentImagePath);
    	return tacGia;
    }
    public TacGia getAuthorForAdd() throws SQLException {
    	TacGia tacGia = new TacGia();
    	TacGiaDAO dao = new TacGiaDAO();
    	tacGia.setMaTacGia(dao.generateMaTacGia());
    	tacGia.setTenTacGia(txtTenTacGia.getText().trim());
    	if (dateChooser.getDate() != null) {
            tacGia.setNgaySinh(new java.sql.Date(dateChooser.getDate().getTime()));
        } else {
            tacGia.setNgaySinh(null);
        }
    	currentImagePath = "";
    	tacGia.setAnhTacGia(currentImagePath);
    	return tacGia;
    }
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }
    
    public void lamMoiForm() {
        txtMaTacGia.setText("");
        txtTenTacGia.setText("");
        dateChooser.setDate(null);
        lblAnhTacGia.setIcon(null);
        table.clearSelection();
    }
    
    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentImagePath = file.getAbsolutePath();
            displayImage(currentImagePath);
        }
    }

    private void displayImage(String imagePath) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            
            int width = lblAnhTacGia.getWidth();
            int height = lblAnhTacGia.getHeight();
            if (width <= 0) width = 180;
            if (height <= 0) height = 230;
            
            Image scaledImage = originalImage.getScaledInstance(
                width, height, Image.SCALE_SMOOTH);
            lblAnhTacGia.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Không thể đọc file ảnh: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý tác giả");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new AuthorManager());
            frame.setVisible(true);
        });
    }

}