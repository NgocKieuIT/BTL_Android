package com.example.btlg05.DonHang;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlg05.DonHang.DonHang;
import com.example.btlg05.DonHang.LichSuFragment;
import com.example.btlg05.R;
import com.example.btlg05.VatLieu.VatlieuKLAdapter;
import com.example.btlg05.VatLieu.vatlieu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class XacNhanFragment extends Fragment {
    TextView tenng, sdtng, diaching, phuongthuc, ngaygui;
    TextView tendt, sdtdt, diachidt, stk,tennganhang;
    private List<vatlieu> danhsachvatlieu;
    Button xacnhandon;
    ListView listView;
    private VatlieuKLAdapter adapter;

    String soTK,nganhang, diemThu,pthuc,dChi, sdt, ngGui, userName, email;
    ArrayList<vatlieu> danhSachVatLieu;
    private DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_xac_nhan, container, false);

        tenng=view.findViewById(R.id.ten);
        sdtng=view.findViewById(R.id.sdt);
        diaching=view.findViewById(R.id.diachi);
        phuongthuc=view.findViewById(R.id.phuongthu);
        ngaygui=view.findViewById(R.id.ngaygui);
        tendt=view.findViewById(R.id.tendiemthu);
        sdtdt=view.findViewById(R.id.sdtdiemthu);
        diachidt=view.findViewById(R.id.diachidt);
        listView = view.findViewById(R.id.listViewVatLieu);
        xacnhandon=view.findViewById(R.id.btnxacnhan);
        
        stk=view.findViewById(R.id.stk);
        tennganhang=view.findViewById(R.id.tennh);

        // Nhận dữ liệu từ Bundle
        Bundle bundle = getArguments();

        if (bundle != null) {
            diemThu = bundle.getString("diemThu");
            pthuc = bundle.getString("phuongThuc");
            dChi = bundle.getString("diaChi");
            sdt = bundle.getString("sdt");
            ngGui = bundle.getString("ngayGui");
            userName=bundle.getString("username");
            email=bundle.getString("emailng");
            danhSachVatLieu = bundle.getParcelableArrayList("danhsach");

            //Gán dữ liệu
            tenng.setText(userName);
            tendt.setText(diemThu);
            sdtng.setText(sdt);
            phuongthuc.setText(pthuc);
            ngaygui.setText(ngGui);
            diaching.setText(dChi);
            adapter = new VatlieuKLAdapter(getContext(), danhSachVatLieu);
            listView.setAdapter(adapter);

            // sdtdt, diachidt được lấy tham chiếu qua tên điểm thu
            // Gọi hàm tìm kiếm
            timDiemThu(diemThu);
        }
        String trangthai="Đang xử lý";
        float sotien=0;
        //Xác nhận
        xacnhandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lấy dữ liệu nhập vào cửa ngân hàng
                soTK=stk.getText().toString();
                nganhang=tennganhang.getText().toString();

                // Khởi tạo Firebase Realtime Database
                databaseReference = FirebaseDatabase.getInstance().getReference("DonHang");


                DonHang donHang=new DonHang(null, danhSachVatLieu,diemThu, email, dChi,
                        sdt, pthuc, ngGui, soTK, nganhang, trangthai, sotien);

                String newId = databaseReference.push().getKey();
                if (newId != null) {
                    donHang.setId(newId);
                    databaseReference.child(newId).setValue(donHang)
                            .addOnSuccessListener(aVoid -> {
                                // Thành công
                                Toast.makeText(getContext(), "Tạo đơn hàng thành công", Toast.LENGTH_SHORT).show();
                                // Tạo một đối tượng Bundle để truyền dữ liệu
                                Bundle bundle = new Bundle();
                                bundle.putString("emailng", email); // Thêm email vào bundle
                                Fragment newFragment = new LichSuFragment();
                                newFragment.setArguments(bundle);
                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                transaction.replace(R.id.nav_host_fragment, newFragment);
                                transaction.addToBackStack(null); // Thêm vào back stack để có thể trở lại
                                transaction.commit();

                            })
                            .addOnFailureListener(e -> {
                                // Thất bại
                                Toast.makeText(getContext(), "Tạo đơn hàng thất bại", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });

            return view;
    }

    private void timDiemThu(String dt) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("DiemThu");
        databaseRef.orderByChild("ten").equalTo(dt).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String diachi = snapshot.child("diachi").getValue(String.class);
                        String sdt = snapshot.child("sdt").getValue(String.class);
                        String ten = snapshot.child("ten").getValue(String.class);
                        diachidt.setText(diachi);
                        sdtdt.setText(sdt);
                    // Use or display the data as needed
                        Log.d("FirebaseData", "Tên: " + ten + ", Địa chỉ: " + diachi + ", SĐT: " + sdt);
                    }
                } else {
                    Log.d("FirebaseData", "Không tìm thấy điểm thu với tên: " + dt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseData", "Error: " + error.getMessage());
            }
        });
    }
}