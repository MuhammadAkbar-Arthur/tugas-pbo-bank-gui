import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Bagian 1 Event Handler: Deklarasi class yang mengimplementasikan 4 interface listener sekaligus
public class AplikasiBankMini implements ActionListener, MouseListener, ItemListener, KeyListener {

    // 1. Deklarasi Top-Level Container
    private JFrame frame;

    // 2. Deklarasi Intermediate Container
    private JTabbedPane tabPane;
    private JPanel panelTabTransaksi, panelForm, panelTombol, panelTabStatistik;
    private JScrollPane scrollRiwayat, scrollStatistik;
    private Box panelBawah;

    // 3. Deklarasi Atomic Component
    private JLabel lblJudul, lblNama, lblJenis, lblNominal, lblTitleInfo, lblInfoJenis, lblStatusMouse;
    private JTextField tfNama, tfNominal;
    private JComboBox<String> cbJenisTransaksi;
    private JButton btnProses, btnBersihkanForm, btnReset, btnKeluar;
    private JTextArea taRiwayat, taStatistik;

    // Implementasi Array of Objects (Materi Bank PBO) untuk menyimpan banyak Nasabah
    private Nasabah[] daftarNasabah;
    private int jumlahNasabah;

    public AplikasiBankMini() {
        // Inisialisasi Array of Objects (kapasitas maksimal 50 nasabah)
        daftarNasabah = new Nasabah[50];
        jumlahNasabah = 0;

        // Inisialisasi Top-Level Container (JFrame)
        frame = new JFrame("Aplikasi Bank Mini - PBO 5C");

        // Inisialisasi Label Judul Utama
        lblJudul = new JLabel("SISTEM TRANSAKSI & MANAJEMEN NASABAH BANK MINI", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 14));
        lblJudul.setForeground(new Color(20, 60, 120));

        // Inisialisasi Intermediate Container JTabbedPane (implementasi konsep CardLayout)
        tabPane = new JTabbedPane();
        panelTabTransaksi = new JPanel(new BorderLayout(10, 10));
        panelTabStatistik = new JPanel(new BorderLayout(10, 10));

        // Inisialisasi Intermediate Container JPanel dengan GridLayout (4 baris, 2 kolom)
        GridLayout layoutForm = new GridLayout(4, 2, 8, 8);
        panelForm = new JPanel(layoutForm);
        panelForm.setBorder(BorderFactory.createTitledBorder("Form Input Transaksi Nasabah"));

        // Inisialisasi Atomic Component pada Form
        lblNama = new JLabel(" Nama Nasabah (0 karakter):");
        tfNama = new JTextField(20);

        lblJenis = new JLabel(" Jenis Transaksi:");
        String[] pilihan = {"Setor Tunai", "Tarik Tunai"};
        cbJenisTransaksi = new JComboBox<>(pilihan);

        lblNominal = new JLabel(" Nominal Transaksi (Rp):");
        tfNominal = new JTextField(20);

        lblTitleInfo = new JLabel(" Ketentuan Layanan:");
        lblInfoJenis = new JLabel("Menambah saldo tabungan nasabah");
        lblInfoJenis.setForeground(new Color(0, 128, 0));

        // Inisialisasi Intermediate Container JPanel dengan FlowLayout untuk Tombol Aksi
        FlowLayout layoutTombol = new FlowLayout(FlowLayout.CENTER, 8, 8);
        panelTombol = new JPanel(layoutTombol);

        btnProses = new JButton("Proses Transaksi");
        btnBersihkanForm = new JButton("Bersihkan Form");
        btnReset = new JButton("Reset Riwayat");
        btnKeluar = new JButton("Keluar");

        // Inisialisasi JTextArea & JScrollPane untuk Tab 1 (Riwayat) dan Tab 2 (Statistik)
        taRiwayat = new JTextArea(9, 42);
        taRiwayat.setEditable(false);
        taRiwayat.setText("=== RIWAYAT TRANSAKSI BANK ===\nSaldo Awal Sistem: Rp 0\n------------------------------------------------------------\n");
        scrollRiwayat = new JScrollPane(taRiwayat);

        taStatistik = new JTextArea(12, 42);
        taStatistik.setEditable(false);
        scrollStatistik = new JScrollPane(taStatistik);

        // Label status interaktif untuk mendemonstrasikan MouseListener
        lblStatusMouse = new JLabel(" Arahkan kursor ke tombol atau klik area riwayat untuk berinteraksi...");
        lblStatusMouse.setForeground(Color.BLUE);

        // Inisialisasi Intermediate Container Box (menggunakan BoxLayout sumbu Y)
        panelBawah = new Box(BoxLayout.Y_AXIS);

