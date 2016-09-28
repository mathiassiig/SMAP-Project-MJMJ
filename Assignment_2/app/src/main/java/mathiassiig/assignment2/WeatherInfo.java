package mathiassiig.assignment2;

import java.security.Timestamp;

/**
 * Created by Mathias on 28-09-2016.
 */

public class WeatherInfo
{
    public long id;
    public String description;
    public float temperature;
    public String timestamp;

    public WeatherInfo(String d, float temp, String time)
    {
        description = d;
        temperature = temp;
        timestamp = time;
    }

}
