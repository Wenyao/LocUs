package com.example.locus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.locus.entity.Sex;
import com.example.locus.entity.User;
public class Profile extends Activity {

	 private int groupId1=1;
	 private int editProfileId = Menu.FIRST;
	 User user;
	 ImageView image;
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		Intent intent = getIntent();
		user = (User)intent.getSerializableExtra("user");
		
//		User userProfile = new User();
		
//		userProfile = CoreFacade.getInstance().getUserProfile(user);
		
		TextView textName = (TextView) findViewById(R.id.textView2);
		TextView textGender = (TextView) findViewById(R.id.textView4);
		TextView textInterests = (TextView) findViewById(R.id.textView6);
		image = (ImageView)findViewById(R.id.imageView1);
		
//		InputStream in = new ByteArrayInputStream(user.getPic());
//		BufferedImage bufImg = ImageIO.read(in);
//		ImageIcon icon = new ImageIcon(bufImg);
//		image.setImageBitmap(icon);

		Bitmap bitmap = BitmapFactory.decodeByteArray(user.getPic() , 0, user.getPic().length);
		 image.setImageBitmap(bitmap );
		 
		textName.setText(user.getName());
		if(user.getSex() == Sex.Male)
			textGender.setText("Male");
		else
			textGender.setText("Female");
		textInterests.setText(user.getInterests());
	}
	
	public void chatClick(View view){
		Intent intent = new Intent(this, Chat.class);
		intent.putExtra("user", user);
		startActivity(intent);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(groupId1, editProfileId, editProfileId, "Edit Profile" );
		
		return super.onCreateOptionsMenu(menu);
	}
	
	 public boolean onOptionsItemSelected(MenuItem item) {
		 
		 switch (item.getItemId()){
		 	
		 case 1 : Intent intent = new Intent(this, MyProfile.class);
		 		  startActivity(intent);
		 		  break;
		 
		 }
		 return super.onOptionsItemSelected(item);
	 }
}