        perbaruiTampilanStatistik();
    }

    public void launchFrame() {
        // Mengatur Layout Manager utama pada Content Pane milik JFrame
        BorderLayout layoutUtama = new BorderLayout(10, 10);
        frame.getContentPane().setLayout(layoutUtama);

        // Menambahkan Atomic Component ke dalam panelForm (GridLayout)
        panelForm.add(lblNama);
        panelForm.add(tfNama);
        panelForm.add(lblJenis);
        panelForm.add(cbJenisTransaksi);
        panelForm.add(lblNominal);
        panelForm.add(tfNominal);
        panelForm.add(lblTitleInfo);
        panelForm.add(lblInfoJenis);

        // Menambahkan keempat JButton ke dalam panelTombol (FlowLayout)
        panelTombol.add(btnProses);
        panelTombol.add(btnBersihkanForm);
        panelTombol.add(btnReset);
        panelTombol.add(btnKeluar);

        // Menyusun panelTombol dan lblStatusMouse secara vertikal di panelBawah (BoxLayout)
        panelBawah.add(panelTombol);
        panelBawah.add(lblStatusMouse);

        // Menyusun komponen ke dalam Tab 1 (panelTabTransaksi)
        panelTabTransaksi.add(panelForm, BorderLayout.NORTH);
        panelTabTransaksi.add(scrollRiwayat, BorderLayout.CENTER);
        panelTabTransaksi.add(panelBawah, BorderLayout.SOUTH);

        // Menyusun komponen ke dalam Tab 2 (panelTabStatistik)
        panelTabStatistik.setBorder(BorderFactory.createTitledBorder("Rekapitulasi Saldo & Statistik Nasabah (Array of Objects)"));
        panelTabStatistik.add(scrollStatistik, BorderLayout.CENTER);

        // Menambahkan kedua panel ke dalam JTabbedPane
        tabPane.add(panelTabTransaksi, "Tab 1: Input & Riwayat Transaksi");
        tabPane.add(panelTabStatistik, "Tab 2: Data Nasabah & Statistik");

        // =====================================================================
        // Bagian 3 Event Handler: Registrasi Listener pada Event Source
        // =====================================================================
        // 1. Registrasi ActionListener
        btnProses.addActionListener(this);
        btnBersihkanForm.addActionListener(this);
        btnReset.addActionListener(this);
        btnKeluar.addActionListener(this);
        tfNominal.addActionListener(this); // Mendukung penekanan tombol Enter di kolom nominal

        // 2. Registrasi MouseListener
        btnProses.addMouseListener(this);
        btnBersihkanForm.addMouseListener(this);
        btnReset.addMouseListener(this);
        btnKeluar.addMouseListener(this);
        taRiwayat.addMouseListener(this);

        // 3. Registrasi ItemListener
        cbJenisTransaksi.addItemListener(this);

        // 4. Registrasi KeyListener
        tfNama.addKeyListener(this);

        // Menambahkan judul dan tabPane ke Content Pane milik JFrame
        frame.getContentPane().add(lblJudul, BorderLayout.NORTH);
        frame.getContentPane().add(tabPane, BorderLayout.CENTER);

        // Pengaturan penutupan JFrame dan menampilkan ke layar
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Method untuk mencari objek Nasabah di dalam Array of Objects atau membuat baru jika belum ada
    private Nasabah cariAtauBuatNasabah(String nama) {
        for (int i = 0; i < jumlahNasabah; i++) {
            if (daftarNasabah[i].getNama().equalsIgnoreCase(nama)) {
                return daftarNasabah[i];
            }
        }
        if (jumlahNasabah < daftarNasabah.length) {
            Nasabah nasabahBaru = new Nasabah(nama, 0);
            daftarNasabah[jumlahNasabah] = nasabahBaru;
            jumlahNasabah++;
            return nasabahBaru;
        }
        return null;
    }

    // Method untuk memperbarui isi teks pada Tab 2 (Data Nasabah & Statistik)
    private void perbaruiTampilanStatistik() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REKAPITULASI DATA NASABAH BANK MINI ===\n");
        sb.append("Jumlah Nasabah Terdaftar : ").append(jumlahNasabah).append(" orang\n");
        sb.append("------------------------------------------------------------\n");

        double totalDanaBank = 0;
        int totalTransaksiBank = 0;

        if (jumlahNasabah == 0) {
            sb.append("Belum ada data nasabah yang tersimpan.\n");
        } else {
            for (int i = 0; i < jumlahNasabah; i++) {
                Nasabah n = daftarNasabah[i];
                sb.append(i + 1).append(". Nama: ").append(n.getNama())
                  .append(" | Saldo Akhir: Rp ").append(n.getSaldo())
                  .append(" | Frekuensi: ").append(n.getJumlahTransaksi()).append("x transaksi\n");
                totalDanaBank += n.getSaldo();
                totalTransaksiBank += n.getJumlahTransaksi();
            }
        }

        sb.append("------------------------------------------------------------\n");
        sb.append("Total Frekuensi Transaksi : ").append(totalTransaksiBank).append(" kali\n");
        sb.append("Total Dana Tersimpan      : Rp ").append(totalDanaBank).append("\n");
        taStatistik.setText(sb.toString());
    }

    // =========================================================================
    // Bagian 2A Event Handler: Implementasi Method dari Interface ActionListener
    // =========================================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnProses || source == tfNominal) {
            prosesTransaksi();
        } else if (source == btnBersihkanForm) {
            tfNama.setText("");
            tfNominal.setText("");
            cbJenisTransaksi.setSelectedIndex(0);
            lblNama.setText(" Nama Nasabah (0 karakter):");
            tfNama.requestFocus();
        } else if (source == btnReset) {
            int result = JOptionPane.showConfirmDialog(
                    frame,
                    "Apakah Anda yakin ingin menghapus seluruh riwayat transaksi dan mereset data nasabah?",
                    "Konfirmasi Reset Data",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                jumlahNasabah = 0;
                taRiwayat.setText("=== RIWAYAT TRANSAKSI BANK ===\nSaldo Awal Sistem: Rp 0\n------------------------------------------------------------\n");
                perbaruiTampilanStatistik();
                JOptionPane.showMessageDialog(
                        frame,
                        "Seluruh riwayat dan data nasabah berhasil direset!",
                        "Informasi Reset",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else if (source == btnKeluar) {
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Apakah Anda yakin ingin keluar dari Aplikasi Bank Mini?",
                    "Konfirmasi Keluar",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        }
    }

    private void prosesTransaksi() {
        String namaInput = tfNama.getText().trim();
        String nominalStr = tfNominal.getText().trim();
        String jenis = (String) cbJenisTransaksi.getSelectedItem();

        if (namaInput.isEmpty() || nominalStr.isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Nama Nasabah dan Nominal Transaksi wajib diisi!",
                    "Peringatan Input",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            double nominal = Double.parseDouble(nominalStr);
            if (nominal <= 0) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Nominal transaksi harus lebih besar dari Rp 0!",
                        "Error Nominal",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            Nasabah nasabah = cariAtauBuatNasabah(namaInput);
            if (nasabah == null) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Kapasitas penyimpanan nasabah sudah penuh!",
                        "Error Kapasitas",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (jenis.equals("Setor Tunai")) {
                nasabah.setorTunai(nominal);
                taRiwayat.append("[SETOR] " + nasabah.getNama() + " : +Rp " + nominal + " | Saldo: Rp " + nasabah.getSaldo() + "\n");
            } else {
                boolean berhasil = nasabah.tarikTunai(nominal);
                if (!berhasil) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Saldo nasabah " + nasabah.getNama() + " tidak mencukupi untuk penarikan ini!",
                            "Transaksi Gagal",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                taRiwayat.append("[TARIK] " + nasabah.getNama() + " : -Rp " + nominal + " | Saldo: Rp " + nasabah.getSaldo() + "\n");
            }

            perbaruiTampilanStatistik();
            tfNominal.setText("");
            tfNominal.requestFocus();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Input nominal harus berupa angka yang valid (tanpa huruf/simbol)!",
                    "Error Format Angka",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================================
    // Bagian 2B Event Handler: Implementasi Method dari Interface ItemListener
    // =========================================================================
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == cbJenisTransaksi && e.getStateChange() == ItemEvent.SELECTED) {
            String pilihan = (String) cbJenisTransaksi.getSelectedItem();
            if (pilihan.equals("Setor Tunai")) {
                lblInfoJenis.setText("Menambah saldo tabungan nasabah");
                lblInfoJenis.setForeground(new Color(0, 128, 0));
            } else {
                lblInfoJenis.setText("Mengurangi saldo (pastikan saldo mencukupi)");
                lblInfoJenis.setForeground(new Color(180, 60, 0));
            }
        }
    }

    // =========================================================================
    // Bagian 2C Event Handler: Implementasi 3 Method dari Interface KeyListener
    // =========================================================================
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == tfNama) {
            int panjangKarakter = tfNama.getText().length();
            lblNama.setText(" Nama Nasabah (" + panjangKarakter + " karakter):");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    // =========================================================================
    // Bagian 2D Event Handler: Implementasi 5 Method dari Interface MouseListener
    // =========================================================================
    @Override
    public void mouseEntered(MouseEvent e) {
        Object source = e.getSource();
        if (source == btnProses) {
            btnProses.setForeground(new Color(0, 128, 0));
            lblStatusMouse.setText(" Info: Klik untuk memproses dan menyimpan transaksi nasabah.");
        } else if (source == btnBersihkanForm) {
            btnBersihkanForm.setForeground(new Color(0, 102, 204));
            lblStatusMouse.setText(" Info: Klik untuk mengosongkan kolom input form.");
        } else if (source == btnReset) {
            btnReset.setForeground(new Color(204, 102, 0));
            lblStatusMouse.setText(" Info: Klik untuk menghapus seluruh riwayat dan data nasabah.");
        } else if (source == btnKeluar) {
            btnKeluar.setForeground(Color.RED);
            lblStatusMouse.setText(" Info: Klik untuk menutup aplikasi.");
        } else if (source == taRiwayat) {
            lblStatusMouse.setText(" Info: Klik di dalam area riwayat ini untuk melihat koordinat X dan Y mouse.");
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() instanceof JButton) {
            ((JButton) e.getSource()).setForeground(Color.BLACK);
        }
        lblStatusMouse.setText(" Arahkan kursor ke tombol atau klik area riwayat untuk berinteraksi...");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == taRiwayat) {
            lblStatusMouse.setText(" Anda mengklik area riwayat pada posisi koordinat X=" + e.getX() + ", Y=" + e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
}