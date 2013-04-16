package com.example.locus.core;

import java.util.Set;

import com.example.locus.entity.Result;
import com.example.locus.entity.User;

public class CoreFacade implements ICore {
	private static CoreFacade instance = null;
	
	private ICore coreImpl;
	
	private CoreFacade(){
		coreImpl = new CoreImpl();
	}
	
	public static CoreFacade getInstance(){
		if (instance == null){
			instance = new CoreFacade();
		}
		
		return instance;
	}

	@Override
	public Result refreshLocation(double lati, double longti) {
		return coreImpl.refreshLocation(lati, longti);
	}

	@Override
	public Set<User> getUsersNearby() {
		return coreImpl.getUsersNearby();
	}

	@Override
	public Result sendMessage(User user, String msg) {
		return coreImpl.sendMessage(user, msg);
	}

	@Override
	public Result broadcastMessage(String msg) {
		return coreImpl.broadcastMessage(msg);
	}

	@Override
	public Result addObserver(IObserver obs) {
		return coreImpl.addObserver(obs);
	}

	@Override
	public Result register(User user) {
		return coreImpl.register(user);
	}

	@Override
	public Result logout() {
		return coreImpl.logout();
	}

	@Override
	public User getCurrentUser() {
		return coreImpl.getCurrentUser();
	}

	@Override
	public void onReceiveMessage(User src, String msg) {
		coreImpl.onReceiveMessage(src, msg);
	}

	@Override
	public void onReceiveUserProfile(User user) {
		coreImpl.onReceiveUserProfile(user);
	}

	@Override
	public User getUserProfile(User target) {
		return coreImpl.getUserProfile(target);
	}

	@Override
	public void onReceiveNearbyUsers(Set<User> users) {
		coreImpl.onReceiveNearbyUsers(users);
	}
}
