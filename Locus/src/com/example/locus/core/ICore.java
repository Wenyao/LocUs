package com.example.locus.core;

import java.util.Set;

import com.example.locus.entity.Result;
import com.example.locus.entity.User;

public interface ICore extends IObservable, IObserver {
	Result register(User user);
	Result logout();
	Result refreshLocation(double lati, double longti);
	
	Set<User> getUsersNearby();
	
	Result sendMessage(User user, String msg);
	Result broadcastMessage(String msg);
	
	User getCurrentUser();
}
