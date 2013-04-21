package com.example.locus;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Chat extends Activity implements OnClickListener{

	private ListView chatView;
	private Button sendButton;
	private EditText tv;
	private List<String> msg;
	private ChatAdapter chatAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		chatView = (ListView)findViewById(R.id.listViewChat);
		sendButton = (Button)findViewById(R.id.sendButton);
		tv = (EditText)findViewById(R.id.chatText);
		
		
		
		msg = new ArrayList<String>();
		chatAdapter = new ChatAdapter (this, R.layout.activity_chat_adapter, msg);
		chatView.setAdapter(chatAdapter);
		
	}
	
	private void addItemsToList(String m){
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

}
