package com.example.pervassivewordgame;

public class PlayerScore implements Comparable<PlayerScore>  {
    private  int score;
    private  String name;
    private  String timeAgo;

    // Default constructor
    public PlayerScore() {
        // Default values
        this.score = 0;
        this.name = "";
        this.timeAgo = "";
    }


    public PlayerScore(int score, String name, String timeAgo) {
        this.score = score;
        this.name = name;
        this.timeAgo = timeAgo;
    }


    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    // compareTo() method implementation
    @Override
    public int compareTo(PlayerScore other) {
        // Compare scores in descending order
        return Double.compare(other.getScore(), this.getScore());
    }
}
