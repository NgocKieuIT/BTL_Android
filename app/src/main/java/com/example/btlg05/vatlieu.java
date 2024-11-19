package com.example.btlg05;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class vatlieu implements Parcelable {
    private  String loai;
    private float khoiluong;

    public vatlieu() {
    }

    public vatlieu(String loai, float khoiluong) {
        this.loai = loai;
        this.khoiluong = khoiluong;
    }

    protected vatlieu(Parcel in) {
        loai = in.readString();
        khoiluong = in.readFloat();
    }

    public static final Creator<vatlieu> CREATOR = new Creator<vatlieu>() {
        @Override
        public vatlieu createFromParcel(Parcel in) {
            return new vatlieu(in);
        }

        @Override
        public vatlieu[] newArray(int size) {
            return new vatlieu[size];
        }
    };

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public float getKhoiluong() {
        return khoiluong;
    }

    public void setKhoiluong(float khoiluong) {
        this.khoiluong = khoiluong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(loai);
        dest.writeFloat(khoiluong);
    }
}
