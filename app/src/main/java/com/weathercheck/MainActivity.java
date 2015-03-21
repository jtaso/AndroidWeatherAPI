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

public class MainActivity extends Activity
{

    private static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather cw;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        getForecast();

        refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getForecast();

            }
        });
    }

    private void getForecast()
    {
        String forecast_url =
                "https://api.forecast.io/forecast/da85ad635feaa319181333e8b56621f4/39.2555,-76.7113";

        if (networkAvailable())
        {
            OkHttpClient client = new OkHttpClient();

            Request req = new Request
                    .Builder()
                    .url(forecast_url)
                    .build();

            Call call = client.newCall(req);
            call.enqueue(new Callback()
            {
                public void onFailure(Request r, IOException e)
                {
                    Log.e(TAG, e.getMessage());
                }

                public void onResponse(Response r) throws IOException
                {
                    {
                        try
                        {
                            String jsonData = r.body().string();
                            Log.v(TAG, jsonData + "=>");

                            if (r.isSuccessful())
                            {
                                cw = getCurrentDetails(jsonData);
                                runOnUiThread(new Runnable()
                                {

                                    @Override
                                    public void run()
                                    {
                                        updateDisplay();

                                    }
                                });
                            }
                            else
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
            Toast.makeText
                    (
                            this,
                            "Network Unavailable",
                            Toast.LENGTH_LONG
                    ).show();
        }
    }

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

    private boolean networkAvailable()
    {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean available = false;

        if (networkInfo != null && networkInfo.isConnected())
        {
            available = true;
        }
        return available;
    }

    private void alertUserAboutError()
    {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}