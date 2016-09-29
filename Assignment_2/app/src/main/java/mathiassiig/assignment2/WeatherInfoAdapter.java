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

    //// TODO: 28-09-2016 IMPLEMENT
    private String getDate(Timestamp timeStamp)
    {
        return "";
    }

    //// TODO: 28-09-2016 IMPLEMENT 
    private String getTime(Timestamp timeStamp)
    {
        return "";
    }

    private int GetWeatherIcon(String weather)
    {
        switch(weather)
        {
            case "Cloudy":
                return R.drawable.cloudy;
            case "Rainy":
                return R.drawable.cloudy;
            case "Clear":
                return R.drawable.cloudy;
            default:
                break;
        }
        return R.drawable.cloudy;
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

        tvDescription.setText(weather.description);
        String date = getDate(weather.timestamp);
        String time = getDate(weather.timestamp);
        tvDate.setText(date);
        tvTime.setText(time);
        tvTemperature.setText(Double.toString(weather.temperature));

        ImageView imgWeather = (ImageView) convertView.findViewById(R.id.imgWeather);
        imgWeather.setImageResource(GetWeatherIcon(weather.description));
        return convertView;
    }
}
