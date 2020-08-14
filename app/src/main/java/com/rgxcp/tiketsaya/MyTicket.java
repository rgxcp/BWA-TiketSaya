package com.rgxcp.tiketsaya;

public class MyTicket {
    String nama_wisata, lokasi_wisata, jumlah_ticket;

    public MyTicket() {
    }

    public MyTicket(String nama_wisata, String lokasi_wisata, String jumlah_ticket) {
        this.jumlah_ticket = jumlah_ticket;
        this.nama_wisata = nama_wisata;
        this.lokasi_wisata = lokasi_wisata;
    }

    public String getNama_wisata() {
        return nama_wisata;
    }

    public void setNama_wisata(String nama_wisata) {
        this.nama_wisata = nama_wisata;
    }

    public String getLokasi_wisata() {
        return lokasi_wisata;
    }

    public void setLokasi_wisata(String lokasi_wisata) {
        this.lokasi_wisata = lokasi_wisata;
    }

    public String getJumlah_ticket() {
        return jumlah_ticket;
    }

    public void setJumlah_ticket(String jumlah_ticket) {
        this.jumlah_ticket = jumlah_ticket;
    }
}
