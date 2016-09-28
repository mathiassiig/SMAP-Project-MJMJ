package mathiassiig.assignment2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
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
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final WeatherInfo weather = getItem(position);

        /*
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_reminder, parent, false);
        TextView tvPlace = (TextView) convertView.findViewById(R.id.textViewPlace);
        TextView tvTask = (TextView) convertView.findViewById(R.id.textViewTask);

        tvPlace.setText(reminder.Place);
        tvTask.setText(reminder.Task);
        ImageView btnDelete = (ImageView) convertView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                reminders.remove(reminder);
                DatabaseHelper.getInstance(getContext()).DeleteReminder(reminder.ID);
                notifyDataSetChanged();
            }
        });
        */
        return convertView;
    }
}
