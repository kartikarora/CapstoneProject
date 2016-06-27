package me.kartikarora.transfersh.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import me.kartikarora.transfersh.contracts.FilesContract;
import me.kartikarora.transfersh.helpers.FilesDBHelper;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.providers
 * Project : Transfer.sh
 * Date : 28/6/16
 */
public class FilesProvider extends ContentProvider {

    private SQLiteDatabase mWritableDatabase, mReadableDatabase;

    @Override
    public boolean onCreate() {
        FilesDBHelper mOpenHelper = new FilesDBHelper(getContext());
        mWritableDatabase = mOpenHelper.getWritableDatabase();
        mReadableDatabase = mOpenHelper.getReadableDatabase();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mReadableDatabase.query(FilesContract.FilesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return FilesContract.FilesEntry.CONTENT_ITEM_TYPE;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri returnUri;
        long _id = mWritableDatabase.insert(FilesContract.FilesEntry.TABLE_NAME, null, values);
        if (_id > 0) {
            returnUri = FilesContract.FilesEntry.buildUri(_id);
        } else
            throw new android.database.SQLException("Failed to insert row into " + uri);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return mWritableDatabase.delete(FilesContract.FilesEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mWritableDatabase.update(FilesContract.FilesEntry.TABLE_NAME, values, selection,
                selectionArgs);
    }
}
