package com.example.locus.core;

import java.util.List;
import java.util.Set;

import com.example.locus.entity.Message;
import com.example.locus.entity.Result;
import com.example.locus.entity.User;

public interface ICore extends IObservable, IObserver {
	Result register(User user);
	Result logout();
	Result refreshLocation(double lati, double longti);
	
	Set<User> getUsersNearby();
	
	List<Message> getMessagesByUser(User user);
	
	Result sendMessage(User target, String msg);
	Result broadcastMessage(String msg);
	User getUserProfile(User target);
	
	User getCurrentUser();
}
