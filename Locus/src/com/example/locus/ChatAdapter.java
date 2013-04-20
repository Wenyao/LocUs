package com.example.locus;

import java.util.List;

import com.example.locus.AdapterList.ListDetailsHolder;
import com.example.locus.entity.Sex;
import com.example.locus.entity.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends ArrayAdapter<String> {

	Context context; 
    int layoutResourceId;    
    List<String> data;
    
    static class ChatHolder
    {
        ImageView ImageView;
        TextView StringView;
    }
    
    public ChatAdapter(Context context, int layoutResourceId, List<String> data){
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
            holder.ImageView = (ImageView)row.findViewById(R.id.chatImageView);
            holder.StringView = (TextView)row.findViewById(R.id.chatTextView);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ChatHolder)row.getTag();
        }
        
        String msg = data.get(position);
        holder.StringView.setText(msg);
//        if(user.getSex() == Sex.Female)
//        	holder.ImageView.setImageResource(R.drawable.femaleicon);
//        else
//        	holder.ImageView.setImageResource(R.drawable.maleicon);
//        
        return row;
    }
    

}
