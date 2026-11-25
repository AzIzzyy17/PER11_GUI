import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class POSFrame extends JFrame {

    private final List<Product> products = new ArrayList<>();
    private final List<CartItem> cart = new ArrayList<>();

    private JTable productTable;
    private JTable cartTable;
    private JTextField qtyField;
    private JLabel selectedLabel;
    private JLabel totalLabel;
    private JLabel pointsLabel;
    private JTextArea receiptArea;

    private DefaultTableModel productModel;
    private DefaultTableModel cartModel;

    private JButton addButton;
    private JButton removeButton; // Challenge 2: Tombol Remove Item
    private JButton checkoutButton;
    private JButton printButton;

    // Challenge 3: Format mata uang Rupiah (id, ID)
    private final NumberFormat rupiahFormat =
        NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public POSFrame() {
        setTitle("POIN Off-Sales - Java Swing");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Ditangani oleh confirmExit()

        initProductData();
        initComponents();
        bindEvents();

        setSize(1000, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initProductData() {
        products.add(new Product("P001", "Air Mineral 600ml", 3000));
        products.add(new Product("P002", "Kopi Sachet", 5000));
        products.add(new Product("P003", "Roti isi", 8000));
        products.add(new Product("P004", "Snack Keripik", 6000));
        products.add(new Product("P005", "Minuman Botol", 12000));
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // --- MENU BAR & CHALLENGE 5 (Shortcut + Konfirmasi Exit) ---
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F); // Alt + F

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK)); // Ctrl + Q
        exitItem.addActionListener(e -> confirmExit());

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H); // Alt + H

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)); // F1
        aboutItem.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this,
                "POIN Off-Sales - Praktikum Java Swing\nDilengkapi Challenge Opsional",
                "Tentang Aplikasi",
                JOptionPane.INFORMATION_MESSAGE
            )
        );

        fileMenu.add(exitItem);
        helpMenu.add(aboutItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // --- PANEL KIRI: PRODUK ---
        JPanel productPanel = new JPanel(new BorderLayout(5, 5));
        productPanel.setBorder(BorderFactory.createTitledBorder("Produk"));

        productModel = new DefaultTableModel(
            new Object[] {"ID", "Nama Produk", "Harga (Rp)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Product p : products) {
            productModel.addRow(new Object[] {
                p.getId(), p.getName(), rupiahFormat.format(p.getPrice())
            });
        }

        productTable = new JTable(productModel);
        productPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel productBottomPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        selectedLabel = new JLabel("Pilih produk dari tabel di kiri");
        selectedLabel.setFont(selectedLabel.getFont().deriveFont(Font.BOLD));

        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        qtyPanel.add(new JLabel("Qty:"));
        qtyField = new JTextField("1", 5);
        addButton = new JButton("Add to Cart");
        qtyPanel.add(qtyField);
        qtyPanel.add(addButton);

        productBottomPanel.add(selectedLabel);
        productBottomPanel.add(qtyPanel);
        productPanel.add(productBottomPanel, BorderLayout.SOUTH);

        // --- PANEL KANAN: KERANJANG ---
        JPanel cartPanel = new JPanel(new BorderLayout(5, 5));
        cartPanel.setBorder(BorderFactory.createTitledBorder("Keranjang"));

        cartModel = new DefaultTableModel(
            new Object[] {"ID", "Nama Produk", "Qty", "Harga", "Subtotal"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cartTable = new JTable(cartModel);
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel cartBottomPanel = new JPanel();
        cartBottomPanel.setLayout(new BoxLayout(cartBottomPanel, BoxLayout.Y_AXIS));
        cartBottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 25, 5, 10));

        totalLabel = new JLabel("Total: " + rupiahFormat.format(0));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        pointsLabel = new JLabel("Points: 0");
        pointsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Panel untuk tombol-tombol aksi keranjang
        JPanel cartActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cartActionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        checkoutButton = new JButton("Checkout");
        removeButton = new JButton("Remove Item");
        printButton = new JButton("Cetak");
        
        // Challenge 4: Nonaktifkan tombol Cetak sebelum ada struk
        printButton.setEnabled(false);

        cartActionPanel.add(checkoutButton);
        cartActionPanel.add(Box.createHorizontalStrut(6));
        cartActionPanel.add(removeButton);
        cartActionPanel.add(Box.createHorizontalStrut(6));
        cartActionPanel.add(printButton);

        JLabel receiptTitleLabel = new JLabel("Struk:");
        receiptTitleLabel.setFont(receiptTitleLabel.getFont().deriveFont(Font.BOLD));
        receiptTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        receiptArea = new JTextArea(8, 30);
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane receiptScroll = new JScrollPane(receiptArea);
        receiptScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        cartBottomPanel.add(totalLabel);
        cartBottomPanel.add(Box.createVerticalStrut(4));
        cartBottomPanel.add(pointsLabel);
        cartBottomPanel.add(Box.createVerticalStrut(8));
        cartBottomPanel.add(cartActionPanel);
        cartBottomPanel.add(Box.createVerticalStrut(10));
        cartBottomPanel.add(receiptTitleLabel);
        cartBottomPanel.add(Box.createVerticalStrut(2));
        cartBottomPanel.add(receiptScroll);

        cartPanel.add(cartBottomPanel, BorderLayout.SOUTH);

        // --- GABUNGKAN KIRI & KANAN ---
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(productPanel);
        mainPanel.add(cartPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void bindEvents() {
        productTable.getSelectionModel().addListSelectionListener(e -> {
            int row = productTable.getSelectedRow();
            if (row >= 0) {
                Product selected = products.get(row);
                selectedLabel.setText(
                    "Dipilih: " + selected.getId() +
                    " - " + selected.getName() +
                    " (" + rupiahFormat.format(selected.getPrice()) + ")"
                );
            }
        });

        addButton.addActionListener(e -> addSelectedProductToCart());
        removeButton.addActionListener(e -> removeSelectedCartItem());
        checkoutButton.addActionListener(e -> checkout());
        printButton.addActionListener(e -> printReceipt());

        // Konfirmasi juga saat tombol X window ditekan
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                confirmExit();
            }
        });
    }

    private void addSelectedProductToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Silakan pilih produk terlebih dahulu dari tabel di kiri.",
                "Produk belum dipilih",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyField.getText().trim());
            if (qty <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Qty harus berupa angka bulat lebih dari 0.",
                "Qty tidak valid",
                JOptionPane.WARNING_MESSAGE
            );
            qtyField.requestFocus();
            return;
        }

        Product selectedProduct = products.get(selectedRow);

        // Challenge 1: Cek apakah produk sudah ada di keranjang; jika ada, gabungkan Qty-nya
        boolean found = false;
        for (CartItem existingItem : cart) {
            if (existingItem.getProduct().getId().equals(selectedProduct.getId())) {
                existingItem.addQty(qty);
                found = true;
                break;
            }
        }

        if (!found) {
            cart.add(new CartItem(selectedProduct, qty));
        }

        refreshCartTable();
        updateSummary();
        qtyField.setText("1");
    }

    // Challenge 2: Menghapus item terpilih dari keranjang
    private void removeSelectedCartItem() {
        int selectedCartRow = cartTable.getSelectedRow();
        if (selectedCartRow < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Pilih baris item pada tabel Keranjang yang ingin dihapus.",
                "Item belum dipilih",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        cart.remove(selectedCartRow);
        refreshCartTable();
        updateSummary();
    }

    private void refreshCartTable() {
        cartModel.setRowCount(0);
        for (CartItem item : cart) {
            Product p = item.getProduct();
            cartModel.addRow(new Object[] {
                p.getId(),
                p.getName(),
                item.getQty(),
                rupiahFormat.format(p.getPrice()),
                rupiahFormat.format(item.getSubtotal())
            });
        }
    }

    private void updateSummary() {
        int total = 0;
        for (CartItem item : cart) {
            total += item.getSubtotal();
        }
        int points = total / 1000;

        totalLabel.setText("Total: " + rupiahFormat.format(total));
        pointsLabel.setText("Points: " + points);
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Keranjang masih kosong. Tambahkan produk terlebih dahulu.",
                "Checkout gagal",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        StringBuilder receipt = new StringBuilder();
        receipt.append("===== POIN OFF-SALES =====\n");
        receipt.append("Toko: Demo Toko\n");
        receipt.append("================================\n");

        int total = 0;
        for (CartItem item : cart) {
            Product p = item.getProduct();
            int subtotal = item.getSubtotal();
            total += subtotal;

            receipt.append(String.format(
                "%s %-14s %d  %s\n",
                p.getId(),
                p.getName(),
                item.getQty(),
                rupiahFormat.format(subtotal)
            ));
        }

        int points = total / 1000;
        receipt.append("--------------------------------\n");
        receipt.append("TOTAL:           " + rupiahFormat.format(total) + "\n");
        receipt.append("POINTS DIDAPAT:  " + points + " (1 point per Rp 1000)\n");
        receipt.append("================================\n");
        receipt.append("Terima kasih! Silakan kunjungi kembali.\n");

        receiptArea.setText(receipt.toString());

        // Challenge 4: Aktifkan tombol Cetak setelah struk tersedia
        printButton.setEnabled(true);

        cart.clear();
        refreshCartTable();
        updateSummary();
    }

    private void printReceipt() {
        if (receiptArea.getText().isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "Belum ada struk yang dapat dicetak.",
                "Struk kosong",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            receiptArea.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Gagal mencetak: " + ex.getMessage(),
                "Error Print",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Challenge 5: Konfirmasi sebelum keluar aplikasi
    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin keluar dari aplikasi?",
            "Konfirmasi Keluar",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}