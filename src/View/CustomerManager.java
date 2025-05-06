package View;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.border.Border;

import DAO.KhachHangDAO;
import controller.KhachHangController;
import model.KhachHang;

public class CustomerManager extends JPanel {
    public JTextField txtMaKH, txtTenKH, txtSDT, txtEmail, txtDiaChi, txtNgaySinh, txtTimKiem;
    public JTable table;
    public DefaultTableModel tableModel;
    private JList<String> listGoiY;
    private DefaultListModel<String> listModel;
    private JPopupMenu popupMenu;
    private KhachHangController controller;
    private KhachHangDAO dao;

    public CustomerManager() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controller = new KhachHangController(this);
        
        // Font và màu sắc
        Font font = new Font("Segoe UI", Font.PLAIN, 16);
        Color bgColor = new Color(245, 250, 255);
        Color panelColor = new Color(200, 255, 200);
        setBackground(bgColor);

        // Panel nhập liệu
        JPanel panelNhap = new JPanel(new GridLayout(3, 4, 10, 10));
        panelNhap.setBorder(BorderFactory.createTitledBorder("👤 Thông tin khách hàng"));
        panelNhap.setBackground(panelColor);
        panelNhap.setFont(font);

        // Các trường nhập liệu
        JLabel lblMaKH = new JLabel("Mã KH:");
        lblMaKH.setFont(font);
        txtMaKH = new JTextField();
        txtMaKH.setFont(font);

        JLabel lblTenKH = new JLabel("Tên KH:");
        lblTenKH.setFont(font);
        txtTenKH = new JTextField();
        txtTenKH.setFont(font);

        JLabel lblSDT = new JLabel("Số ĐT:");
        lblSDT.setFont(font);
        txtSDT = new JTextField();
        txtSDT.setFont(font);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(font);
        txtEmail = new JTextField();
        txtEmail.setFont(font);

        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setFont(font);
        txtDiaChi = new JTextField();
        txtDiaChi.setFont(font);

        JLabel lblNgaySinh = new JLabel("Ngày sinh (yyyy-mm-dd):");
        lblNgaySinh.setFont(font);
        txtNgaySinh = new JTextField();
        txtNgaySinh.setFont(font);

        // Thêm các thành phần vào panel nhập liệu
        panelNhap.add(lblMaKH);
        panelNhap.add(txtMaKH);
        panelNhap.add(lblTenKH);
        panelNhap.add(txtTenKH);
        panelNhap.add(lblSDT);
        panelNhap.add(txtSDT);
        panelNhap.add(lblEmail);
        panelNhap.add(txtEmail);
        panelNhap.add(lblDiaChi);
        panelNhap.add(txtDiaChi);
        panelNhap.add(lblNgaySinh);
        panelNhap.add(txtNgaySinh);

        // Panel tìm kiếm
        JPanel panelTimKiem = new JPanel(new BorderLayout(5, 5));
        panelTimKiem.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                "🔍 Tìm kiếm khách hàng"
        ));
        panelTimKiem.setBackground(panelColor);
        
        txtTimKiem = new JTextField();
        txtTimKiem.setFont(font);
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập mã, tên, SĐT hoặc email...");

        listModel = new DefaultListModel<>();
        listGoiY = new JList<>(listModel);
        listGoiY.setFont(font);
        popupMenu = new JPopupMenu();
        popupMenu.add(new JScrollPane(listGoiY));

        // Sự kiện tìm kiếm khi nhập
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
                    controller.timKiemKhachHang();
                }
            }
        });

        JButton btnTim = new JButton("Tìm kiếm");
        btnTim.setFont(font);
        btnTim.addActionListener(e -> controller.timKiemKhachHang());

        panelTimKiem.add(txtTimKiem, BorderLayout.CENTER);
        panelTimKiem.add(btnTim, BorderLayout.EAST);

        // Bảng danh sách
        String[] columnNames = {"Mã KH", "Tên KH", "Số ĐT", "Email", "Địa chỉ", "Ngày sinh"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setFont(font);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(Color.GRAY);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        // Sự kiện chọn hàng trong bảng
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaKH.setText(table.getValueAt(row, 0).toString());
                    txtTenKH.setText(table.getValueAt(row, 1).toString());
                    txtSDT.setText(table.getValueAt(row, 2).toString());
                    txtEmail.setText(table.getValueAt(row, 3).toString());
                    txtDiaChi.setText(table.getValueAt(row, 4).toString());
                    txtNgaySinh.setText(table.getValueAt(row, 5).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        // Panel button
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelButton.setBackground(bgColor);

        String[] buttonLabels = {"Thêm", "Sửa", "Xóa", "Làm mới"};
        for (String label : buttonLabels) {
            JButton btn = new JButton(label);
            btn.addActionListener(controller);
            btn.setFont(font);
            btn.setBackground(new Color(100, 150, 255));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(120, 35));
            panelButton.add(btn);
        }

        // Sắp xếp các panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        topPanel.setBackground(bgColor);
        topPanel.add(panelNhap);
        topPanel.add(panelTimKiem);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);

        // Tải dữ liệu ban đầu
        controller.loadAllKhachHang();
    }

    private void capNhatDanhSachGoiY(String input) {
    	this.dao = new KhachHangDAO();
        listModel.clear();
        List<KhachHang> dsKhachHang = dao.getAllKhachHang();
        
        for (KhachHang kh : dsKhachHang) {
            if (kh.getTenKH().toLowerCase().contains(input.toLowerCase()) ||
                kh.getMaKH().toLowerCase().contains(input.toLowerCase()) ||
                kh.getSoDienThoai().toLowerCase().contains(input.toLowerCase()) ||
                kh.getEmail().toLowerCase().contains(input.toLowerCase())) {
                listModel.addElement(kh.getTenKH());
            }
        }
        listGoiY.setVisibleRowCount(Math.min(listModel.size(), 5));
    }

    public void loadTable(List<KhachHang> list) {
        tableModel.setRowCount(0);
        for (KhachHang kh : list) {
            tableModel.addRow(new Object[]{
                kh.getMaKH(),
                kh.getTenKH(),
                kh.getSoDienThoai(),
                kh.getEmail(),
                kh.getDiaChi(),
                kh.getNgaySinh()
            });
        }
    }

    public void clearFields() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtNgaySinh.setText("");
        txtTimKiem.setText("");
        table.clearSelection();
    }

    public int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // Getter methods
    public String getMaKH() { return txtMaKH.getText(); }
    public String getTenKH() { return txtTenKH.getText(); }
    public String getSDT() { return txtSDT.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public String getDiaChi() { return txtDiaChi.getText(); }
    public String getNgaySinh() { return txtNgaySinh.getText(); }
    public String getTimKiem() { return txtTimKiem.getText(); }
    public JTable getTable() { return table; }
}