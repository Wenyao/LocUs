package com.example.locus.network;

import java.util.Set;

import com.example.locus.core.IObservable;
import com.example.locus.entity.Result;
import com.example.locus.entity.User;

public interface IMessagePasser extends IObservable {
	Result sendMessage(User src, User target, String msg);
	Result sendImage(User src, User target, byte[] image);
	Result broadcast(User src, Set<User> targets, String msg);
	User getUserProfile(User target);
	Result startReceive();
	Result stopReceive();
	//CheckProfile.listen(2222);
}
