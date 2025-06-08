package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

public class SignupActivity extends AppCompatActivity {
    EditText editTextFirstAndLastName_SignUp, editTextEmail_SignUp, editTextPassword_SignUp, editTextRe_enterPassword_SignUp;
    CheckBox checkBoxShowPass_SignUp;
    androidx.appcompat.widget.AppCompatButton btnCreateAccount_SignUp, btnSignIn_SignUp;
    UserAPI userAPI;
    TMDBAPI tmdbAPI;
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

        btnCreateAccount_SignUp.setOnClickListener(v -> {
            try {
                onClick_CreateAccount_SignUp(v);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        });

        userAPI = new UserAPI();
        tmdbAPI = new TMDBAPI();
    }
    public void onClick_CreateAccount_SignUp(View view) throws IOException, JSONException {
        String username = editTextFirstAndLastName_SignUp.getText().toString();
        String email = editTextEmail_SignUp.getText().toString();
        String password = editTextPassword_SignUp.getText().toString();
        String passwordConfirm = editTextRe_enterPassword_SignUp.getText().toString();

        if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("email", email);
        body.put("password", password);

        userAPI.call_api(userAPI.get_UserAPI() + "/user/signup", body.toString(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("SignupActivity", "Failed to signup", e);
                runOnUiThread(() -> Toast.makeText(SignupActivity.this, "Failed to signup, please signup again", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 201) {
                    final String[] session_id = {""};
                    tmdbAPI.get_api(tmdbAPI.get_url_new_session(), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Log.e("LoginActivity", "Failed to login", e);
                            runOnUiThread(() -> Toast.makeText(SignupActivity.this, "Have problem when login, try again", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) {
                            Log.d("LoginActivity", response.code() + "");
                            if (response.code() == 200) {
                                try {
                                    assert response.body() != null;
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    session_id[0] = jsonObject.getString("guest_session_id");
                                    Log.d("LoginActivity", session_id[0] + "ff");
                                    if (!session_id[0].isEmpty()) {
                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        intent.putExtra("session_id", session_id[0]);
                                        startActivity(intent);
                                    }
                                } catch (JSONException | IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(SignupActivity.this, "Failed to signup, please signup again", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    public void onClick_SignIn_SignUp(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}