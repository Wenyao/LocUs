package com.example.locus;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locus.core.CoreFacade;
import com.example.locus.core.ICore;
import com.example.locus.core.IObserver;
import com.example.locus.entity.Sex;
import com.example.locus.entity.User;

public class Demo extends Activity implements IObserver{

	 double latitude = 0;
	 double longitude = 0;
	 String username;
	 String ipAdd;
	 String gender;
	 private ListView listView;
	 private TextView latituteField;
	 private TextView longitudeField;
	 ICore core;
	 User currentUser;
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_users);
		Intent intent = getIntent();
		currentUser = new User();
		//create Icore instance
		core = CoreFacade.getInstance();
		//core.addObserver(this);
		username = intent.getStringExtra("userName");
		latitude = Double.parseDouble(intent.getStringExtra("latitude"));
		longitude = Double.parseDouble(intent.getStringExtra("longitude"));
		ipAdd = intent.getStringExtra("IP");
		gender = intent.getStringExtra("sex");
		
		currentUser.setLatitude(latitude);
	    currentUser.setLongtitude(longitude);
	    currentUser.setIp(ipAdd);
		currentUser.setName(username);
		if(gender.equals("Male"))
			currentUser.setSex(Sex.Male);
		else
			currentUser.setSex(Sex.Female);
		
		latituteField = (TextView) findViewById(R.id.textView1);
	    longitudeField = (TextView) findViewById(R.id.textView2);
	    latituteField.setText(String.valueOf(latitude));
	    longitudeField.setText(String.valueOf(longitude));
	
	    //----------------------------- FOR LIST VIEW ---------------------------------------------------------
	    List<User> data = core.getUsersNearby();
	    AdapterList adapter = new AdapterList (this, R.layout.activity_list_adapter, data);
	    
	    listView = (ListView)findViewById(R.id.listView);
	    listView.setAdapter(adapter);
	    
	    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	        @Override
	        public void onItemClick(AdapterView<?> adapter, View view, int position,
	                long id) {
	            // TODO Auto-generated method stub
	            User o = (User)adapter.getItemAtPosition(position);
	            String str_text = o.getName();
	            Toast.makeText(getApplicationContext(),str_text+" SelecteD\n"+"IP = "+o.getIp()+"\nLat="+o.getLatitude()+" Lon="+o.getLongtitude(), Toast.LENGTH_LONG).show();
	        }

	    });  
	 }
	 
	 //------------------------------------------------------------------------------------------------------------------------
	 /* Request updates at startup */
	 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list_users, menu);
		return true;
	}

	@Override
	public void onReceiveMessage(User src, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveUserProfile(User user) {
		// TODO Auto-generated method stub
		
	}
	public void onDestroy(){
		core.logout();
	}
}
