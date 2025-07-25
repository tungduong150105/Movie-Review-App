package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moviereviewapp.Models.UserAPI;
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

public class Password_Assistance_Activity extends AppCompatActivity {
    EditText editTextEmail_PhoneNumber_PasswordAssistance;
    androidx.appcompat.widget.AppCompatButton btn_Continue_PasswordAssistance;
    UserAPI userAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_assistance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTextEmail_PhoneNumber_PasswordAssistance = findViewById(R.id.editTextEmail_PhoneNumber_PasswordAssistance);
        btn_Continue_PasswordAssistance = findViewById(R.id.btn_Continue_PasswordAssistance);
        userAPI = new UserAPI();
        btn_Continue_PasswordAssistance.setOnClickListener(v -> {
            String email = editTextEmail_PhoneNumber_PasswordAssistance.getText().toString();
            //TODO: xử lý sự kiện nhập và xác nhận email
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject body = new JSONObject();
            try {
                body.put("email", email);
                userAPI.call_api(userAPI.get_UserAPI() + "/user/request_reset_password", body.toString(), new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() -> Toast.makeText(Password_Assistance_Activity.this, "Failed to request reset password, please try again!!!", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        if (response.code() == 201) {
                            Intent intent = new Intent(Password_Assistance_Activity.this, SecurityCode_Entering_Activity.class);
                            startActivity(intent);
                        } else {
                            runOnUiThread(() -> Toast.makeText(Password_Assistance_Activity.this, "Failed to request reset password, please try again", Toast.LENGTH_SHORT).show());
                        }
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }
}