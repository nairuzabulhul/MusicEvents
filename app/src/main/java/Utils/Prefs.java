package Utils;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by nairuz on 0007, July, 7, 2015.
 */
public class Prefs {

    // This class always the user to change the city to any city in the world
    SharedPreferences sharedPreferences;

    public Prefs(Activity activity) {

        sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }


    //method for changing the city
    public void setCity(String city) {

        sharedPreferences.edit().putString("city", city).commit();
    }

    //if the user did not choose a city , return default;
    public String getCity (String city){

            return sharedPreferences.getString("city","Paris");

    }

}
