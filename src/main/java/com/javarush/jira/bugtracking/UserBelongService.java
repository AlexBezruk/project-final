package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class UserBelongService {
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final UserBelongRepository userBelongRepository;

    public void addSubscriber(Long taskId, long userId) {
        Task task = taskRepository.getExisted(taskId);
        User user = userRepository.getExisted(userId);

        UserBelong userBelongTask = new UserBelong();
        userBelongTask.setObjectId(task.getId());
        userBelongTask.setObjectType(ObjectType.TASK);
        userBelongTask.setUserId(user.getId());
        userBelongTask.setUserTypeCode("subscriber");

        userBelongRepository.save(userBelongTask);
    }

    public List<User> getSubscribers(Long taskId) {
        List<UserBelong> subscribersUserBelong = userBelongRepository
                .getUserBelongsByObjectIdAndUserTypeCodeAndObjectType(taskId, "subscriber", ObjectType.TASK);

        return subscribersUserBelong.stream()
                .map(UserBelong::getUserId)
                .map(userRepository::getExisted)
                .collect(Collectors.toList());
    }
}
