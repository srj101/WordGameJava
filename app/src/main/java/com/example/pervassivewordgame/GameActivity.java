package com.example.pervassivewordgame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.ktx.Firebase;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView currentSelectionTextView;
    private TextView wordCountTextView;
    private int wordCount = 0;
    private String currentSelection = "Current Selection : ";


    private TextView countdownTimer;
    private GridLayout lettersGrid;
    private Button submitButton;
    private Button resetButton;

    private String selectedMode;
    private CountDownTimer timer;
    private int countdownDuration;

    FirebaseDatabase database;
    DatabaseReference scoresRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);





        // Retrieve the selected mode from the intent
        selectedMode = getIntent().getStringExtra("selectedMode");

        // Initialize views
        countdownTimer = findViewById(R.id.countdownTextView);
        currentSelectionTextView = findViewById(R.id.currentSelectionTextView);
        wordCountTextView = findViewById(R.id.wordCountTextView);
        lettersGrid = findViewById(R.id.lettersGrid);
        submitButton = findViewById(R.id.submitButton);
        resetButton = findViewById(R.id.resetButton);

        // Set countdown duration based on the selected mode
        setCountdownDuration();

        // Start the countdown timer
        startCountdown();

        // Set click listeners for letter buttons
        setLetterButtonClickListeners();

        // Set click listeners for submit and reset buttons
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = currentSelectionTextView.getText().toString().split(":")[1];

                DictionaryAPI dictionaryAPI = new DictionaryAPI();

                dictionaryAPI.isValidWord(word, isValid -> {

                    System.out.println("isValid ->"+isValid);
                    // Handle the result here
                    if (isValid) {
                        // Increment the word count and update the UI
                        wordCount = wordCount+5;
                        updateWordCount();


                        // Start a new round at the beginning
                        startNewRound();

                        // Reset the letter buttons
                        resetSelectedLetters();
                    } else {

                        wordCount = wordCount - 5;
                        updateWordCount();

                        // Start a new round at the beginning
                        startNewRound();

                        // Reset the letter buttons
                        resetSelectedLetters();

                        // Display an error message to the user
                        Toast.makeText(GameActivity.this, "Invalid word!", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });





        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new round at the beginning
                startNewRound();
            }
        });

        // Start a new round at the beginning
        startNewRound();


        int childCount = lettersGrid.getChildCount();

// Iterate over the child views and set click listeners for letter buttons
        for (int i = 0; i < childCount; i++) {
            final View childView = lettersGrid.getChildAt(i);
            if (childView instanceof Button) {
                final Button letterButton = (Button) childView;
                final String letter = letterButton.getText().toString();

                letterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Append the clicked letter to the current selection
                        String currentSelection = currentSelectionTextView.getText().toString();
                        currentSelection += letter;
                        currentSelectionTextView.setText(currentSelection);
                        // Change the button color to indicate selection (e.g., set background color to green)
                        letterButton.setBackgroundColor(Color.GREEN);
                    }
                });
            }
        }

    }







    private String selectRandomLetter() {
        // Define the English letters
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        // Generate a random index within the range of the letters array
        int randomIndex = new Random().nextInt(letters.length);

        // Return the randomly selected letter
        return letters[randomIndex];
    }

    private void startNewRound() {
        // Select the first letter randomly
        String firstLetter = selectRandomLetter();
        currentSelectionTextView.setText("Current Selection : " + firstLetter);

        // Reset the letter buttons
        resetSelectedLetters();
    }

    private void updateWordCount() {
        System.out.println("Word Count---> "+wordCount);
        wordCountTextView.setText("Word Count: "+wordCount);
    }

    private void resetSelectedLetters() {
        // Clear the selected letters
        // Change the appearance of all the letter buttons back to normal
        for (int i = 0; i < lettersGrid.getChildCount(); i++) {
            View child = lettersGrid.getChildAt(i);
            if (child instanceof Button) {
                Button letterButton = (Button) child;
                letterButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
        }
    }

    private void setCountdownDuration() {
        if (selectedMode.equals("Easy")) {
            countdownDuration = 30 * 1000; // 30 seconds
        } else if (selectedMode.equals("Difficult")) {
            countdownDuration = 20 * 1000; // 20 seconds
        } else if (selectedMode.equals("Hard")) {
            countdownDuration = 10 * 1000; // 10 seconds
        }
    }

    private void startCountdown() {
        timer = new CountDownTimer(countdownDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the countdown timer display
                long seconds = millisUntilFinished / 1000;
                countdownTimer.setText(String.format("Countdown: %02d:%02d", seconds / 60, seconds % 60));
            }

            @Override
            public void onFinish() {
                // Countdown finished, start the ScoreActivity
                Intent intent = new Intent(GameActivity.this, ScoreActivity.class);

                intent.putExtra("score", wordCount); // Pass the score to the ScoreActivity
                System.out.println("Score ---> "+wordCount);
                // Store score, name, and current time in Firebase
                String playerName = "SR Joy";
                Object currentTime = ServerValue.TIMESTAMP;

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference scoresRef = database.getReference("scores");


                PlayerScore playerScore = new PlayerScore(wordCount, playerName, currentTime.toString());
                scoresRef.push().setValue(playerScore);


                startActivity(intent);
                finish(); // Close the current activity
            }

        }.start();
    }

    private void setLetterButtonClickListeners() {

        // Define the English letters
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        // Calculate the number of rows
        int numRows = (int) Math.ceil((double) letters.length / 4);
        lettersGrid.setRowCount(numRows);

        // Set up letter buttons dynamically
        for (int i = 0; i < letters.length; i++) {
            Button letterButton = new Button(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(i % 4);
            params.rowSpec = GridLayout.spec(i / 4);
            letterButton.setLayoutParams(params);
            letterButton.setText(letters[i]);


            letterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle letter button click
                    Button clickedButton = (Button) v;
                    // Change the appearance of the clicked button (e.g., make it green)
                    clickedButton.setBackgroundColor(getResources().getColor(R.color.green));
                }
            });

            lettersGrid.addView(letterButton);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the countdown timer to avoid leaks
        if (timer != null) {
            timer.cancel();
        }
    }
}
