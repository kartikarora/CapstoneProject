package me.kartikarora.transfersh.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.kartikarora.transfersh.contracts.FilesContract;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.helpers
 * Project : Transfer.sh
 * Date : 28/6/16
 */

public class FilesDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "files.db";

    public FilesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + FilesContract.FilesEntry.TABLE_NAME + " (" +
                FilesContract.FilesEntry._ID + " INTEGER PRIMARY KEY, " +
                FilesContract.FilesEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                FilesContract.FilesEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                FilesContract.FilesEntry.COLUMN_URL + " TEXT NOT NULL, " +
                FilesContract.FilesEntry.COLUMN_SIZE + " TEXT NOT NULL );";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FilesContract.FilesEntry.TABLE_NAME);
        onCreate(db);
    }

}
