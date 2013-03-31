package com.example.locus.dht;


import java.util.Set;

import com.example.locus.entity.Result;
import com.example.locus.entity.User;

public interface IDHT {
	    Result join();
	    Result create();
        Result put(User user);
        Set<User> getUsersByKey(User user);
        Result delete(User user);
        void leave();
}
