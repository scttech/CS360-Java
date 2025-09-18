package com.example.app_poll.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.app_poll.classes.ClassItem;

import java.util.ArrayList;
import java.util.List;

public class AppDatabaseHelper extends SQLiteOpenHelper {
    private final Context appContext;

    private static final String DATABASE_NAME = "app_popularity.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String T_APPS = "apps";
    private static final String T_CLASSES = "classes";
    private static final String T_VOTES = "votes";

    // Common columns
    private static final String C_ID = "id";

    // Apps columns
    private static final String C_APP_NAME = "name";
    private static final String C_APP_URL = "url";
    private static final String C_APP_DESC = "description";

    // Classes columns
    private static final String C_CLASS_NAME = "name";
    private static final String C_CLASS_SECTION = "section";
    private static final String C_CLASS_SEMESTER = "semester";
    private static final String C_CLASS_YEAR = "year";

    // Votes columns
    private static final String C_VOTE_APP_ID = "app_id";
    private static final String C_VOTE_CLASS_ID = "class_id";

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.appContext = context.getApplicationContext();
    }

    /**
     * Inserts a new app into the database.
     * Returns the row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long insertApp(String name, String url, String description) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(C_APP_NAME, name);
            values.put(C_APP_URL, url);
            values.put(C_APP_DESC, description);
            return db.insert(T_APPS, null, values);
        }
    }

    /**
     * Inserts a new class into the database.
     * Returns the row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long insertClass(String name, String section, String semester, String year) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(C_CLASS_NAME, name);
            values.put(C_CLASS_SECTION, section);
            values.put(C_CLASS_SEMESTER, semester);
            values.put(C_CLASS_YEAR, year);
            return db.insert(T_CLASSES, null, values);
        }
    }

    public List<ClassItem> getAllClasses() {
        List<ClassItem> classes = new ArrayList<>();
        String sql = "SELECT " + C_ID + ", " + C_CLASS_NAME + ", " + C_CLASS_SECTION + ", " + C_CLASS_SEMESTER + ", " + C_CLASS_YEAR +
                     " FROM " + T_CLASSES +
                     " ORDER BY " + C_CLASS_NAME + " ASC";
        try (SQLiteDatabase db = getReadableDatabase();
             Cursor c = db.rawQuery(sql, null)) {
            while (c.moveToNext()) {
                classes.add(new ClassItem(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4)
                ));
            }
        }
        return classes;
    }

    /**
     * Returns top apps by vote count (descending), limited to 'limit'.
     */
    public List<AppVotes> getTopAppsByVotes(int limit) {
        List<AppVotes> result = new ArrayList<>();
        String sql =
                "SELECT a.name, COUNT(v.app_id) AS votes " +
                        "FROM " + T_APPS + " a " +
                        "LEFT JOIN " + T_VOTES + " v ON v." + C_VOTE_APP_ID + " = a." + C_ID + " " +
                        "GROUP BY a." + C_ID + ", a." + C_APP_NAME + " " +
                        "ORDER BY votes DESC, a." + C_APP_NAME + " ASC " +
                        "LIMIT ?";

        String[] args = new String[] { String.valueOf(Math.max(0, limit)) };

        try (SQLiteDatabase db = getReadableDatabase();
             Cursor c = db.rawQuery(sql, args)) {
            while (c.moveToNext()) {
                result.add(new AppVotes(c.getString(0), c.getInt(1)));
            }
        }
        return result;
    }

    /**
     * Inserts a vote for the given appId and classId.
     * Returns the row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long insertVote(long appId, long classId) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(C_VOTE_APP_ID, appId);
            values.put(C_VOTE_CLASS_ID, classId);
            return db.insert(T_VOTES, null, values);
        }
    }

    /**
     * Creates the database tables.
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + T_APPS + " (" +
                        C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        C_APP_NAME + " TEXT NOT NULL," +
                        C_APP_URL + " TEXT," +
                        C_APP_DESC + " TEXT" +
                        ");"
        );
        db.execSQL(
                "CREATE TABLE " + T_CLASSES + " (" +
                        C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        C_CLASS_NAME + " TEXT NOT NULL," +
                        C_CLASS_SECTION + " TEXT NOT NULL," +
                        C_CLASS_SEMESTER + " TEXT NOT NULL," +
                        C_CLASS_YEAR + " TEXT NOT NULL" +
                        ");"
        );
        db.execSQL(
                "CREATE TABLE " + T_VOTES + " (" +
                        C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        C_VOTE_APP_ID + " INTEGER NOT NULL," +
                        C_VOTE_CLASS_ID + " INTEGER NOT NULL," +
                        "FOREIGN KEY(" + C_VOTE_APP_ID + ") REFERENCES " + T_APPS + "(" + C_ID + ")," +
                        "FOREIGN KEY(" + C_VOTE_CLASS_ID + ") REFERENCES " + T_CLASSES + "(" + C_ID + ")" +
                        ");"
        );
    }

    /**
     * Upgrades the database by dropping existing tables and recreating them.
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_VOTES);
        db.execSQL("DROP TABLE IF EXISTS " + T_APPS);
        db.execSQL("DROP TABLE IF EXISTS " + T_CLASSES);
        onCreate(db);
    }

    /**
     * Adds sample data to the database if it is empty.
     */
    public void addSampleData() {
        if(!isEmpty()) {
            return;
        }
        new CsvSampleDataLoader(appContext, this).populateData();
    }

    /**
     * Checks if the database is empty
     * @param db The database.
     * @return True if the database is empty, false otherwise.
     */
    private boolean isEmpty(SQLiteDatabase db) {
        return DatabaseUtils.queryNumEntries(db, T_APPS) == 0L
                && DatabaseUtils.queryNumEntries(db, T_CLASSES) == 0L;
    }

    public boolean isEmpty() {
        try (SQLiteDatabase db = getReadableDatabase()) {
            return isEmpty(db);
        }
    }
}
