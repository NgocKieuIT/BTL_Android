package com.example.btlg05.Diemthu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlg05.DonHang.DonHang;
import com.example.btlg05.DonHang.LichSuFragment;
import com.example.btlg05.DonHang.TaoDonFragment;
import com.example.btlg05.Helpers.ImageManager;
import com.example.btlg05.Map.MapsFragment;
import com.example.btlg05.R;
import com.example.btlg05.User.DangkyActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChiTietDTFragment extends Fragment {
    private TextView textViewName, textViewAddress, textViewPhone;
    private ImageView imageViewPlace;
    private ImageButton imageBack, imagetaodon;
    private Button buttonEdit, buttonDelete;
    private DiemThu diemThu;

    private String currentLat ="";
    private String locationName ="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_chi_tiet_d_t, container, false);
        textViewName = view.findViewById(R.id.tv_tendt);
        textViewAddress = view.findViewById(R.id.tv_diachidt);
        textViewPhone = view.findViewById(R.id.tv_sdtdt);
        imageViewPlace = view.findViewById(R.id.img_detail_info);
        imageBack = view.findViewById(R.id.imagebuttonBack);
        imagetaodon=view.findViewById(R.id.btnoder);
        buttonDelete=view.findViewById(R.id.btn_delete);
        buttonEdit = view.findViewById(R.id.btn_edit);
        TextView textView = view.findViewById(R.id.tv);
        textView.setOnClickListener(v -> openMapFragment());

        //Kiểm tra có phải admin không
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        String layemailng = sharedPreferences.getString("EMAIL", "Unknown");
        if(layemailng != null && layemailng.equals("ngockieu1692003@gmail.com")){
            buttonEdit.setVisibility(View.VISIBLE);
            buttonDelete.setVisibility(View.VISIBLE);
        }

        if (getArguments() != null) {
            diemThu = (DiemThu) getArguments().getSerializable("diemthu"); // Lấy đối tượng Diemthu từ Bundle
            if (diemThu != null) {
                textViewName.setText(diemThu.getTen());
                textViewAddress.setText(diemThu.getDiachi());
                textViewPhone.setText(diemThu.getSdt());

                if (diemThu.getImageUrl() != null && !diemThu.getImageUrl().isEmpty()) {
                    Bitmap bitmap = ImageManager.convertBase64ToBitmap(diemThu.getImageUrl());
                    imageViewPlace.setImageBitmap(bitmap);
                } else {
                    imageViewPlace.setVisibility(View.GONE);
                }
            }
            imageBack.setOnClickListener(v -> {

                getParentFragmentManager().popBackStack();
            });

            imagetaodon.setOnClickListener(v->{
                Fragment newFragment = new TaoDonFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null); // Thêm vào back stack để có thể trở lại
                transaction.commit();
            });
        }
        buttonDelete.setOnClickListener(v->{
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DiemThu");
            // Tìm điểm thu có id
            databaseReference.orderByChild("id").equalTo(diemThu.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Xóa điểm thu
                            snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Xóa điểm thu thành công!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Xóa điểm thu thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getContext(), "Không tìm thấy điểm thu này!", Toast.LENGTH_SHORT).show();
                    }
                    Fragment newFragment = new DiemThuFragment();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, newFragment);
                    transaction.addToBackStack(null); // Thêm vào back stack để có thể trở lại
                    transaction.commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi
                    Toast.makeText(getContext(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
        buttonEdit.setOnClickListener(v->{
            EditDiemThuFragment newFragment = new EditDiemThuFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("diemthusua", diemThu);
            // Đưa bundle vào fragment
            newFragment.setArguments(bundle);
            // Sử dụng FragmentManager để thực hiện thay thế Fragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, newFragment);
            transaction.addToBackStack(null); // Thêm vào back stack để có thể trở lại
            transaction.commit();
        });

        return view;
    }
    private void openMapFragment() {
        currentLat = textViewAddress.getText().toString();
        locationName = textViewName.getText().toString();
        MapsFragment mapsFragment = MapsFragment.newInstance(
                currentLat, locationName
        );

        // Chuyển sang MapsFragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, mapsFragment); // R.id.fragment_container là container chứa fragment
        transaction.addToBackStack(null);  // Thêm vào back stack để có thể quay lại fragment trước
        transaction.commit();
    }
}