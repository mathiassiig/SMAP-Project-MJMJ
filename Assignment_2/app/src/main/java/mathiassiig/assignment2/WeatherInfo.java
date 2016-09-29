package mathiassiig.assignment2;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Mathias on 28-09-2016.
 */

public class WeatherInfo implements Serializable
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
