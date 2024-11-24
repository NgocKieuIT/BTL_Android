package com.example.btlg05.Diemthu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.btlg05.DonHang.ChiTietFragment;
import com.example.btlg05.DonHang.TaoDonFragment;
import com.example.btlg05.Helpers.ImageManager;
import com.example.btlg05.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DiemThuAdapter extends ArrayAdapter<DiemThu> {

    private Context context;
    private List<DiemThu> listDiemThu;

    public DiemThuAdapter(Context context, List<DiemThu> listDiemThu) {
        super(context, 0, listDiemThu);
        this.context = context;
        this.listDiemThu = listDiemThu;
    }
    @Override
    public int getCount() {
        return listDiemThu.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_diemthu, parent, false);
        }
        DiemThu item = listDiemThu.get(position);

        if (item != null) {
            TextView textViewName, textViewSDT, textViewAddress;
            ImageView imageViewPlace;
            textViewName = convertView.findViewById(R.id.textten);
            textViewSDT = convertView.findViewById(R.id.textsdt);
            textViewAddress = convertView.findViewById(R.id.textdiachi);
            imageViewPlace = convertView.findViewById(R.id.image);

            textViewName.setText(item.getTen());
            textViewSDT.setText(item.getSdt());
            textViewAddress.setText(item.getDiachi());

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
            ChiTietDTFragment newFragment = new ChiTietDTFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("diemthu", item);
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
