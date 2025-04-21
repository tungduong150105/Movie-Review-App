package com.example.moviereviewapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moviereviewapp.R;

public class Changing_Password_Activity extends AppCompatActivity {
    EditText editText_EnterNewPassword_ChangePassword, editText_Re_EnterPassword_ChangePassword;
    androidx.appcompat.widget.AppCompatButton btn_SaveChanges_ChangePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_changing_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editText_EnterNewPassword_ChangePassword = findViewById(R.id.editText_EnterNewPassword_ChangePassword);
        editText_Re_EnterPassword_ChangePassword = findViewById(R.id.editText_Re_EnterPassword_ChangePassword);
        btn_SaveChanges_ChangePassword = findViewById(R.id.btn_SaveChanges_ChangePassword);
    }
    public void onClick_SaveChanges_ChangePassword(View view) {
        String newPassword = editText_EnterNewPassword_ChangePassword.getText().toString();
        String reEnteredPassword = editText_Re_EnterPassword_ChangePassword.getText().toString();
        //ToDo: Xử lý sự kiện khi người dùng nhấn nút "Save changes"
    }
}