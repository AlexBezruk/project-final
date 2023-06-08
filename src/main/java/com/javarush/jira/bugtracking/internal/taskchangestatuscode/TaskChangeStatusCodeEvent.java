package com.javarush.jira.bugtracking.internal.taskchangestatuscode;

import com.javarush.jira.common.AppEvent;
import com.javarush.jira.login.User;

import java.util.List;

public record TaskChangeStatusCodeEvent(List<User> users) implements AppEvent {
}
