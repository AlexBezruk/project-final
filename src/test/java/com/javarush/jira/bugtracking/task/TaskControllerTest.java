package com.javarush.jira.bugtracking.task;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Set;

import static com.javarush.jira.bugtracking.task.TaskController.REST_URL;
import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerTest extends AbstractControllerTest {
    public static final int TASK_ID = 2;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    TaskMapper mapper;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void addTags() throws Exception {
        Set<String> tags = Set.of("frontend", "backend");

        perform(MockMvcRequestBuilders.post(REST_URL + "/" + TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(tags)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(tags, taskRepository.getExisted(TASK_ID).getTags());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteTags() throws Exception {
        Set<String> tags = Set.of("frontend", "backend", "none");
        Set<String> deleteTags = Set.of("frontend", "backend");

        perform(MockMvcRequestBuilders.post(REST_URL + "/" + TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(tags)))
                .andDo(print())
                .andExpect(status().isNoContent());

        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(deleteTags)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(Set.of("none"), taskRepository.getExisted(TASK_ID).getTags());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteWrongTags() throws Exception {
        Set<String> tags = Set.of("frontend", "backend", "none");
        Set<String> deleteTags = Set.of("abracadabra");

        perform(MockMvcRequestBuilders.post(REST_URL + "/" + TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(tags)))
                .andDo(print())
                .andExpect(status().isNoContent());

        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(deleteTags))).andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(tags, taskRepository.getExisted(TASK_ID).getTags());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteFromEmptyTags() throws Exception {
        Set<String> deleteTags = Set.of("frontend", "backend", "none");

        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(deleteTags)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(Collections.emptySet(), taskRepository.getExisted(TASK_ID).getTags());
    }
}
