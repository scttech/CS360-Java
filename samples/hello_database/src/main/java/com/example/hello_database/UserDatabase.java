package com.example.hello_database;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * UserDatabase class to manage user data in SQLite database.
 */
public class UserDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_database.db";
    private static final int DATABASE_VERSION = 1;

    public UserDatabase(android.content.Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final class UserTable {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AGE = "age";
    }

    /**
     * Adds sample data to the database. This method first clears any existing data
     * and resets the autoincrement counter to ensure consistent IDs for the sample data.
     */
    public void addSampleData() {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        // Delete all existing rows
        db.delete(UserTable.TABLE_NAME, null, null);

        // Reset autoincrement counter, so that the IDs start from 1 again
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + UserTable.TABLE_NAME + "'");

        //Now add sample data
        android.content.ContentValues values = new android.content.ContentValues();
        values.put(UserTable.COLUMN_NAME, "Alice");
        values.put(UserTable.COLUMN_AGE, 30);
        db.insert(UserTable.TABLE_NAME, null, values);

        values.put(UserTable.COLUMN_NAME, "Bob");
        values.put(UserTable.COLUMN_AGE, 25);
        db.insert(UserTable.TABLE_NAME, null, values);

        db.close();
    }

    /**
     * Retrieves a user by their name.
     *
     * @param name The name of the user to retrieve.
     * @return A User object if found, otherwise null.
     */
    public User getUserByName(String name) {
        android.database.sqlite.SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = String.format("SELECT * FROM %s WHERE %s = ?", UserTable.TABLE_NAME, UserTable.COLUMN_NAME);
        android.database.Cursor cursor = db.rawQuery(selectQuery, new String[]{name});
        User user = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COLUMN_ID));
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_NAME));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COLUMN_AGE));
            user = new User(id, userName, age);
        }
        cursor.close();
        db.close();
        return user;
    }

    /**
     * Deletes a user by their name.
     *
     * @param name The name of the user to delete.
     */
    public void deleteUserByName(String name) {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UserTable.TABLE_NAME, UserTable.COLUMN_NAME + " = ?", new String[]{name});
        db.close();
    }

    /**
     * Updates the age of a user identified by their name.
     *
     * @param name   The name of the user to update.
     * @param newAge The new age to set for the user.
     */
    public void updateUserAge(String name, int newAge) {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put(UserTable.COLUMN_AGE, newAge);
        db.update(UserTable.TABLE_NAME, values, UserTable.COLUMN_NAME + " = ?", new String[]{name});
        db.close();
    }

    /**
     * Adds a new user to the database.
     *
     * @param name The name of the user.
     * @param age  The age of the user.
     */
    public void addUser(String name, int age) {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put(UserTable.COLUMN_NAME, name);
        values.put(UserTable.COLUMN_AGE, age);
        db.insert(UserTable.TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of User objects.
     */
    public java.util.List<User> getAllUsers() {
        java.util.List<User> userList = new java.util.ArrayList<>();
        String selectQuery = String.format("SELECT * FROM %s", UserTable.TABLE_NAME);
        android.database.sqlite.SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_NAME));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COLUMN_AGE));
                userList.add(new User(id, name, age));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    /**
     * Creates the user table in the database.
     * @param db The database.
     */
    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        String createUserTable = String.format(
                "CREATE TABLE %s " +
                        "(" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, " +
                        "%s INTEGER" +
                        ")",
                UserTable.TABLE_NAME,
                UserTable.COLUMN_ID,
                UserTable.COLUMN_NAME,
                UserTable.COLUMN_AGE
        );
        db.execSQL(createUserTable);
    }

    /**
     * Upgrades the database by dropping the existing user table and recreating it.
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropUserTable = String.format("DROP TABLE IF EXISTS %s", UserTable.TABLE_NAME);
        db.execSQL(dropUserTable);
        onCreate(db);
    }
}
