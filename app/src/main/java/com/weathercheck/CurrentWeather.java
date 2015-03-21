package com.weathercheck;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * MODEL OBJECT
 */
public class CurrentWeather
{

    private String icon;
    private long time;
    private double temp;
    private double humidity;
    private double precipChance;
    private String summary;
    private String timeZone;

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public int getIconId()
    {
        int iconId;

        switch(icon)
        {
            case("clear-day"):
                iconId = R.drawable.clear_day;
                break;
            case("clear-night"):
                iconId = R.drawable.clear_night;
                break;
            case("rain"):
                iconId = R.drawable.rain;
                break;
            case("snow"):
                iconId = R.drawable.snow;
                break;
            case("sleet"):
                iconId = R.drawable.sleet;
                break;
            case("fog"):
                iconId = R.drawable.fog;
                break;
            case("cloudy"):
                iconId = R.drawable.cloudy;
                break;
            case("partly-cloudy-day"):
                iconId = R.drawable.partly_cloudy;
                break;
            case("partly-cloudy-night"):
                iconId = R.drawable.cloudy_night;
                break;
            default:
                iconId = R.drawable.clear_day;
        }

        return iconId;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public int getTemp()
    {
        return Math.round((int) temp);
    }

    public void setTemp(double temp)
    {
        this.temp = temp;
    }

    public double getHumidity()
    {
        return humidity;
    }

    public void setHumidity(double humidity)
    {
        this.humidity = humidity;
    }

    public int getPrecipChance()
    {
        return Math.round((int) precipChance * 100);
    }

    public void setPrecipChance(double precipChance)
    {
        this.precipChance = precipChance;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getTimeZone()                 { return timeZone;}
    public void setTimeZone(String timezone)    {this.timeZone = timezone;}

    public String formatTime()
    {
        SimpleDateFormat form = new SimpleDateFormat("h:mm a");
        form.setTimeZone(TimeZone.getTimeZone(getTimeZone()));

        Date dt = new Date(getTime() * 1000);
        String date = form.format(dt);

        return date;
    }


}
