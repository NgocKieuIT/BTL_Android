package com.example.btlg05.Post;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.btlg05.Diemthu.ChiTietDTFragment;
import com.example.btlg05.Diemthu.DiemThu;
import com.example.btlg05.Helpers.ImageManager;
import com.example.btlg05.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    private Context context;
    private List<Post> listBaiPost;

    public PostAdapter(Context context, List<Post> listBaiPost) {
        super(context, 0, listBaiPost);
        this.context = context;
        this.listBaiPost = listBaiPost;
    }
    @Override
    public int getCount() {
        return listBaiPost.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        }
        Post item = listBaiPost.get(position);

        if (item != null) {
            TextView textViewTitle, textViewContent;
            ImageView imageViewPlace;
            textViewTitle = convertView.findViewById(R.id.tv_title);
            textViewContent = convertView.findViewById(R.id.tv_content);
            imageViewPlace = convertView.findViewById(R.id.imageViewPost);

            textViewTitle.setText(item.getTitle());
            textViewContent.setText(item.getContent());

            // Tải hình ảnh (nếu có)
            if (item.getImageUrl() != null) {
                if (isUrl(item.getImageUrl())) {
                    Picasso.get().load(item.getImageUrl()).into(imageViewPlace);
                } else {
                    imageViewPlace.setImageBitmap(ImageManager.convertBase64ToBitmap(item.getImageUrl()));
                }
            } else {
                imageViewPlace.setVisibility(View.GONE);
            }
        }
        convertView.setOnClickListener(v -> {
            ChitietPostFragment newFragment = new ChitietPostFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("baipost", item);
            // Đưa bundle vào fragment
            newFragment.setArguments(bundle);
            // Sử dụng FragmentManager để thực hiện thay thế Fragment
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, newFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return convertView;
    }

    // Kiểm tra xem URL có hợp lệ hay không
    public boolean isUrl(String imageUrl) {
        return imageUrl.startsWith("http://") || imageUrl.startsWith("https://");
    }
}
