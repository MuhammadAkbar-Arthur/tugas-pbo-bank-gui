import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Bagian 1 Event Handler: Deklarasi class yang mengimplementasikan 2 interface listener
public class AplikasiBankMini implements ActionListener, MouseListener {

    // Deklarasi Top-Level Container
    private JFrame frame;

    // Deklarasi Intermediate Container
    private JPanel panelForm;
    private JPanel panelTombol;
    private JScrollPane scrollRiwayat;
    private Box panelBawah;

    // Deklarasi Atomic Component
    private JLabel lblNama, lblJenis, lblNominal, lblStatusMouse;
    private JTextField tfNama, tfNominal;
    private JComboBox<String> cbJenisTransaksi;
    private JButton btnProses, btnReset, btnKeluar;
    private JTextArea taRiwayat;

    // Objek Nasabah (Model OOP)
    private Nasabah nasabah;

    public AplikasiBankMini() {
        nasabah = new Nasabah("", 0);

        // 1. Inisialisasi Top-Level Container (JFrame)
        frame = new JFrame("Aplikasi Bank Mini - PBO 5C");

        // 2. Inisialisasi Intermediate Container 1 (JPanel dengan GridLayout untuk Form)
        GridLayout layoutForm = new GridLayout(3, 2, 5, 10);
        panelForm = new JPanel(layoutForm);
        panelForm.setBorder(BorderFactory.createTitledBorder("Form Input Transaksi"));

        // Inisialisasi Atomic Component untuk Form
        lblNama = new JLabel(" Nama Nasabah:");
        tfNama = new JTextField(20);

        lblJenis = new JLabel(" Jenis Transaksi:");
        String[] pilihan = {"Setor Tunai", "Tarik Tunai"};
        cbJenisTransaksi = new JComboBox<>(pilihan);

        lblNominal = new JLabel(" Nominal (Rp):");
        tfNominal = new JTextField(20);

        // 3. Inisialisasi Intermediate Container 2 (JPanel dengan FlowLayout untuk Tombol)
        FlowLayout layoutTombol = new FlowLayout(FlowLayout.CENTER, 10, 10);
        panelTombol = new JPanel(layoutTombol);

        btnProses = new JButton("Proses Transaksi");
        btnReset = new JButton("Bersihkan Riwayat");
        btnKeluar = new JButton("Keluar");

        // 4. Inisialisasi Atomic Component (JTextArea) & Intermediate Container (JScrollPane)
        taRiwayat = new JTextArea(8, 35);
        taRiwayat.setEditable(false);
        taRiwayat.setText("=== RIWAYAT TRANSAKSI BANK ===\nSaldo Awal: Rp 0\n------------------------------\n");
        scrollRiwayat = new JScrollPane(taRiwayat);

        // Label informasi di bagian bawah untuk mendemonstrasikan MouseListener
        lblStatusMouse = new JLabel(" Arahkan kursor atau klik tombol untuk berinteraksi...");
        lblStatusMouse.setForeground(Color.BLUE);

        // Gabungkan panelTombol dan lblStatusMouse menggunakan Box (BoxLayout)
        panelBawah = new Box(BoxLayout.Y_AXIS);
    }

