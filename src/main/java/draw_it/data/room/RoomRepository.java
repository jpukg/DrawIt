package draw_it.data.room;

import org.springframework.stereotype.Repository;

import java.util.concurrent.CopyOnWriteArrayList;

@Repository("roomRepository")
public class RoomRepository {

    public CopyOnWriteArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(CopyOnWriteArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    private CopyOnWriteArrayList<Room> rooms = new CopyOnWriteArrayList<>();

    public synchronized void addRoom(Room room) {
        if (room.getId() == 0) {
            if (rooms.size() > 0) {
                room.setId(rooms.get(rooms.size() - 1).getId() + 1);
            } else {
                room.setId(1);
            }
        }
        if (room.getTitle() == null) {
            room.setTitle("Room_" + room.getId());
        }
        rooms.add(room);
    }

    public synchronized Room findRoomById(long roomId) {
        for (Room r : rooms) {
            if (r.getId() == roomId) {
                return r;
            }
        }
        return null;
    }

    public synchronized void removeRoom(Room room) {
        rooms.remove(room);
    }

    public synchronized int getRoomAmount() {
        return rooms.size();
    }
}
