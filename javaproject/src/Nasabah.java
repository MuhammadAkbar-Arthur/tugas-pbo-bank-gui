public class Nasabah {
    private String nama;
    private double saldo;

    public Nasabah(String nama, double saldoAwal) {
        this.nama = nama;
        this.saldo = saldoAwal;
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

    public void setorTunai(double nominal) {
        this.saldo += nominal;
    }

    public boolean tarikTunai(double nominal) {
        if (nominal > this.saldo) {
            return false;
        }
        this.saldo -= nominal;
        return true;
    }

    public void resetSaldo() {
        this.saldo = 0;
    }
}