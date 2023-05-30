package com.example.pervassivewordgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Spinner modeSpinner;
    private Button playNowButton;
    private Button scoreboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        modeSpinner = findViewById(R.id.modeSpinner);
        playNowButton = findViewById(R.id.playNowButton);
        scoreboardButton = findViewById(R.id.scoreboardButton); // Initialize scoreboard button


        // Set default selection for mode spinner (e.g., Easy)
        modeSpinner.setSelection(0);

        // Handle Play Now button click
        playNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Transition to the main screen
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("selectedMode", modeSpinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        // Set click listener for scoreboard button
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle scoreboard button click
                // Navigate to the ScoreboardActivity
                startActivity(new Intent(MainActivity.this, ScoreBoard.class));
            }
        });
    }
}
