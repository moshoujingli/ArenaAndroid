package com.moshoujingli.arena.service;

import com.moshoujingli.arena.model.User;

/**
 * Created by bixiaopeng on 2017/1/28.
 */

public interface IUserService {
    void addUser(User user);
    User generateTemporaryUser();
}
