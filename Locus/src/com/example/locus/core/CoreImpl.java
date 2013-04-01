package com.example.locus.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.example.locus.dht.DHTFacade;
import com.example.locus.dht.IDHT;
import com.example.locus.entity.ErrorCodes;
import com.example.locus.entity.Result;
import com.example.locus.entity.User;
import com.example.locus.network.IMessagePasser;
import com.example.locus.network.MessagePasserFacade;

public class CoreImpl implements ICore {

	private IDHT dht;
	private IMessagePasser mp;
	private User user;
	private Set<User> nearbyUsers;

	private List<IObserver> observers;

	public CoreImpl() {
		dht = DHTFacade.getInstance();
		mp = MessagePasserFacade.getInstance();
		observers = new ArrayList<IObserver>();
	}

	@Override
	public Result refreshLocation(double lati, double longti) {
		if (user != null) {
			user.setLatitude(lati);
			user.setLongtitude(longti);
			dht.put(user);
			return Result.Success;
		} else {
			return new Result(false, ErrorCodes.NullUser);
		}
	}

	@Override
	public Set<User> getUsersNearby() {
		if (user != null) {
			nearbyUsers =  dht.getUsersByKey(user);
			return nearbyUsers;
		} else {
			return null;
		}
	}

	@Override
	public Result sendMessage(User user, String msg) {
		return mp.sendMessage(this.user, user, msg);
	}

	@Override
	public Result broadcastMessage(String msg) {
		return mp.broadcast(user, nearbyUsers, msg);
	}

	@Override
	public Result addObserver(IObserver obs) {
		observers.add(obs);
		return Result.Success;
	}

	@Override
	public Result register(User user) {
		this.user = user;
		return refreshLocation(user.getLatitude(), user.getLongtitude());
	}

	@Override
	public Result logout() {
		if (user != null) {
			dht.delete(user);
		}
		return Result.Success;
	}

	@Override
	public User getCurrentUser() {
		return this.user;
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
