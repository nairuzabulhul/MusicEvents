package data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import model.Event;
import nairuz.com.musicevents.ActivityEventDetails;
import nairuz.com.musicevents.AppController;
import nairuz.com.musicevents.R;

/**
 * Created by nairuz on 0007, July, 7, 2015.
 */
public class CustomListViewAdapter extends ArrayAdapter <Event> {

    private LayoutInflater inflater;
    private Activity mConetxt;
    private ArrayList <Event> data;
    private int LayoutResourceId;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListViewAdapter(Activity context, int resource, ArrayList<Event> objs) {
        super(context, resource, objs);
        data = objs;
        mConetxt = context;
        LayoutResourceId = resource;
    }

    //Overridden methods


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Event getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(Event item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row = convertView;
        ViewHolder viewHolder = null;

        if ( row == null) {
            inflater = LayoutInflater.from(mConetxt); // the layout inflates from the activity
            row = inflater.inflate(LayoutResourceId,parent,false);


            //new view Holder
            viewHolder = new ViewHolder();

            viewHolder.bandImage = (NetworkImageView) row.findViewById(R.id.bandImage);
            viewHolder.headLiner = (TextView) row.findViewById(R.id.headlinerText);
            viewHolder.veneu =(TextView) row.findViewById(R.id.venueText);
            viewHolder.when = (TextView) row.findViewById(R.id.whenText);
            viewHolder.where =(TextView) row.findViewById(R.id.whereText);


            row.setTag(viewHolder); //set the tag for the information
        }else{
            viewHolder =(ViewHolder) row.getTag();
        }

        //take the current position of event and pass it to data
        viewHolder.event = data.get(position);

        //display the data
        viewHolder.headLiner.setText("Headliner: " + viewHolder.event.getHeadLiner());
        viewHolder.veneu.setText("Venue: " + viewHolder.event.getVenueName());
        viewHolder.when.setText("When: " + viewHolder.event.getStartDate());
        viewHolder.where.setText("Where: " + viewHolder.event.getStreet() + ","+
         viewHolder.event.getCity()+ " ," + viewHolder.event.getCountry());

        // for the image
        viewHolder.bandImage.setImageUrl(viewHolder.event.getUrl(), imageLoader);
        viewHolder.website = viewHolder.event.getWebsite();


        Log.v("VENUE",viewHolder.event.getVenueName());


        final ViewHolder finalViewHolder = viewHolder;
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mConetxt, ActivityEventDetails.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("eventObj", finalViewHolder.event);
                i.putExtras(mBundle);
                mConetxt.startActivity(i);



            }
        });



        return row;
    }


    public class ViewHolder{

        Event event;
        NetworkImageView bandImage;
        TextView headLiner;
        TextView veneu;
        TextView where;
        TextView when;
        String website;



    }
}
