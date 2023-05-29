package com.javarush.jira.profile.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.Profile;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.profile.web.ProfileRestController.REST_URL;
import static com.javarush.jira.profile.web.ProfileTestData.*;
import static com.javarush.jira.profile.web.ProfileTestData.getUpdated;
import static com.javarush.jira.profile.web.ProfileTestData.ADMIN_MAIL;
import static com.javarush.jira.profile.web.ProfileTestData.USER_MAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    ProfileMapper mapper;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getUser() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(userProfile));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAdmin() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(adminProfile));
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        ProfileTo updatedTo = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Profile dbProfileAfter = profileRepository.getExisted(USER_ID);
        ProfileTo profileToAfter = mapper.toTo(dbProfileAfter);
        assertEquals(updatedTo.getContacts(), profileToAfter.getContacts());
        assertEquals(updatedTo.getMailNotifications(), profileToAfter.getMailNotifications());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateWithWrongMailNotification() throws Exception {
        ProfileTo updatedTo = getUpdatedWithWrongMailNotification();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateWithWrongContact() throws Exception {
        ProfileTo updatedTo = getUpdatedWithWrongContact();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateWithMailNotificationAndContactIsNull() throws Exception {
        ProfileTo updatedTo = getUpdatedWithMailNotificationAndContactIsNull();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Profile dbProfileAfter = profileRepository.getExisted(USER_ID);
        ProfileTo profileToAfter = mapper.toTo(dbProfileAfter);
        assertEquals(updatedTo.getContacts(), profileToAfter.getContacts());
        assertEquals(updatedTo.getMailNotifications(), profileToAfter.getMailNotifications());
    }
}
