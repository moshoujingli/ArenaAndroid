package com.moshoujingli.arena.service.impl;

import com.moshoujingli.arena.model.User;
import com.moshoujingli.arena.service.IUserService;

/**
 * Created by bixiaopeng on 2017/1/28.
 */

public class LeanCloudUserService implements IUserService {
    @Override
    public void addUser(User user) {

    }

    @Override
    public User generateTemporaryUser() {
        return new User();
    }
}
