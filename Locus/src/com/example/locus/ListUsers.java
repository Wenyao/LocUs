package com.example.locus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

public class ListUsers extends Activity implements LocationListener, IObserver {

	 private TextView latituteField;
	 private TextView longitudeField;
	 private LocationManager locationManager;
	 private String provider;
	 double latitude = 0;
	 double longitude = 0;
	 String ipAddress;
	 String username;
	 String ipAdd;
	 String gender;
	 String interests;
	 private ListView listView;
	 ICore core;
	 User currentUser;
	 
	 private int groupId1=1;
	 private int editProfileId = Menu.FIRST;

	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_users);
		currentUser = new User();
		//create Icore instance
		core = CoreFacade.getInstance();
		core.addObserver(this);

		Intent intent = getIntent();
		username = intent.getStringExtra("userName");
		gender = intent.getStringExtra("sex");
		ipAdd = intent.getStringExtra("IP");
		interests = intent.getStringExtra("interests");
		currentUser.setIp(ipAdd);
		currentUser.setName(username);
		currentUser.setInterests(interests);
		if(gender.equals("Male"))
			currentUser.setSex(Sex.Male);
		else
			currentUser.setSex(Sex.Female);

		latituteField = (TextView) findViewById(R.id.textView1);
	    longitudeField = (TextView) findViewById(R.id.textView2);
	    // Get the location manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the locatioin provider -> use
	    // default
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, true);
	    Location location = locationManager.getLastKnownLocation(provider);

	    // Initialize the location fields
	    if (location != null) {
	    	Toast.makeText(getBaseContext(),"Provider " + provider + " has been selected.",Toast.LENGTH_SHORT).show();
	      //onLocationChanged(location);
	    } else {
	    	Toast.makeText(getBaseContext(),"Location NOt available", Toast.LENGTH_SHORT).show();
	    	latituteField.setText("Location not available");
	    	longitudeField.setText("Location not available");
	    }

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
	    

	    /*ListDetails data[] = new ListDetails[]{
	    		
	    		new ListDetails(R.drawable.a, "Car1"),
	    		new ListDetails(R.drawable.b, "Car2"),
	    		new ListDetails(R.drawable.c, "Car3"),
	    		new ListDetails(R.drawable.d, "Car4"),
	    		new ListDetails(R.drawable.d, "Car5"),
	    		new ListDetails(R.drawable.d, "Car6"),
	    		new ListDetails(R.drawable.d, "Car7"),
	    		new ListDetails(R.drawable.d, "Car8"),
	    		new ListDetails(R.drawable.a, "Car9"),
	    		new ListDetails(R.drawable.b, "Car10"),
	    		new ListDetails(R.drawable.c, "Car11"),
	    		new ListDetails(R.drawable.d, "Car12"),
	    		new ListDetails(R.drawable.e, "Car13"),
	    		new ListDetails(R.drawable.a, "Car14"),
	    		new ListDetails(R.drawable.b, "Car15"),
	    		new ListDetails(R.drawable.c, "Car15"),
	    		
	    		new ListDetails(R.drawable.e, "Car5")
	    };*/
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
	            Toast.makeText(getApplicationContext(),str_text+" \n"+"IP = "+o.getIp()+"\nLat="+o.getLatitude()+" Lon="+o.getLongtitude(), Toast.LENGTH_LONG).show();
	            Intent intent = new Intent(getApplicationContext(), Profile.class);
	            intent.putExtra("user", o);
	            
	            startActivity(intent);
	        }

	    });  
	 }

	 //------------------------------------------------------------------------------------------------------------------------
	 /* Request updates at startup */
	 
	 
	 
	  @Override
	  protected void onResume() {
	    super.onResume();
	    locationManager.requestLocationUpdates(provider, 400, 1, this);
	  }

	  /* Remove the locationlistener updates when Activity is paused */
	  @Override
	  protected void onPause() {
	    super.onPause();
	    locationManager.removeUpdates(this);
	  }

	  @Override
	  public void onLocationChanged(Location location) {

	    latitude = (double) (location.getLatitude());
	    longitude = (double) (location.getLongitude());
	    currentUser.setLatitude(latitude);
	    currentUser.setLongtitude(longitude);
	    
	    CoreFacade.getInstance().register(currentUser);
	    //core.refreshLocation(latitude, longitude);
	    latituteField.setText(String.valueOf(latitude));
	    longitudeField.setText(String.valueOf(longitude));

	  }

	  @Override
	  public void onStatusChanged(String provider, int status, Bundle extras) {
	    // TODO Auto-generated method stub

	  }

	  @Override
	  public void onProviderEnabled(String provider) {
	    Toast.makeText(this, "Enabled new provider " + provider,
	        Toast.LENGTH_SHORT).show();

	  }

	  @Override
	  public void onProviderDisabled(String provider) {
	    Toast.makeText(this, "Disabled provider " + provider,
	        Toast.LENGTH_SHORT).show();
	  }


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