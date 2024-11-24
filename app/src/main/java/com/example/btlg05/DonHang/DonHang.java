package com.example.btlg05.DonHang;

import java.io.Serializable;
import java.util.List;

public class DonHang implements Serializable {
    private String id;
    private List<com.example.btlg05.VatLieu.vatlieu> vatlieu;
    private String tendiemthu;
    private String emailng;
    private String diaching;
    private String sdtng;
    private String phuongthuc;
    private String ngaygui;
    private  String stk;
    private  String tennganhang;
    private String trangthai;
    private  float sotien;

    public DonHang() {
    }

    public DonHang(String id, List<com.example.btlg05.VatLieu.vatlieu> vatlieu, String tendiemthu, String emailng, String diaching, String sdtng, String phuongthuc, String ngaygui, String stk, String tennganhang, String trangthai, float sotien) {
        this.id = id;
        this.vatlieu = vatlieu;
        this.tendiemthu = tendiemthu;
        this.emailng = emailng;
        this.diaching = diaching;
        this.sdtng = sdtng;
        this.phuongthuc = phuongthuc;
        this.ngaygui = ngaygui;
        this.stk = stk;
        this.tennganhang = tennganhang;
        this.trangthai = trangthai;
        this.sotien = sotien;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<com.example.btlg05.VatLieu.vatlieu> getVatlieu() {
        return vatlieu;
    }

    public void setVatlieu(List<com.example.btlg05.VatLieu.vatlieu> vatlieu) {
        this.vatlieu = vatlieu;
    }

    public String getTendiemthu() {
        return tendiemthu;
    }

    public void setTendiemthu(String tendiemthu) {
        this.tendiemthu = tendiemthu;
    }

    public String getEmailng() {
        return emailng;
    }

    public void setEmailng(String emailng) {
        this.emailng = emailng;
    }

    public String getDiaching() {
        return diaching;
    }

    public void setDiaching(String diaching) {
        this.diaching = diaching;
    }

    public String getSdtng() {
        return sdtng;
    }

    public void setSdtng(String sdtng) {
        this.sdtng = sdtng;
    }

    public String getPhuongthuc() {
        return phuongthuc;
    }

    public void setPhuongthuc(String phuongthuc) {
        this.phuongthuc = phuongthuc;
    }

    public String getNgaygui() {
        return ngaygui;
    }

    public void setNgaygui(String ngaygui) {
        this.ngaygui = ngaygui;
    }

    public String getStk() {
        return stk;
    }

    public void setStk(String stk) {
        this.stk = stk;
    }

    public String getTennganhang() {
        return tennganhang;
    }

    public void setTennganhang(String tennganhang) {
        this.tennganhang = tennganhang;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public float getSotien() {
        return sotien;
    }

    public void setSotien(float sotien) {
        this.sotien = sotien;
    }
}
