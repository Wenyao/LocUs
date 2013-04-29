package com.example.locus;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.locus.core.CoreFacade;
import com.example.locus.entity.Sex;
import com.example.locus.entity.User;

public class MainActivity extends Activity {

	private CoreFacade core;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		CoreFacade.getInstance().setContext(this.getApplicationContext());

		User user = CoreFacade.getInstance().getCurrentUser();

		if(user == null){
			Intent intent = new Intent(this, MyProfile.class);
			startActivity(intent);

		}
		else{
			Intent listUser = new Intent(this, Demo.class);
			listUser.putExtra("userName", user.getName());
			listUser.putExtra("latitude", ""+user.getLatitude());
			listUser.putExtra("longitude", ""+user.getLongtitude());
			listUser.putExtra("userName", user.getName());
			String ipAdd;
			try {
				ipAdd = IPAddress.getIPAddress(true);

				listUser.putExtra("IP", ipAdd);
				if(user.getSex() == Sex.Female)
					listUser.putExtra("sex", "Female");
				else
					listUser.putExtra("sex", "Male");
				listUser.putExtra("interests", user.getInterests());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			startActivity(listUser);
		}

	}



}
