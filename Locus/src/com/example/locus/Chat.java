package com.example.locus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.example.locus.core.CoreFacade;
import com.example.locus.core.IObserver;
import com.example.locus.entity.Message;
import com.example.locus.entity.Result;
import com.example.locus.entity.User;
import com.sun.xml.internal.bind.v2.TODO;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Chat extends Activity implements OnClickListener, IObserver {

	private ListView chatView;
	private Button sendButton;
	private EditText tv;
	private List<String> msg;
	private ChatAdapter chatAdapter;
	String userName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		userName = intent.getStringExtra("username");
		CoreFacade.getInstance().addObserver(this);

		setContentView(R.layout.activity_chat);

		chatView = (ListView) findViewById(R.id.listViewChat);
		sendButton = (Button) findViewById(R.id.sendButton);
		tv = (EditText) findViewById(R.id.chatText);

		msg = new ArrayList<String>();
		chatAdapter = new ChatAdapter(this, R.layout.activity_chat_adapter, msg);
		chatView.setAdapter(chatAdapter);

	}

	private void addItemsToList(String m) {
		msg.add(m);
		chatAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_chat, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		String txt = tv.getText().toString();
		addItemsToList(txt);

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
			msg.add((String)result.getData());
		}
	}
}
