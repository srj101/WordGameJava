package com.example.pervassivewordgame;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreBoard extends AppCompatActivity {
    private TableLayout scoreboardTable;
    private DatabaseReference scoresRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        // Initialize views
        scoreboardTable = findViewById(R.id.scoreboardTableLayout);

        // Get a reference to the "scores" node in Firebase
        scoresRef = FirebaseDatabase.getInstance().getReference("scores");

        // Retrieve the scoreboard data from Firebase
        retrieveScoreboardData();
    }

    private void retrieveScoreboardData() {
        // Query the scores in descending order by score
        Query query = scoresRef.orderByChild("score").limitToLast(10);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PlayerScore> playerScores = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve the PlayerScore object from the snapshot
                    PlayerScore playerScore = snapshot.getValue(PlayerScore.class);

                    if (playerScore != null) {
                        playerScores.add(playerScore);
                    }
                }

                // Sort the playerScores list in descending order by score
                Collections.sort(playerScores, Collections.reverseOrder());

                // Display the playerScores in the scoreboard table
                displayScoreboard(playerScores);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

    private void displayScoreboard(List<PlayerScore> playerScores) {
        // Clear the scoreboard table
        scoreboardTable.removeAllViews();

        // Add the table headers
        TableRow headerRow = new TableRow(this);
        TextView rankHeader = createHeaderTextView("Rank");
        TextView nameHeader = createHeaderTextView("Name");
        TextView scoreHeader = createHeaderTextView("Score");
        headerRow.addView(rankHeader);
        headerRow.addView(nameHeader);
        headerRow.addView(scoreHeader);
        scoreboardTable.addView(headerRow);

        // Add the player scores to the scoreboard table
        for (int i = 0; i < playerScores.size(); i++) {
            PlayerScore playerScore = playerScores.get(i);

            TableRow row = new TableRow(this);
            TextView rankTextView = createTextView(String.valueOf(i + 1));
            TextView nameTextView = createTextView(playerScore.getName());
            TextView scoreTextView = createTextView(String.valueOf(playerScore.getScore()));


            row.addView(rankTextView);
            row.addView(nameTextView);
            row.addView(scoreTextView);


            scoreboardTable.addView(row);
        }
    }


    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setBackgroundResource(R.drawable.cell_header_background);
        textView.setTextColor(getResources().getColor(R.color.white));
        return textView;
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setBackgroundResource(R.drawable.cell_background);
        return textView;
    }
}
