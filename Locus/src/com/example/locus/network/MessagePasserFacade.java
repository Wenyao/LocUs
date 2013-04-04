package com.example.locus.network;

import java.util.Set;

import com.example.locus.core.CoreFacade;
import com.example.locus.core.IObserver;
import com.example.locus.entity.Result;
import com.example.locus.entity.User;

public class MessagePasserFacade implements IMessagePasser {
	private static MessagePasserFacade instance = null;
	
	private static int port = 8888;
	
	//private IMessagePasser messagePasser;
	
	private MessagePasserFacade(){
		//TODO set message passer
	}
	
	public static MessagePasserFacade getInstance(){
		if (instance == null){
			instance = new MessagePasserFacade();
		}
		
		return instance;
	}

	@Override
	public Result addObserver(IObserver obs) {
		//return messagePasser.addObserver(obs);
		return null;
	}

	@Override
	public Result sendMessage(User src, User target, String msg) {
		//return messagePasser.sendMessage(src, target, msg);
		return Result.Success;
	}

	@Override
	public Result broadcast(User src, Set<User> targets, String msg) {
		//return messagePasser.broadcast(src, targets, msg);
		return Result.Success;
	}

	@Override
	public Result startReceive() {
		//return messagePasser.startReceive();
		CheckProfile.listen(port, CoreFacade.getInstance());
		return Result.Success;
	}

	@Override
	public User getUserProfile(User target) {
		return (User) CheckProfile.connect(target.getIp(), port);
		//return null;
	}
}
