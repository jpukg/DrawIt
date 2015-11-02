package draw_it.data.message;

import draw_it.data.user.User;

import java.util.Date;

public class TimeMessage extends Message {
    public final static String BEFORE_GAME = "before_game";
    public final static String TURN_CHANGED = "turn_changed";

    private String timeDescription;
    private Date time;

    public TimeMessage(String timeDescription, Date time) {
        this.timeDescription = timeDescription;
        this.time = time;
    }

    public String getTimeDescription() {
        return timeDescription;
    }

    public Date getTime() {
        return time;
    }
}
