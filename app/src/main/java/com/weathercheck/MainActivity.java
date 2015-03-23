package com.weathercheck;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Class will contact forecast.io api and get a JSON file as a response.
 * Will have a CurrentWeather class as a member to organize the data from
 * the JSON file.
 *
 * Class includes function to test for Network Connection and handles errors
 * with an AlertDialogFragment
 *
 * This is the Launcher Activity
 *
 */
public class MainActivity extends Activity
{

    private static final String TAG = MainActivity.class.getSimpleName();

    // Class that stores weather data.
    private CurrentWeather cw;

    // See 'ButterKnife' API Documentation
    // add "compile 'com.jakewharton:butterknife:6.1.0'" to build.gradle under dependencies
    // Same as Object 'objectname' = (Object)findViewById(R.id.'id_of_object');

    @InjectView(R.id.time_label)
    TextView time_label;
    @InjectView(R.id.temp_label)
    TextView temp_label;
    @InjectView(R.id.humid_value)
    TextView humid_value;
    @InjectView(R.id.precip_value)
    TextView precip_value;
    @InjectView(R.id.sum_label)
    TextView sum_label;
    @InjectView(R.id.weather_icon)
    ImageView weather_icon;
    @InjectView(R.id.refresh_button)
    ImageView refresh;

    /**
     * This method is run when the activity is created. This will be where the
     * majority of the code to determine the behavior of the activity is written.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // See 'ButterKnife' code above.
        ButterKnife.inject(this);

        // Handles the Http request to forecast.io see below
        getForecast();

        /**
         * setOnClickListener is used to determine the behavior of an object that is
         * clicked. It 'Listens' for a button press and runs the code below.
         *
         * refresh is a button near the top of the screen that simply gets the forecast again.
         *
         */
        refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getForecast();

            }
        });
    }

    /**
     * This function contacts forecast.io using the OKHttp API
     * The response from forecast.io returns a JSON object with weather data.
     *
     * The weather data is passed in to a CurrentWeather model object that does
     * some processing and stores the current weather data.
     *
     * Function also handles if network is down or response from forecast.io fails.
     *
     * NOTE: you must add <uses-permission android:name="android.permission.INTERNET"/> to
     * AndroidManifest.xml in order for this to work.
     *
     */
    private void getForecast()
    {
        String forecast_url =
                "https://api.forecast.io/forecast/da85ad635feaa319181333e8b56621f4/39.2555,-76.7113";

        // See function defined below for networkAvailable()
        if (networkAvailable())
        {
            // See 'OkHttp' API documentation
            // add "compile 'com.squareup.okhttp:okhttp:2.2.0'" to build.gradle
            // API creates an http request and call to a url.
            OkHttpClient client = new OkHttpClient();

            /*
                Create a request using the forecast_url defined above, methods are chained together
                for convenience.
            */
            Request req = new Request
                    .Builder()
                    .url(forecast_url)
                    .build();

            /*
                The call sends the request to forecast.io and handles the response with a Callback
                This is very similar to using AJAX for web development.
             */
            Call call = client.newCall(req);

            /*
                Call is enqueued so that it does not interrupt the main UI thread which handles
                User input and activity. Callback() handles what happens when the response is received
            */
            call.enqueue(new Callback()
            {
                // Log a message if the response does not come.
                public void onFailure(Request r, IOException e)
                {
                    Log.e(TAG, e.getMessage());
                }

                /**
                 * If the JSON body is successfully accessed, get the current data
                 * and pass it into the CurrentWeather object.
                 *
                 * @param r as Response
                 * @throws IOException
                 */
                public void onResponse(Response r) throws IOException
                {
                    {
                        try
                        {
                            // Get the response as a String that will be formatted into a JSON object
                            String jsonData = r.body().string();
                            Log.v(TAG, jsonData + "=>");

                            if (r.isSuccessful())
                            {
                                // See 'CurrentWeather' Documentation
                                cw = getCurrentDetails(jsonData);

                                // Creates a thread that updates the weather information, a Thread is just
                                // a background process that runs at the same time as the main process.
                                runOnUiThread(new Runnable()
                                {

                                    // Runs the thread, this particular function updates the TextViews
                                    // and such that display the weather data.
                                    @Override
                                    public void run()
                                    {
                                        updateDisplay();

                                    }
                                });
                            }
                            else
                            //  Uses a Dialog Fragment to notify user of an Error.
                                alertUserAboutError();

                        } catch (IOException e)
                        {
                            Log.e("REQUEST ERROR", e.getMessage());

                        } catch (JSONException j)
                        { Log.e("JSON Error", j.getMessage());}
                    }
                }
            });
        }
        else
        {
            // Toast is just a simple message that pops up and disappears quickly.
            Toast.makeText
                    (
                            this,
                            "Network Unavailable",
                            Toast.LENGTH_LONG
                    ).show();
        }
    }

    // Simple function that puts out the information from the CurrentWeather Object
    private void updateDisplay()
    {
        temp_label.setText(cw.getTemp() + "");
        time_label.setText("@ " + cw.formatTime());
        humid_value.setText(cw.getHumidity() + "");
        precip_value.setText(cw.getPrecipChance() + "%");
        sum_label.setText(cw.getSummary());

        Drawable draw = getResources().getDrawable(cw.getIconId());

        weather_icon.setImageDrawable(draw);

    }

    /**
     * Function creates a new JSONObject from the jsonData string response from
     * forecast.io API. The current weather data is broken down further from within
     * the main JSONObject.
     *
     * JSON data is accessed by providing the 'key' in the key-value pair
     * ex. forecast.getString("timezone") will retrieve the value of "eastern" from
     * the Json pair ("timezone" => "eastern")
     *
     *
     * @param jsonData as String
     * @return cw as Current Weather
     * @throws JSONException
     */
    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException
    {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);
        JSONObject currently = forecast.getJSONObject("currently");

        Log.i(TAG, currently.getString("summary"));

        CurrentWeather cw = new CurrentWeather();

        cw.setHumidity(currently.getDouble("humidity"));
        cw.setTime(currently.getLong("time"));
        cw.setIcon(currently.getString("icon"));
        cw.setPrecipChance(currently.getDouble("precipProbability"));
        cw.setSummary(currently.getString("summary"));
        Log.i(TAG, cw.getSummary());
        cw.setTemp(currently.getDouble("temperature"));
        Log.i(TAG, cw.getTemp() + "=> TEMPERATURE");

        cw.setTimeZone(forecast.getString("timezone"));

        return cw;
    }

    /**
     *  Uses ConnectivityManager to determine if the the device is connected to a network.
     *  NOTE: you must add <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/> to
     *  the AndroidManifest.xml file in order to use this.
     *
     *  This code can be more or less copied into most projects and maintain functionality.
     *
     * @return available as Boolean
     */
    private boolean networkAvailable()
    {
        // Must cast Connectivity Service to Connectivity Manager
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean available = false;

        // Test to see if the network is null (no service/wifi off) or if the network cannot connect (airplane mode)
        if (networkInfo != null && networkInfo.isConnected())
        {
            available = true;
        }
        return available;
    }

    // Method raises an error dialog using custom class AlertDialogFragment
    private void alertUserAboutError()
    {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}