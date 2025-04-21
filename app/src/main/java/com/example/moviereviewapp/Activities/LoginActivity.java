package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moviereviewapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    void login(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public void onClick_SignIn_LogIn(View view) throws JSONException {
        String username = editTextEmail_PhoneNumber_LogIn.getText().toString();
        String password = editTextPassword_LogIn.getText().toString();

        //ToDo: Xử lý đăng nhập
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);

        login("https://movie-review-app-be.onrender.com/user/login", body.toString(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("LoginActivity", "Failed to login", e);
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Failed to login, please login again", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 202) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}