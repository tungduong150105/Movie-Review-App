package com.example.moviereviewapp.Activities;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moviereviewapp.R;

public class Password_Assistance_Activity extends AppCompatActivity {
    EditText editTextEmail_PhoneNumber_PasswordAssistance;
    androidx.appcompat.widget.AppCompatButton btn_Continue_PasswordAssistance;
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
        //ToDo: Qua trinh thay doi Password tu Password_Assistance_Activity -> SecurityCode_Entering_Activity -> Changing_Password_Activity
    }
}