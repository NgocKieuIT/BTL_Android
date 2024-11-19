package com.example.btlg05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class VatlieuKLAdapter extends ArrayAdapter<vatlieu> {
    private Context context;
    private List<vatlieu> vatLieuList;

    public VatlieuKLAdapter(Context context, List<vatlieu> vatLieuList) {
        super(context, 0, vatLieuList);
        this.context = context;
        this.vatLieuList = vatLieuList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_kl, parent, false);
        }

        vatlieu item = vatLieuList.get(position);
        if (item != null) {
            TextView tvLoaiVatLieu = convertView.findViewById(R.id.tvLoaiVatLieu);
            TextView tvKhoiLuong = convertView.findViewById(R.id.tvKhoiLuong);

            tvLoaiVatLieu.setText(item.getLoai());
            tvKhoiLuong.setText(String.valueOf(item.getKhoiluong()));
        }
        return convertView;
    }
}
