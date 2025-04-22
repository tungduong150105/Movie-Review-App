package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class SecurityCode_Entering_Activity extends AppCompatActivity {
    EditText editText_EnterSecurityCode_Verification;
    androidx.appcompat.widget.AppCompatButton btn_Continue_Verification;
    TextView textView_ResendCode_Verification;
    //Todo: Kich hoat su kien onClick cho textView_ResendCode_Verification
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_security_code_entering);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editText_EnterSecurityCode_Verification = findViewById(R.id.editText_EnterSecurityCode_Verification);
        btn_Continue_Verification = findViewById(R.id.btn_Continue_Verification);
        textView_ResendCode_Verification = findViewById(R.id.textView_ResendCode_Verification);

        textView_ResendCode_Verification.setOnClickListener(v -> {
            //TODO: xử lý sự kiện click vào textView_ResendCode_Verification để gửi lại mã xác nhận
        });
    }
    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    void check_reset_token(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public void onClick_Continue_Verification(View view) throws JSONException {
        String securityCode = editText_EnterSecurityCode_Verification.getText().toString();
        //TODO: xử lý sự kiện nhập và xác nhận mã bảo mật

        JSONObject body = new JSONObject();
        body.put("token", securityCode);

        check_reset_token("https://movie-review-app-be.onrender.com/user/check_reset_token", body.toString(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(SecurityCode_Entering_Activity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    Intent intent = new Intent(SecurityCode_Entering_Activity.this, Changing_Password_Activity.class);
                    intent.putExtra("token", securityCode);
                    startActivity(intent);
                } else {
                    runOnUiThread(() -> Toast.makeText(SecurityCode_Entering_Activity.this, "Wrong token, please try again", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

}