package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import DAO.*;
import controller.SachController;
import model.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BookManager extends JPanel {
    public JTable bookTable;
    private DefaultTableModel tableModel;
    public JTextField tfMaSach, tfTenSach, tfTacGia, tfGia, tfSoLuong, tfImagePath;
    public JComboBox<String> cbTheLoai, cbTacGia, cbNhaXuatBan;
    public JLabel imageLabel;
    public String currentImagePath;
    private Map<String, String> theLoaiMap = new HashMap<>();
    private Map<String, String> tacGiaMap = new HashMap<>();
    private Map<String, String> nxbMap = new HashMap<>();
    // DAOs
    private TheLoaiDAO theLoaiDAO = new TheLoaiDAO();
    private TacGiaDAO tacGiaDAO = new TacGiaDAO();
    private NhaXuatBanDAO nhaXuatBanDAO = new NhaXuatBanDAO();
    private SachDAO sachDAO = new SachDAO();

    public BookManager() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(200, 255, 200));
        
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(200, 255, 200));
        
        // Panel nhập thông tin sách
        JPanel inputPanel = createInputPanel();
        topPanel.add(inputPanel, BorderLayout.CENTER);
        
        // Panel hiển thị hình ảnh
        JPanel imagePanel = createImagePanel();
        topPanel.add(imagePanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        JScrollPane tableScrollPane = createBookTable();
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Load dữ liệu ban đầu
        loadInitialData();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin sách"));
        panel.setBackground(new Color(200, 255, 200));
        
        panel.add(new JLabel("Mã sách:"));
        tfMaSach = new JTextField();
        tfMaSach.setEditable(false);
        panel.add(tfMaSach);
     
        panel.add(new JLabel("Tên sách:"));
        tfTenSach = new JTextField();
        panel.add(tfTenSach);
        
        panel.add(new JLabel("Thể loại:"));
        cbTheLoai = new JComboBox<>();
        panel.add(cbTheLoai);
        
        panel.add(new JLabel("Tác giả:"));
        cbTacGia = new JComboBox<>();
        panel.add(cbTacGia);
        
        panel.add(new JLabel("Nhà xuất bản:"));
        cbNhaXuatBan = new JComboBox<>();
        panel.add(cbNhaXuatBan);

        panel.add(new JLabel("Giá (VND):"));
        tfGia = new JTextField();
        panel.add(tfGia);

        panel.add(new JLabel("Số lượng:"));
        tfSoLuong = new JTextField();
        panel.add(tfSoLuong);
        
        panel.add(new JLabel("Ảnh sách:"));
        JPanel imagePathPanel = new JPanel(new BorderLayout());
        tfImagePath = new JTextField();
        tfImagePath.setEditable(false);
        imagePathPanel.add(tfImagePath, BorderLayout.CENTER);
        
        JButton btnBrowse = new JButton("...");
        btnBrowse.addActionListener(e -> browseImage());
        imagePathPanel.add(btnBrowse, BorderLayout.EAST);
        panel.add(imagePathPanel);

        return panel;
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Ảnh sách"));
        panel.setPreferredSize(new Dimension(200, 250));
        panel.setBackground(new Color(200, 255, 200));
        
        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(180, 230));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        panel.add(imageLabel, BorderLayout.CENTER);
        return panel;
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
            tfImagePath.setText(currentImagePath);
            displayImage(currentImagePath);
        }
    }

    private void displayImage(String imagePath) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            
            int width = imageLabel.getWidth();
            int height = imageLabel.getHeight();
            if (width <= 0) width = 180;
            if (height <= 0) height = 230;
            
            Image scaledImage = originalImage.getScaledInstance(
                width, height, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Không thể đọc file ảnh: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JScrollPane createBookTable() {
        String[] columnNames = {"Mã sách", "Tên sách", "Thể loại", "Tác giả", "NXB", "Giá (VND)", "Số lượng", "Ảnh"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedBook();
            }
        });

        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        return new JScrollPane(bookTable);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(200, 255, 200));
        
        ActionListener controller = (ActionListener) new SachController(this);
        
        JButton btnAdd = new JButton("Thêm");
        btnAdd.addActionListener(controller);
        panel.add(btnAdd);

        JButton btnEdit = new JButton("Sửa");
        btnEdit.addActionListener(controller);
        panel.add(btnEdit);

        JButton btnDelete = new JButton("Xóa");
        btnDelete.addActionListener(controller);
        panel.add(btnDelete);

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(controller);
        panel.add(btnRefresh);

        return panel;
    }

    private void loadInitialData() {
        loadTheLoai();
        loadTacGia();
        loadNhaXuatBan();
        loadSach();
    }

    private void loadTheLoai() {
        try {
            cbTheLoai.removeAllItems();
            List<TheLoai> dsTheLoai = theLoaiDAO.getAll();
            for (TheLoai tl : dsTheLoai) {
                cbTheLoai.addItem(tl.getTenTheLoai());
                theLoaiMap.put(tl.getTenTheLoai(),tl.getMaTheLoai());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi load thể loại: " + e.getMessage());
        }
    }

    private void loadTacGia() {
        try {
            cbTacGia.removeAllItems();
            List<TacGia> dsTacGia = tacGiaDAO.getAll();
            for (TacGia tg : dsTacGia) {
                cbTacGia.addItem(tg.getTenTacGia());
                tacGiaMap.put(tg.getTenTacGia(), tg.getMaTacGia());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi load tác giả: " + e.getMessage());
        }
    }

    private void loadNhaXuatBan() {
        try {
            cbNhaXuatBan.removeAllItems();
            List<NhaXuatBan> dsNXB = nhaXuatBanDAO.getAll();
            for (NhaXuatBan nxb : dsNXB) {
                cbNhaXuatBan.addItem(nxb.getTenNXB());
                nxbMap.put(nxb.getTenNXB(), nxb.getMaNXB());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi load NXB: " + e.getMessage());
        }
    }

    public void loadSach() {
        try {
            tableModel.setRowCount(0); // Xóa dữ liệu cũ
            List<Sach> dsSach = sachDAO.getAll();
            
            for (Sach sach : dsSach) {
                // Kiểm tra null để tránh NullPointerException
                String tenTheLoai = (sach.getMaTheLoai() != null) ? sach.getMaTheLoai().getTenTheLoai() : "";
                String tenTacGia = (sach.getMaTacGia() != null) ? sach.getMaTacGia().getTenTacGia() : "";
                String tenNXB = (sach.getMaNxb() != null) ? sach.getMaNxb().getTenNXB() : "";
                
                tableModel.addRow(new Object[]{
                    sach.getMaSach(),
                    sach.getTenSach(),
                    tenTheLoai,
                    tenTacGia,
                    tenNXB,
                    sach.getGiaban(),
                    sach.getSoluong(),
                    sach.getImgpath()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi load sách: " + e.getMessage());
            e.printStackTrace(); // In stack trace để debug
        }
    }
    public void showSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            tfMaSach.setText(tableModel.getValueAt(selectedRow, 0).toString());
            tfTenSach.setText(tableModel.getValueAt(selectedRow, 1).toString());
            
            setSelectedItem(cbTheLoai, tableModel.getValueAt(selectedRow, 2).toString());
            setSelectedItem(cbTacGia, tableModel.getValueAt(selectedRow, 3).toString());
            setSelectedItem(cbNhaXuatBan, tableModel.getValueAt(selectedRow, 4).toString());
            
            tfGia.setText(tableModel.getValueAt(selectedRow, 5).toString());
            tfSoLuong.setText(tableModel.getValueAt(selectedRow, 6).toString());
            
            Object imageValue = tableModel.getValueAt(selectedRow, 7);
            String imagePath = (imageValue != null) ? imageValue.toString() : "";
            tfImagePath.setText(imagePath);
            currentImagePath = imagePath;
            
            if (imagePath != null && !imagePath.isEmpty()) {
                displayImage(imagePath);
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("Không có ảnh");
            }
        }
    }

    private void setSelectedItem(JComboBox<String> comboBox, String value) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(value)) {
                comboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    public void refreshFields() {
        tfMaSach.setText("");
        tfTenSach.setText("");
        cbTheLoai.setSelectedIndex(0);
        cbTacGia.setSelectedIndex(0);
        cbNhaXuatBan.setSelectedIndex(0);
        tfGia.setText("");
        tfSoLuong.setText("");
        tfImagePath.setText("");
        currentImagePath = null;
        imageLabel.setIcon(null);
        imageLabel.setText("");
        bookTable.clearSelection();
    }

    public Sach getSachFromInput() {
        Sach sach = new Sach();
        sach.setMaSach(tfMaSach.getText().trim());
        sach.setTenSach(tfTenSach.getText().trim());
        
        TheLoai tl = new TheLoai();
        String tenTheLoai = (String) cbTheLoai.getSelectedItem();
        tl.setTenTheLoai(tenTheLoai);
        tl.setMaTheLoai(getMaTheLoaiByTen(tenTheLoai));
        sach.setMaTheLoai(tl);
        
        TacGia tg = new TacGia();
        String tenTacGia = (String) cbTacGia.getSelectedItem();
        tg.setTenTacGia(tenTacGia);
        tg.setMaTacGia(getMaTacGiaByTen(tenTacGia)); 
        sach.setMaTacGia(tg); 
        
        NhaXuatBan nxb = new NhaXuatBan();
        String tenNXB = (String) cbNhaXuatBan.getSelectedItem();
        nxb.setTenNXB(tenNXB);
        nxb.setMaNXB(getMaNXBByTen(tenNXB));
        sach.setMaNxb(nxb); 
        
        try {
            sach.setGiaban(Double.parseDouble(tfGia.getText().trim()));
            sach.setSoluong(Integer.parseInt(tfSoLuong.getText().trim()));
        } catch (NumberFormatException e) {
            return null;
        }
        
        String standardizedPath = currentImagePath.replace("\\", "/");
        sach.setImgpath(standardizedPath);
        return sach;
    }
    public Sach getSachForAdd() throws SQLException {
        Sach sach = new Sach();
        sachDAO = new SachDAO();
        sach.setMaSach(sachDAO.generateMaSach());
        sach.setTenSach(tfTenSach.getText().trim());
        
        TheLoai tl = new TheLoai();
        String tenTheLoai = (String) cbTheLoai.getSelectedItem();
        tl.setTenTheLoai(tenTheLoai);
        tl.setMaTheLoai(getMaTheLoaiByTen(tenTheLoai));
        sach.setMaTheLoai(tl);
        
        TacGia tg = new TacGia();
        String tenTacGia = (String) cbTacGia.getSelectedItem();
        tg.setTenTacGia(tenTacGia);
        tg.setMaTacGia(getMaTacGiaByTen(tenTacGia)); 
        sach.setMaTacGia(tg); 
        
        NhaXuatBan nxb = new NhaXuatBan();
        String tenNXB = (String) cbNhaXuatBan.getSelectedItem();
        nxb.setTenNXB(tenNXB);
        nxb.setMaNXB(getMaNXBByTen(tenNXB));
        sach.setMaNxb(nxb); 
        
        try {
            sach.setGiaban(Double.parseDouble(tfGia.getText().trim()));
            sach.setSoluong(Integer.parseInt(tfSoLuong.getText().trim()));
        } catch (NumberFormatException e) {
            return null;
        }
        
        if (currentImagePath != null && !currentImagePath.isEmpty()) {
            String standardizedPath = currentImagePath.replace("\\", "/");
            sach.setImgpath(standardizedPath);
        } else {
            
            sach.setImgpath(tfImagePath.getText().trim());
        }

        return sach;
    }
    private String getMaTheLoaiByTen(String tenTheLoai) {
        if (tenTheLoai == null || tenTheLoai.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thể loại không được trống");
        }
        
        String maTheLoai = theLoaiMap.get(tenTheLoai);
        if (maTheLoai == null) {
            throw new RuntimeException("Không tìm thấy mã thể loại cho: " + tenTheLoai);
        }
        return maTheLoai;
    }

    private String getMaTacGiaByTen(String tenTacGia) {
        if (tenTacGia == null || tenTacGia.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên tác giả không được trống");
        }
        
        String maTacGia = tacGiaMap.get(tenTacGia);
        if (maTacGia == null) {
            throw new RuntimeException("Không tìm thấy mã tác giả cho: " + tenTacGia);
        }
        return maTacGia;
    }

    private String getMaNXBByTen(String tenNXB) {
        if (tenNXB == null || tenNXB.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên NXB không được trống");
        }
        
        String maNXB = nxbMap.get(tenNXB);
        if (maNXB == null) {
            throw new RuntimeException("Không tìm thấy mã NXB cho: " + tenNXB);
        }
        return maNXB;
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }

    public static void main(String[] args) {
    	try {
    	    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); // hoặc WindowsLookAndFeel
    	} catch (Exception ex) {
    	    ex.printStackTrace();
    	}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý sách");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);
            
            frame.add(new BookManager());
            frame.setVisible(true);
        });
    }
}