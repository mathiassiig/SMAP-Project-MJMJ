package mathiassiig.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mathias on 28-09-2016.
 */

//Code inspired from https://thebhwgroup.com/blog/how-android-sqlite-onupgrade
public class DatabaseHelper extends SQLiteOpenHelper
{
    //region Singleton shit
    private static DatabaseHelper sInstance;
    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if (sInstance == null)
            sInstance = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        return sInstance;
    }

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        Log.v("Debug", "DatabaseHelper instance retrieved");
        database = getWritableDatabase();
    }
    //endregion

    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "Gruppe22_Assignment2_Database.db";
    private static final int DATABASE_VERSION = 1;

    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    public static final String TABLE_WEATHERINFOS = "weatherinfos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String DATABASE_CREATE_REMINDERS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_WEATHERINFOS + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DESCRIPTION + " string, "
            + COLUMN_TEMPERATURE + " double, "
            + COLUMN_TIMESTAMP + " string)";


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.v("Debug", "creating database");
        db.execSQL(DATABASE_CREATE_REMINDERS_TABLE);
    }


    public long AddWeatherInfo(WeatherInfo w)
    {
        ContentValues insertValues = new ContentValues();
        insertValues.put(COLUMN_DESCRIPTION, w.description);
        insertValues.put(COLUMN_TEMPERATURE, w.temperature);
        insertValues.put(COLUMN_TIMESTAMP, w.timestamp.toString());
        long primaryKey = database.insert(TABLE_WEATHERINFOS, null, insertValues);
        return primaryKey;
    }



    public void DeleteWeatherInfo(long weather_id)
    {
        String where = COLUMN_ID + " =? ";
        database.delete(TABLE_WEATHERINFOS, where, new String[]{String.valueOf(weather_id)});
    }



    //http://stackoverflow.com/questions/10111166/get-all-rows-from-sqlite
    public ArrayList<WeatherInfo> GetAllWeatherInfos()
    {
        ArrayList<WeatherInfo> weatherInfos = new ArrayList<WeatherInfo>();
        try
        {
            String query = "SELECT * FROM " + TABLE_WEATHERINFOS;
            Cursor cursor = database.rawQuery(query, null);
            if(cursor.moveToFirst())
            {
                while(cursor.isAfterLast() == false)
                {
                    WeatherInfo weather = GetWeatherFromCursor(cursor);
                    weatherInfos.add(weather);
                    cursor.moveToNext();
                }
            }
        }
        catch(Exception ex)
        {
            Log.v("Debug", "Sum ting wen wong : " + ex.getMessage());
        }
        return weatherInfos;
    }



    private WeatherInfo GetWeatherFromCursor(Cursor cursor) throws ParseException {
        long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
        double temperature = cursor.getDouble(cursor.getColumnIndex(COLUMN_TEMPERATURE));
        String stamp = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));
        Date parsedDate = format.parse(stamp);
        java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());


        WeatherInfo weather = new WeatherInfo(description, temperature, timestamp);
        return weather;
    }



    public WeatherInfo GetWeatherInfo(long reminder_id)
    {
        WeatherInfo reminder = null;
        try
        {
            String query = "SELECT * FROM " + TABLE_WEATHERINFOS + " WHERE " + COLUMN_ID + " = " + reminder_id;
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            reminder = GetWeatherFromCursor(cursor);
        }
        catch(Exception ex)
        {
            Log.v("Debug", "Oh noes, SQL error! " + ex.getMessage());
        }

        return reminder;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}