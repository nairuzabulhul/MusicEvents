package nairuz.com.musicevents;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import model.Event;


public class ActivityEventDetails extends ActionBarActivity {

    private Event event;
    private TextView venue;
    private TextView where;
    private TextView when;
    private TextView headline;
    private NetworkImageView image;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_event_details);

        event = (Event) getIntent().getSerializableExtra("eventObj");

        venue = (TextView) findViewById(R.id.detsVenue);
        where = (TextView) findViewById(R.id.detsWhere);
        when = (TextView) findViewById(R.id.detsWhen);
        headline = (TextView) findViewById(R.id.detsHeadLines);
        image = (NetworkImageView) findViewById(R.id.detsBandImage);


        venue.setText("Venue" + event.getVenueName());
        headline.setText("Head Line" + event.getHeadLiner());
        image.setImageUrl(event.getUrl(), imageLoader);
        where.setText("Where" + event.getStreet() + "," + event.getCity() + "," + event.getCountry());
        when.setText("When" + event.getStartDate());




        Log.v("serial", event.getVenueName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.website_ID) {

            String url = event.getWebsite();

            if (!url.equals("")){

                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }else {

                Toast.makeText(getApplicationContext(),"No Website",Toast.LENGTH_LONG ).show();

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
