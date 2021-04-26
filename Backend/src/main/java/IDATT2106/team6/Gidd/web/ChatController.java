package IDATT2106.team6.Gidd.web;

import IDATT2106.team6.Gidd.models.Activity;
import IDATT2106.team6.Gidd.models.Chat;
import IDATT2106.team6.Gidd.models.FriendGroup;
import IDATT2106.team6.Gidd.models.User;
import IDATT2106.team6.Gidd.service.*;
import IDATT2106.team6.Gidd.util.Logger;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static IDATT2106.team6.Gidd.web.ControllerUtil.formatJson;
import static IDATT2106.team6.Gidd.web.ControllerUtil.getRandomID;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("")
public class ChatController {
    private static Logger log = new Logger(ChatController.class.toString());
    @Autowired
    private ChatService chatService;
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private FriendGroupService friendGroupService;

    @GetMapping("/chat/{groupId}")
    public ResponseEntity getMessageLog(@PathVariable Integer groupId){
        log.info("den kommer inn hit pog");
        ArrayList<Chat> messages = new ArrayList<>();
        HttpHeaders header = new HttpHeaders();
        Activity activity = activityService.getActivity(groupId);
        HashMap<String, String> body = new HashMap<>();
        if(activity != null) {
            List<Chat> messageList = chatService.getMessages(activity);
            if (messageList != null) {
                JSONObject jsonObject = new JSONObject();
                System.out.println(JSONArray.toJSONString(messageList));
                return ResponseEntity
                        .ok()
                        .headers(header)
                        .body(JSONArray.toJSONString(messageList));
            }
            return ResponseEntity
                    .badRequest()
                    .headers(header)
                    .body(formatJson(body));
        }
        body.put("error", "the activity does not exist");
        return ResponseEntity
                .badRequest()
                .headers(header)
                .body(formatJson(body));
    }


    @MessageMapping("/chat/{activityId}")
    public void sendMessage(@DestinationVariable Integer activityId, @Payload String message) throws ParseException {
        log.info("received message to group " + activityId);
        JSONParser parser = new JSONParser();
        System.out.println("message is: " + message);
        JSONObject chatJson = (JSONObject) parser.parse(message);
        Activity activity = activityService.getActivity(activityId);
        User user = userService.getUser(chatJson.getAsNumber("userId").intValue());

        Chat newChat = new Chat(activity, user, String.valueOf(chatJson.get("message")));
        //todo make thread safe and ensure that id does not exist
        newChat.setChatId(getRandomID());
        if(chatService.saveChat(newChat)){
            template.convertAndSend("/client/chat/" + activityId, message);
        }
        else {
            template.convertAndSend("{\"error\":\"could not save chat message\"");
        }
    }
}
