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

public class Changing_Password_Activity extends AppCompatActivity {
    EditText editText_EnterNewPassword_ChangePassword, editText_Re_EnterPassword_ChangePassword;
    androidx.appcompat.widget.AppCompatButton btn_SaveChanges_ChangePassword;
    UserAPI userAPI;

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
        btn_SaveChanges_ChangePassword.setOnClickListener((v) -> {
            String newPassword = editText_EnterNewPassword_ChangePassword.getText().toString();
            String reEnteredPassword = editText_Re_EnterPassword_ChangePassword.getText().toString();
            //ToDo: Xử lý sự kiện khi người dùng nhấn nút "Save changes"
            if (newPassword.isEmpty()) {
                Toast.makeText(this, "Please enter your new password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPassword.equals(reEnteredPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newPassword.length() < 8) {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            String token = getIntent().getStringExtra("token");
            JSONObject body = new JSONObject();
            try {
                body.put("token", token);
                body.put("password", newPassword);
                userAPI.call_api(userAPI.get_UserAPI() + "user/update_password", body.toString(), new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() -> Toast.makeText(Changing_Password_Activity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        if (response.code() == 200) {
                            Intent intent = new Intent(Changing_Password_Activity.this, LoginActivity.class);
                            startActivity(intent);
                            runOnUiThread(() -> Toast.makeText(Changing_Password_Activity.this, "Password changed successfully", Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(Changing_Password_Activity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show());
                        }
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        userAPI = new UserAPI();
    }
}