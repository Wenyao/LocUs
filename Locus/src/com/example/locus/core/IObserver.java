package com.example.locus.core;

import java.util.Set;

import com.example.locus.entity.User;

public interface IObserver {
	public void onReceiveMessage(User src, String msg);
	public void onReceiveUserProfile(User user);
	public void onReceiveNearbyUsers(Set<User> users);
}
