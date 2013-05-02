package com.example.locus;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.locus.core.CoreFacade;
import com.example.locus.entity.Message;
import com.example.locus.entity.User;

public class ChatAdapter extends ArrayAdapter<Message> {

	Context context; 
    int layoutResourceId;    
    List<Message> data;
    
    static class ChatHolder
    {
        TextView NameView;
        TextView StringView;
        ImageView ImageView;
    }
    
    public ChatAdapter(Context context, int layoutResourceId, List<Message> data){
    	 super(context, layoutResourceId, data);
         this.layoutResourceId = layoutResourceId;
         this.context = context;
         this.data = data;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ChatHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ChatHolder();
            holder.NameView = (TextView)row.findViewById(R.id.textView1);
            holder.StringView = (TextView)row.findViewById(R.id.chatTextView);
            holder.ImageView = (ImageView)row.findViewById(R.id.imageView1);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ChatHolder)row.getTag();
        }
        
        Message msg = data.get(position);
        User user = CoreFacade.getInstance().getCurrentUser();
        
        if(msg.getSrc().getName().equals(user.getName())){
        	holder.ImageView.setLayoutParams(new LinearLayout.LayoutParams(holder.ImageView.getWidth(), holder.ImageView.getHeight(), Gravity.LEFT));
        	holder.StringView.setGravity(Gravity.LEFT);
        	holder.NameView.setGravity(Gravity.LEFT);
        	holder.NameView.setTextColor(Color.GREEN);
        	if (user.getPic() != null) {
    			Bitmap bitmap = BitmapFactory.decodeByteArray(user.getPic(), 0,
    					user.getPic().length);
    			holder.ImageView.setImageBitmap(bitmap);
    		}
        	
        	holder.StringView.setText((String) msg.getData());
            holder.NameView.setText((String) msg.getSrc().getName());
        	
        }else{
        	holder.ImageView.setLayoutParams(new LinearLayout.LayoutParams(holder.ImageView.getWidth(), holder.ImageView.getHeight(), Gravity.RIGHT));
        	holder.StringView.setGravity(Gravity.RIGHT);
        	
        	holder.NameView.setGravity(Gravity.RIGHT);
        	holder.NameView.setTextColor(Color.BLUE);
        	holder.StringView.setText((String) msg.getData());
        	if (msg.getSrc().getPic() != null) {
    			Bitmap bitmap = BitmapFactory.decodeByteArray(msg.getSrc().getPic(), 0,
    					msg.getSrc().getPic().length);
    			holder.ImageView.setImageBitmap(bitmap);
    		}
        	
            holder.NameView.setText((String) msg.getSrc().getName());
        }
       
//        if(user.getSex() == Sex.Female)
//        	holder.ImageView.setImageResource(R.drawable.femaleicon);
//        else
//        	holder.ImageView.setImageResource(R.drawable.maleicon);
//        
        return row;
    }
    

}
