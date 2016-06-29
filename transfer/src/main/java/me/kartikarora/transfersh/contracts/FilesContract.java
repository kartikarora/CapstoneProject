package me.kartikarora.transfersh.contracts;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import me.kartikarora.transfersh.BuildConfig;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.contracts
 * Project : Transfer.sh
 * Date : 28/6/16
 */
public class FilesContract {

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + BuildConfig.APPLICATION_ID);

    public static final class FilesEntry implements BaseColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + BuildConfig.APPLICATION_ID;

        public static final String TABLE_NAME = "filesdata";
//        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_SIZE = "size";

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(BASE_CONTENT_URI, id);
        }

    }
}
