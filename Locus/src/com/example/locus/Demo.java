package com.example.locus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
	 String interests;
	 private ListView listView;
	 private TextView latituteField;
	 private TextView longitudeField;
	 ICore core;
	 User currentUser;
	 
	 private int groupId1=1;
	 private int editProfileId = Menu.FIRST;

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
		interests = intent.getStringExtra("interests");
		currentUser.setLatitude(latitude);
	    currentUser.setLongtitude(longitude);
	    currentUser.setIp(ipAdd);
		currentUser.setName(username);
		currentUser.setInterests(interests);
		if(gender.equals("Male"))
			currentUser.setSex(Sex.Male);
		else
			currentUser.setSex(Sex.Female);

		Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		        try {
		        	CoreFacade.getInstance().register(currentUser);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		});

		thread.start(); 
		//CoreFacade.getInstance().register(currentUser);
		latituteField = (TextView) findViewById(R.id.textView1);
	    longitudeField = (TextView) findViewById(R.id.textView2);
	    latituteField.setText(String.valueOf(latitude));
	    longitudeField.setText(String.valueOf(longitude));

	    //----------------------------- FOR LIST VIEW ---------------------------------------------------------
	    Set<User> data_set;
	    List<User> data = new ArrayList<User>();
	    try{
	    data_set = core.getUsersNearby();
	    
	    data.addAll(data_set);
	    }
	    catch(NullPointerException e){
	    	Toast.makeText(getBaseContext(),"No users Nearby", Toast.LENGTH_SHORT).show();
	    }
	    


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
			menu.add(groupId1, editProfileId, editProfileId, "Edit Profile" );
			
			return super.onCreateOptionsMenu(menu);
		}
		
		 public boolean onOptionsItemSelected(MenuItem item) {
			 
			 switch (item.getItemId()){
			 	
			 case 1 : Intent intent = new Intent(this, MainActivity.class);
			 		  startActivity(intent);
			 		  break;
			 
			 }
			 return super.onOptionsItemSelected(item);
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
		super.onDestroy();
		core.logout();
	}
}