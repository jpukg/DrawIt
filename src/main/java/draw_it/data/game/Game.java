package draw_it.data.game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {

    @JsonIgnore
    private CopyOnWriteArrayList<Player> players;

    @JsonIgnore
    private Date turnChangedTime;

    @JsonIgnore
    private int currentTurnIndex = -1;

    @JsonIgnore
    private int turnsLeft;

    private Player currentTurnPlayer;

    @JsonIgnore
    private String currentWord;

    public Game(Collection<Player> players) {
        this.players = new CopyOnWriteArrayList<>(players);
        turnsLeft = players.size();

        // Is suitable for a more complex game.
//        if (players.size() <= 5) {
//            turnsLeft = 2 * players.size();
//        } else {
//            turnsLeft = players.size();
//        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public synchronized Date getTurnChangedTime() {
        return turnChangedTime;
    }

    public synchronized int getTurnsLeft() {
        return turnsLeft;
    }

    public synchronized void setTurnsLeft(int turnsLeft) {
        this.turnsLeft = turnsLeft;
    }

    public synchronized Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public synchronized void setCurrentTurnPlayer(Player currentTurnPlayer) {
        this.currentTurnPlayer = currentTurnPlayer;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
        for (Player player : players){
            player.setPointsGot(false);
        }
    }

    public synchronized boolean hasNextTurn() {
        return turnsLeft > 0;
    }

    public synchronized void nextTurn() {
        if (turnsLeft > 0) {
            currentTurnIndex = (currentTurnIndex + 1) % players.size();
            currentTurnPlayer = players.get(currentTurnIndex);
            turnsLeft--;
            turnChangedTime = new Date();
        } else {
            throw new IllegalStateException("The game is over.");
        }
    }
}
