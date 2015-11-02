package draw_it.data.message;

import draw_it.data.user.User;

/**
 * Created by Marina on 07.06.2015.
 */
public class ScoreInfo {

    private User member;
    private int score;

    public ScoreInfo(User member, int score) {
        this.member = member;
        this.score = score;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
