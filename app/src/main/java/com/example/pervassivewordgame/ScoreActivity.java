package com.example.pervassivewordgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {
    private TextView scoreTextView;
    private Button playAgainButton;
    private Button scoreboardButton; // Added scoreboard button reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Initialize views
        scoreTextView = findViewById(R.id.scoreTextView);
        playAgainButton = findViewById(R.id.playAgainButton);
        scoreboardButton = findViewById(R.id.scoreboardButton); // Initialize scoreboard button

        // Retrieve the score from the intent
        int score = getIntent().getIntExtra("score", 0);

        // Display the score
        scoreTextView.setText("Score: " + score);

        // Set click listener for play again button
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle play again button click
                // Redirect the user back to the initial screen to select the mode and play again
                startActivity(new Intent(ScoreActivity.this, MainActivity.class));
                finish(); // Finish the current activity
            }
        });

        // Set click listener for scoreboard button
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle scoreboard button click
                // Navigate to the ScoreboardActivity
                startActivity(new Intent(ScoreActivity.this, ScoreBoard.class));
            }
        });
    }
}
