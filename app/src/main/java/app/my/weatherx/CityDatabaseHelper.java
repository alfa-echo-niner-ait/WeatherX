package app.my.weatherx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CityDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "city_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "city_table";
    private static final String COLUMN_NAME = "city_name";

    public CityDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " TEXT PRIMARY KEY)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }

    // Add a method to insert a city name
    public void insertCity(String cityName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + "=?", new String[]{cityName});
        if (cursor.getCount() > 0) {
            // City already exists (ignore)
        }
        else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, cityName);
            db.insert(TABLE_NAME, null, values);
        }
        cursor.close();
        db.close();
    }

    // Add a method to retrieve all city names
    public List<String> getAllCities() {
        List<String> cityList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                String cityName = cursor.getString(0);
                cityList.add(cityName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cityList;
    }
}
