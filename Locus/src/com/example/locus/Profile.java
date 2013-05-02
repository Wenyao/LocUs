package com.example.locus;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locus.core.Constants;
import com.example.locus.core.CoreFacade;
import com.example.locus.core.IObserver;
import com.example.locus.entity.Message;
import com.example.locus.entity.Sex;
import com.example.locus.entity.User;

public class Profile extends Activity implements IObserver {

	private int groupId1 = 1;
	private int editProfileId = Menu.FIRST;
	User user;
	ImageView image;
	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("user");

		btn = (Button)findViewById(R.id.button1);
		CoreFacade.getInstance().addObserver(this);

		UpdateUserProfileTask updateUserProfileTask = new UpdateUserProfileTask();
		updateUserProfileTask.execute(user);
	}

	public void chatClick(View view) {
		Intent intent = new Intent(this, Chat.class);
		intent.putExtra("user", user);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(groupId1, editProfileId, editProfileId, "Edit Profile");

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 1:
			Intent intent = new Intent(this, MyProfile.class);
			startActivity(intent);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void updateUI(User user) {
		TextView textName = (TextView) findViewById(R.id.textView2);
		TextView textGender = (TextView) findViewById(R.id.textView4);
		TextView textInterests = (TextView) findViewById(R.id.textView6);
		image = (ImageView) findViewById(R.id.imageView1);

		if (user.getPic() != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(user.getPic(), 0,
					user.getPic().length);
			image.setImageBitmap(bitmap);
		}

		textName.setText(user.getName());
		if (user.getSex() == Sex.Male)
			textGender.setText("Male");
		else
			textGender.setText("Female");
		textInterests.setText(user.getInterests());
	}

	@Override
	public void onReceiveMessage(Message msg) {
	}

	@Override
	public void onReceiveUserProfile(User user) {
	}

	@Override
	public void onReceiveNearbyUsers(Set<User> users) {
	}

	private class UpdateUserProfileTask extends AsyncTask<User, Integer, User> {
		@Override
		protected User doInBackground(User... params) {
			try{
				return CoreFacade.getInstance().getUserProfile(params[0]);
			}
			catch(Exception e){
				return null;
			}
		}

		@Override
		protected void onPostExecute(User result) {
			if(result == null){
				Toast.makeText(getApplicationContext(), "User has gone Offline", Toast.LENGTH_LONG).show();
				btn.setEnabled(false);
			}
			else{
				Log.v(Constants.AppUITag, "get user pic = " + result.getPicURL());
				updateUI(result);
			}
		}
	}

}
