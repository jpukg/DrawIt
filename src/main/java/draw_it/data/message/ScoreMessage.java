package draw_it.data.message;

import draw_it.data.user.User;

public class ScoreMessage extends Message {

    private ScoreInfo scoreInfo;
    private String description;

    public ScoreMessage(ScoreInfo scoreInfo) {
        this.scoreInfo = scoreInfo;
    }

    public ScoreInfo getScoreInfo() {
        return scoreInfo;
    }

    public void setScoreInfo(ScoreInfo scoreInfo) {
        this.scoreInfo = scoreInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
