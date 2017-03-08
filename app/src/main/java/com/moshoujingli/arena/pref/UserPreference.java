package com.moshoujingli.arena.pref;

import com.moshoujingli.arena.model.User;

/**
 * Created by bixiaopeng on 2017/1/28.
 */

public class UserPreference {
    private static User curUser;
    public static synchronized User getCurrentUser(){
        return curUser;
    }

    public static void attachUser(User user) {
        curUser = user;
    }
}
