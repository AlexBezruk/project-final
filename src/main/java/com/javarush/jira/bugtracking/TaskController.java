package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.taskchangestatuscode.TaskChangeStatusCodeEvent;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.User;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping(value = TaskController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {
    static final String REST_URL = "/api/bugtracking/task";

    private final TaskService taskService;
    private final UserBelongService userBelongService;

    private final ApplicationEventPublisher eventPublisher;

    @PatchMapping("/{id}/tags")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addTags(@PathVariable("id") Long taskId, @RequestBody String[] tags) {
        taskService.addTags(taskId, Set.of(tags));
    }

    @DeleteMapping("/{id}/tags")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTags(@PathVariable("id") Long taskId, @RequestBody String[] tags) {
        taskService.deleteTags(taskId, Set.of(tags));
    }

    @PostMapping("/{id}/subscriber")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addSubscriber(@PathVariable("id") Long taskId, @AuthenticationPrincipal AuthUser authUser) {
        userBelongService.addSubscriber(taskId, authUser.id());
    }

    @PatchMapping("/{id}/statusCode")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeStatus(@PathVariable("id") Long taskId, @RequestBody String statusCode) {
        taskService.changeStatus(taskId, statusCode);

        List<User> subscribers = userBelongService.getSubscribers(taskId);

        if (!subscribers.isEmpty()) {
            eventPublisher.publishEvent(new TaskChangeStatusCodeEvent(subscribers));
        }
    }
}
