package draw_it.data.message;

import draw_it.data.room.Room;

public class RoomMessage extends Message {
    public final static String ADDED_ACTION = "added";
    public final static String REMOVED_ACTION = "removed";

    private String action;
    private Room room;

    public RoomMessage(String action, Room room) {
        this.action = action;
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public String getAction() {
        return action;
    }
}
