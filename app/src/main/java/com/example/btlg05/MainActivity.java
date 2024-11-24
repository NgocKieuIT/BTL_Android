package com.example.btlg05;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.btlg05.Diemthu.DiemThuFragment;
import com.example.btlg05.DonHang.LichSuFragment;
import com.example.btlg05.DonHang.TaoDonFragment;
import com.example.btlg05.Post.HomeFragment;
import com.example.btlg05.User.TaiKhoanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("emailng");

        SharedPreferences sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USERNAME", username);
        editor.putString("EMAIL", email);
        editor.apply();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                      selectedFragment = new HomeFragment();
                      break;
                case R.id.nav_create_order:
                    TaoDonFragment taoDonFragment = new TaoDonFragment();
                    // Chuyển sang fragment
                    selectedFragment = taoDonFragment;
                     break;
                case R.id.nav_search:
                    selectedFragment = new DiemThuFragment();
                    break;
                case R.id.nav_order_history:
                    // Chuyển sang lịch sử đơn hàng
                    LichSuFragment lichSuFragment= new LichSuFragment();
                    selectedFragment = lichSuFragment;
                    break;
                case R.id.nav_account:
                    selectedFragment = new TaiKhoanFragment();
                    break;
            }
            // Thay thế Fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, selectedFragment).commit();
            return true;
        });
        // Thiết lập Fragment mặc định
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home); // Mặc định là Home
        }
    }
}
