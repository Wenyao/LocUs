package com.example.locus;

import java.util.ArrayList;

import com.example.locus.core.CoreFacade;
import com.example.locus.entity.Message;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BroadCast extends Activity {

	public Button bcButton;
	public EditText et;
	protected static final int RESULT_SPEECH = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broad_cast);
		et = (EditText) findViewById(R.id.broadCasteditText);
		bcButton = (Button) findViewById(R.id.broadcastbutton);
		

	}

	public void sendBroadCast(View view){
		String txt = et.getText().toString();

		BroadMessageTask task = new BroadMessageTask();
		task.execute(txt);
		finish();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_broad_cast, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.broadcast_voice:
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

			try {
				startActivityForResult(intent, RESULT_SPEECH);
				et.setText("");
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

				et.setText(text.get(0));
			}
			break;
		}
	}

	private class BroadMessageTask extends
	AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			try{
				CoreFacade.getInstance().broadcastMessage((String)params[0]);
				return params[0];
			}
			catch(Exception e){

				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			if(result == null)
				Toast.makeText(getApplicationContext(), "Error in BroadCasting", Toast.LENGTH_LONG)
				.show();
			else{
				String str_text = String.format("Broadcast msg sent.", result.toString());
				Toast.makeText(getApplicationContext(), str_text, Toast.LENGTH_LONG)
				.show();
			}
		}
	}

}
