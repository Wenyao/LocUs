package com.example.locus;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.locus.core.CoreFacade;
import com.example.locus.entity.User;

public class MainActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		User user = CoreFacade.getInstance().getCurrentUser();

		if(user == null){
			Intent intent = new Intent(this, MyProfile.class);
			startActivity(intent);

		}
		else{
			Intent listUser = new Intent(this, Demo.class);
			listUser.putExtra("userName", user.getName());
			listUser.putExtra("latitude", user.getLatitude());
			listUser.putExtra("longitude", user.getLongtitude());
			listUser.putExtra("userName", user.getName());
			String ipAdd;
			try {
				ipAdd = IPAddress.getIPAddress(true);

				listUser.putExtra("IP", ipAdd);
				listUser.putExtra("sex", user.getSex());
				listUser.putExtra("interests", user.getInterests());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			startActivity(listUser);
		}

	}



}
