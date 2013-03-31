package com.example.locus.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.example.locus.entity.Result;
import com.example.locus.entity.Sex;
import com.example.locus.entity.User;

public class FakeCoreImpl implements ICore {
	static Logger log = Logger.getLogger(FakeCoreImpl.class.getName());
	
	private User user;
	private List<IObserver> observers = new ArrayList<IObserver>();

	@Override
	public Result refreshLocation(double lati, double longti) {
		log.info(String.format("refreshLocation (%.3f, %.3f)", lati, longti));
		user.setLatitude(lati);
		user.setLongtitude(longti);
		return Result.Success;
	}

	@Override
	public List<User> getUsersNearby() {
		List<User> results = new ArrayList<User>();
		results.add(new User("Alice", Sex.Female, "192.168.1.1", 0, 0));
		results.add(new User("Bob", Sex.Male, "192.168.1.2", 0, 0));
		results.add(new User("Charlie", Sex.Unknown, "192.168.1.3", 0, 0));
		results.add(new User());
		log.info(Arrays.toString(results.toArray()));
		return results;
	}

	@Override
	public Result sendMessage(User user, String msg) {
		log.info(String.format("sendMessage target = %s, msg = %s", user.toString(), msg));
		return Result.Success;
	}

	@Override
	public Result broadcastMessage(String msg) {
		log.info(String.format("broadcast msg = %s", msg));
		return Result.Success;
	}

	@Override
	public Result addObserver(IObserver obs) {
		log.info(String.format("addObserver %s", obs));
		observers.add(obs);
		return Result.Success;
	}
	
	@Override
	public Result register(User user) {
		log.info(String.format("register %s", user));
		this.user = user;
		return Result.Success;
	}

	@Override
	public Result logout() {
		log.info(String.format("logout %s", user));
		return Result.Success;
	}

	@Override
	public User getCurrentUser() {
		return new User("Test User", Sex.Female, "192.168.1.123", 0, 0);
	}

	@Override
	public void onReceiveMessage(User src, String msg) {
		for (IObserver observer : observers) {
			observer.onReceiveMessage(src, msg);
		}
	}

	@Override
	public void onReceiveUserProfile(User user) {
		for (IObserver observer : observers) {
			observer.onReceiveUserProfile(user);
		}
	}

}
