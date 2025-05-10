 package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import DAO.HoaDonDAO;
import DAO.KhachHangDAO;
import DAO.SachDAO;
import controller.OrderController;
import model.ChiTietHoaDon;
import model.HoaDon;
import model.KhachHang;
import model.Sach;
import model.Session;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.swing.border.*;
import java.util.Date;
import java.util.List;

public class OrderManager extends JPanel {
    public JTextField txtSearchBook,txtname,txtsdt,txtdc;
    public JTable tableBooks, tableCart, tableOrders;
    public DefaultTableModel modelBooks, modelCart, modelOrders;
    public JSpinner spinnerQuantity;
    public JButton btnAddToCart, btnRemoveFromCart, btnSave, btnClear, btnSearchBook,btnaddCus;
    public JLabel lblOrderDate, lblTotal,lblname,lblsdt,lbldc;
    private JTabbedPane tabbedPane;
    public String customerName = "";
    public String customerPhone = "";
    private String customerAddress = "";
    private SachDAO dao;
	private KhachHangDAO khdao;
	private HoaDonDAO hddao;
	public double tongTien;
	ActionListener ac = (ActionListener) new OrderController(this);
    public OrderManager() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Color bgColor = new Color(200, 255, 200);
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        setBackground(bgColor);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(bgColor);
        
        // Create panels for each tab
        JPanel orderPanel = createOrderPanel(bgColor, font);
        JPanel viewOrdersPanel = createViewOrdersPanel(bgColor, font);
        
        // Add tabs
        tabbedPane.addTab("Tạo hóa đơn", new ImageIcon(), orderPanel);
        tabbedPane.addTab("Danh sách hóa đơn", new ImageIcon(), viewOrdersPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        addbooksTotable();
        xoaSachChon();
        loadDanhSachHoaDon();
//        xemChiTietHoaDon();
    }
    private JPanel createOrderPanel(Color bgColor, Font font) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(bgColor);

        // Top: Order Info (only date)
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        panelTop.setBackground(bgColor);
        panelTop.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        lblOrderDate = new JLabel("Ngày lập: " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        lblOrderDate.setFont(font);
        panelTop.add(lblOrderDate);
        panel.add(panelTop, BorderLayout.NORTH);

        // Center: Split left (book list) and right (cart)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(500);
        splitPane.setBorder(null);

        // Left: Book Search & List
        JPanel panelLeft = new JPanel(new BorderLayout(10, 10));
        panelLeft.setBackground(bgColor);
        panelLeft.setBorder(BorderFactory.createTitledBorder("Tìm kiếm & chọn sách"));
        JPanel panelSearch = new JPanel(new BorderLayout(5, 5));
        panelSearch.setBackground(bgColor);
        txtSearchBook = new JTextField();
        txtSearchBook.setFont(font);
        txtSearchBook.setPreferredSize(new Dimension(200, 30));
        btnSearchBook = new JButton("Tìm kiếm");
        btnSearchBook.setFont(font);
        btnSearchBook.setBackground(new Color(100, 150, 255));
        btnSearchBook.setForeground(Color.WHITE);
        btnSearchBook.setFocusPainted(false);
        panelSearch.add(txtSearchBook, BorderLayout.CENTER);
        panelSearch.add(btnSearchBook, BorderLayout.EAST);
        panelLeft.add(panelSearch, BorderLayout.NORTH);

        String[] bookCols = {"Mã sách", "Tên sách", "Tác giả", "Giá"};
        modelBooks = new DefaultTableModel(bookCols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableBooks = new JTable(modelBooks);
        tableBooks.setFont(font);
        tableBooks.setRowHeight(24);
        tableBooks.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scrollBooks = new JScrollPane(tableBooks);
        panelLeft.add(scrollBooks, BorderLayout.CENTER);

        JPanel panelAdd = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelAdd.setBackground(bgColor);
        panelAdd.add(new JLabel("Số lượng:")).setFont(font);
        spinnerQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinnerQuantity.setFont(font);
        panelAdd.add(spinnerQuantity);
        btnAddToCart = new JButton("Thêm vào hóa đơn");
        btnAddToCart.setFont(font);
        btnAddToCart.setBackground(new Color(100, 150, 255));
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setFocusPainted(false);
        btnAddToCart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					chuyenSachVaoHoaDon();
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		});
        panelAdd.add(btnAddToCart);
        btnaddCus = new JButton("Khách Hàng");
        btnaddCus.setFont(font);
        btnaddCus.setBackground(new Color(100, 150, 255));
        btnaddCus.setForeground(Color.WHITE);
        panelLeft.add(panelAdd, BorderLayout.SOUTH);
        panelAdd.add(btnaddCus);
        splitPane.setLeftComponent(panelLeft);
        
