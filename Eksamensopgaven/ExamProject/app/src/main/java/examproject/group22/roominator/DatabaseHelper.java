package examproject.group22.roominator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Maria Dam on 02-10-2016.
 */

//Code inspired from https://thebhwgroup.com/blog/how-android-sqlite-onupgrade

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;
    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "Gruppe22_ExamProject_Database.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERNFO = "userinfo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_IMAGE = "image";

    public static final String DATABASE_CREATE_USERINFO_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USERNFO + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " string, "
            + COLUMN_TOTAL + " double, "
            + COLUMN_IMAGE + " string)";


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

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.v("Debug", "creating database");
        db.execSQL(DATABASE_CREATE_USERINFO_TABLE);
    }

    public long AddUserInfo(UserInfo user)
    {
        ContentValues insertValues = new ContentValues();
        insertValues.put(COLUMN_NAME, user.userName);
        insertValues.put(COLUMN_TOTAL, user.totalPrice);
        insertValues.put(COLUMN_IMAGE, user.userImg);
        long primaryKey = database.insert(TABLE_USERNFO, null, insertValues);
        return primaryKey;
    }


    public void DeleteUserInfo(long user_id)
    {
        String where = COLUMN_ID + " =? ";
        database.delete(TABLE_USERNFO, where, new String[]{String.valueOf(user_id)});
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
