package draw_it.data.message;

import draw_it.data.room.Room;

import java.util.List;

public class RoomListMessage extends Message {

    private List<Room> rooms;

    public RoomListMessage() {
    }

    public RoomListMessage(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
