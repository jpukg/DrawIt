package draw_it.data.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import draw_it.data.game.Game;
import draw_it.data.game.Player;
import draw_it.data.user.User;
import draw_it.data.user.users_registration.UserProfileRepository;
import draw_it.data.words.WordService;
import draw_it.utils.AtmosphereUtils;
import draw_it.utils.MessageUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class Room {

    public static final int MIN_MEMBER_AMOUNT = 2;
    public static final int MAX_MEMBER_AMOUNT = 8;

    private long id;
    private String title;

    @JsonIgnore
    private Date beforeGameTime;

    @JsonIgnore
    private Game game;

    @JsonIgnore
    private CopyOnWriteArrayList<User> members = new CopyOnWriteArrayList<>();

    @JsonIgnore
    private RoomLifeCycle roomLifeCycle;

    public Room() {
    }

    public Room(long id, String title, Date beforeGameTime) {
        this.id = id;
        this.title = title;
    }

    public void startLifeCycle(AtmosphereUtils atmosphereUtils,
                               MessageUtils messageUtils,
                               WordService wordService,
                               RoomRepository roomRepository, UserProfileRepository userProfileRepository){
        if (roomLifeCycle!=null){
            throw new IllegalStateException("Room life cycle has already started.");
        }
        roomLifeCycle = new RoomLifeCycle(this, atmosphereUtils, messageUtils, wordService, roomRepository, userProfileRepository);
        roomLifeCycle.start();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getBeforeGameTime() {
        return beforeGameTime;
    }

    public void setBeforeGameTime(Date beforeGameTime) {
        this.beforeGameTime = beforeGameTime;
    }

    public Game getGame() {
        return game;
    }

    public synchronized boolean containsMember(User user) {
        return members.contains(user);
    }

    public synchronized void addMember(User user) {
        members.add(user);
    }

    public synchronized void removeMember(User user) {
        members.remove(user);
    }

    public synchronized List<User> getAllMembers() {
        return members;
    }

    public void startGame(){
        List<Player> players = members.stream()
                .map(m -> new Player(m)).collect(Collectors.toList());
        game = new Game(players);
    }

    public synchronized void stopGame(){
        game = null;
    }
}
