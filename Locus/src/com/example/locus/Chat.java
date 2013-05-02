package com.example.locus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locus.core.Constants;
import com.example.locus.core.CoreFacade;
import com.example.locus.core.IObserver;
import com.example.locus.entity.Message;
import com.example.locus.entity.User;

public class Chat extends Activity implements OnClickListener, IObserver {

	private ListView chatView;
	private Button sendButton;
	private EditText tv;
	private TextView nameText;
	private TextView msgText;
	private List<Message> msg;
	private ChatAdapter chatAdapter;
	User oppUser;
	User currUser;
	ImageView currImage;
	ImageView oppImage;
	Bitmap bitmap1;
	Bitmap bitmap2;
	protected static final int RESULT_SPEECH = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		oppUser = (User) intent.getSerializableExtra("user");

		CoreFacade.getInstance().addObserver(this);

		setContentView(R.layout.activity_chat);
		currImage = (ImageView) findViewById(R.id.currImage);
		oppImage = (ImageView) findViewById(R.id.oppImage);
		Log.v(Constants.AppUITag,
				"get current user to show image user get pic = "
						+ CoreFacade.getInstance().getCurrentUser().getPic());
		currUser = CoreFacade.getInstance().getCurrentUser();
		if (oppUser.getPic() != null) {
			bitmap1 = BitmapFactory.decodeByteArray(oppUser.getPic(), 0,
					oppUser.getPic().length);
			oppImage.setImageBitmap(bitmap1);
		}
		if (currUser.getPic() != null) {
			bitmap2 = BitmapFactory.decodeByteArray(currUser.getPic(), 0,
					currUser.getPic().length);
			currImage.setImageBitmap(bitmap2);
		}

		chatView = (ListView) findViewById(R.id.listViewChat);
		sendButton = (Button) findViewById(R.id.sendButton);
		tv = (EditText) findViewById(R.id.chatText);
		nameText = (TextView) findViewById(R.id.textView1);
		msgText = (TextView) findViewById(R.id.chatText);

		msg = new ArrayList<Message>();

		chatAdapter = new ChatAdapter(this, R.layout.activity_chat_adapter, msg);
		chatView.setAdapter(chatAdapter);

		// To populate the list with the previous msgs
		msg.removeAll(msg);

		List<Message> chats = new ArrayList<Message>();
		Log.v(Constants.AppUITag, "get msgs with user = " + oppUser);
		chats = CoreFacade.getInstance().getMessagesByUser(oppUser);
		if (chats != null) {
			for (Message s : chats) {
				addItemsToList(s);
			}
		}

	}

	private void addItemsToList(Message m) {
		chatAdapter.add(m);
		chatAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_chat, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.voice:
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

			try {
				startActivityForResult(intent, RESULT_SPEECH);
				tv.setText("");
			} catch (ActivityNotFoundException a) {
				Toast t = Toast.makeText(getApplicationContext(),
						"Opps! Your device doesn't support Speech to Text",
						Toast.LENGTH_SHORT);
				t.show();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH:
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				tv.setText(text.get(0));
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// Message m = new Message();
		String messg = tv.getText().toString();

		SendMessageTask sendMessageTask = new SendMessageTask();

		User currentUser = CoreFacade.getInstance().getCurrentUser();
		Message mesg = new Message(currentUser, oppUser, "Normal", messg);
		addItemsToList(mesg);
		sendMessageTask.execute(mesg);
		tv.setText("");
		scrollMyListViewToBottom();

	}

	private void scrollMyListViewToBottom() {
		chatView.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				chatView.setSelection(chatView.getCount() - 1);
			}
		});
	}

	@SuppressLint("NewApi")
	public void createNotification(Message m) {
		Intent intent2 = new Intent(getApplicationContext(), Chat.class);
		intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent2.putExtra("user", m.getSrc());
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent2, 0);

		// Build notification
		// Actions are just fake
		Notification noti = new NotificationCompat.Builder(this)
				.setAutoCancel(true)
				.setContentTitle("New Message from " + m.getSrc().getName())
				.setContentText(m.getData().toString())
				.setSmallIcon(R.drawable.locus).setContentIntent(pIntent)
				.addAction(R.drawable.msg1, "View", pIntent).build();

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		// noti.flags |= Notification.DEFAULT_VIBRATE;
		// noti.flags |= Notification.DEFAULT_SOUND;

		notificationManager.notify(0, noti);

	}

	private class SendMessageTask extends AsyncTask<Message, Integer, Message> {
		@Override
		protected Message doInBackground(Message... params) {
			try {
				CoreFacade.getInstance().sendMessage(params[0].getDst(),
						(String) params[0].getData());
				return params[0];
			} catch (Exception e) {
				return null;

			}

		}

		@Override
		protected void onPostExecute(Message result) {
			if (result == null) {
				Toast.makeText(getApplicationContext(),
						"Check Internet Connection", Toast.LENGTH_LONG).show();

			} else {
				String str_text = String.format("msg sent.  msg = %s",
						result.toString());
//				Toast.makeText(getApplicationContext(), str_text,
//						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onReceiveMessage(Message msg) {
		// TODO refresh message list, check src
		AsyncTask<Message, Integer, Message> updateUITask = new OnReceiveMessageUpdateUITask();
		updateUITask.execute(msg);
	}

	@Override
	public void onReceiveUserProfile(User user) {
	}

	@Override
	public void onReceiveNearbyUsers(Set<User> users) {
	}

	private class OnReceiveMessageUpdateUITask extends
			AsyncTask<Message, Integer, Message> {
		@Override
		protected Message doInBackground(Message... params) {
			try {
				return params[0];
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Message result) {
			if (result == null) {
				Toast.makeText(getApplicationContext(),
						"Check Internet Connection", Toast.LENGTH_LONG).show();
			} else {
				String str_text = result.toString();
//				Toast.makeText(getApplicationContext(), str_text,
//						Toast.LENGTH_LONG).show();
				msg.removeAll(msg);

				List<Message> chats = CoreFacade.getInstance()
						.getMessagesByUser(result.getSrc());

				for (Message s : chats) {
					addItemsToList(s);
				}
				
				try{
					createNotification(chats.get(chats.size() - 1));
				}catch (Exception e){
					Log.e(Constants.AppUITag, "create notification error =" + e);
				}
			}

		}
	}
}
