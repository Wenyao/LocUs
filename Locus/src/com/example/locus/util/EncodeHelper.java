package com.example.locus.util;

import android.util.Base64;

public class EncodeHelper {
	public static String bytesToString(byte[] bytes){
		return Base64.encodeToString(bytes,0);
	}
	
	public static byte[] stringToBytes(String str){
		return Base64.decode(str, 0);
	}
}
