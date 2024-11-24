package com.example.btlg05.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlg05.MainActivity;
import com.example.btlg05.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DangnhapActivity extends AppCompatActivity {
    Button btndangNhap;
    TextView tvdangKy;
    EditText email, matkhau;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        tvdangKy=findViewById(R.id.tvDangKyLink);
        btndangNhap= findViewById(R.id.btnLogin);
        email=findViewById(R.id.etEmail);
        matkhau=findViewById(R.id.etPassword);

        btndangNhap.setOnClickListener(v -> loginUser());

        // Xử lý sự kiện khi nhấn vào "Đăng ký"
        tvdangKy.setOnClickListener(v -> {
            // Chuyển sang trang đăng ký
            startActivity(new Intent(DangnhapActivity.this, DangkyActivity.class));
        });


    }

    private void loginUser() {
        String useremail = email.getText().toString().trim();
        String password = matkhau.getText().toString().trim();

        // Kiểm tra đầu vào
        if (TextUtils.isEmpty(useremail) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng nhập với Firebase Authentication
        auth.signInWithEmailAndPassword(useremail, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công, có thể lấy thông tin từ Realtime Database nếu cần
                        FirebaseUser user = auth.getCurrentUser();
                        // Lấy thông tin người dùng từ Realtime Database nếu cần
                        databaseReference.child(user.getUid()).get().addOnCompleteListener(dbTask -> {
                            if (dbTask.isSuccessful()) {
                                // Xử lý thông tin người dùng ở đây
                                Intent intent = new Intent(DangnhapActivity.this, MainActivity.class);
                                User loggedInUser = dbTask.getResult().getValue(User.class);
                                intent.putExtra("username", loggedInUser.getUsername());
                                intent.putExtra("emailng", loggedInUser.getEmail());
                                Toast.makeText(this, "Đăng nhập thành công: " + loggedInUser.getUsername(), Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        });
                    } else {
                        Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}