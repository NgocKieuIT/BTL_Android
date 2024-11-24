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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlg05.Helpers.ImageManager;
import com.example.btlg05.Post.Post;
import com.example.btlg05.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class AddDiemThuFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    TextView tendiemthu, diachi,sdt;
    Button btnaddDT, btnimage;
    ImageView imageView;
    private Button btnchonanh, btnAddDT;
    private Bitmap selectedImageBitmap;
    private DatabaseReference databaseReference;
    // ActivityResultLauncher để xử lý Intent
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_diem_thu, container, false);
        tendiemthu=view.findViewById(R.id.editName);
        diachi=view.findViewById(R.id.editDiaChi);
        sdt=view.findViewById(R.id.editSDT);
        btnaddDT=view.findViewById(R.id.buttonAdd);
        btnimage=view.findViewById(R.id.buttonImage);
        imageView=view.findViewById(R.id.imageview);
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
                            imageView.setImageBitmap(selectedImageBitmap);
                            imageView.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        // Chọn ảnh
        btnimage.setOnClickListener(v -> chonImage());

        // Thêm bài post
        btnaddDT.setOnClickListener(v -> addDT());
        return view;
    }

    private void addDT() {
        String stten = tendiemthu.getText().toString().trim();
        String stdiadiem = diachi.getText().toString().trim();
        String stSDT = sdt.getText().toString().trim();

        if (stten.isEmpty() || stdiadiem.isEmpty()|| stSDT.isEmpty()) {
            Toast.makeText(getContext(), "Vui long nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển đổi hình ảnh sang Base64
        String imageBase64 = null;
        if (selectedImageBitmap != null) {
            imageBase64 = ImageManager.convertBitmapToBase64(selectedImageBitmap);
        }

        String diemthuid = databaseReference.push().getKey();
        DiemThu diemThu = new DiemThu(diemthuid,stSDT,stdiadiem,stten,imageBase64);
        // Lưu vào Firebase
        databaseReference.child(diemthuid).setValue(diemThu).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Thêm bài điểm thu thành công", Toast.LENGTH_SHORT).show();
                // Reset các trường
                tendiemthu.setText("");
                diachi.setText("");
                sdt.setText("");
                imageView.setVisibility(View.GONE);
                selectedImageBitmap = null;
            } else {
                Toast.makeText(getContext(), "Thêm điểm thu thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chonImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent); // Sử dụng launcher để thay thế startActivityForResult
    }
}