package edu.hitsz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OnlineCheckActivity extends AppCompatActivity {
    private TextView titleText;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_check);
        Intent prevIntent = getIntent();
        boolean victoryFlag = prevIntent.getBooleanExtra("victoryFlag", false);
        titleText = findViewById(R.id.resultText);
        titleText.setText(victoryFlag ? "VICTORY" : "FAILED");
        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMenu = new Intent(OnlineCheckActivity.this, LauncherActivity.class);
                startActivity(backToMenu);
            }
        });
    }
}