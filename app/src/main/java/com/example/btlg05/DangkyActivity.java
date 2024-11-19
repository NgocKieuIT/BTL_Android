package com.example.btlg05;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;

import java.util.List;

public class DangkyActivity extends AppCompatActivity {
    Button dangky, dangnhap;
    EditText tenTK, email,password, xacnhanMK;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        dangky=findViewById(R.id.btnDangKy);
        dangnhap=findViewById(R.id.btnDangNhap);
        tenTK=findViewById(R.id.etUsername);
        email=findViewById(R.id.etEmail);
        password=findViewById(R.id.etPassword);
        xacnhanMK=findViewById(R.id.etXacNhanPassword);

        dangky.setOnClickListener(v -> registerUser());
        dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DangnhapActivity.class);
                startActivity(intent);
            }
        });

    }

    private void registerUser() {
        String username = tenTK.getText().toString().trim();
        String useremail = email.getText().toString().trim();
        String userpassword = password.getText().toString().trim();
        String xacnhan = xacnhanMK.getText().toString().trim();

        // Check dữ liệu rỗng
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(useremail) || TextUtils.isEmpty(userpassword) || TextUtils.isEmpty(xacnhan)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check match password
        if (!userpassword.equals(xacnhan)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng ký
        auth.createUserWithEmailAndPassword(useremail, userpassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Lấy Id và lưu và database
                        String userId = auth.getCurrentUser().getUid();
                        User user = new User(username, useremail, userpassword); // Lưu mật khẩu dạng thuần

                        // Lưu thông tin người dùng vào Firebase Realtime Database
                        databaseReference.child(userId).setValue(user)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(getApplicationContext(),DangnhapActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(this, "Lỗi lưu thông tin", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Lỗi đăng ký: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}