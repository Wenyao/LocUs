package com.example.locus;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BroadCast extends Activity {

	public Button bcButton;
	public EditText et;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broad_cast);
		EditText et = (EditText) findViewById(R.id.broadCasteditText);
		bcButton = (Button) findViewById(R.id.broadcastbutton);
		
	}

	public void sendBroadCast(View view){
		String txt = et.getText().toString();
		//TODO
	
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_broad_cast, menu);
		return true;
	}

}
