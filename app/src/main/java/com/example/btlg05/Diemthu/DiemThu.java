package com.example.btlg05.Diemthu;

import java.io.Serializable;

public class DiemThu implements Serializable {
    private String id;
    private String sdt;
    private String diachi;
    private String ten;
    private String imageUrl;

    public DiemThu() {
    }

    public DiemThu(String id, String sdt, String diachi, String ten, String imageUrl) {
        this.id = id;
        this.sdt = sdt;
        this.diachi = diachi;
        this.ten = ten;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
