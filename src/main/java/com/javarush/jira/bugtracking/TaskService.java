package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.model.Activity;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.ActivityRepository;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.to.TaskTo;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {
    private final ActivityRepository activityRepository;

    public TaskService(TaskRepository taskRepository, TaskMapper mapper, ActivityRepository activityRepository) {
        super(taskRepository, mapper);
        this.activityRepository = activityRepository;
    }

    public List<TaskTo> getAll() {
        return mapper.toToList(repository.getAll());
    }

    public void addTags(Long id, Set<String> tags) {
        Task task = repository.getExisted(id);
        task.getTags().addAll(tags);
        repository.save(task);
    }

    public void deleteTags(Long id, Set<String> tags) {
        Task task = repository.getExisted(id);
        task.getTags().removeAll(tags);
        repository.save(task);
    }

    public void changeStatus(Long id, String statusCode) {
        Task task = repository.getExisted(id);
        task.setStatusCode(statusCode);
        repository.save(task);
    }

    public Duration taskWasInProgress(Long taskId) {
        Optional<Activity> inProgress = activityRepository.findByIdAndStatusCode(taskId, "in progress");
        Optional<Activity> ready = activityRepository.findByIdAndStatusCode(taskId, "ready");
        if (inProgress.isPresent() && ready.isPresent()) {
            LocalDateTime localDateTime1 = inProgress.get().getUpdated();
            LocalDateTime localDateTime2 = ready.get().getUpdated();
            return Duration.between(localDateTime1, localDateTime2);
        }
        return null;
    }

    public Duration taskWasInTesting(Long taskId) {
        Optional<Activity> ready = activityRepository.findByIdAndStatusCode(taskId, "ready");
        Optional<Activity> testing = activityRepository.findByIdAndStatusCode(taskId, "testing");
        if (ready.isPresent() && testing.isPresent()) {
            LocalDateTime localDateTime1 = ready.get().getUpdated();
            LocalDateTime localDateTime2 = testing.get().getUpdated();
            return Duration.between(localDateTime1, localDateTime2);
        }
        return null;
    }
}
