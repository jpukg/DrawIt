package draw_it.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import draw_it.data.message.MemberMessage;
import draw_it.data.message.RoomListMessage;
import draw_it.data.message.RoomMessage;
import draw_it.data.room.Room;
import draw_it.data.room.RoomLifeCycle;
import draw_it.data.room.RoomRepository;
import draw_it.data.user.User;
import draw_it.data.user.users_registration.UserProfileRepository;
import draw_it.data.words.WordService;
import draw_it.utils.AtmosphereUtils;
import draw_it.utils.MessageUtils;
import draw_it.utils.SecurityUtils;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    @Autowired
    private AtmosphereUtils atmosphereUtils;
    @Autowired
    private MessageUtils messageUtils;
    @Autowired
    private RoomRepository roomRepository;

    // For room lifecycle.
    @Autowired
    private WordService wordService;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @RequestMapping(value = {"/", "main"}, method = RequestMethod.GET)
    public String main(Model model) {
        return "main";
    }

    @RequestMapping(value = "/roomlist/atm", method = RequestMethod.GET)
    @ResponseBody
    public void roomListSubscribe(final AtmosphereResource resource) {
        atmosphereUtils.suspend("/roomlist", resource);
    }

    // Works via Ajax:
    @RequestMapping(value = "/roomlist/all")
    @ResponseBody
    public String getRoomList(HttpServletResponse response) throws JsonProcessingException {
        response.setContentType("text/JSON");

        String jsonMessage = messageUtils.formJsonListMessage(new RoomListMessage(roomRepository.getRooms()));
        return jsonMessage;
    }

	    @RequestMapping(value = "room/leave/{roomId}", method = RequestMethod.GET)
    public String leaveRoom(final AtmosphereResource resource,
                           @PathVariable Long roomId) throws JsonProcessingException {
        Room room = roomRepository.findRoomById(roomId);
        if (room != null) {

            User currentUser = SecurityUtils.getCurrentUser();
            if (room.containsMember(currentUser)) {
                room.removeMember(new User(currentUser));

                String jsonMessage = messageUtils.formJsonListMessage
                        (new MemberMessage(MemberMessage.MEMBER_REMOVED, currentUser));
                atmosphereUtils.getBroadcasterFactory().lookup("/room/" + roomId, true).broadcast(jsonMessage);
            }
        }

        return "redirect:/main";
    }
	
    @RequestMapping(value = "room/join/{roomId}", method = RequestMethod.GET)
    public String joinRoom(final AtmosphereResource resource,
                           @PathVariable Long roomId) throws JsonProcessingException {
        Room room = roomRepository.findRoomById(roomId);
        if (room == null) {
            throw new NullPointerException("No such room exists.");
        }

        User currentUser = SecurityUtils.getCurrentUser();
        if (!room.containsMember(currentUser)) {
            room.addMember(new User(currentUser));

            String jsonMessage = messageUtils.formJsonListMessage
                    (new MemberMessage(MemberMessage.MEMBER_ADDED, currentUser));
            atmosphereUtils.getBroadcasterFactory().lookup("/room/" + roomId, true).broadcast(jsonMessage);
        }

        return "redirect:/room/" + roomId;
    }

    @RequestMapping(value = "room/{roomId}", method = RequestMethod.GET)
    public ModelAndView openRoom(final AtmosphereResource resource,
                                 @PathVariable Long roomId) {

        // TODO:check if user belongs to this room

        Room room = roomRepository.findRoomById(roomId);
        if (room == null) {
            throw new NullPointerException("No such room exists.");
        }

        ModelAndView model = new ModelAndView();
        model.setViewName("room");
        model.addObject("roomId", room.getId());
        model.addObject("roomTitle", room.getTitle());
        model.addObject("gameInterval", RoomLifeCycle.GAME_INTERVAL);
        model.addObject("turnInterval", RoomLifeCycle.TURN_INTERVAL);

        return model;
    }
	
	@RequestMapping(value = "/room/add", method = RequestMethod.GET)
    public String addRoom(final AtmosphereResource resource) throws JsonProcessingException {
        Room room = new Room();
        roomRepository.addRoom(room);

        String jsonMessage = messageUtils.formJsonListMessage(new RoomMessage(RoomMessage.ADDED_ACTION, room));

        Broadcaster broadcaster = atmosphereUtils.getBroadcasterFactory().lookup("/roomlist");
        broadcaster.broadcast(jsonMessage);

        // Starting the timer and so on.
        room.startLifeCycle(atmosphereUtils, messageUtils, wordService, roomRepository, userProfileRepository);

        return "redirect:/room/join/" + room.getId();
    }
}
