package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import DAO.ThongKeDAO;
import model.ThongKeDoanhThu;
import model.ThongKeKhachHang;
import model.ThongKeSanPham;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StatisticManager extends JPanel {
    private Color primaryColor = new Color(0, 102, 204); 
    private Color secondaryColor = new Color(242, 242, 242); 
    private Color accentColor = new Color(255, 153, 51);
    private DefaultTableModel model;
    private JComboBox<String> cbxLoaiThongKe;
	private JDateChooser dateFrom;
	private JDateChooser dateTo; 
    private ThongKeDAO dao;
    public StatisticManager() throws SQLException {
        setLayout(new BorderLayout(10, 10));
        setBackground(secondaryColor);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        initUI();
        loadData();
    }
    
    private void initUI() {
        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel lblTitle = new JLabel("THỐNG KÊ BÁN HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        
        // ===== CONTROL PANEL =====
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Loại thống kê
        JLabel lblLoaiTK = new JLabel("Loại thống kê:");
        lblLoaiTK.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        cbxLoaiThongKe = new JComboBox<>(new String[]{
            "Doanh thu theo ngày", 
            "Sản phẩm bán chạy", 
            "Khách hàng thân thiết"
        });
        cbxLoaiThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbxLoaiThongKe.setBackground(Color.WHITE);
        
        // Ngày thống kê
        JLabel lblFrom = new JLabel("Từ ngày:");
        lblFrom.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateFrom.setDate(new Date());
        dateFrom.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JLabel lblTo = new JLabel("Đến ngày:");
        lblTo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        dateTo.setDate(new Date());
        dateTo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Nút thống kê
        JButton btnThongKe = new JButton("THỐNG KÊ");
        btnThongKe.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThongKe.setBackground(accentColor);
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setFocusPainted(false);
        btnThongKe.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnThongKe.addActionListener(e -> {
            try {
                thucHienThongKe(e);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi thống kê: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Thêm components vào control panel
        controlPanel.add(lblLoaiTK);
        controlPanel.add(cbxLoaiThongKe);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(lblFrom);
        controlPanel.add(dateFrom);
        controlPanel.add(lblTo);
        controlPanel.add(dateTo);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(btnThongKe);
        
        // ===== DATA PANEL =====
        JPanel dataPanel = new JPanel(new BorderLayout());
        dataPanel.setBackground(Color.WHITE);
        dataPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Bảng dữ liệu
        model = new DefaultTableModel(
            new Object[]{"Ngày", "Số hóa đơn", "Doanh thu", "Lợi nhuận"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setGridColor(new Color(220, 220, 220));
        
        // Style header table
        JTableHeader header = table.getTableHeader();
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(table);
        dataPanel.add(scrollPane, BorderLayout.CENTER);
        
        // ===== SUMMARY PANEL =====
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JPanel summaryBox = new JPanel(new GridLayout(1, 3, 15, 0));
        summaryBox.setBackground(new Color(240, 240, 240));
        summaryBox.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        dao = new ThongKeDAO();
        JPanel totalRevenuePanel = createSummaryItem("TỔNG DOANH THU", dao.RevueSum() + "đ", primaryColor);
        
        JPanel totalInvoicePanel = createSummaryItem("TỔNG HÓA ĐƠN", dao.OrderSum()+"", new Color(229, 57, 53));
        
        summaryBox.add(totalRevenuePanel);
        summaryBox.add(totalInvoicePanel);
        summaryPanel.add(summaryBox);
        
        // ===== ADD TO MAIN PANEL =====
        add(headerPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(dataPanel, BorderLayout.CENTER);
        bottomPanel.add(summaryPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSummaryItem(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValue.setForeground(Color.WHITE);
        
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void thucHienThongKe(ActionEvent e) throws SQLException {
        String loaiTK = (String) cbxLoaiThongKe.getSelectedItem();
        Date from = dateFrom.getDate();
        Date to = dateTo.getDate();
        
        switch(loaiTK) {
            case "Doanh thu theo ngày":
                thongKeDoanhThu(from, to);
                break;
            case "Sản phẩm bán chạy":
                thongKeSanPham(from, to);
                break;
            case "Khách hàng thân thiết":
                thongKeKhachHang(from, to);
                break;
        }
    }
    
    private void thongKeDoanhThu(Date from, Date to) throws SQLException {
        model.setColumnIdentifiers(new String[]{"Ngày", "Số lượng hóa đơn", "Tổng doanh thu"});
        model.setRowCount(0); // Xóa dữ liệu cũ

        // Chuyển đổi từ java.util.Date sang java.sql.Date
        java.util.Date utilDateFrom = dateFrom.getDate();
        java.util.Date utilDateTo = dateTo.getDate();

        java.sql.Date sqlDateFrom = new java.sql.Date(utilDateFrom.getTime());
        java.sql.Date sqlDateTo = new java.sql.Date(utilDateTo.getTime());

        // Gọi DAO để lấy dữ liệu thống kê
        List<ThongKeDoanhThu> list = dao.thongKeDoanhThu(sqlDateFrom, sqlDateTo);

        // Format ngày và tiền
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("#,### đ");

        // Đổ dữ liệu vào bảng
        for (ThongKeDoanhThu tk : list) {
            model.addRow(new Object[]{
                sdf.format(tk.getNgay()),
                tk.getSoLuongHoaDon(),
                df.format(tk.getTongDoanhThu())
            });
        }
    }
    
    private void thongKeSanPham(Date from, Date to) throws SQLException {
        model.setColumnIdentifiers(new String[]{"Mã sách", "Tên sách", "Số lượng bán", "Doanh thu"});
        model.setRowCount(0);
        java.util.Date utilDateFrom = dateFrom.getDate();
        java.util.Date utilDateTo = dateTo.getDate();

        java.sql.Date sqlDateFrom = new java.sql.Date(utilDateFrom.getTime());
        java.sql.Date sqlDateTo = new java.sql.Date(utilDateTo.getTime());

        List<ThongKeSanPham> list = dao.thongKeSanPham(sqlDateFrom, sqlDateTo,5);

        

        // Đổ dữ liệu vào bảng
        for (ThongKeSanPham tk : list) {
            model.addRow(new Object[]{
                tk.getMaSach(),
                tk.getTenSach(),
                tk.getSoLuongBan(),
                tk.getDoanhThu()
            });
        }
    }
    
    private void thongKeKhachHang(Date from, Date to) throws SQLException {
        model.setColumnIdentifiers(new String[]{"Mã KH", "Tên KH", "Số hóa đơn", "Tổng tiền mua"});
        model.setRowCount(0);
        
        java.util.Date utilDateFrom = dateFrom.getDate();
        java.util.Date utilDateTo = dateTo.getDate();

        java.sql.Date sqlDateFrom = new java.sql.Date(utilDateFrom.getTime());
        java.sql.Date sqlDateTo = new java.sql.Date(utilDateTo.getTime());

        List<ThongKeKhachHang> list = dao.thongKeKhachHang(sqlDateFrom, sqlDateTo,5);

        

        // Đổ dữ liệu vào bảng
        for (ThongKeKhachHang tk : list) {
            model.addRow(new Object[]{
                tk.getMaKH(),
                tk.getTenKH(),
                tk.getSoHoaDon(),
                tk.getTongTienMua()
            });
        }    }
    
    private void loadData() throws SQLException {
        // Mặc định load thống kê doanh thu
        thongKeDoanhThu(new Date(), new Date());
    }
}