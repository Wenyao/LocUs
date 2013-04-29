package com.example.locus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locus.core.CoreFacade;
import com.example.locus.core.IObserver;
import com.example.locus.entity.Message;
import com.example.locus.entity.Sex;
import com.example.locus.entity.User;

public class Demo extends Activity implements IObserver {

	double latitude = 0;
	double longitude = 0;
	String username;
	String ipAdd;
	String gender;
	String interests;
	private ListView listView;
	private TextView latituteField;
	private TextView longitudeField;
	CoreFacade core;
	User currentUser;
	Sex gend;

	private int groupId1 = 1;
	private int editProfileId = Menu.FIRST;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_users);
		Intent intent = getIntent();
		currentUser = new User();
		// create Icore instance
		core = CoreFacade.getInstance();
		core.addObserver(this);

		username = intent.getStringExtra("userName");
		latitude = Double.parseDouble(intent.getStringExtra("latitude"));
		longitude = Double.parseDouble(intent.getStringExtra("longitude"));
		ipAdd = intent.getStringExtra("IP");
		gender = intent.getStringExtra("sex");
		interests = intent.getStringExtra("interests");
		currentUser.setLatitude(latitude);
		currentUser.setLongtitude(longitude);
		currentUser.setIp(ipAdd);
		currentUser.setName(username);
		currentUser.setInterests(interests);
		if (gender.equals("Male"))
			currentUser.setSex(Sex.Male);
		else
			currentUser.setSex(Sex.Female);

		AsyncTask<User, Integer, Set<User>> registerTask = new RegisterTask();
		registerTask.execute(currentUser);

		System.out.println("Call CoreFacade's register");
		latituteField = (TextView) findViewById(R.id.textView1);
		longitudeField = (TextView) findViewById(R.id.textView2);
		latituteField.setText(String.valueOf(latitude));
		longitudeField.setText(String.valueOf(longitude));
	}

	// ------------------------------------------------------------------------------------------------------------------------
	/* Request updates at startup */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menus, menu);
        return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.editProfile:
			Intent intent = new Intent(this, MyProfile.class);
			startActivity(intent);
			break;
		case R.id.broadCast:
			Intent intentBroadCast = new Intent(this, BroadCast.class);
			startActivity(intentBroadCast);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onReceiveMessage(Message msg) {
		AsyncTask<Message, Integer, Message> updateUITask = new OnReceiveMessageUpdateUITask();
		updateUITask.execute(msg);
	}

	@Override
	public void onReceiveUserProfile(User user) {
	}

	public void onDestroy() {
		super.onDestroy();
		core.logout();
	}

	@Override
	public void onReceiveNearbyUsers(Set<User> users) {
	}

	private class RegisterTask extends AsyncTask<User, Integer, Set<User>> {

		@Override
		protected Set<User> doInBackground(User... params) {
			CoreFacade.getInstance().register(currentUser);
			return CoreFacade.getInstance().getUsersNearby();
		}

		@Override
		protected void onPostExecute(Set<User> result) {
			System.out.println("onPostExecute");
			List<User> data = new ArrayList<User>();
			try {
				data.addAll(result);
			} catch (NullPointerException e) {
				Toast.makeText(getBaseContext(), "No users Nearby",
						Toast.LENGTH_SHORT).show();
			}

			AdapterList adapter = new AdapterList(Demo.this,
					R.layout.activity_list_adapter, data);

			listView = (ListView) findViewById(R.id.listView);
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View view,
						int position, long id) {
					User o = (User) adapter.getItemAtPosition(position);
					GetUserProfileTask getUserProfileTask = new GetUserProfileTask();
					getUserProfileTask.execute(o);
					
					SendMessageTask sendMessageTask = new SendMessageTask();
					
					Message msg = new Message(currentUser, o, "Normal", "lala");
					sendMessageTask.execute(msg);
				}
			});
		}
	}

	private class GetUserProfileTask extends AsyncTask<User, Integer, User> {
		@Override
		protected User doInBackground(User... params) {
			return CoreFacade.getInstance().getUserProfile(params[0]);
		}

		@Override
		protected void onPostExecute(User result) {
			String str_text = result.getName();
			Toast.makeText(
					getApplicationContext(),
					str_text + " \n" + "IP = " + result.getIp() + "\nLat="
							+ result.getLatitude() + " Lon="
							+ result.getLongtitude() + " Int = "
							+ result.getInterests(), Toast.LENGTH_LONG).show();
		}
	}

	private class OnReceiveMessageUpdateUITask extends
			AsyncTask<Message, Integer, Message> {
		@Override
		protected Message doInBackground(Message... params) {
			return params[0];
		}

		@Override
		protected void onPostExecute(Message result) {
			// TODO add new message notification on the list
			String str_text = result.toString();
			Toast.makeText(getApplicationContext(), str_text, Toast.LENGTH_LONG)
					.show();
		}
	}

	private class SendMessageTask extends
			AsyncTask<Message, Integer, Message> {
		@Override
		protected Message doInBackground(Message... params) {
			CoreFacade.getInstance().sendMessage(params[0].getDst(), (String)params[0].getData());
			return params[0];
		}

		@Override
		protected void onPostExecute(Message result) {
			String str_text = String.format("msg sent.  msg = %s", result.toString());
			Toast.makeText(getApplicationContext(), str_text, Toast.LENGTH_LONG)
					.show();
		}
	}
	
}
