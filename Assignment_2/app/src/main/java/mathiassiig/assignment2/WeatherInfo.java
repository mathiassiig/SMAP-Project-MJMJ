package mathiassiig.assignment2;

import java.sql.Timestamp;

/**
 * Created by Mathias on 28-09-2016.
 */

public class WeatherInfo
{
    public long id;
    public String description;
    public double temperature;
    public Timestamp timestamp;

    public WeatherInfo(String d, double temp, Timestamp time)
    {
        description = d;
        temperature = temp;
        timestamp = time;
    }

}
