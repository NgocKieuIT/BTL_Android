package com.example.btlg05;

public class DiemThu {
    private int id;
    private String sdt;
    private double diachi;
    private String ten;

    public DiemThu() {
    }

    public DiemThu(int id, String sdt, double diachi, String ten) {
        this.id = id;
        this.sdt = sdt;
        this.diachi = diachi;
        this.ten = ten;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public double getDiachi() {
        return diachi;
    }

    public void setDiachi(double diachi) {
        this.diachi = diachi;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
