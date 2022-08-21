package com.example.cobaaan;

public class DataUser {
    private String id;

    private String nama;
    private String penerimaan;
    private String produksi;
    private String tgl_pendaftaran;

    public DataUser() {
    }

    public DataUser(String nama, String penerimaan, String produksi, String tgl_pendaftaran) {
        this.nama = nama;
        this.penerimaan = penerimaan;
        this.produksi = produksi;
        this.tgl_pendaftaran = tgl_pendaftaran;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getPenerimaan() {
        return penerimaan;
    }

    public String getProduksi() {
        return produksi;
    }

    public String getTgl_pendaftaran() {
        return tgl_pendaftaran;
    }
}