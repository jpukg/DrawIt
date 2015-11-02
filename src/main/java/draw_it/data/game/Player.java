package draw_it.data.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import draw_it.data.user.User;

public class Player {

    private User user;

    private int points;

    private int kickAmount;

    private boolean pointsGot;

    public Player() {
    }

    public Player(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getKickAmount() {
        return kickAmount;
    }

    public void setKickAmount(int kickAmount) {
        this.kickAmount = kickAmount;
    }

    public boolean isPointsGot() {
        return pointsGot;
    }

    public void setPointsGot(boolean pointsGot) {
        this.pointsGot = pointsGot;
    }
}
