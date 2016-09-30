package mathiassiig.assignment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Mathias on 28-09-2016.
 */

//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
//http://stackoverflow.com/questions/17525886/listview-with-add-and-delete-buttons-in-each-row-in-android
public class WeatherInfoAdapter extends ArrayAdapter<WeatherInfo>
{
    ArrayList<WeatherInfo> weatherInfos;
    public WeatherInfoAdapter(Context context, ArrayList<WeatherInfo> weatherInfos)
    {
        super(context, 0, weatherInfos);
        this.weatherInfos = weatherInfos;
    }

    public static String getDate(Timestamp timeStamp)
    {
        Date date = new Date(timeStamp.getTime());
        return date.toString();
    }


    public static String getTime(Timestamp timeStamp)
    {
        Time time = new Time(timeStamp.getTime());
        return time.toString();
    }

    public static int GetWeatherIcon(String weather)
    {
        switch(weather)
        {
            case "Clouds":
                return R.drawable.cloudy;
            case "Rain":
                return R.drawable.rainy;
            case "Clear":
                return R.drawable.sunny;
            default:
                break;
        }
        return R.drawable.cloudy;
    }

    public static int GetWeatherText(String weather)
    {
        switch(weather)
        {
            case "Clouds":
                return R.string.weather_clouds;
            case "Rain":
                return R.string.weather_rain;
            case "Clear":
                return R.string.weather_clear;
            default:
                return -1;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final WeatherInfo weather = getItem(position);


        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_weatherinfo, parent, false);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.txtDescription);
        TextView tvDate = (TextView) convertView.findViewById(R.id.txtDate);
        TextView tvTemperature = (TextView) convertView.findViewById(R.id.txtTemperature);
        TextView tvTime = (TextView) convertView.findViewById(R.id.txtTime);
        int localizedName = GetWeatherText(weather.description);
        if(localizedName != -1)
            tvDescription.setText(localizedName);
        else
            tvDescription.setText(weather.description);
        String date = getDate(weather.timestamp);
        String time = getTime(weather.timestamp);
        tvDate.setText(date);
        tvTime.setText(time);
        tvTemperature.setText(Double.toString(Math.round(weather.temperature)) + " C");

        ImageView imgWeather = (ImageView) convertView.findViewById(R.id.imgWeather);
        imgWeather.setImageResource(GetWeatherIcon(weather.description));
        return convertView;
    }
}
