package com.example.btlg05.Diemthu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.btlg05.DonHang.DonHangAdapter;
import com.example.btlg05.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiemThuFragment extends Fragment {
    private ListView listView;
    private DiemThuAdapter adapter;
    private List<DiemThu> diemThuList;
    private List<DiemThu> locdiadiem; // Danh sách đã lọc
    private EditText editSearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_diem_thu, container, false);
        listView=view.findViewById(R.id.listviewdiadiem);

        diemThuList = new ArrayList<>();
        locdiadiem=new ArrayList<>();
        // Tạo Adapter
        adapter = new DiemThuAdapter(requireContext(), locdiadiem);
        listView.setAdapter(adapter);

        //Tìm kiếm
        // 4. Thiết lập thanh tìm kiếm
        editSearch = view.findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLocations(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Hiển thị từ firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DiemThu");
        // Lấy dữ liệu từ Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diemThuList.clear(); // Xóa danh sách cũ để tránh trùng lặp
                for (DataSnapshot data : snapshot.getChildren()) {
                    DiemThu diemThu = data.getValue(DiemThu.class); // Map Firebase data sang object
                    diemThuList.add(diemThu);
                }
                filterLocations(editSearch.getText().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void filterLocations(String keyword) {
        locdiadiem.clear();

        if (keyword.isEmpty()) {
            // Nếu không có từ khóa, hiển thị toàn bộ danh sách
            locdiadiem.addAll(diemThuList);
        } else {
            for (DiemThu item : diemThuList) {
                // Kiểm tra null trước khi so sánh
                String name = item.getTen() != null ? item.getTen().toLowerCase() : "";
                String address = item.getDiachi() != null ? item.getDiachi().toLowerCase() : "";

                if (name.contains(keyword.toLowerCase()) || address.contains(keyword.toLowerCase())) {
                    locdiadiem.add(item);
                }
            }
        }

        // Cập nhật RecyclerView sau khi lọc
        adapter.notifyDataSetChanged();
    }
}