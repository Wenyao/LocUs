package com.example.locus.network;

import java.util.Set;

import com.example.locus.core.IObserver;
import com.example.locus.entity.Result;
import com.example.locus.entity.User;

public class MessagePasserFacade implements IMessagePasser {
	private static MessagePasserFacade instance = null;
	
	private IMessagePasser messagePasser;
	
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
		return messagePasser.addObserver(obs);
	}

	@Override
	public Result sendMessage(User src, User target, String msg) {
		return messagePasser.sendMessage(src, target, msg);
	}

	@Override
	public Result broadcast(User src, Set<User> targets, String msg) {
		return messagePasser.broadcast(src, targets, msg);
	}

	@Override
	public Result startReceive() {
		return messagePasser.startReceive();
	}
}
