package com.example.btlg05;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class TaoDonFragment extends Fragment {
    Button xacnhan;
    private ListView listView;
    private Button buttonAdd;
    private VatLieuAdapter adapter;
    private List<vatlieu> listvatlieu;
    Spinner diemThu, phuongThuc;
    EditText diaChi, SDT,ngayGui;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_tao_don, container, false);
         xacnhan= view.findViewById(R.id.btntaodon);
         listView=view.findViewById(R.id.list_view);
         buttonAdd=view.findViewById(R.id.btn_add);
         diemThu=view.findViewById(R.id.dsdiemthu);
         phuongThuc=view.findViewById(R.id.dsphuongthuc);
         diaChi=view.findViewById(R.id.diachiuser);
         SDT=view.findViewById(R.id.sdtuser);
         ngayGui=view.findViewById(R.id.ngaygui);


        listvatlieu = new ArrayList<>();
        adapter = new VatLieuAdapter(getActivity(), listvatlieu);
        listView.setAdapter(adapter);

        buttonAdd.setOnClickListener(v -> {
            vatlieu newvatlieu = new vatlieu();
            listvatlieu.add(newvatlieu);
            adapter.notifyDataSetChanged();
            setChieuCao(listView);

        });

        xacnhan.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // Cập nhật dữ liệu từ từng item trong ListView vào listvatlieu
                 for (int i = 0; i < listvatlieu.size(); i++) {
                     View itemView = listView.getChildAt(i);
                     if (itemView != null) {
                         // Lấy các giá trị từ từng item và cập nhật vào listvatlieu
                         EditText khoiLuongField = itemView.findViewById(R.id.khoiluong);
                         Spinner loaiSpinner = itemView.findViewById(R.id.loai);

                         // Lưu thông tin vào đối tượng vatlieu tương ứng
                         vatlieu vl = listvatlieu.get(i);
                         vl.setKhoiluong(Float.parseFloat(khoiLuongField.getText().toString()));
                         vl.setLoai(loaiSpinner.getSelectedItem().toString());
                     }
                 }

                 // Lấy dữ liệu
                 String laydiemthu=diemThu.getSelectedItem().toString();
                 String layphuongthuc=phuongThuc.getSelectedItem().toString();
                 String laydiachi=diaChi.getText().toString();
                 String laysdt=SDT.getText().toString();
                 String layngay=ngayGui.getText().toString();

                 String layusername=getArguments().getString("username");
                 String layemailng=getArguments().getString("emailng");

                 ArrayList<vatlieu>danhsach=new ArrayList<>();

                 for (vatlieu vatLieu : listvatlieu) {
                     // Lấy thông tin từ từng item
                     String loai = vatLieu.getLoai();
                     float khoiLuong = vatLieu.getKhoiluong();
                     danhsach.add(new vatlieu(loai, khoiLuong));
                 }
                 // Tạo Bundle để truyền dữ liệu
                 Bundle bundle = new Bundle();
                 bundle.putString("diemThu", laydiemthu);
                 bundle.putString("phuongThuc", layphuongthuc);
                 bundle.putString("diaChi", laydiachi);
                 bundle.putString("sdt", laysdt);
                 bundle.putString("ngayGui", layngay);
                 bundle.putString("username", layusername);
                 bundle.putString("emailng", layemailng);
                 bundle.putParcelableArrayList("danhsach",danhsach);


                 // Chuyển đến Fragment
                 Fragment newFragment = new XacNhanFragment();
                 newFragment.setArguments(bundle);
                 FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                 // Thay đổi fragment trong container
                 transaction.replace(R.id.nav_host_fragment, newFragment);
                 transaction.addToBackStack(null); // Thêm vào back stack để có thể trở lại
                 transaction.commit();
             }
         });

         return view;
    }
    private void setChieuCao(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // Đo kích thước item
            totalHeight += listItem.getMeasuredHeight(); // Cộng chiều cao của từng item
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}