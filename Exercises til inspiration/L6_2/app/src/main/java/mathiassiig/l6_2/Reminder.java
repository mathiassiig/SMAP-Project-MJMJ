package mathiassiig.l6_2;

/**
 * Created by Mathias on 25-09-2016.
 */

public class Reminder
{
    public String Task;
    public String Place;
    public long ID;

    public Reminder(String t, String p, long id)
    {
        Task = t;
        Place = p;
        ID = id;
    }
    public Reminder(String t, String p)
    {
        Task = t;
        Place = p;
    }
}
