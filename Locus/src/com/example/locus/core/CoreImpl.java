package com.example.locus.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.example.locus.dht.DHTFacade;
import com.example.locus.dht.IDHT;
import com.example.locus.entity.ErrorCodes;
import com.example.locus.entity.Message;
import com.example.locus.entity.Result;
import com.example.locus.entity.User;
import com.example.locus.network.IMessagePasser;
import com.example.locus.network.MessagePasserFacade;

public class CoreImpl implements ICore {

	private Context context;
	private AccountDataSource accountDataSource;
	private MessageDataSource messageDataSource;

	private IDHT dht;
	private IMessagePasser mp;
	private User user;
	private Set<User> nearbyUsers;

	private List<IObserver> observers;

	public CoreImpl() {
		context = null;
		accountDataSource = null;
		messageDataSource = null;

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
			nearbyUsers = dht.getUsersByKey(user);
			onReceiveNearbyUsers(nearbyUsers);
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
		if (accountDataSource != null) {
			this.user = accountDataSource.createUser(user);
			dht.join();
			mp.startReceive();
			return refreshLocation(user.getLatitude(), user.getLongtitude());
		} else {
			Log.e("Locus.DataSource",
					"Please set context before call register.");
			return new Result(false, ErrorCodes.DataSourceError);
		}
	}

	@Override
	public Result logout() {
		if (user != null) {
			dht.delete(user);
		}
		
		accountDataSource.close();
		messageDataSource.close();
		return Result.Success;
	}

	@Override
	public User getCurrentUser() {
		if (user == null) {
			if (accountDataSource == null) {
				Log.e("Locus.DataSource",
						"Please set context before call getCurrentUser.");
				return null;
			} else {
				List<User> users = accountDataSource.getAllUsers();
				if (users.size() == 0) {
					return null;
				} else {
					user = users.get(0);
					dht.join();
					mp.startReceive();
					refreshLocation(user.getLatitude(), user.getLongtitude());
					return user;
				}
			}
		} else {
			return user;
		}
	}

	@Override
	public void onReceiveMessage(Message msg) {
		Log.i("com.example.locus.CoreImpl", "receive msg = " + msg.toString());
		System.out.println("receive msg = " + msg.toString());
		
		if (msg != null) {
			User temp = accountDataSource.getUserById(msg.getSrc().getId());
			if (temp == null) {
				accountDataSource.createUser(msg.getSrc());
			}

			messageDataSource.createMessage(msg);

			for (IObserver observer : observers) {
				observer.onReceiveMessage(msg);
			}
		}
	}

	@Override
	public void onReceiveUserProfile(User user) {
		for (IObserver observer : observers) {
			observer.onReceiveUserProfile(user);
		}
	}

	@Override
	public User getUserProfile(User target) {
		return mp.getUserProfile(target);
	}

	@Override
	public void onReceiveNearbyUsers(Set<User> users) {
		for (IObserver observer : observers) {
			observer.onReceiveUserProfile(user);
		}
	}

	public void setContext(Context context) {
		this.context = context;
		accountDataSource = new AccountDataSource(context);
		accountDataSource.open();
		messageDataSource = new MessageDataSource(context);
		messageDataSource.open();
	}

}
