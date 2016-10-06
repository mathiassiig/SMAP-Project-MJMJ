package examproject.group22.roominator;
import android.content.Context;


public class DatabaseHelper {

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if (sInstance == null)
            sInstance = new DatabaseHelper();
        return sInstance;
    }

    private DatabaseHelper()
    {

    }

}
