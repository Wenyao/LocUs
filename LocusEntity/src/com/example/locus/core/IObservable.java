package com.example.locus.core;

import com.example.locus.entity.Result;

public interface IObservable {
	Result addObserver(IObserver obs);
}
