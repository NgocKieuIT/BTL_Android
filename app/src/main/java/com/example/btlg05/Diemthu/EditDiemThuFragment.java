package com.example.btlg05.Diemthu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlg05.Helpers.ImageManager;
import com.example.btlg05.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class EditDiemThuFragment extends Fragment {
    private EditText edname, eddiachi, edphone;
    private ImageView imageViewPlace;
    private Button sua, chonanh;
    private DiemThu diemThu;
    private Bitmap selectedImageBitmap;
    private DatabaseReference databaseReference;
    // ActivityResultLauncher để xử lý Intent
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_diem_thu, container, false);
        edname = view.findViewById(R.id.suaName);
        eddiachi = view.findViewById(R.id.suaDiaChi);
        edphone = view.findViewById(R.id.suaSDT);
        imageViewPlace = view.findViewById(R.id.imageviewsua);
        sua=view.findViewById(R.id.buttonedit);
        chonanh=view.findViewById(R.id.buttonImageEdit);
        if (getArguments() != null) {
            diemThu = (DiemThu) getArguments().getSerializable("diemthusua"); // Lấy đối tượng Diemthu từ Bundle
            if (diemThu != null) {
                edname.setText(diemThu.getTen());
                eddiachi.setText(diemThu.getDiachi());
                edphone.setText(diemThu.getSdt());
                // Tải hình ảnh (nếu có)
                if (diemThu.getImageUrl() != null) {
                    if (isUrl(diemThu.getImageUrl())) {
                        Picasso.get().load(diemThu.getImageUrl()).into(imageViewPlace);
                    } else {
                        imageViewPlace.setImageBitmap(ImageManager.convertBase64ToBitmap(diemThu.getImageUrl()));
                    }
                } else {
                    imageViewPlace.setVisibility(View.GONE);
                }
            }
        }
        // Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("DiemThu");
        // Khởi tạo ActivityResultLauncher

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                            imageViewPlace.setImageBitmap(selectedImageBitmap);
                            imageViewPlace.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        chonanh.setOnClickListener(v -> chonImage());
        sua.setOnClickListener(v -> editDT());
            return view;
    }

    private void editDT() {
        String stten = edname.getText().toString().trim();
        String stdiadiem = eddiachi.getText().toString().trim();
        String stSDT = edphone.getText().toString().trim();

        if (stten.isEmpty() || stdiadiem.isEmpty()|| stSDT.isEmpty()) {
            Toast.makeText(getContext(), "Vui long nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        // Chuyển đổi hình ảnh sang Base64
        String imageBase64 = null;
        if (selectedImageBitmap != null) {
            imageBase64 = ImageManager.convertBitmapToBase64(selectedImageBitmap);
        }
        diemThu.setTen(stten);
        diemThu.setDiachi(stdiadiem);
        diemThu.setSdt(stSDT);
        diemThu.setImageUrl(imageBase64);
        databaseReference.child(diemThu.getId()).setValue(diemThu).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack(); // Quay lại Fragment trước đó
            } else {
                Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chonImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent); // Sử dụng launcher để thay thế startActivityForResult
    }
    public boolean isUrl(String imageUrl) {
        return imageUrl.startsWith("http://") || imageUrl.startsWith("https://");
    }
}