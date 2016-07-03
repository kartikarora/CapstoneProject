/**
 * Copyright 2016 Kartik Arora
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.kartikarora.transfersh.services;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import me.kartikarora.transfersh.R;
import me.kartikarora.transfersh.contracts.FilesContract;
import me.kartikarora.transfersh.providers.FilesAppWidgetProvider;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.services
 * Project : Transfer.sh
 * Date : 2/7/16
 */
public class FilesRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FileViewFactory(getApplicationContext());
    }

    class FileViewFactory implements RemoteViewsFactory {

        private Context mContext;
        private Cursor mCursor;

        public FileViewFactory(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate() {
            setup();
        }

        @Override
        public void onDataSetChanged() {
            setup();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_file_item);
            if (mCursor != null && !mCursor.isAfterLast()) {
                int nameCol = mCursor.getColumnIndex(FilesContract.FilesEntry.COLUMN_NAME);
                int urlCol = mCursor.getColumnIndex(FilesContract.FilesEntry.COLUMN_URL);
                int idCol = mCursor.getColumnIndex(FilesContract.FilesEntry._ID);
                long id = mCursor.getLong(idCol);
                String name = mCursor.getString(nameCol);
                String url = mCursor.getString(urlCol);

                Log.d("name", name);
                Log.d("url", url);

                views.setTextViewText(R.id.widget_file_item_name_text_view, name);

                Bundle extras = new Bundle();
                extras.putLong(FilesAppWidgetProvider.EXTRA_ITEM, id);
                extras.putString(FilesContract.FilesEntry.COLUMN_URL, url);
                Intent shareIntent = new Intent();
                shareIntent.putExtras(extras);
                shareIntent.setAction(FilesAppWidgetProvider.ACTION_SHARE);
                views.setOnClickFillInIntent(R.id.widget_file_item_share_image_button, shareIntent);

                Bundle bundle = new Bundle();
                bundle.putLong(FilesAppWidgetProvider.EXTRA_ITEM, id);
                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(bundle);
                fillInIntent.setAction(FilesAppWidgetProvider.ACTION_SHOW);
                views.setOnClickFillInIntent(R.id.widget_file_item_name_text_view, fillInIntent);

                mCursor.moveToNext();
            }
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(mContext.getPackageName(), R.layout.widget_loading_layout);
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private void setup() {
            mCursor = mContext.getContentResolver().query(FilesContract.BASE_CONTENT_URI, null, null, null, null);
            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
            }
        }
    }
}
