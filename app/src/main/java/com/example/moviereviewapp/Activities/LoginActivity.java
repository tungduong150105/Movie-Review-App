package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moviereviewapp.R;

public class LoginActivity extends AppCompatActivity {
    TextView textView_PasswordAssistance;
    EditText editTextEmail_PhoneNumber_LogIn, editTextPassword_LogIn;
    CheckBox checkBoxShowPass_LogIn;
    androidx.appcompat.widget.AppCompatButton btnSignIn_LogIn, btnCreateAccount_LogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các View
        editTextEmail_PhoneNumber_LogIn = findViewById(R.id.editTextEmail_PhoneNumber_LogIn);
        editTextPassword_LogIn = findViewById(R.id.editTextPassword_LogIn);
        btnSignIn_LogIn = findViewById(R.id.btnSignIn_LogIn);
        btnCreateAccount_LogIn = findViewById(R.id.btnCreateAccount_LogIn);
        checkBoxShowPass_LogIn = findViewById(R.id.checkBoxShowPass_LogIn);
        textView_PasswordAssistance = findViewById(R.id.textView_PasswordAssistance);

        textView_PasswordAssistance.setOnClickListener((v) -> {
            Intent intent = new Intent(this, Password_Assistance_Activity.class);
            startActivity(intent);
        });

        checkBoxShowPass_LogIn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Typeface currentTypeface = editTextPassword_LogIn.getTypeface();

            if (isChecked) {
                editTextPassword_LogIn.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                editTextPassword_LogIn.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

            editTextPassword_LogIn.setTypeface(currentTypeface);

            editTextPassword_LogIn.setSelection(editTextPassword_LogIn.getText().length());
        });

        //ToDo: Xử lý sự kiện đăng nhập
    }

    public void onClick_CreateAccount_LogIn(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
}