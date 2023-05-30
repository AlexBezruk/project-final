package com.javarush.jira.bugtracking.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = TaskController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {
    static final String REST_URL = "/api/bugtracking/task";

    @Autowired
    private TaskService taskService;

    @PostMapping("/{id}")
    public String addTags(@PathVariable("id") Long taskId, @RequestBody String[] tags) {
        taskService.addTags(taskId, Set.of(tags));
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String deleteTags(@PathVariable("id") Long taskId, @RequestBody String[] tags) {
        taskService.deleteTags(taskId, Set.of(tags));
        return "redirect:/";
    }
}
