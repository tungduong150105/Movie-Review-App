package com.example.moviereviewapp.Activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moviereviewapp.R;

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
    }
}