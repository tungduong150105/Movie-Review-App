package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moviereviewapp.Models.TMDBAPI;
import com.example.moviereviewapp.Models.UserAPI;
import com.example.moviereviewapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    TextView textView_PasswordAssistance;
    EditText editTextEmail_PhoneNumber_LogIn, editTextPassword_LogIn;
    CheckBox checkBoxShowPass_LogIn;
    androidx.appcompat.widget.AppCompatButton btnSignIn_LogIn, btnCreateAccount_LogIn;
    UserAPI userAPI;
    TMDBAPI tmdbAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        userAPI = new UserAPI();
        tmdbAPI = new TMDBAPI();
    }

    public void onClick_CreateAccount_LogIn(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClick_SignIn_LogIn(View view) throws JSONException {
        String username = editTextEmail_PhoneNumber_LogIn.getText().toString();
        String password = editTextPassword_LogIn.getText().toString();

        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);

        userAPI.call_api(userAPI.get_UserAPI() + "/user/login", body.toString(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("LoginActivity", "Failed to login", e);
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Failed to login, please login again", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 202) {
                    try {
                        assert response.body() != null;
                        JSONObject loginJson = new JSONObject(response.body().string());
                        final String[] session_id = {"ad7f515e2f5abce04aa825d07044467b"};
                        if (!session_id[0].isEmpty()) {
//                            Intent intent = new Intent(LoginActivity.this, MainScreen.class);
                            Intent intent = new Intent(LoginActivity.this, MainScreen.class);
                            intent.putExtra("username", username);
                            intent.putExtra("token", loginJson.getString("token"));
                            intent.putExtra("session_id", session_id[0]);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Wrong username or password.", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}