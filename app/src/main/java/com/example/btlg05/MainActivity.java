package com.example.btlg05;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.btlg05.Diemthu.DiemThuFragment;
import com.example.btlg05.DonHang.LichSuFragment;
import com.example.btlg05.DonHang.TaoDonFragment;
import com.example.btlg05.Post.HomeFragment;
import com.example.btlg05.User.TaiKhoanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //sensor
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private BottomNavigationView bottomNavigationView;

    private static final float LIGHT_THRESHOLD = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

            // Đăng ký lắng nghe sự kiện của cảm biến ánh sáng
            if (lightSensor != null) {
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Kiểm tra nếu sự kiện là từ cảm biến ánh sáng
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightLevel = event.values[0];  // Giá trị ánh sáng

            // Kiểm tra nếu mức độ ánh sáng vượt quá ngưỡng
            if (lightLevel > LIGHT_THRESHOLD) {
                bottomNavigationView.setBackgroundColor(Color.parseColor("#ffe14a"));
                ColorStateList textColor = ColorStateList.valueOf(Color.parseColor("#05FF05"));
                bottomNavigationView.setItemIconTintList(textColor);
                bottomNavigationView.setItemTextColor(textColor);
            } else {
                bottomNavigationView.setBackgroundColor(Color.WHITE);
                ColorStateList textColor = ColorStateList.valueOf(Color.parseColor("#248B46"));
                bottomNavigationView.setItemIconTintList(textColor);
                bottomNavigationView.setItemTextColor(textColor);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
