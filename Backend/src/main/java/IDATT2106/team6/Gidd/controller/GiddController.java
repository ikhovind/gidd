package IDATT2106.team6.Gidd.controller;

import IDATT2106.team6.Gidd.models.*;
import IDATT2106.team6.Gidd.models.ActivityLevel;
import IDATT2106.team6.Gidd.models.Tag;
import IDATT2106.team6.Gidd.models.User;
import IDATT2106.team6.Gidd.models.Activity;
import IDATT2106.team6.Gidd.service.UserService;
import IDATT2106.team6.Gidd.service.ActivityService;

import java.util.HashMap;
import java.util.Map;

import IDATT2106.team6.Gidd.models.ActivityLevel;
import IDATT2106.team6.Gidd.service.EquipmentService;
import IDATT2106.team6.Gidd.service.UserService;
import java.net.URI;
import java.sql.Timestamp;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Controller
public class GiddController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public ResponseEntity home(){
        activityService.doNothing();
        return ResponseEntity
            .ok()
            .body("hi");
    }

    @PostMapping(value = "/testNewUser", consumes = "application/json", produces = "application/json")
    public ResponseEntity newUserTest(@RequestBody User object){
        userService.testNewUser(object);
        return ResponseEntity
            .created(URI.create("/user/id"))
            .body("hihi");
    }

    @PostMapping(value = "/testNewActivity", consumes = "application/json", produces = "application/json")
    public ResponseEntity newActivityTest(@RequestBody HashMap<String, Object> map){
        Timestamp newTime = Timestamp.valueOf(map.get("time").toString());

        User user = userService.getUser(Integer.parseInt(map.get("userId").toString()));
        int newId = getRandomID();

        //TODO Verify that user-input is valid
        activityService.addActivity(    newId,
            map.get("title").toString(), newTime, (int) map.get("repeat"), user,
            (int) map.get("capacity"), (int) map.get("groupId"), map.get("description").toString(), (byte[])map.get("image"),
            ActivityLevel.valueOf(map.get("activityLevel").toString()), (List<Tag>)map.get("tags"), (Double) map.get("latitude"), (Double) map.get("longitude"));
        return ResponseEntity
            .created(URI.create(String.format("/activity/%d", newId)))
            .body("Insert ResponseBody here");
    }

    @PostMapping(value = "/testAddNewActivityForUser", consumes = "application/json", produces = "application/json")
    public ResponseEntity addNewActivityForUser(@RequestBody HashMap<String, Object> map){
        Timestamp time = new Timestamp(new Date().getTime());

        User user = userService.getUser(Integer.parseInt(map.get("userId").toString()));
        Activity activity = activityService.findActivity(Integer.parseInt(map.get("activityId").toString()));

        HttpHeaders header = new HttpHeaders();

        if(user == null){
            header.add("Status", "400 BAD REQUEST");
            header.add("Content-Type", "application/json; charset=UTF-8");

            return ResponseEntity
                    .badRequest()
                    .headers(header)
                    .body("Something is wrong with the user");
        }

        if(activity == null){
            header.add("Status", "400 BAD REQUEST");
            header.add("Content-Type", "application/json; charset=UTF-8");

            return ResponseEntity
                    .badRequest()
                    .headers(header)
                    .body("Something is wrong with the activity");
        }

        //Legge inn sjekk om den allerede er registrert
        List<ActivityUser> activityUser = user.getActivities();
        ArrayList<Integer> activityIds = new ArrayList<>();

        for(ActivityUser as : activityUser){
            activityIds.add(as.getActivity().getActivityId());
        }

        if(activityIds.contains(activity.getActivityId())){
            header.add("Status", "400 BAD REQUEST");
            header.add("Content-Type", "application/json; charset=UTF-8");

            return ResponseEntity
                    .badRequest()
                    .headers(header)
                    .body("The user is already registered at the activity");
        }

        int id = getRandomID();

        //Kalle insert-metode helt til den blir true

        ArrayList<ActivityUser> activityUsers = new ArrayList<>();
        ArrayList<Activity> activities = activityService.getAllActivities();

        for(Activity a : activities){
            activityUsers.addAll(a.getRegisteredParticipants());
        }

        ArrayList<Integer> ids = new ArrayList<>();

        for(ActivityUser au : activityUsers){
            ids.add(au.getId());
        }

        while(ids.contains(id)){
            id = getRandomID();
        }


        if(userService.addUserToActivity(id, activity, user, time)){
            if(activityService.addUserToActivity(id, activity, user, time)){
                header.add("Status", "200 OK");
                header.add("Content-Type", "application/json; charset=UTF-8");

                return ResponseEntity
                        .ok()
                        .headers(header)
                        .body("Added to activity");
            }
            userService.removeActivity(id, user);
            header.add("Status", "400 BAD REQUEST");
            header.add("Content-Type", "application/json; charset=UTF-8");

            return ResponseEntity
                    .badRequest()
                    .headers(header)
                    .body("Something is wrong with the activity");
        }
        header.add("Status", "400 BAD REQUEST");
        header.add("Content-Type", "application/json; charset=UTF-8");

        return ResponseEntity
                .badRequest()
                .headers(header)
                .body("Something is wrong with the user");

    }

    @GetMapping(value = "/testGetAllActivitiesForUser", consumes = "application/json", produces = "application/json")
    public ResponseEntity getAllActivitiesForUser(@RequestBody HashMap<String, Object> map){
        User user = userService.getUser(Integer.parseInt(map.get("userId").toString()));

        HttpHeaders header = new HttpHeaders();

        if(user == null){
            header.add("Status", "400 BAD REQUEST");
            header.add("Content-Type", "application/json; charset=UTF-8");
            return ResponseEntity
                    .badRequest()
                    .headers(header)
                    .body("Something is wrong with the user");
        }

        List<ActivityUser> activityUser = user.getActivities();

        header.add("Status", "200 OK");
        header.add("Content-Type", "application/json; charset=UTF-8");

        return ResponseEntity
                .ok()
                .headers(header)
                .body(activityUser.toString());
    }

    @DeleteMapping(value = "/testDeleteActivitiesToUser/{userId}/{activityId}", produces = "application/json")
    public ResponseEntity deleteActivitiesToUser(@PathVariable Integer userId, @PathVariable Integer activityId){
        User user = userService.getUser(userId);
        Activity activity = activityService.findActivity(activityId);

        HttpHeaders header = new HttpHeaders();

        if(user == null){
            header.add("Status", "400 REQUEST");
            header.add("Content-Type", "application/json; charset=UTF-8");

            return ResponseEntity
                    .badRequest()
                    .headers(header)
                    .body("Something is wrong with the user");
        }

        if(activity == null){
            header.add("Status", "400 REQUEST");
            header.add("Content-Type", "application/json; charset=UTF-8");

            return ResponseEntity
                    .badRequest()
                    .headers(header)
                    .body("Something is wrong with the activity");
        }

        List<ActivityUser> activityUsers = user.getActivities();
        List<Integer> activitiesIds = new ArrayList<>();

        for(ActivityUser au : activityUsers){
            activitiesIds.add(au.getActivity().getActivityId());
        }

        if(!activitiesIds.contains(activityId)){
            header.add("Status", "400 REQUEST");
            header.add("Content-Type", "application/json; charset=UTF-8");

            return ResponseEntity
                    .badRequest()
                    .headers(header)
                    .body("The user is not registered to the activity");
        }

        int activityUserId = userService.getActivityUser(activity, user);

        ActivityUser activityUser = userService.getActivityUserById(activityUserId);
        if(activityUser == null){
            header.add("Status", "400 REQUEST");
            header.add("Content-Type", "application/json; charset=UTF-8");

            return ResponseEntity
                    .badRequest()
                    .headers(header)
                    .body("The user is not registered to the activity");
        }
        if(!userService.deleteConnection(activityUser) || !userService.removeActivity(activityUserId, user) || !activityService.removeUserFromActivity(activityUserId, activity)){
            header.add("Status", "400 REQUEST");
            header.add("Content-Type", "application/json; charset=UTF-8");

            return ResponseEntity
                    .badRequest()
                    .headers(header)
                    .body("Something wrong happened when trying to delete");
        }

        header.add("Status", "200 OK");
        header.add("Content-Type", "application/json; charset=UTF-8");
        return ResponseEntity
                .ok()
                .headers(header)
                .body("It work");
    }

    private int getRandomID(){
        Random rand = new Random();
        return rand.nextInt();
    }
    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody HashMap<String, Object> map){

	System.out.println("is map null " + (map == null));
	System.out.println(map.toString());
	System.out.println("email " + map.get("email"));
	System.out.println("password " + map.get("password"));
	System.out.println("first name " + map.get("firstName"));

	User result = userService.registerUser(
			map.get("email").toString(),
              map.get("password").toString(),
	      map.get("firstName").toString(),
	      map.get("surname").toString(),
              Integer.parseInt(map.get("phoneNumber").toString()),
              ActivityLevel.valueOf(map.get("activityLevel").toString()));
	//todo return result of registering new user

        Map<String, String> body = new HashMap<>();
        HttpHeaders header = new HttpHeaders();
        header.add("Status", "200 OK");

        header.add("Content-Type", "application/json; charset=UTF-8");

		if(result != null){
            body.put("id", String.valueOf(result.getUserId()));

	return ResponseEntity.ok()
            .headers(header)
            .body(formatJson(body));
		}
        body.put("id", "error");
		return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody HashMap<String, String> map){
		boolean result = userService.login(map.get("email").toString(), map.get("password").toString());
        Map<String, String> body = new HashMap<>();
        if(result){
            body.put("id", String.valueOf(userService.getUser(map.get("email")).getUserId()));
			return new ResponseEntity<>(body, HttpStatus.OK);
        }
        body.put("id", "invalid password");
		return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
    private String formatJson(Map values){
        String result = "{";
        Iterator it = values.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String goose = "";

            //todo very scuffed
            try {
                Integer.parseInt(pair.getValue().toString());
            } catch (Exception e) {
                goose = "\"";
            }

            System.out.println("goose: " + goose + " because " + pair.getValue() instanceof String);
            result += "\"" + pair.getKey() + "\":" + goose + pair.getValue() + goose + ",\n";
            it.remove(); // avoids a ConcurrentModificationException
        }
        //remove trailing comma
        return result.substring(0, result.length() - 2) + "}";
    }
}
