package com.android.setup;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TestSetupActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_setup1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button test = findViewById(R.id.testbutton1);
        test.setOnClickListener(v -> onStt());
    }
    protected void onStt() {
        Settings.Global.putInt(getContentResolver(), Settings.Global.DEVICE_PROVISIONED, 0);
        Settings.Secure.putInt(getContentResolver(), "user_setup_complete", 0);
        Settings.Secure.putInt(getContentResolver(), "setup_wizard_has_run", 0);
        //Start MainActivity
        Intent intent = new Intent(TestSetupActivity1.this,MainActivity.class);
        startActivity(intent);
    }
}