package draw_it.data.message;

import java.util.List;

public class FinalScoreMessage extends Message {

    private List<ScoreInfo> scoreInfos;
    private String description;

    public FinalScoreMessage(List<ScoreInfo> scoreInfos) {
        this.scoreInfos = scoreInfos;
    }

    public List<ScoreInfo> getScoreInfos() {
        return scoreInfos;
    }

    public void setScoreInfos(List<ScoreInfo> scoreInfos) {
        this.scoreInfos = scoreInfos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
