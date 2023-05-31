package com.javarush.jira.bugtracking.userBelong;

import com.javarush.jira.login.AuthUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = com.javarush.jira.bugtracking.userBelong.UserBelongController.REST_URL,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UserBelongController {
    static final String REST_URL = "/api/bugtracking/userBelong";

    private final UserBelongService userBelongService;

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addSubscriber(@PathVariable("id") Long taskId, @AuthenticationPrincipal AuthUser authUser) {
        userBelongService.addSubscriber(taskId, authUser.id());
    }
}
