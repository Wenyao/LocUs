package com.example.locus.core;

import java.util.Set;

import android.content.Context;

import com.example.locus.entity.Result;
import com.example.locus.entity.User;

public interface ICore extends IObservable, IObserver {
	Result register(User user);
	Result logout();
	Result refreshLocation(double lati, double longti);
	
	Set<User> getUsersNearby();
	
	Result sendMessage(User target, String msg);
	Result broadcastMessage(String msg);
	User getUserProfile(User target);
	
	User getCurrentUser();
	
	void setContext(Context context);
}
