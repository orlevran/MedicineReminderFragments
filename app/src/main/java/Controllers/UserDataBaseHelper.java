package Controllers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import Models.SecurityQuestion;
import Models.Sponsor;
import Models.User;

public class UserDataBaseHelper extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "Users.db";
    public static final String USERS_TABLE = "users", COLUMN_USERNAME = "username",
            COLUMN_PASSWORD = "password", COLUMN_FIRST_NAME = "firstName",
            COLUMN_LAST_NAME = "lastName", COLUMN_SPONSOR_FIRST_NAME = "sponsorFirstName",
            COLUMN_SPONSOR_LAST_NAME = "sponsorLastName", COLUMN_SPONSOR_PHONE = "sponsorPhoneNumber",
            COLUMN_SECURITY_QUESTION = "securityQuestion", COLUMN_SECURITY_ANSWER = "securityAnswer";

    public UserDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = " CREATE TABLE " + USERS_TABLE + " ( " + COLUMN_USERNAME +
                " TEXT PRIMARY KEY, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_FIRST_NAME +
                " TEXT, " + COLUMN_LAST_NAME + " TEXT, " + COLUMN_SPONSOR_FIRST_NAME + " TEXT, " +
                COLUMN_SPONSOR_LAST_NAME + " TEXT, " + COLUMN_SPONSOR_PHONE + " TEXT, " +
                COLUMN_SECURITY_QUESTION + " TEXT, " + COLUMN_SECURITY_ANSWER + " TEXT );";
        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        onCreate(sqLiteDatabase);
        sqLiteDatabase.close();
    }

    @SuppressLint("Range")
    public User getUser(String username, String password){
        User user = new User();
        SQLiteDatabase db = getWritableDatabase();
        String findUserQuery = "SELECT * FROM " + USERS_TABLE + " WHERE " +
                COLUMN_USERNAME + " = \"" + username + "\"";
        Cursor cursor = db.rawQuery(findUserQuery, null);
        cursor.moveToFirst();

        if(!cursor.isAfterLast()){
            if(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)) != null){
                if(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)).equals(password)){
                    user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
                    user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                    user.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)));
                    user.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)));
                    user.setSponsor(new Sponsor(
                            cursor.getString(cursor.getColumnIndex(COLUMN_SPONSOR_FIRST_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SPONSOR_LAST_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SPONSOR_PHONE))));
                    user.setSecurityQuestion(new SecurityQuestion(
                            cursor.getString(cursor.getColumnIndex(COLUMN_SECURITY_QUESTION)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SECURITY_ANSWER))));
                }
            }
        }
        return user;
    }

    public boolean isUsernameExists(String username){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + COLUMN_USERNAME + " = \"" + username + "\"";
        Cursor cursor = db.rawQuery(query , null);
        return cursor != null && cursor.getCount() > 0;
    }

    public void addUser(User user){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_SPONSOR_FIRST_NAME, user.getSponsor().getFirstName());
        values.put(COLUMN_SPONSOR_LAST_NAME, user.getSponsor().getLastName());
        values.put(COLUMN_SPONSOR_PHONE, user.getSponsor().getPhoneNumber());
        values.put(COLUMN_SECURITY_QUESTION, user.getSecurityQuestion().getQuestion());
        values.put(COLUMN_SECURITY_ANSWER, user.getSecurityQuestion().getAnswer());

        if(db.insertOrThrow(USERS_TABLE, null, values) == 0){
            throw new SQLiteConstraintException();
        }
    }
}
