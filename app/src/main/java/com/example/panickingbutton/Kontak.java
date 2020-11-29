package com.example.panickingbutton;

public class Kontak {
    private final String nama;
    private final String nomor;

    public Kontak(String nama, String nomor) {
        this.nama = nama;
        this.nomor = nomor;
    }

    @Override
    public String toString() {
        return this.nama;
    }

    public String getNomorKontak() {
        return this.nomor;
    }

    public String getNamaKontak() {
        return this.nama;
    }
}
