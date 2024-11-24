package com.example.btlg05.Post;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlg05.Helpers.ImageManager;
import com.example.btlg05.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class AddPostFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    TextView title, content;
    Button btnaddPost, image;
    ImageView imageView;
    private Button btnSelectImage, btnAddPost;
    private Bitmap selectedImageBitmap;
    private DatabaseReference databaseReference;
    // ActivityResultLauncher để xử lý Intent
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_add_post, container, false);
        title=view.findViewById(R.id.editTitle);
        content=view.findViewById(R.id.editContent);
        btnaddPost=view.findViewById(R.id.buttonAddPost);
        image=view.findViewById(R.id.buttonAddImage);
        imageView=view.findViewById(R.id.imagev);
        // Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
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
        image.setOnClickListener(v -> openImagePicker());

        // Thêm bài post
        btnaddPost.setOnClickListener(v -> addPost());
        return view;
    }

    private void addPost() {
        String sttitle = title.getText().toString().trim();
        String stcontent = content.getText().toString().trim();

        if (sttitle.isEmpty() || stcontent.isEmpty()) {
            Toast.makeText(getContext(), "Vui long nhập Tiêu đề và nội dung bài viết", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển đổi hình ảnh sang Base64
        String imageBase64 = null;
        if (selectedImageBitmap != null) {
            imageBase64 = ImageManager.convertBitmapToBase64(selectedImageBitmap);
        }

        // Tạo object Post
        String postId = databaseReference.push().getKey();
        Post post = new Post(postId, sttitle, stcontent, imageBase64);

        // Lưu vào Firebase
        databaseReference.child(postId).setValue(post).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Thêm bài đăng thành công", Toast.LENGTH_SHORT).show();
                // Reset các trường
                title.setText("");
                content.setText("");
                imageView.setVisibility(View.GONE);
                selectedImageBitmap = null;
            } else {
                Toast.makeText(getContext(), "Thêm bài đăng thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent); // Sử dụng launcher để thay thế startActivityForResult
    }

}