    public void launchFrame() {
        // Mengatur Layout Manager utama pada Content Pane milik JFrame
        BorderLayout layoutUtama = new BorderLayout(10, 10);
        frame.getContentPane().setLayout(layoutUtama);

        // Menambahkan Atomic Component ke dalam panelForm
        panelForm.add(lblNama);
        panelForm.add(tfNama);
        panelForm.add(lblJenis);
        panelForm.add(cbJenisTransaksi);
        panelForm.add(lblNominal);
        panelForm.add(tfNominal);

        // Menambahkan tombol ke dalam panelTombol
        panelTombol.add(btnProses);
        panelTombol.add(btnReset);
        panelTombol.add(btnKeluar);

        // Menyusun panel bawah
        panelBawah.add(panelTombol);
        panelBawah.add(lblStatusMouse);

        // Bagian 3 Event Handler: Registrasi Listener pada Event Source
        btnProses.addActionListener(this);
        btnReset.addActionListener(this);
        btnKeluar.addActionListener(this);
        tfNominal.addActionListener(this);

        btnProses.addMouseListener(this);
        btnReset.addMouseListener(this);
        btnKeluar.addMouseListener(this);
        taRiwayat.addMouseListener(this);

        // Menambahkan semua komponen ke Content Pane JFrame sesuai area BorderLayout
        frame.getContentPane().add(panelForm, BorderLayout.NORTH);
        frame.getContentPane().add(scrollRiwayat, BorderLayout.CENTER);
        frame.getContentPane().add(panelBawah, BorderLayout.SOUTH);

        // Pengaturan penutupan dan tampilan Frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // =========================================================================
    // Bagian 2 Event Handler: Implementasi Method dari Interface ActionListener
    // =========================================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnProses || source == tfNominal) {
            prosesTransaksi();
        } else if (source == btnReset) {
            int result = JOptionPane.showConfirmDialog(
                    frame,
                    "Apakah Anda yakin ingin menghapus riwayat transaksi?",
                    "Konfirmasi Reset",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                nasabah.resetSaldo();
                taRiwayat.setText("=== RIWAYAT TRANSAKSI BANK ===\nSaldo Awal: Rp 0\n------------------------------\n");
                JOptionPane.showMessageDialog(frame, "Riwayat berhasil direset!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (source == btnKeluar) {
            System.exit(0);
        }
    }

    private void prosesTransaksi() {
        String namaInput = tfNama.getText().trim();
        String nominalStr = tfNominal.getText().trim();
        String jenis = (String) cbJenisTransaksi.getSelectedItem();

        if (namaInput.isEmpty() || nominalStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nama dan Nominal harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double nominal = Double.parseDouble(nominalStr);
            if (nominal <= 0) {
                JOptionPane.showMessageDialog(frame, "Nominal harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            nasabah.setNama(namaInput);

            if (jenis.equals("Setor Tunai")) {
                nasabah.setorTunai(nominal);
                taRiwayat.append("[SETOR] " + nasabah.getNama() + " : +Rp " + nominal + " | Saldo: Rp " + nasabah.getSaldo() + "\n");
            } else {
                boolean berhasil = nasabah.tarikTunai(nominal);
                if (!berhasil) {
                    JOptionPane.showMessageDialog(frame, "Saldo tidak mencukupi!", "Transaksi Gagal", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                taRiwayat.append("[TARIK] " + nasabah.getNama() + " : -Rp " + nominal + " | Saldo: Rp " + nasabah.getSaldo() + "\n");
            }

            tfNominal.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Input nominal harus berupa angka!", "Error Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ========================================================================
    // Bagian 2 Event Handler: Implementasi 5 Method dari Interface MouseListener
    // ========================================================================
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == btnProses) {
            btnProses.setForeground(Color.GREEN.darker());
            lblStatusMouse.setText(" Info: Klik untuk menyimpan transaksi ke dalam riwayat.");
        } else if (e.getSource() == btnReset) {
            btnReset.setForeground(Color.ORANGE.darker());
            lblStatusMouse.setText(" Info: Klik untuk mereset saldo dan membersihkan riwayat.");
        } else if (e.getSource() == btnKeluar) {
            btnKeluar.setForeground(Color.RED);
            lblStatusMouse.setText(" Info: Klik untuk menutup aplikasi.");
        } else if (e.getSource() == taRiwayat) {
            lblStatusMouse.setText(" Info: Area ini menampilkan log transaksi nasabah.");
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() instanceof JButton) {
            ((JButton) e.getSource()).setForeground(Color.BLACK);
        }
        lblStatusMouse.setText(" Arahkan kursor atau klik tombol untuk berinteraksi...");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == taRiwayat) {
            lblStatusMouse.setText(" Anda mengklik area riwayat pada koordinat X=" + e.getX() + ", Y=" + e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
}