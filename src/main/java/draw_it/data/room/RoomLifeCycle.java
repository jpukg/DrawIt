package draw_it.data.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import draw_it.data.game.Player;
import draw_it.data.message.*;
import draw_it.data.user.AuthUser;
import draw_it.data.user.User;
import draw_it.data.user.users_registration.UserProfileRepository;
import draw_it.data.words.WordService;
import draw_it.utils.AtmosphereUtils;
import draw_it.utils.MessageUtils;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class RoomLifeCycle extends Thread {

    public static final int GAME_INTERVAL = 30 * 1000;
    public static final int TURN_INTERVAL = 30 * 1000;

    private static final int DELTA = (int) 1e6;

    private final Room room;

    private final AtmosphereUtils atmosphereUtils;
    private final MessageUtils messageUtils;
    private final WordService wordService;
    private final RoomRepository roomRepository;
    private final UserProfileRepository userProfileRepository;

    public RoomLifeCycle(Room room,
                         AtmosphereUtils atmosphereUtils,
                         MessageUtils messageUtils,
                         WordService wordService,
                         RoomRepository roomRepository, UserProfileRepository userProfileRepository) {
        this.room = room;

        this.atmosphereUtils = atmosphereUtils;
        this.messageUtils = messageUtils;
        this.wordService = wordService;
        this.roomRepository = roomRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public void run() {
        System.out.println("Life cycle in room " + room.getId() + " started.");

        try {
            // Before starting the game.
            room.setBeforeGameTime(new Date());
            while (true) {
                Thread.sleep(GAME_INTERVAL);
                if (room.getAllMembers().size() >= Room.MIN_MEMBER_AMOUNT) {
                    break;
                }
                room.setBeforeGameTime(new Date());
                String jsonMessage = messageUtils.formJsonListMessage(
                        new GameMessage(GameMessage.GAME_DELAYED),
                        new TimeMessage(TimeMessage.BEFORE_GAME, room.getBeforeGameTime()));
                atmosphereUtils.getBroadcasterFactory().lookup("/room/" + room.getId(), true)
                        .broadcast(jsonMessage);
            }

            // Starting and adding info about first player.
            room.startGame();
            removeRoomFromRoomList();
            room.getGame().nextTurn();
            room.getGame().setCurrentWord(wordService.pickOneWord());

            Player currentPlayer = room.getGame().getCurrentTurnPlayer();

            Message gameMessage = new GameMessage(GameMessage.GAME_STARTED);
            Message memberMessage = new MemberMessage(MemberMessage.MEMBER_GOT_TURN, currentPlayer.getUser());
            Message timeMessage = new TimeMessage(TimeMessage.TURN_CHANGED, room.getGame().getTurnChangedTime());
            Message wordMessage = new WordMessage(room.getGame().getCurrentWord());
            ;

            Broadcaster allPlayersBroadcaster = atmosphereUtils.getBroadcasterFactory()
                    .lookup("/room/" + room.getId());

            // For player with current turn.
            String jsonMessage = messageUtils.formJsonListMessage(
                    gameMessage, memberMessage, timeMessage, wordMessage);
            Broadcaster currentPlayerBroadcaster =
                    atmosphereUtils.getBroadcasterFactory().lookup("/room/" + room.getId() + "/currentPlayer", true);
            AtmosphereResource lastCurrentPlayerBroadcaster = null;
            for (AtmosphereResource r : currentPlayerBroadcaster.getAtmosphereResources()) {
                lastCurrentPlayerBroadcaster = r;
            }
            if (lastCurrentPlayerBroadcaster != null) {
                currentPlayerBroadcaster.removeAtmosphereResource(lastCurrentPlayerBroadcaster);
            }

            // Crutch
            Collection<AtmosphereResource> allPlayersResources = null;
            AtmosphereResource currentPlayerResource = null;
            int counter = DELTA;
            do {
                counter--;
                allPlayersResources = allPlayersBroadcaster.getAtmosphereResources();
                for (AtmosphereResource r : allPlayersResources) {
                    String userLogin = r.getRequest().getSession().getAttribute("userLogin").toString();
                    if (currentPlayer.getUser().getLogin().equals(userLogin)) {
                        currentPlayerResource = r;
                        break;
                    }
                }
            } while (currentPlayerResource == null && counter > 0);

            currentPlayerBroadcaster.addAtmosphereResource(currentPlayerResource);
            Future<Object> result = currentPlayerBroadcaster
                    .broadcast(jsonMessage);
            result.get();

            // For other players.
            jsonMessage = messageUtils.formJsonListMessage(
                    gameMessage, memberMessage, timeMessage);
            result = allPlayersBroadcaster.broadcast(jsonMessage);
            result.get();

            Thread.sleep(TURN_INTERVAL);

        // Actually the game.
            while (room.getGame().hasNextTurn()) {
                room.getGame().nextTurn();
                room.getGame().setCurrentWord(wordService.pickOneWord());

                currentPlayer = room.getGame().getCurrentTurnPlayer();

                memberMessage = new MemberMessage(MemberMessage.MEMBER_GOT_TURN, currentPlayer.getUser());
                timeMessage = new TimeMessage(TimeMessage.TURN_CHANGED, room.getGame().getTurnChangedTime());
                wordMessage = new WordMessage(room.getGame().getCurrentWord());

                allPlayersBroadcaster = atmosphereUtils.getBroadcasterFactory()
                        .lookup("/room/" + room.getId());

                // For player with current turn.
                jsonMessage = messageUtils.formJsonListMessage(memberMessage, timeMessage, wordMessage);
                currentPlayerBroadcaster =
                        atmosphereUtils.getBroadcasterFactory().lookup("/room/" + room.getId() + "/currentPlayer", true);
                lastCurrentPlayerBroadcaster = null;
                for (AtmosphereResource r : currentPlayerBroadcaster.getAtmosphereResources()) {
                    lastCurrentPlayerBroadcaster = r;
                }
                if (lastCurrentPlayerBroadcaster != null) {
                    currentPlayerBroadcaster.removeAtmosphereResource(lastCurrentPlayerBroadcaster);
                }

                // Crutch
                allPlayersResources = null;
                currentPlayerResource = null;
                counter = DELTA;
                do {
                    counter--;
                    allPlayersResources = allPlayersBroadcaster.getAtmosphereResources();
                    for (AtmosphereResource r : allPlayersResources) {
                        String userLogin = r.getRequest().getSession().getAttribute("userLogin").toString();
                        if (currentPlayer.getUser().getLogin().equals(userLogin)) {
                            currentPlayerResource = r;
                            break;
                        }
                    }
                } while (currentPlayerResource == null && counter > 0);

                currentPlayerBroadcaster.addAtmosphereResource(currentPlayerResource);
                result = currentPlayerBroadcaster
                        .broadcast(jsonMessage);
                result.get();

                // For other players.
                jsonMessage = messageUtils.formJsonListMessage(
                        memberMessage, timeMessage);
                result = allPlayersBroadcaster.broadcast(jsonMessage);
                result.get();
                Thread.sleep(TURN_INTERVAL);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Game in room " + room.getId() + " failed.");
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            System.out.println("Game in room " + room.getId() + " failed.");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Game in room " + room.getId() + " failed.");
            e.printStackTrace();
        }

        // After the game.
        try {
            saveAuthorizedUsersScore(room.getGame().getPlayers());
            List<ScoreInfo> scoreInfoList = room.getGame().getPlayers().stream()
                    .map((p) -> new ScoreInfo(p.getUser(), p.getPoints())).collect(Collectors.toList());
            room.stopGame();

            String jsonMessage = messageUtils.formJsonListMessage(
                    new GameMessage(GameMessage.GAME_FINISHED),
                    new FinalScoreMessage(scoreInfoList));
            Future<Object> result = atmosphereUtils.getBroadcasterFactory().lookup("/room/" + room.getId())
                    .broadcast(jsonMessage);
            result.get();
            removeRoomFromRepository();
        } catch (JsonProcessingException e) {
            System.out.println("Game in room " + room.getId() + " failed to finish.");
            e.printStackTrace();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Game in room " + room.getId() + " failed to finish.");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Game in room " + room.getId() + " failed to finish.");
            e.printStackTrace();
        }

        System.out.println("Life cycle in room " + room.getId() + " finished.");
    }

    private void saveAuthorizedUsersScore(List<Player> players) {
        for (Player player : players){
            User user = player.getUser();
            if (user.getRole().equals(AuthUser.ROLE_AUTH)){
              /*  UserProfile profile = user.getProfile();
                profile.setGameAmount(profile.getGameAmount()+1);
                profile.setPointAmount(profile.getPointAmount() + player.getPoints());
                userProfileRepository.save(profile);*/
            }
        }
    }

    private void removeRoomFromRoomList() throws JsonProcessingException {
        String jsonMessage = messageUtils.formJsonListMessage(new RoomMessage(RoomMessage.REMOVED_ACTION, room));

        Broadcaster broadcaster = atmosphereUtils.getBroadcasterFactory().lookup("/roomlist");
        broadcaster.broadcast(jsonMessage);
    }

    private void removeRoomFromRepository() {
        roomRepository.removeRoom(room);
    }
}
