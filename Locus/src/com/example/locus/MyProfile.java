package com.example.locus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.locus.entity.User;

public class MyProfile extends Activity {

	private RadioGroup radioGenderGroup;
	User currentUser;
	 private static int RESULT_LOAD_IMAGE = 1;
	
	 FileInputStream fstream;
	 BufferedInputStream bstream;
	 byte[] imageArray;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);
		//getWindow().getDecorView().setBackgroundColor(Color.BLACK);
		 Button buttonLoadImage = (Button) findViewById(R.id.imageButton);
	        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View arg0) {

	                Intent i = new Intent(
	                        Intent.ACTION_PICK,
	                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

	                startActivityForResult(i, RESULT_LOAD_IMAGE);
	            }
	        });
	    }

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
             
            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            
            //Convert image to byte array
            try{
            	 
            	File fileImageActual = new File(picturePath);
				//Size of file
            	imageArray = new byte[(int) fileImageActual.length()];

				//Read File & Write Image
				fstream = new FileInputStream(fileImageActual);
				bstream = new BufferedInputStream(fstream);
				bstream.read(imageArray, 0, imageArray.length);
				
				//currentUser.setPic(imageArray);
				
            	}catch(IOException e){
            		System.out.println(e.getMessage());
            	}	
                   
        }
     
     
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
         
        
		int selectedID = radioGenderGroup.getCheckedRadioButtonId();
        rB = (RadioButton) findViewById(selectedID);
        String gender = (String)rB.getText();
        
		EditText userName = (EditText) findViewById(R.id.editText1);
		String message = userName.getText().toString();
		
		EditText interestText = (EditText) findViewById(R.id.editText4);
		String interests = interestText.getText().toString();
		
		String ipAdd = IPAddress.getIPAddress(true);

		EditText lat = (EditText) findViewById(R.id.editText2);
		EditText lon = (EditText) findViewById(R.id.editText3);
		
		

		if(isEmpty(userName))
			Toast.makeText(this,"Enter UserName" , Toast.LENGTH_LONG).show();
		else if ( isEmpty(interestText))
			Toast.makeText(this,"Enter Interests" , Toast.LENGTH_LONG).show();
		else if(imageArray == null)
			Toast.makeText(this, "Select a Picture", Toast.LENGTH_LONG).show();
		else{
			if( isEmpty(lat) || isEmpty(lon)){
				Intent intent = new Intent(this, ListUsers.class);
				intent.putExtra("userName", message);
				intent.putExtra("sex", gender);	
				intent.putExtra("IP", ipAdd);
				intent.putExtra("interests", interests);
				intent.putExtra("pic", imageArray);
				startActivity(intent);
			}
			else{
				Intent intent = new Intent(this, Demo.class);
				String longi = lon.getText().toString();
				String lati = lat.getText().toString();
				intent.putExtra("userName", message);
				intent.putExtra("sex", gender);
				intent.putExtra("IP", ipAdd);
				intent.putExtra("interests", interests);
				intent.putExtra("latitude", lati);
				intent.putExtra("longitude", longi);
				intent.putExtra("pic", imageArray);
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
