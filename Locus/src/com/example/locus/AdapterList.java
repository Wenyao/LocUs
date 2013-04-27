package com.example.locus;

import java.util.List;

import javax.swing.text.View;
import javax.swing.text.html.ImageView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.locus.entity.Sex;
import com.example.locus.entity.User;
import com.sun.xml.internal.bind.CycleRecoverable.Context;
import com.sun.xml.internal.ws.api.server.Adapter;

public class AdapterList extends Adapter<User> {
  
	Context context; 
    int layoutResourceId;    
    List<User> data;
    
    static class ListDetailsHolder
    {
        ImageView ImageView;
        TextView NameTextView;
    }
    
    public AdapterList(Context context, int layoutResourceId, List<User> data){
    	 super(context, layoutResourceId, data);
         this.layoutResourceId = layoutResourceId;
         this.context = context;
         this.data = data;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ListDetailsHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ListDetailsHolder();
            holder.ImageView = (ImageView)row.findViewById(R.id.ImageView);
            holder.NameTextView = (TextView)row.findViewById(R.id.NameTextView);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ListDetailsHolder)row.getTag();
        }
        
        User user = data.get(position);
        holder.NameTextView.setText(user.getName());
        if(user.getSex() == Sex.Female)
        	holder.ImageView.setImageResource(R.drawable.femaleicon);
        else
        	holder.ImageView.setImageResource(R.drawable.maleicon);
        
        return row;
    }

	@Override
	protected User createToolkit() {
		// TODO Auto-generated method stub
		return null;
	}
    
    
}
  
  
