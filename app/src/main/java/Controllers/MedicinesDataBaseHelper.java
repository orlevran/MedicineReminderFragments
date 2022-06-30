package Controllers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import Models.Medicines.Drops;
import Models.Medicines.Medicine;
import Models.Medicines.Ointment;
import Models.Medicines.Pill;
import Models.Medicines.Syrup;

public class MedicinesDataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Medicines.db", MEDICINES_TABLE = "medicines";
    private static final String COLUMN_IDENTIFIER = "identifier", COLUMN_NAME = "name",
            COLUMN_TYPE = "type", COLUMN_NIGHT = "nightTaking", COLUMN_MORNING = "morningTaking",
            COLUMN_AFTERNOON = "afternoonTaking", COLUMN_EVENING = "eveningTaking",
            COLUMN_INITIAL_AMOUNT = "initialAmount", COLUMN_CURRENT_AMOUNT = "currentAmount",
            COLUMN_DOSAGE = "dosage";

    public MedicinesDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = " CREATE TABLE " + MEDICINES_TABLE + " (" + COLUMN_IDENTIFIER +
                " TEXT PRIMARY KEY, " + COLUMN_NAME + " TEXT, " + COLUMN_TYPE + " TEXT, " +
                COLUMN_NIGHT + " INTEGER, " + COLUMN_MORNING + " INTEGER, " + COLUMN_AFTERNOON +
                " INTEGER, " + COLUMN_EVENING + " INTEGER, " + COLUMN_INITIAL_AMOUNT + " REAL, " +
                COLUMN_CURRENT_AMOUNT + " REAL, " + COLUMN_DOSAGE + " REAL);";
        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MEDICINES_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void addMedicine(Medicine medicine){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMN_IDENTIFIER, medicine.getIdentifier());
        values.put(COLUMN_NAME, medicine.getName());
        values.put(COLUMN_TYPE, medicine.getType().toString());
        values.put(COLUMN_NIGHT, medicine.getNightTaking());
        values.put(COLUMN_MORNING, medicine.getMorningTaking());
        values.put(COLUMN_AFTERNOON, medicine.getAfternoonTaking());
        values.put(COLUMN_EVENING, medicine.getEveningTaking());
        values.put(COLUMN_INITIAL_AMOUNT, -1);
        values.put(COLUMN_CURRENT_AMOUNT, -1);
        values.put(COLUMN_DOSAGE, -1);

        if(db.insertOrThrow(MEDICINES_TABLE, null, values) == 0){
            throw new SQLiteConstraintException();
        }
    }

    @SuppressLint("Range")
    public ArrayList<Medicine> readMedicinesArrayList(String username){
        ArrayList<Medicine> medicines = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query;
        String identifier, name, type;
        int night, morning, afternoon, evening;
        for(int i = 0 ; i < 10 ; i++){
            query = " SELECT * FROM " + MEDICINES_TABLE + " WHERE " + COLUMN_IDENTIFIER +
                    " = \"" + username + "_" + i + "\";";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()){
                identifier = cursor.getString(cursor.getColumnIndex(COLUMN_IDENTIFIER));
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                night = cursor.getInt(cursor.getColumnIndex(COLUMN_NIGHT));
                morning = cursor.getInt(cursor.getColumnIndex(COLUMN_MORNING));
                afternoon = cursor.getInt(cursor.getColumnIndex(COLUMN_AFTERNOON));
                evening = cursor.getInt(cursor.getColumnIndex(COLUMN_EVENING));
                switch (type){
                    case "Pill":
                        int initial = cursor.getInt(cursor.getColumnIndex(COLUMN_INITIAL_AMOUNT));
                        int current = cursor.getInt(cursor.getColumnIndex(COLUMN_CURRENT_AMOUNT));
                        int dosagePill = cursor.getInt(cursor.getColumnIndex(COLUMN_DOSAGE));
                        medicines.add(new Pill(identifier, name, night, morning,
                                afternoon, evening, initial, current, dosagePill));
                        break;
                    case "Syrup":
                        double dosageSyrup = cursor.getDouble(cursor.getColumnIndex(COLUMN_DOSAGE));
                        medicines.add(new Syrup(identifier, name, night, morning,
                                afternoon, evening, dosageSyrup));
                        break;
                    case "Ointment":
                        medicines.add(new Ointment(identifier, name, night, morning,
                                afternoon, evening));
                        break;
                    case "Drops":
                        int dosageDrops = cursor.getInt(cursor.getColumnIndex(COLUMN_DOSAGE));
                        medicines.add(new Drops(identifier, name, night, morning, afternoon, night, dosageDrops));
                        break;
                    default:
                        medicines.add(new Medicine(identifier));
                        break;
                }
                cursor.moveToNext();
            }
        }

        String findUserQuery = "SELECT * FROM " + MEDICINES_TABLE + " WHERE " +
                COLUMN_IDENTIFIER + " = \"" + username + "\"";
        Cursor cursor = db.rawQuery(findUserQuery, null);
        cursor.moveToFirst();

        return medicines;
    }

    public void setToEmpty(UserController controller, int position){
        String query = " UPDATE " + MEDICINES_TABLE + " SET " + COLUMN_NAME + " = \"Empty\", " +
                COLUMN_TYPE + " = \"Empty\", " + COLUMN_NIGHT + " = -1, " + COLUMN_MORNING +
                " = -1, " + COLUMN_AFTERNOON + " = -1, " + COLUMN_EVENING + " = -1, " +
                COLUMN_INITIAL_AMOUNT + " = -1, " + COLUMN_CURRENT_AMOUNT + " = -1, " +
                COLUMN_DOSAGE + " = -1 WHERE " + COLUMN_IDENTIFIER + " = \"" +
                controller.getUser().getMedicines().get(position).getIdentifier() + "\";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

    public void setToPill(Pill pill, UserController controller, int position) {
        String query = " UPDATE " + MEDICINES_TABLE + " SET " + COLUMN_NAME + " = \"" +
                pill.getName() + "\", " + COLUMN_TYPE + " = \"Pill\", " + COLUMN_NIGHT +
                " = \"" + pill.getNightTaking() + "\", " + COLUMN_MORNING + " = \"" +
                pill.getMorningTaking() + "\", " + COLUMN_AFTERNOON + " = \"" +
                pill.getAfternoonTaking() + "\", " + COLUMN_EVENING + " = \"" +
                pill.getEveningTaking() + "\", " + COLUMN_INITIAL_AMOUNT + " = \"" +
                pill.getInitialAmount() + "\"," + COLUMN_CURRENT_AMOUNT + " = \"" +
                pill.getCurrentAmount() + "\"," + COLUMN_DOSAGE + " = \"" +
                pill.getDosage() + "\" WHERE " + COLUMN_IDENTIFIER + " = \"" +
                controller.getUser().getMedicines().get(position).getIdentifier() + "\";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

    public void setToSyrup(Syrup syrup, UserController controller, int position) {

        String query = " UPDATE " + MEDICINES_TABLE + " SET " + COLUMN_NAME + " = \"" +
                syrup.getName() + "\", " + COLUMN_TYPE + " = \"Syrup\", " + COLUMN_NIGHT + " = " +
                syrup.getNightTaking() + ", " + COLUMN_MORNING + " = " + syrup.getMorningTaking() +
                ", " + COLUMN_AFTERNOON + " = " + syrup.getAfternoonTaking() + ", " +
                COLUMN_EVENING + " = " + syrup.getEveningTaking() + ", " + COLUMN_INITIAL_AMOUNT +
                " = -1, " + COLUMN_CURRENT_AMOUNT + " = -1, " + COLUMN_DOSAGE + " = " +
                syrup.getDosage() + " WHERE " + COLUMN_IDENTIFIER + " = \"" +
                controller.getUser().getMedicines().get(position).getIdentifier() + "\";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

    public void setToOintment(Ointment ointment, UserController controller, int position) {
        String query = " UPDATE " + MEDICINES_TABLE + " SET " + COLUMN_NAME + " = \"" +
                ointment.getName() + "\", " + COLUMN_TYPE + " = \"Ointment\", " + COLUMN_NIGHT +
                " = " + ointment.getNightTaking() + ", " + COLUMN_MORNING + " = " +
                ointment.getMorningTaking() + ", " + COLUMN_AFTERNOON + " = " +
                ointment.getAfternoonTaking() + ", " + COLUMN_EVENING + " = " +
                ointment.getEveningTaking() + ", " +COLUMN_INITIAL_AMOUNT + " =  -1, " +
                COLUMN_CURRENT_AMOUNT + " =  -1, " + COLUMN_DOSAGE + " = -1 WHERE " +
                COLUMN_IDENTIFIER + " = \"" + controller.getUser().getMedicines().get(position) +
                "\";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

    public void setToDrops(Drops drops, UserController controller, int position){
        String query = " UPDATE " + MEDICINES_TABLE + " SET " + COLUMN_NAME + " = \"" +
                drops.getName() + "\", " + COLUMN_TYPE + " = \"Drops\", " + COLUMN_NIGHT +
                " = " + drops.getNightTaking() + ", " + COLUMN_MORNING + " = " +
                drops.getMorningTaking() + ", " + COLUMN_AFTERNOON + " = " +
                drops.getAfternoonTaking() + ", " + COLUMN_EVENING + " = " +
                drops.getEveningTaking() + ", " + COLUMN_INITIAL_AMOUNT + " =  -1, " +
                COLUMN_CURRENT_AMOUNT + " =  -1, " + COLUMN_DOSAGE + " = " +
                drops.getDosage() + ", WHERE " + COLUMN_IDENTIFIER + " = \"" +
                controller.getUser().getMedicines().get(position) + "\";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }
}
