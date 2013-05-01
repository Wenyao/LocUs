package com.example.locus.network;

import java.util.Set;

import com.example.locus.core.Constants;
import com.example.locus.core.CoreFacade;
import com.example.locus.core.IObserver;
import com.example.locus.entity.Result;
import com.example.locus.entity.User;
import com.example.locus.security.SecurityFacade;
import com.example.locus.util.EncodeHelper;

public class MessagePasserFacade implements IMessagePasser {
	private static MessagePasserFacade instance = null;

	private static int port = 2222;

	// private IMessagePasser messagePasser;

	private MessagePasserFacade() {
		// TODO set message passer
	}

	public static MessagePasserFacade getInstance() {
		if (instance == null) {
			instance = new MessagePasserFacade();
		}

		return instance;
	}

	@Override
	public Result addObserver(IObserver obs) {
		// return messagePasser.addObserver(obs);
		return null;
	}

	@Override
	public Result sendMessage(User src, User target, String msg) {
		MessagePasser.sendMessage(src, target, port, Constants.PlainTextKind, SecurityFacade
				.getInstance().encrypt_data(msg, target.getPublicKey()));
		return Result.Success;
	}
	
	@Override
	public Result sendImage(User src, User target, byte[] image) {
		MessagePasser.sendMessage(src, target, port, Constants.ImageKind, SecurityFacade
				.getInstance().encrypt_data(EncodeHelper.bytesToString(image), target.getPublicKey()));
		return Result.Success;
	}

	@Override
	public Result broadcast(User src, Set<User> targets, String msg) {
		for (User target : targets) {
			if (!src.equals(target)) {
				sendMessage(src, target, msg);
			}
		}
		return Result.Success;
	}

	@Override
	public Result startReceive() {
		MessagePasser.listen(port, CoreFacade.getInstance());
		return Result.Success;
	}

	@Override
	public User getUserProfile(User target) {
		return (User) MessagePasser.checkProfile(target, Integer.valueOf(port));
	}

	@Override
	public Result stopReceive() {
		MessagePasser.stopListen();
		return Result.Success;
	}

}
