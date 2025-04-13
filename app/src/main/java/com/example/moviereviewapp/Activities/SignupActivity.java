package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moviereviewapp.R;

public class SignupActivity extends AppCompatActivity {
    EditText editTextFirstAndLastName_SignUp, editTextEmail_SignUp, editTextPassword_SignUp, editTextRe_enterPassword_SignUp;
    CheckBox checkBoxShowPass_SignUp;
    androidx.appcompat.widget.AppCompatButton btnCreateAccount_SignUp, btnSignIn_SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTextFirstAndLastName_SignUp = findViewById(R.id.editTextFirstAndLastName_SignUp);
        editTextEmail_SignUp = findViewById(R.id.editTextEmail_SignUp);
        editTextPassword_SignUp = findViewById(R.id.editTextPassword_SignUp);
        editTextRe_enterPassword_SignUp = findViewById(R.id.editTextRe_enterPassword_SignUp);
        checkBoxShowPass_SignUp = findViewById(R.id.checkBoxShowPass_SignUp);
        btnCreateAccount_SignUp = findViewById(R.id.btnCreateAccount_SignUp);
        btnSignIn_SignUp = findViewById(R.id.btnSignIn_SignUp);

        checkBoxShowPass_SignUp.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Typeface currentTypeface = editTextPassword_SignUp.getTypeface();

            if (isChecked) {
                editTextPassword_SignUp.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                editTextRe_enterPassword_SignUp.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                editTextPassword_SignUp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editTextRe_enterPassword_SignUp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

            editTextPassword_SignUp.setTypeface(currentTypeface);
            editTextRe_enterPassword_SignUp.setTypeface(currentTypeface);

            editTextRe_enterPassword_SignUp.setSelection(editTextRe_enterPassword_SignUp.getText().length());
        });

        //ToDo: Xử lý đăng ký tài khoản
    }
    public void onClick_CreateAccount_SignUp(View view) {
        String firstName = editTextFirstAndLastName_SignUp.getText().toString();
        String email = editTextEmail_SignUp.getText().toString();
        String password = editTextPassword_SignUp.getText().toString();
        String reEnterPassword = editTextRe_enterPassword_SignUp.getText().toString();
        //ToDo: Xử lý đăng ký tài khoản

    }

    public void onClick_SignIn_SignUp(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}