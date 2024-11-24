package com.example.btlg05.DonHang;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.btlg05.R;

import java.util.List;

public class DonHangAdapter extends ArrayAdapter<DonHang> {
    private Context context;
    private List<DonHang> donHangList;

    public DonHangAdapter(Context context, List<DonHang> donHangList) {
        super(context, 0, donHangList);
        this.context = context;
        this.donHangList = donHangList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_don, parent, false);
        }

        DonHang donHang = donHangList.get(position);

        TextView tvMaDonHang = convertView.findViewById(R.id.mahang);
        TextView tvTenDiemThu = convertView.findViewById(R.id.tendt);
        Button btnXemChiTiet = convertView.findViewById(R.id.btnChiTiet);

        tvMaDonHang.setText(donHang.getId());
        tvTenDiemThu.setText(donHang.getTendiemthu());

        btnXemChiTiet.setOnClickListener(v -> showDonHangDetails(donHang));

        return convertView;
    }

    private void showDonHangDetails(DonHang donHang) {
        Fragment newFragment = new ChiTietFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("donhang", donHang);
        newFragment.setArguments(bundle);

        // Sử dụng FragmentManager để thực hiện thay thế Fragment
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, newFragment)
                .addToBackStack(null)
                .commit();
    }

}
