package com.example.locus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
	protected static final int RESULT_SPEECH = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		oppUser = new User();
		oppUser = (User) intent.getSerializableExtra("user");
		CoreFacade.getInstance().addObserver(this);

		setContentView(R.layout.activity_chat);

		chatView = (ListView) findViewById(R.id.listViewChat);
		sendButton = (Button) findViewById(R.id.sendButton);
		tv = (EditText) findViewById(R.id.chatText);
		nameText = (TextView) findViewById(R.id.textView1);
		msgText = (TextView) findViewById(R.id.chatText);

		msg = new ArrayList<Message>();
		chatAdapter = new ChatAdapter(this, R.layout.activity_chat_adapter, msg);
		chatView.setAdapter(chatAdapter);

	}

	private void addItemsToList(Message m) {
		// msg.add(m);
		// chatAdapter.clear();

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

	}

	private class SendMessageTask extends AsyncTask<Message, Integer, Message> {
		@Override
		protected Message doInBackground(Message... params) {
			CoreFacade.getInstance().sendMessage(params[0].getDst(),
					(String) params[0].getData());
			return params[0];
		}

		@Override
		protected void onPostExecute(Message result) {
			String str_text = String.format("msg sent.  msg = %s",
					result.toString());
			Toast.makeText(getApplicationContext(), str_text, Toast.LENGTH_LONG)
					.show();
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
			return params[0];
		}

		@Override
		protected void onPostExecute(Message result) {
			String str_text = result.toString();
			Toast.makeText(getApplicationContext(), str_text, Toast.LENGTH_LONG)
					.show();
			msg.removeAll(msg);

			List<Message> chats = CoreFacade.getInstance().getMessagesByUser(
					result.getSrc());

			for (Message s : chats) {
				addItemsToList(s);
			}

		}
	}
}
