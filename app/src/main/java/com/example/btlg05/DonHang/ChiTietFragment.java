package com.example.btlg05.DonHang;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlg05.DonHang.DonHang;
import com.example.btlg05.DonHang.LichSuFragment;
import com.example.btlg05.R;
import com.example.btlg05.VatLieu.VatlieuKLAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChiTietFragment extends Fragment {
    TextView tvemailng,tvdChing,tvsdtng,tvdiemthu,tvpthuc,tvstk,tvtennh,tvngaygui,tvtrangthai, tvmadon;
    ListView listView;
    private VatlieuKLAdapter adapter;
    private DonHang donHang;
    Button huy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chi_tiet, container, false);
        tvemailng=view.findViewById(R.id.tvemailng);
        tvdChing=view.findViewById(R.id.tvdiaching);
        tvsdtng=view.findViewById(R.id.tvsdtng);
        tvdiemthu=view.findViewById(R.id.tvtendiemthu);
        tvpthuc=view.findViewById(R.id.tvphuongthuc);
        tvstk=view.findViewById(R.id.tvstk);
        tvtennh=view.findViewById(R.id.tvtennh);
        tvngaygui=view.findViewById(R.id.tvngaygui);
        tvtrangthai=view.findViewById(R.id.trangthai);
        tvmadon=view.findViewById(R.id.tvmadonhang);
        listView=view.findViewById(R.id.listViewdonhang);
        huy=view.findViewById(R.id.btnHuy);

        if (getArguments() != null) {
            donHang = (DonHang) getArguments().getSerializable("donhang"); // Lấy đối tượng DonHang từ Bundle
            if (donHang != null) {
                // Gán dữ liệu vào TextView
                tvemailng.setText(donHang.getEmailng());
                tvdChing.setText(donHang.getDiaching());
                tvsdtng.setText(donHang.getSdtng());
                tvdiemthu.setText(donHang.getTendiemthu());
                tvpthuc.setText(donHang.getPhuongthuc());
                tvstk.setText(donHang.getStk());
                tvtennh.setText(donHang.getTennganhang());
                tvngaygui.setText(donHang.getNgaygui());
                tvtrangthai.setText(donHang.getTrangthai());
                tvmadon.setText(donHang.getId());
                adapter = new VatlieuKLAdapter(getContext(), donHang.getVatlieu());
                listView.setAdapter(adapter);
            }
        }

        // Kiểm tra trạng thái đơn hàng và hiển thị/ẩn nút "Hủy"
        if ("Đang xử lý".equals(donHang.getTrangthai())) {
            huy.setVisibility(View.VISIBLE);
            huy.setOnClickListener(v -> {
                // Tạo một hộp thoại xác nhận
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận hủy")
                        .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            // Nếu người dùng chọn "Có", gọi hàm hủy đơn hàng
                            huyDonHang(donHang);
                        })
                        .setNegativeButton("Không", (dialog, which) -> {
                            // Nếu người dùng chọn "Không", đóng hộp thoại mà không làm gì
                            dialog.dismiss();
                        })
                        .show();
            });
        } else {
            huy.setVisibility(View.GONE);
        }
        return view;
    }

    private void huyDonHang(DonHang donHang) {
        // Thực hiện hủy đơn hàng trong Firebase
        DatabaseReference donHangRef = FirebaseDatabase.getInstance().getReference("DonHang").child(donHang.getId());

        donHangRef.child("trangthai").setValue("Đã hủy")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Đơn hàng đã được hủy", Toast.LENGTH_SHORT).show();
                    donHang.setTrangthai("Đã hủy"); // Cập nhật trạng thái cục bộ để phản ánh ngay lập tức

                    // Tạo một đối tượng Bundle để truyền dữ liệu
                    Bundle bundle = new Bundle();
                    bundle.putString("emailng", donHang.getEmailng()); // Thêm email vào bundle
                    Fragment newFragment = new LichSuFragment();
                    newFragment.setArguments(bundle);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, newFragment);
                    transaction.addToBackStack(null); // Thêm vào back stack để có thể trở lại
                    transaction.commit();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Không thể hủy đơn hàng", Toast.LENGTH_SHORT).show();
                });
    }
}

