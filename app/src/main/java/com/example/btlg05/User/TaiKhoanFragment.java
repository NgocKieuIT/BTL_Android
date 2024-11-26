package com.example.btlg05.User;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlg05.Diemthu.AddDiemThuFragment;
import com.example.btlg05.DonHang.TaoDonFragment;
import com.example.btlg05.MainActivity;
import com.example.btlg05.Post.AddPostFragment;
import com.example.btlg05.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TaiKhoanFragment extends Fragment {
    TextView  tvuser,tvemail;
    Button dangbai, themdiemthu,dangxuat, huytk;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_tai_khoan, container, false);

        dangxuat=view.findViewById(R.id.btnDXuat);
        tvuser=view.findViewById(R.id.tvName);
        tvemail=view.findViewById(R.id.tvEmail);
        dangbai=view.findViewById(R.id.admin_addPost);
        themdiemthu=view.findViewById(R.id.admin_addDT);
        huytk=view.findViewById(R.id.btnHuyTK);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("USERNAME", "Unknown");
        String emailng = sharedPreferences.getString("EMAIL", "Unknown");
        tvuser.setText(username);
        tvemail.setText(emailng);
        //Kiểm tra có phải admin không
        if(emailng != null && emailng.equals("ngockieu1692003@gmail.com")){
            themdiemthu.setVisibility(View.VISIBLE);
            dangbai.setVisibility(View.VISIBLE);
        }

        dangbai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new AddPostFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null); // Thêm vào back stack để có thể trở lại
                transaction.commit();
            }
        });
        themdiemthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new AddDiemThuFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null); // Thêm vào back stack để có thể trở lại
                transaction.commit();
            }
        });
        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_INFO", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Xóa toàn bộ dữ liệu
                editor.apply();
                Intent intent = new Intent(getActivity(), DangkyActivity.class);
                startActivity(intent);
            }
        });
        huytk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một hộp thoại xác nhận
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận hủy")
                        .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            // Nếu người dùng chọn "Có", gọi hàm hủy đơn hàng
                            huytaikhoan(emailng);
                        })
                        .setNegativeButton("Không", (dialog, which) -> {
                            // Nếu người dùng chọn "Không", đóng hộp thoại mà không làm gì
                            dialog.dismiss();
                        })
                        .show();
            }
        });
        return view;
    }

    private void huytaikhoan(String emailng) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference.orderByChild("email").equalTo(emailng).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Xóa dữ liệu trong Realtime Database
                        snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Xóa dữ liệu tài khoản thành công!", Toast.LENGTH_SHORT).show();
                                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                if (currentUser != null) {
                                    currentUser.delete().addOnCompleteListener(task_new -> {
                                        if (task_new.isSuccessful()) {
                                            Toast.makeText(getContext(), "Xóa tài khoản thành công!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Xóa tài khoản thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Không có người dùng đăng nhập!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Xóa dữ liệu tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy tài khoản với email này!", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getActivity(), DangkyActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
                Toast.makeText(getContext(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}