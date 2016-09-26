package mathiassiig.l6_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mathias on 25-09-2016.
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
    private static final String DATABASE_NAME = "L6_2_Database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String COLUMN_ID = "_id";

    public static final String TABLE_REMINDERS = "reminders";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_PLACE = "place";

    public static final String DATABASE_CREATE_REMINDERS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_REMINDERS + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TASK + " string, "
            + COLUMN_PLACE + " string)";


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.v("Debug", "creating database");
        db.execSQL(DATABASE_CREATE_REMINDERS_TABLE);
    }

    public long AddReminder(Reminder r)
    {
        ContentValues insertValues = new ContentValues();
        insertValues.put(COLUMN_TASK, r.Task);
        insertValues.put(COLUMN_PLACE, r.Place);
        long primaryKey = database.insert(TABLE_REMINDERS, null, insertValues);
        return primaryKey;
    }

    public void DeleteReminder(long reminder_id)
    {
        String where = COLUMN_ID + " =? ";
        database.delete(TABLE_REMINDERS, where, new String[]{String.valueOf(reminder_id)});
    }

    //http://stackoverflow.com/questions/10111166/get-all-rows-from-sqlite
    public ArrayList<Reminder> GetAllReminders()
    {
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();
        try
        {
            String query = "SELECT * FROM " + TABLE_REMINDERS;
            Cursor cursor = database.rawQuery(query, null);
            if(cursor.moveToFirst())
            {
                while(cursor.isAfterLast() == false)
                {
                    Reminder reminder = GetReminderFromCursor(cursor);
                    reminders.add(reminder);
                    cursor.moveToNext();
                }
            }
        }
        catch(Exception ex)
        {
            Log.v("Debug", "Ey you fuck'd up somehow : " + ex.getMessage());
        }
        return reminders;
    }

    private Reminder GetReminderFromCursor(Cursor cursor)
    {
        long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        String task = cursor.getString(cursor.getColumnIndex(COLUMN_TASK));
        String place = cursor.getString(cursor.getColumnIndex(COLUMN_PLACE));
        Reminder reminder = new Reminder(task, place, id);
        return reminder;
    }

    public Reminder GetReminder(long reminder_id)
    {
        Reminder reminder = null;
        try
        {
            String query = "SELECT * FROM " + TABLE_REMINDERS + " WHERE " + COLUMN_ID + " = " + reminder_id;
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            reminder = GetReminderFromCursor(cursor);
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
