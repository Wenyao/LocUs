package com.example.locus;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.locus.entity.User;

public class MainActivity extends Activity {

	private RadioGroup radioGenderGroup;
	User currentUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		
		return true;
	}

	public void onClick(View view) throws IOException{

		radioGenderGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
		RadioButton rB;
        //RadioButton rd2=(RadioButton) findViewById(R.id.radio2);
        
		int selectedID = radioGenderGroup.getCheckedRadioButtonId();
        rB = (RadioButton) findViewById(selectedID);
        String gender = (String)rB.getText();
        
		EditText userName = (EditText) findViewById(R.id.editText1);
		String message = userName.getText().toString();
		String ipAdd = IPAddress.getIPAddress(true);
		
		EditText lat = (EditText) findViewById(R.id.editText2);
		EditText lon = (EditText) findViewById(R.id.editText3);

		if(isEmpty(userName))
			Toast.makeText(this,"Enter UserName" + "ip = " + ipAdd, Toast.LENGTH_LONG).show();
		else{
			if( isEmpty(lat) || isEmpty(lon)){
				Intent intent = new Intent(this, ListUsers.class);
				intent.putExtra("userName", message);
				intent.putExtra("sex", gender);	
				intent.putExtra("IP", ipAdd);
				startActivity(intent);
			}
			else{
				Intent intent = new Intent(this, Demo.class);
				String longi = lon.getText().toString();
				String lati = lat.getText().toString();
				intent.putExtra("userName", message);
				intent.putExtra("sex", gender);
				intent.putExtra("IP", ipAdd);
				intent.putExtra("latitude", lati);
				intent.putExtra("longitude", longi);
				startActivity(intent);

			}
		}
	}

	private boolean isEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}
}


