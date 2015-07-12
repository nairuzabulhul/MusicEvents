package nairuz.com.musicevents;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Prefs;
import data.CustomListViewAdapter;
import model.Event;


public class MainActivity extends ActionBarActivity {

    private CustomListViewAdapter adapter;
    private ArrayList<Event> events = new ArrayList<>();

    private String url= "http://ws.audioscrobbler.com/2.0/?method=geo.getevents&location=spokane&api_key=6adc3a5a72ddce8ef01e155239d7bbc6&format=json";  //change to weather API

    private String urlLeft = "http://ws.audioscrobbler.com/2.0/?method=geo.getevents&location=";
    private String urlRight = "&api_key=6adc3a5a72ddce8ef01e155239d7bbc6&format=json";



    private ListView listView;
    private TextView selectedCity;

    private ProgressDialog progressDialog ; //for the progress dialogue

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedCity = (TextView)findViewById(R.id.selectedLocationText);

        listView =(ListView) findViewById(R.id.listId);

        adapter = new CustomListViewAdapter(MainActivity.this, R.layout.list_row, events);

        listView.setAdapter(adapter);


        Prefs prefs = new Prefs(MainActivity.this);
        String city = prefs.getCity(" ");

        selectedCity.setText("Selected City: " + city);

        showEvent(city);

    }


    private void getEvents (String city){

        //clear data first
        events.clear();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        String urlFinal =  urlLeft +city +urlRight ;
        JsonObjectRequest eventRequest = new JsonObjectRequest(Request.Method.GET,
                urlFinal, (JSONObject) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Hide loading progress
                hideProgress();

                try {
                    JSONObject eventsObject = response.getJSONObject("events");
                    JSONArray eventsArray = eventsObject.getJSONArray("event");

                    for ( int i = 0 ; i < eventsArray.length(); i ++ ){
                        JSONObject jsonObject = eventsArray.getJSONObject(i);

                        //get the artist name
                        JSONObject artistObject = jsonObject.getJSONObject("artists");
                        String headLinerText = artistObject.getString("headliner");



                        //get the venue:
                        JSONObject venueObject = jsonObject.getJSONObject("venue");
                        String venueName = venueObject.getString("name");

                        //Location
                        JSONObject locationObject = venueObject.getJSONObject("location");
                        String city = locationObject.getString("city");
                        String country = locationObject.getString("country");
                        String street = locationObject.getString("street");
                        String postalCode = locationObject.getString("postalcode");


                        //get url Image:
                        JSONArray imageArray = jsonObject.getJSONArray("image");

                        //get image :
                        JSONObject largeImage = imageArray.getJSONObject(3);

                        //get Actual image
                        String image = largeImage.getString("#text");

                        //get the date
                        String startDate = jsonObject.getString("startDate");

                        //get a website :
                        String website = jsonObject.getString("website");

                        Event event = new Event();

                        event.setHeadLiner(headLinerText);
                        event.setCity(city);
                        event.setCountry(country);
                        event.setStreet(street);
                        event.setPostalCode(postalCode);
                        event.setStartDate(startDate);
                        event.setVenueName(venueName);
                        event.setUrl(image);
                        event.setWebsite(website);



                        // add all sub events to the main event (Arraylist)
                        events.add(event);



                        Log.v("HeadLiner:", headLinerText);
                    }

                    adapter.notifyDataSetChanged();

                   // Log.v("Data:", eventObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideProgress();

            }
        });

        AppController.getInstance().addToRequestQueue(eventRequest);

    }

//get




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.changing_location) {

            showInputDialog();

        }

        return super.onOptionsItemSelected(item);
    }



    private void showInputDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder (MainActivity.this);

        builder.setTitle("Change City");

        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Paris");
        builder.setView(cityInput);


        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Prefs cityPreferences = new Prefs(MainActivity.this);
                cityPreferences.setCity(cityInput.getText().toString());

                String newCity = cityPreferences.getCity("");

                selectedCity.setText("Selected City:" + newCity);

                showEvent(newCity);

            }
        });


        builder.show();
    }


    private void showEvent(String newCity) {

        getEvents(newCity);

    }


    private void hideProgress (){

        if ( progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
