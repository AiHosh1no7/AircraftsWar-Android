package edu.hitsz.dao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Player implements Serializable {
    private int playerID;
    private String playerName;
    private int score;
    private LocalDateTime dateTime;

    public Player(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        dateTime = LocalDateTime.now();
    }

    public int getScore() {
        return score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }
}
