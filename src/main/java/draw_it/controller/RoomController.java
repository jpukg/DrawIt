package draw_it.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import draw_it.data.game.Game;
import draw_it.data.game.Player;
import draw_it.data.message.*;
import draw_it.data.room.Room;
import draw_it.data.room.RoomRepository;
import draw_it.data.user.User;
import draw_it.utils.AtmosphereUtils;
import draw_it.utils.MessageUtils;
import draw_it.utils.SecurityUtils;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RoomController {

    @Autowired
    private AtmosphereUtils atmosphereUtils;
    @Autowired
    private MessageUtils messageUtils;
    @Autowired
    private RoomRepository roomRepository;

    @RequestMapping(value = "/room/{roomId}/atm", method = RequestMethod.POST)
    @ResponseBody
    public void roomPost(final AtmosphereResource resource,
                         @PathVariable Long roomId,
                         @RequestBody String message) throws Exception {

        Class clazz = messageUtils.getClassFromJsonMessage(message);

        if (clazz == ChatMessage.class) {
            chatPost(resource, roomId, (ChatMessage) messageUtils.readMessage(message, ChatMessage.class));
        } else if (clazz == DrawMessage.class) {
            drawingPost(resource, roomId, (DrawMessage) messageUtils.readMessage(message, DrawMessage.class));
        } else {
            throw new Exception("Error! Bad message type.");
        }

    }

    @RequestMapping(value = "/room/{roomId}/atm", method = RequestMethod.GET)
    @ResponseBody
    public void roomSubscribe(final AtmosphereResource resource, @PathVariable Long roomId)
            throws JsonGenerationException, JsonMappingException, IOException {
        atmosphereUtils.suspend("/room/" + roomId, resource);
    }

    // Works via Ajax:
    @RequestMapping(value = "/room/{roomId}/init")
    @ResponseBody
    public String roomInit(@PathVariable Long roomId, HttpServletResponse response) throws JsonProcessingException {
        Room room = roomRepository.findRoomById(roomId);
        if (room == null) {
            throw new NullPointerException("No such room exists.");
        }

        TimeMessage timeMessage;
        if (room.getGame() != null) {
            timeMessage = new TimeMessage(TimeMessage.TURN_CHANGED, room.getGame().getTurnChangedTime());
        } else {
            timeMessage = new TimeMessage(TimeMessage.BEFORE_GAME, room.getBeforeGameTime());
        }

        List<Member> memberList = room.getAllMembers().stream()
                .map((u) -> new Member(u)).collect(Collectors.toList());
        String jsonMessage = messageUtils.formJsonListMessage(new MemberListMessage(memberList),
                timeMessage);
        response.setContentType("text/JSON");
        return jsonMessage;
    }

    // Chat
    private void chatPost(final AtmosphereResource resource,
                          Long roomId,
                          ChatMessage chatMessage)
            throws IOException {

        User currentUser = SecurityUtils.getCurrentUser();
        String author = currentUser.getLogin();

        chatMessage.setFrom(author);
        chatMessage.setTime(new Date());

        if (roomRepository.findRoomById(roomId).getGame() != null) {
            Room room = roomRepository.findRoomById(roomId);
            Game game = room.getGame();
            if (chatMessage.getText().contains(game.getCurrentWord().toLowerCase())) {

                Player turnPlayer = room.getGame().getCurrentTurnPlayer();

                // TODO not to add points twice
                int points = 0;
                int points2 = 0;

                for (Player player : game.getPlayers()) {
                    if (player.getUser().equals(currentUser)) {
                        if (!player.isPointsGot()) {
                            if (!turnPlayer.isPointsGot()) {
                                points = player.getPoints() + 3;
                            } else {
                                points = player.getPoints() + 1;
                            }
                            player.setPoints(points);
                            player.setPointsGot(true);
                        }
                        break;
                    }
                }

                if (!room.getGame().getCurrentTurnPlayer().isPointsGot()) {
                    Player player = room.getGame().getCurrentTurnPlayer();
                    points2 = player.getPoints() + 2;
                    player.setPoints(points2);
                    room.getGame().getCurrentTurnPlayer().setPointsGot(true);
                }

                if (points !=0) {
                    if (points2 == 0) {
                        BroadcasterFactory factory = BroadcasterFactory.getDefault();
                        Broadcaster broadcaster = factory.lookup("/room/" + roomId);
                        String stringMessage =
                                messageUtils.formJsonListMessage(new ScoreMessage(new ScoreInfo(currentUser, points)));
                        broadcaster.broadcast(stringMessage);
                    } else {
                        BroadcasterFactory factory = BroadcasterFactory.getDefault();
                        Broadcaster broadcaster = factory.lookup("/room/" + roomId);
                        String stringMessage =
                                messageUtils.formJsonListMessage(new ScoreMessage(new ScoreInfo(currentUser, points)),
                                        new ScoreMessage(
                                                new ScoreInfo(room.getGame().getCurrentTurnPlayer().getUser(), points2)));
                        broadcaster.broadcast(stringMessage);
                    }
                }
                return;
            }
        }

        try {
            BroadcasterFactory factory = BroadcasterFactory.getDefault();
            Broadcaster broadcaster = factory.lookup("/room/" + roomId);
            String stringMessage =
                    messageUtils.formJsonListMessage(chatMessage);
            broadcaster.broadcast(stringMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Drawing
    private void drawingPost(final AtmosphereResource resource,
                             Long roomId,
                             DrawMessage drawMessage)
            throws IOException {

        // Check if this user is valid needed.

        BroadcasterFactory factory = BroadcasterFactory.getDefault();
        Broadcaster broadcaster = factory.lookup("/room/" + roomId, true);
        broadcaster.broadcast(
                messageUtils.formJsonListMessage(drawMessage));
    }
}
