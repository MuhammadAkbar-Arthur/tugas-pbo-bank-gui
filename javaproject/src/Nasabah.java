public class Nasabah {
    private String nama;
    private double saldo;
    private int jumlahTransaksi;

    public Nasabah(String nama, double saldoAwal) {
        this.nama = nama;
        this.saldo = saldoAwal;
        this.jumlahTransaksi = 0;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getSaldo() {
        return saldo;
    }

    public int getJumlahTransaksi() {
        return jumlahTransaksi;
    }

    public void setorTunai(double nominal) {
        this.saldo += nominal;
        this.jumlahTransaksi++;
    }

    public boolean tarikTunai(double nominal) {
        if (nominal > this.saldo) {
            return false;
        }
        this.saldo -= nominal;
        this.jumlahTransaksi++;
        return true;
    }

    public void resetData() {
        this.saldo = 0;
        this.jumlahTransaksi = 0;
    }
}