import javax.swing.*;
import java.awt.*;

public class ComponentDemo extends JFrame {

    public ComponentDemo() {
        setTitle("Swing Component Demo - Input & Event");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- STEP 4: Komponen Input ---
        JTextField nameField = new JTextField(15);
        JComboBox<String> deptCombo = new JComboBox<>(
            new String[] {"Informatika", "Elektronika", "Mesin"}
        );
        JCheckBox activeCheck = new JCheckBox("Mahasiswa aktif", true);
        
        JRadioButton regulerRadio = new JRadioButton("Reguler", true);
        JRadioButton ekstensiRadio = new JRadioButton("Ekstensi");
        ButtonGroup group = new ButtonGroup();
        group.add(regulerRadio);
        group.add(ekstensiRadio);

        JTextArea outputArea = new JTextArea(6, 25);
        outputArea.setEditable(false);
        JButton showButton = new JButton("Tampilkan");

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        formPanel.add(new JLabel("Nama:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Jurusan:"));
        formPanel.add(deptCombo);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(activeCheck);
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioPanel.add(regulerRadio);
        radioPanel.add(ekstensiRadio);
        formPanel.add(new JLabel("Kelas:"));
        formPanel.add(radioPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(showButton);

        // --- STEP 5 & STEP 6: Event Handling & Validasi ---
        showButton.addActionListener(e -> {
            // Step 6: Validasi input kosong
            if (nameField.getText().isBlank()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Nama wajib diisi.",
                    "Input tidak valid",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String nama = nameField.getText();
            String jurusan = (String) deptCombo.getSelectedItem();
            boolean aktif = activeCheck.isSelected();
            String kelas = regulerRadio.isSelected() ? "Reguler" : "Ekstensi";

            outputArea.setText(
                "Nama: " + nama +
                "\nJurusan: " + jurusan +
                "\nAktif: " + aktif +
                "\nKelas: " + kelas
            );
        });

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ComponentDemo::new);
    }
}