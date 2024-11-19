package com.example.btlg05;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LichSuFragment extends Fragment {
    private Button btnXuLy, btnHoanThanh, btnHuy;
    private ListView listView;
    private DonHangAdapter adapter;
    private List<DonHang> donHangList;
    String emailng;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_su, container, false);

        emailng=getArguments().getString("emailng");
        loadDonHang("Đang xử lý");
        btnXuLy = view.findViewById(R.id.btnXuLy);
        btnHoanThanh = view.findViewById(R.id.btnHoanThanh);
        btnHuy = view.findViewById(R.id.btnHuy);

        listView = view.findViewById(R.id.listviewdonhang);

        donHangList = new ArrayList<>();
        adapter = new DonHangAdapter(requireContext(), donHangList);
        listView.setAdapter(adapter);

        btnXuLy.setOnClickListener(v ->
                loadDonHang("Đang xử lý"));
        btnHoanThanh.setOnClickListener(v ->
                loadDonHang("Hoàn thành"));
        btnHuy.setOnClickListener(v ->
                loadDonHang("Đã hủy"));
        return view;

    }

    private void loadDonHang(String status) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("DonHang");
        database.orderByChild("emailng").equalTo(emailng).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    DonHang donHang = data.getValue(DonHang.class);
                    if (donHang != null && status.equals(donHang.getTrangthai())) {
                        donHangList.add(donHang);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
