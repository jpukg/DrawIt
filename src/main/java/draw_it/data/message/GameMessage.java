package draw_it.data.message;

public class GameMessage extends Message {

    public static final String GAME_STARTED = "started";
    public static final String GAME_DELAYED = "delayed";
    public static final String GAME_INTERRUPTED = "interrupted";
    public static final String GAME_FINISHED = "finished";

    private String action;
    private String description;

    public GameMessage(String action) {
        this.action = action;
    }

    public GameMessage(String action, String description) {
        this.action = action;
        this.description = description;
    }

    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