        btnaddCus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame addcus = new JFrame("Thêm khách hàng");
                addcus.setSize(500, 350); // Tăng chiều cao để chứa phần tìm kiếm
                addcus.setLocationRelativeTo(null);

                JPanel panel = new JPanel(new BorderLayout(10, 10));
                panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                Font font = new Font("Segoe UI", Font.BOLD, 16);

                // ==== Panel tìm kiếm khách hàng theo SĐT ====
                JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel lblSearch = new JLabel("Tìm theo SĐT:");
                lblSearch.setFont(font);
                JTextField txtSearch = new JTextField(15);
                txtSearch.setFont(font);
                JButton btnSearch = new JButton("Tìm");
                btnSearch.setFont(font);
                btnSearch.setBackground(new Color(100, 150, 255));
                btnSearch.setForeground(Color.WHITE);

                searchPanel.add(lblSearch);
                searchPanel.add(txtSearch);
                searchPanel.add(btnSearch);

                // ==== Panel thông tin khách hàng ====
                JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 10));
                lblname = new JLabel("Tên khách hàng:");
                lblname.setFont(font);
                txtname = new JTextField();
                txtname.setFont(font);
                lblsdt = new JLabel("Số điện thoại:");
                lblsdt.setFont(font);
                txtsdt = new JTextField();
                txtsdt.setFont(font);
                lbldc = new JLabel("Địa chỉ:");
                lbldc.setFont(font);
                txtdc = new JTextField();
                txtdc.setFont(font);

                infoPanel.add(lblname); infoPanel.add(txtname);
                infoPanel.add(lblsdt); infoPanel.add(txtsdt);
                infoPanel.add(lbldc); infoPanel.add(txtdc);

                // ==== Panel nút xác nhận ====
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton btnConfirm = new JButton("Xác nhận");
                btnConfirm.setFont(font);
                btnConfirm.setBackground(new Color(100, 150, 255));
                btnConfirm.setForeground(Color.WHITE);

                btnConfirm.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        customerName = txtname.getText();
                        customerPhone = txtsdt.getText();
                        customerAddress = txtdc.getText();

                        if (customerName.isEmpty() || customerPhone.isEmpty()) {
                            JOptionPane.showMessageDialog(addcus,
                                "Vui lòng nhập ít nhất tên và số điện thoại khách hàng!",
                                "Thông báo", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        JOptionPane.showMessageDialog(addcus,
                            "Đã thêm thông tin khách hàng thành công!",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        addcus.dispose();
                    }
                });

                buttonPanel.add(btnConfirm);

                btnSearch.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String sdt = txtSearch.getText().trim();
                        if (sdt.isEmpty()) {
                            JOptionPane.showMessageDialog(addcus, 
                                "Vui lòng nhập số điện thoại để tìm!", 
                                "Thông báo", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        KhachHang kh = null;
						try {
							kh = KhachHangDAO.findCustomerByPhone(sdt);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
                        if (kh != null) {
                            txtname.setText(kh.getTenKH());
                            txtsdt.setText(kh.getSoDienThoai());
                            txtdc.setText(kh.getDiaChi());
                            JOptionPane.showMessageDialog(addcus,
                                "Đã tìm thấy khách hàng!", 
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(addcus,
                                "Không tìm thấy khách hàng!", 
                                "Thông báo", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });

                // ==== Thêm các panel vào frame chính ====
                panel.add(searchPanel, BorderLayout.NORTH);
                panel.add(infoPanel, BorderLayout.CENTER);
                panel.add(buttonPanel, BorderLayout.SOUTH);

                addcus.add(panel);
                addcus.setVisible(true);
            }
        });
        JPanel panelRight = new JPanel(new BorderLayout(10, 10));
        panelRight.setBackground(bgColor);
        panelRight.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn"));
        String[] cartCols = {"Mã sách", "Tên sách", "Số lượng", "Đơn giá", "Thành tiền", ""};
        modelCart = new DefaultTableModel(cartCols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableCart = new JTable(modelCart);
        tableCart.setFont(font);
        tableCart.setRowHeight(24);
        tableCart.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        int[] widths = {80, 200, 70, 100, 120, 40};
        for (int i = 0; i < widths.length; i++) {
            if (i < tableCart.getColumnCount()) {
                tableCart.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }
        }
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tableCart.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableCart.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableCart.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tableCart.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        JScrollPane scrollCart = new JScrollPane(tableCart);
        panelRight.add(scrollCart, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panelBottom.setBackground(bgColor);
        lblTotal = new JLabel("Tổng cộng: 0 đ");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelBottom.add(lblTotal);
        btnSave = new JButton("Lưu hóa đơn");
        btnSave.setFont(font);
        btnSave.setBackground(new Color(100, 150, 255));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(ac);
        panelBottom.add(btnSave);
        btnClear = new JButton("Làm mới");
        btnClear.setFont(font);
        btnClear.setBackground(new Color(100, 150, 255));
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(ac);
        panelBottom.add(btnClear);
        panelRight.add(panelBottom, BorderLayout.SOUTH);
        splitPane.setRightComponent(panelRight);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
        
    }
    
    private void addbooksTotable() {
    	try {
    	dao = new SachDAO();
    	modelBooks.setRowCount(0);
    	List<Sach> list = dao.getAll();
    	for (Sach sach : list) {
			modelBooks.addRow(new Object[] {
					sach.getMaSach(),
					sach.getTenSach(),
					sach.getMaTacGia().getTenTacGia(),
					sach.getGiaban()
			});
		}}
    	catch (Exception e) {
		
		}
    }
    private JPanel createViewOrdersPanel(Color bgColor, Font font) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(bgColor);
        JTextField txtSearchOrder = new JTextField(20);
        txtSearchOrder.setFont(font);
        JButton btnSearchOrder = new JButton("Tìm kiếm");
        btnSearchOrder.setFont(font);
        btnSearchOrder.setBackground(new Color(100, 150, 255));
        btnSearchOrder.setForeground(Color.WHITE);
        btnSearchOrder.setFocusPainted(false);
        searchPanel.add(new JLabel("Tìm hóa đơn:"));
        searchPanel.add(txtSearchOrder);
        searchPanel.add(btnSearchOrder);
        panel.add(searchPanel, BorderLayout.NORTH);

        String[] orderCols = {"Mã hóa đơn", "Ngày lập", "Tổng tiền", "Trạng thái", "Chi tiết"};
        modelOrders = new DefaultTableModel(orderCols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableOrders = new JTable(modelOrders);
        tableOrders.setFont(font);
        tableOrders.setRowHeight(24);
        tableOrders.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scrollOrders = new JScrollPane(tableOrders);
        panel.add(scrollOrders, BorderLayout.CENTER);

        // Bottom buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(bgColor);
        JButton btnViewDetails = new JButton("Xem chi tiết");
        btnViewDetails.setFont(font);
        btnViewDetails.setBackground(new Color(100, 150, 255));
        btnViewDetails.setForeground(Color.WHITE);
        btnViewDetails.setFocusPainted(false);
        buttonPanel.add(btnViewDetails);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnViewDetails.addActionListener(e -> {
            int selectedRow = tableOrders.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn hóa đơn để xem chi tiết!");
                return;
            }
            String maHD = tableOrders.getValueAt(selectedRow, 0).toString();
            new OrderController(this).xemChiTietHoaDon(maHD);
        });

        return panel;
    }
    public void showOrderDetailDialog(Component parent, String storeName, String orderTime, String personInCharge, String customer, String[][] orderItems, String total, Font font) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Chi tiết hóa đơn", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(600, 450);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(200, 255, 200));

        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        infoPanel.setBackground(new Color(200, 255, 200));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        JLabel lblStore = new JLabel("Tên cửa hàng: ");
        lblStore.setFont(font);
        JLabel lblStoreVal = new JLabel(storeName);
        lblStoreVal.setFont(font);
        JLabel lblTime = new JLabel("Thời gian lập: ");
        lblTime.setFont(font);
        JLabel lblTimeVal = new JLabel(orderTime);
        lblTimeVal.setFont(font);
        JLabel lblPerson = new JLabel("Người lập: ");
        lblPerson.setFont(font);
        JLabel lblPersonVal = new JLabel(personInCharge);
        lblPersonVal.setFont(font);
        JLabel lblCustomer = new JLabel("Khách hàng: ");
        lblCustomer.setFont(font);
        JLabel lblCustomerVal = new JLabel(customer);
        lblCustomerVal.setFont(font);
        infoPanel.add(lblStore); infoPanel.add(lblStoreVal);
        infoPanel.add(lblTime); infoPanel.add(lblTimeVal);
        infoPanel.add(lblPerson); infoPanel.add(lblPersonVal);
        infoPanel.add(lblCustomer); infoPanel.add(lblCustomerVal);
        dialog.add(infoPanel, BorderLayout.NORTH);

        // Table of order items
        String[] itemCols = {"Mã sách", "Tên sách", "Số lượng", "Đơn giá", "Thành tiền"};
        JTable itemTable = new JTable(orderItems, itemCols);
        itemTable.setFont(font);
        itemTable.setRowHeight(22);
        itemTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(itemTable);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Total
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(new Color(200, 255, 200));
        JLabel lblTotal = new JLabel("Tổng cộng: " + total);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalPanel.add(lblTotal);
        dialog.add(totalPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
    public void reset() {

        customerName = "";
        customerPhone = "";
        customerAddress = "";
        txtname.setText("");
        txtsdt.setText("");
        txtdc.setText("");
        modelCart.setRowCount(0);
        lblTotal.setText("Tổng cộng: 0 đ");
        spinnerQuantity.setValue(1);
        tableBooks.clearSelection();
        txtSearchBook.requestFocus();
    }
    private double tinhTongTien() {
        tongTien = 0;
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            String thanhTienStr = modelCart.getValueAt(i, 4).toString().replaceAll("[^\\d]", "");
            tongTien += Double.parseDouble(thanhTienStr);
        }
        return tongTien;
    }
    private void capNhatTongTien() {
        tongTien = 0;
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            String thanhTienStr = modelCart.getValueAt(i, 4).toString().replaceAll("[^\\d]", "");
            tongTien += Double.parseDouble(thanhTienStr);
        }
        lblTotal.setText("Tổng cộng: " + String.format("%,.0f đ", tongTien));
    }
    public int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }
    public void xoaSachChon() {
        // Lắng nghe sự kiện click vào bảng
        tableCart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableCart.getSelectedRow();
                if (selectedRow != -1) {
                    // Hiển thị hộp thoại xác nhận
                    int option = JOptionPane.showConfirmDialog(
                        null, 
                        "Bạn muốn xóa sách này?", 
                        "Xác nhận", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE
                    );

                    // Nếu người dùng chọn "Yes", xóa dòng
                    if (option == JOptionPane.YES_OPTION) {
                        // Xóa dòng trong bảng
                        ((DefaultTableModel) tableCart.getModel()).removeRow(selectedRow);
                    }
                }
            }
        });
    }

    public void chuyenSachVaoHoaDon() throws HeadlessException, SQLException {
        int selectedRow = tableBooks.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sách cần thêm", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(customerName == "" && customerPhone == "" && customerAddress == "") {
        	JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin khách hàng", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maSach = modelBooks.getValueAt(selectedRow, 0).toString();
        String tenSach = modelBooks.getValueAt(selectedRow, 1).toString();
        double giaBan = Double.parseDouble(modelBooks.getValueAt(selectedRow, 3).toString());
        int soLuong = (int) spinnerQuantity.getValue();
        
        try {
            try {
				if (!SachDAO.kiemTraSoLuongTon(maSach, soLuong)) {
				    int soLuongTon = SachDAO.getSoLuongTon(maSach);
				    JOptionPane.showMessageDialog(this, 
				        "Số lượng tồn không đủ!\nHiện chỉ còn " + soLuongTon + " cuốn", 
				        "Lỗi", JOptionPane.ERROR_MESSAGE);
				    return;
				}
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            for (int i = 0; i < modelCart.getRowCount(); i++) {
                if (modelCart.getValueAt(i, 0).equals(maSach)) {
                    int soLuongHienTai = Integer.parseInt(modelCart.getValueAt(i, 2).toString());
                    modelCart.setValueAt(soLuongHienTai + soLuong, i, 2);
                    
                    double thanhTien = (soLuongHienTai + soLuong) * giaBan;
                    modelCart.setValueAt(String.format("%,.0f đ", thanhTien), i, 4);
                    
                    capNhatTongTien();
                    return;
                }
            }
            double thanhTien = giaBan * soLuong;
            modelCart.addRow(new Object[]{
                maSach, 
                tenSach, 
                soLuong, 
                String.format("%,.0f đ", giaBan), 
                String.format("%,.0f đ", thanhTien),
                "Xóa"
            });
            
            capNhatTongTien();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra số lượng tồn", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    //thong tin khach hang
    public KhachHang customerInfo() {
    	KhachHang kh = new KhachHang();
    	KhachHangDAO khdao = new KhachHangDAO();
    	String makh = "";
		try {
			makh = khdao.generateMaKH();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	kh.setMaKH(makh);
    	kh.setTenKH(customerName);
    	kh.setSoDienThoai(customerPhone);
    	kh.setDiaChi(customerAddress);
    	return kh;
    }
    public void loadDanhSachHoaDon() {
    	hddao = new HoaDonDAO();
    	
	    try {
	        List<HoaDon> danhSach = hddao.getAllHoaDon(); 
	        modelOrders.setRowCount(0);

	        for (HoaDon hd : danhSach) {
	            modelOrders.addRow(new Object[]{
	                hd.getMaHD(),
	                new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(hd.getNgayLap()),
	                String.format("%,.0f VND", hd.getThanhTien()),
	                "Đã lập",
	                "Xem"
	            });
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách hóa đơn: " + e.getMessage());
	    }
	}
	public void xemChiTietHoaDon(String maHD) {
	    try {
	        List<ChiTietHoaDon> chiTietList = hddao.getChiTietHoaDon(maHD);
	        HoaDon hoaDon = hddao.getAllHoaDon().stream()
	                .filter(hd -> hd.getMaHD().equals(maHD))
	                .findFirst()
	                .orElse(null);

	        if (hoaDon == null) {
	            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn!");
	            return;
	        }

	        String[][] data = new String[chiTietList.size()][5];
	        for (int i = 0; i < chiTietList.size(); i++) {
	            ChiTietHoaDon ct = chiTietList.get(i);
	            data[i][0] = ct.getMaSach();
	            data[i][1] = ct.getSach().getTenSach();
	            data[i][2] = String.valueOf(ct.getSoLuong());
	            data[i][3] = String.format("%,.0f", ct.getDonGia());
	            data[i][4] = String.format("%,.0f", ct.getThanhTien());
	        }

	        showOrderDetailDialog(this,
	            "Nhà sách ABC", 
	            new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(hoaDon.getNgayLap()),
	            hoaDon.getNhanVien().getTenNV(),
	            hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getTenKH() : "Khách lẻ",
	            data, 
	            String.format("%,.0f VND", hoaDon.getThanhTien()),
	            new Font("Segoe UI", Font.PLAIN, 14)
	        );

	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi khi xem chi tiết: " + e.getMessage());
	    }
	}



    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý hóa đơn");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 650);
            frame.setLocationRelativeTo(null);
            frame.add(new OrderManager());
            frame.setVisible(true);
        });
    }
} 
