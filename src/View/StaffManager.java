package View;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import DAO.NhanVienDAO;
import controller.StaffController;
import model.NhanVien;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StaffManager extends JPanel {
    public JTextField txtMaNV, txtTenNV, txtDiaChi, txtSDT, txtEmail, txtNgaySinh, txtLuong, txtTimKiem;
    public JDateChooser ngaySinh;
    public JTable table;
    public DefaultTableModel tableModel;
    private JList<String> listGoiY;
    private DefaultListModel<String> listModel;
    private JPopupMenu popupMenu;
    private NhanVienDAO dao;
    public StaffManager() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Font font = new Font("Segoe UI", Font.PLAIN, 16);
        Color bgColor = new Color(245, 250, 255);
        Color panelColor = new Color(200, 255, 200);

        setBackground(bgColor);

        // Panel nhập liệu
        JPanel panelNhap = new JPanel(new GridLayout(4, 2, 10, 10));
        panelNhap.setBorder(BorderFactory.createTitledBorder("👤 Thông tin nhân viên"));
        panelNhap.setBackground(panelColor);
        panelNhap.setFont(font);

        // Thêm các trường thông tin nhân viên
        panelNhap.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField();
        txtMaNV.setFont(font);
        txtMaNV.setEditable(false);
        panelNhap.add(txtMaNV);

        panelNhap.add(new JLabel("Tên NV:"));
        txtTenNV = new JTextField();
        txtTenNV.setFont(font);
        panelNhap.add(txtTenNV);

        panelNhap.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        txtDiaChi.setFont(font);
        panelNhap.add(txtDiaChi);

        panelNhap.add(new JLabel("SĐT:"));
        txtSDT = new JTextField();
        txtSDT.setFont(font);
        panelNhap.add(txtSDT);

        panelNhap.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        txtEmail.setFont(font);
        panelNhap.add(txtEmail);

        panelNhap.add(new JLabel("Ngày sinh:"));
        ngaySinh = new JDateChooser();
        ngaySinh.setFont(font);
        panelNhap.add(ngaySinh);

        panelNhap.add(new JLabel("Lương(VND):"));
        txtLuong = new JTextField();
        txtLuong.setFont(font);
        panelNhap.add(txtLuong);

        // Panel tìm kiếm
        JPanel panelTimKiem = new JPanel(new BorderLayout(5, 5));
        panelTimKiem.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                "🔍 Tìm kiếm nhân viên"
        ));
        panelTimKiem.setBackground(panelColor);
        
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
                    timKiemNhanVien(selected);
                }
            }
        });

        JButton btnTim = new JButton("Tìm kiếm");
        btnTim.setIcon(new ImageIcon(getClass().getResource("/resources/icons/search.png")));
        btnTim.setFont(font);
        btnTim.addActionListener(e -> timKiemNhanVien(txtTimKiem.getText()));

        panelTimKiem.add(txtTimKiem, BorderLayout.CENTER);
        panelTimKiem.add(btnTim, BorderLayout.EAST);

        tableModel = new DefaultTableModel(new Object[]{"Mã NV", "Tên NV", "Địa chỉ", "SĐT", "Email", "Ngày sinh", "Lương"}, 0) {
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

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaNV.setText(table.getValueAt(row, 0).toString());
                    txtTenNV.setText(table.getValueAt(row, 1).toString());
                    txtDiaChi.setText(table.getValueAt(row, 2).toString());
                    txtSDT.setText(table.getValueAt(row, 3).toString());
                    txtEmail.setText(table.getValueAt(row, 4).toString());
                    ngaySinh.setDate((Date) table.getValueAt(row, 5));
                    txtLuong.setText(table.getValueAt(row, 6).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        // Panel button
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelButton.setBackground(bgColor);

        String[] buttonLabels = {"Thêm", "Sửa", "Xóa", "Làm mới"};
        ActionListener ac = (ActionListener) new StaffController(this);
        for (String label : buttonLabels) {
            JButton btn = new JButton(label);
            btn.addActionListener(ac);
            btn.setFont(font);
            btn.setBackground(new Color(100, 150, 255));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setOpaque(true);
            btn.setContentAreaFilled(true); 
            btn.setPreferredSize(new Dimension(120, 35));
            panelButton.add(btn);
        }

        JPanel topPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        topPanel.setBackground(bgColor);
        topPanel.add(panelNhap);
        topPanel.add(panelTimKiem);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);
        
        taiLaiDuLieu();
    }

    public int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }

    private void capNhatDanhSachGoiY(String input) {
        listModel.clear();
        dao = new NhanVienDAO();
        List<String> dsNhanVien = new ArrayList<>();
        List<NhanVien> nvList = dao.getAll();
        for (NhanVien nv : nvList) {
            dsNhanVien.add(nv.getTenNV());
        }
        
        for (String tenNV : dsNhanVien) {
            if (tenNV.toLowerCase().contains(input.toLowerCase())) {
                listModel.addElement(tenNV);
            }
        }
        listGoiY.setVisibleRowCount(Math.min(listModel.size(), 5));
    }

    public void taiLaiDuLieu() {
    	dao = new NhanVienDAO();
        try {
            tableModel.setRowCount(0);
            List<NhanVien> listNV = dao.getAll();
            for (NhanVien nv : listNV) {
                tableModel.addRow(new Object[] {
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getDiaChi(),
                    nv.getSoDienThoai(),
                    nv.getEmail(),
                    nv.getNgaySinh(),
                    String.format("%,.0f", nv.getLuong())
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NhanVien getNhanVien() throws SQLException {
        NhanVien nv = new NhanVien();
        dao = new NhanVienDAO();
        if(txtMaNV == null) {
        	String Manv = dao.sinhMaNV();
            nv.setMaNV(Manv);
        }
        else{
        	nv.setMaNV(txtMaNV.getText().trim());
        }
        nv.setTenNV(txtTenNV.getText().trim());
        nv.setDiaChi(txtDiaChi.getText().trim());
        nv.setSoDienThoai(txtSDT.getText().trim());
        nv.setEmail(txtEmail.getText().trim());
        Date utilDate = ngaySinh.getDate();
        if (utilDate != null) {
            nv.setNgaySinh(new java.sql.Date(utilDate.getTime()));
        } else {
            nv.setNgaySinh(null);
        }
        try {
            nv.setLuong(Double.parseDouble(txtLuong.getText().trim()));
        } catch (NumberFormatException e) {
            nv.setLuong(0);
        }
        return nv;
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private void timKiemNhanVien(String keyword) {
        tableModel.setRowCount(0);
        dao = new NhanVienDAO();
        List<NhanVien> nvList = dao.getAll();
        for (NhanVien nv : nvList) {
            if (nv.getTenNV().equalsIgnoreCase(keyword)) {
                tableModel.addRow(new Object[] {
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getDiaChi(),
                    nv.getSoDienThoai(),
                    nv.getEmail(),
                    nv.getNgaySinh(),
                    String.format("%,.0f VND", nv.getLuong())
                });
            }
        }
    }
   
    public void lamMoiForm() {
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        ngaySinh.setDate(null);
        txtLuong.setText("");
        table.clearSelection();
    }
}