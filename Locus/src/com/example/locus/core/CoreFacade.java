package com.example.locus.core;

import java.util.List;
import java.util.Set;

import android.content.Context;

import com.example.locus.entity.Message;
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
	/*
	 * call this method after null value returning from getCurrentUser()
	 * or call it after user update his/her profile.
	 * 
	 * this method is for registers new user or updating user profiles
	 * @see com.example.locus.core.ICore#register(com.example.locus.entity.User)
	 */
	public Result register(User user) {
		return coreImpl.register(user);
	}

	@Override
	public Result logout() {
		return coreImpl.logout();
	}

	@Override
	/*
	 * call this method every time app launches after setContext() is called
	 * returns null if no user is registered or context is not set
	 * returns User if there is a registered user.  Always have only one user registered.
	 * @see com.example.locus.core.ICore#getCurrentUser()
	 */
	public User getCurrentUser() {
		return coreImpl.getCurrentUser();
	}

	@Override
	public void onReceiveMessage(Message msg) {
		coreImpl.onReceiveMessage(msg);
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

	/*
	 * call this method initially once when app launches.
	 * The context is required to create a database helper.
	 * @see com.example.locus.core.ICore#setContext(android.content.Context)
	 */
	public void setContext(Context context) {
		if (coreImpl instanceof CoreImpl){
			((CoreImpl)coreImpl).setContext(context);
		}
	}

	@Override
	public List<Message> getMessagesByUser(User user) {
		return coreImpl.getMessagesByUser(user);
	}

	@Override
	public Result sendExMessage(User target, String kind, byte[] obj) {
		return coreImpl.sendExMessage(target, kind, obj);
	}
}
