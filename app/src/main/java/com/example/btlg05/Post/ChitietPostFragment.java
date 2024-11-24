package com.example.btlg05.Post;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btlg05.Diemthu.DiemThu;
import com.example.btlg05.Helpers.ImageManager;
import com.example.btlg05.R;

public class ChitietPostFragment extends Fragment {
    private TextView tvTitle, tvContent;
    private ImageView imageViewPlace;
    private ImageButton imageBack;
    private Post post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chitiet_post, container, false);
        tvTitle = view.findViewById(R.id.textTitle);
        tvContent = view.findViewById(R.id.textContent);
        imageViewPlace = view.findViewById(R.id.imagePost);
        imageBack = view.findViewById(R.id.imagebuttonBack);

        if (getArguments() != null) {
            post = (Post) getArguments().getSerializable("baipost"); // Lấy đối tượng Diemthu từ Bundle
            if (post != null) {
                tvTitle.setText(post.getTitle());
                tvContent.setText(post.getContent());

                if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                    Bitmap bitmap = ImageManager.convertBase64ToBitmap(post.getImageUrl());
                    imageViewPlace.setImageBitmap(bitmap);
                } else {
                    imageViewPlace.setVisibility(View.GONE);
                }
            }
            imageBack.setOnClickListener(v -> {

                getParentFragmentManager().popBackStack();
            });
        }
            return view;
    }
}