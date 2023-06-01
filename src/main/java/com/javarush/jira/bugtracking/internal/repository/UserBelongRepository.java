package com.javarush.jira.bugtracking.internal.repository;

import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.common.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface UserBelongRepository extends BaseRepository<UserBelong> {
    List<UserBelong> getUserBelongsByObjectIdAndUserTypeCodeAndObjectType(
            Long objectId, String userTypeCode, ObjectType objectType);
}
