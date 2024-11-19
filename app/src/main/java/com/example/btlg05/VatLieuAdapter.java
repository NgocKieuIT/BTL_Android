package com.example.btlg05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VatLieuAdapter extends ArrayAdapter<vatlieu> {
    private Context context;
    private List<vatlieu> listVL;

    public VatLieuAdapter(Context context, List<vatlieu> vatlieus) {
        super(context, 0, vatlieus);
        this.context = context;
        this.listVL = vatlieus;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_vatlieu, parent, false);
        }

        vatlieu vl = listVL.get(position);
        Spinner loaiVL = convertView.findViewById(R.id.loai);
        ImageView imageView = convertView.findViewById(R.id.vatlieuImage);
        EditText khoiL = convertView.findViewById(R.id.khoiluong);
        ImageView xoa=convertView.findViewById(R.id.imgxoa);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.material_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loaiVL.setAdapter(adapter);

        loaiVL.setSelection(adapter.getPosition(vl.getLoai()));
        khoiL.setText(String.valueOf(vl.getKhoiluong()));

        loaiVL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) { // Giấy
                    imageView.setImageResource(R.drawable.giay);
                    imageView.setVisibility(View.VISIBLE);
                } else if (pos == 1) { // nhựa
                    imageView.setImageResource(R.drawable.nhua);
                    imageView.setVisibility(View.VISIBLE);
                } else if (pos == 2) { // Kim loại
                    imageView.setImageResource(R.drawable.kimloai);
                    imageView.setVisibility(View.VISIBLE);
                }
                else if (pos == 3) { // thủy tinh
                    imageView.setImageResource(R.drawable.thuytinh);
                    imageView.setVisibility(View.VISIBLE);
                }else {
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                imageView.setVisibility(View.GONE);
            }
        });
        xoa.setOnClickListener(v -> {
            listVL.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
    public List<vatlieu> getVatLieuList() {
        return listVL;
    }

}
