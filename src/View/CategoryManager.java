package View;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import DAO.TheLoaiDAO;
import controller.CategoryController;
import model.TheLoai;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.Border;

public class CategoryManager extends JPanel {
    public JTextField txtMaDanhMuc, txtTenDanhMuc, txtTimKiem;
    public JTable table;
    public DefaultTableModel tableModel;
    private JList<String> listGoiY;
    private DefaultListModel<String> listModel;
    private JPopupMenu popupMenu;
    private TheLoaiDAO dao;
    private TheLoai model;

    public CategoryManager() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Font font = new Font("Segoe UI", Font.PLAIN, 16);
        Color bgColor = new Color(245, 250, 255);
        Color panelColor = new Color(200, 255, 200);

        setBackground(bgColor);

        // Panel nhập liệu
        JPanel panelNhap = new JPanel(new GridLayout(2, 2, 10, 10));
        panelNhap.setBorder(BorderFactory.createTitledBorder("🔖 Thông tin danh mục"));
        panelNhap.setBackground(panelColor);
        panelNhap.setFont(font);

        JLabel lblMa = new JLabel("Mã danh mục:");
        lblMa.setFont(font);
        txtMaDanhMuc = new JTextField();
        txtMaDanhMuc.setFont(font);
        txtMaDanhMuc.setEditable(false);

        JLabel lblTen = new JLabel("Tên danh mục:");
        lblTen.setFont(font);
        txtTenDanhMuc = new JTextField();
        txtTenDanhMuc.setFont(font);

        panelNhap.add(lblMa);
        panelNhap.add(txtMaDanhMuc);
        panelNhap.add(lblTen);
        panelNhap.add(txtTenDanhMuc);

        // Panel tìm kiếm
        JPanel panelTimKiem = new JPanel(new BorderLayout(5, 5));
        panelTimKiem.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20), // giảm padding trong border
                "🔍 Tìm kiếm danh mục"
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
                    timKiemDanhMuc(selected);
                }
            }
        });

        JButton btnTim = new JButton("Tìm kiếm");
        btnTim.setIcon(new ImageIcon(getClass().getResource("/resources/icons/search.png")));

        btnTim.setFont(font);
        btnTim.addActionListener(e -> timKiemDanhMuc(txtTimKiem.getText()));

        panelTimKiem.add(txtTimKiem, BorderLayout.CENTER);
        panelTimKiem.add(btnTim, BorderLayout.EAST);
        // Bảng danh sách
        tableModel = new DefaultTableModel(new Object[]{" Mã Danh Mục", " Tên Danh Mục"}, 0) {
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
                    txtMaDanhMuc.setText(table.getValueAt(row, 0).toString());
                    txtTenDanhMuc.setText(table.getValueAt(row, 1).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        // Panel button
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelButton.setBackground(bgColor);

        String[] buttonLabels = {"Thêm", "Sửa", "Xóa", "Làm mới"};
        ActionListener ac = (ActionListener) new CategoryController(this);
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton btn = new JButton(buttonLabels[i]);
            btn.addActionListener(ac);
            btn.setFont(font);
            btn.setBackground(new Color(100, 150, 255));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setOpaque(true); // Cho phép vẽ nền
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
        List<String> dsDanhMuc = new ArrayList<String>();
        List<TheLoai> tl = TheLoaiDAO.getAll();
        for (TheLoai theLoai : tl) {
            dsDanhMuc.add(theLoai.getTenTheLoai());
        }
        
        for (String tenDM : dsDanhMuc) {
            if (tenDM.toLowerCase().contains(input.toLowerCase())) {
                listModel.addElement(tenDM);
            }
        }
        listGoiY.setVisibleRowCount(Math.min(listModel.size(), 5));
    }

    public void taiLaiDuLieu() {
        try {
            tableModel.setRowCount(0);
            List<TheLoai> listcate = TheLoaiDAO.getAll();
            for (TheLoai tl : listcate) {
                tableModel.addRow(new Object[] {
                    tl.getMaTheLoai(),
                    tl.getTenTheLoai()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TheLoai getTheLoaiForAdd() throws SQLException {
        TheLoai loai = new TheLoai();
        dao = new TheLoaiDAO();
        loai.setMaTheLoai(dao.generateMaTL());
        loai.setTenTheLoai(txtTenDanhMuc.getText().trim());
        return loai;
    }

    public TheLoai getTheLoaiForEdit() {
        TheLoai loai = new TheLoai();
        loai.setMaTheLoai(txtMaDanhMuc.getText().trim());
        loai.setTenTheLoai(txtTenDanhMuc.getText().trim());
        return loai;
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private void timKiemDanhMuc(String keyword) {
        tableModel.setRowCount(0);
        List<TheLoai> tl = TheLoaiDAO.getAll();
        for (TheLoai theLoai : tl) {
            if(keyword.equals(theLoai.getTenTheLoai())) {
                tableModel.addRow(new Object[] {
                        theLoai.getMaTheLoai(),
                        theLoai.getTenTheLoai()
                });
            }
        }
    }
   
    public void lamMoiForm() {
        txtMaDanhMuc.setText("");
        txtTenDanhMuc.setText("");
        table.clearSelection();
    }
}