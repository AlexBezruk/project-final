package com.javarush.jira.profile.web;

import com.javarush.jira.MatcherFactory;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProfileTestData {
    public static final MatcherFactory.Matcher<ProfileTo> PROFILE_TO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(ProfileTo.class, "lastLogin");

    public static final long USER_ID = 1;
    public static final long ADMIN_ID = 2;
    public static final String USER_MAIL = "user@gmail.com";
    public static final String ADMIN_MAIL = "admin@gmail.com";

    public static final ProfileTo userProfile = new ProfileTo(USER_ID,
            new HashSet<>(Arrays.asList("assigned", "deadline", "overdue")),
            new HashSet<>(Arrays.asList(new ContactTo("skype", "userSkype"),
                    new ContactTo("mobile", "+01234567890"),
                    new ContactTo("website", "user.com"))));

    public static final ProfileTo adminProfile = new ProfileTo(ADMIN_ID,
            new HashSet<>(Arrays.asList("one_day_before_deadline", "two_days_before_deadline", "three_days_before_deadline")),
            new HashSet<>(Arrays.asList(new ContactTo("github", "adminGitHub"),
                    new ContactTo("tg", "adminTg"))));

    public static ProfileTo getUpdated() {
        return new ProfileTo(USER_ID, Set.of("overdue"),
                Set.of(new ContactTo("github", "userGitHub"),
                        new ContactTo("tg", "userTg")));
    }

    public static ProfileTo getUpdatedWithWrongMailNotification() {
        return new ProfileTo(USER_ID, Set.of("abracadabra"), null);
    }

    public static ProfileTo getUpdatedWithWrongContact() {
        return new ProfileTo(USER_ID, null, Set.of(new ContactTo("1", "2")));
    }

    public static ProfileTo getUpdatedWithMailNotificationAndContactIsNull() {
        return new ProfileTo(USER_ID, null, null);
    }
}
