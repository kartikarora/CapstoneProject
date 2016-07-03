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

package me.kartikarora.transfersh.providers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import me.kartikarora.transfersh.R;
import me.kartikarora.transfersh.activities.TransferActivity;
import me.kartikarora.transfersh.contracts.FilesContract;
import me.kartikarora.transfersh.services.FilesRemoteViewsService;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.providers
 * Project : Transfer.sh
 * Date : 1/7/16
 */
public class FilesAppWidgetProvider extends AppWidgetProvider {
    public static final String EXTRA_ITEM = "id";
    public static final String ACTION_SHARE = "share";
    public static final String ACTION_SHOW = "show";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int currentWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, TransferActivity.class).putExtra("id", -1);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            Intent adapterIntent = new Intent(context, FilesRemoteViewsService.class);
            adapterIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            adapterIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, currentWidgetId);


            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.widget_item, pendingIntent);

            views.setRemoteAdapter(R.id.scores_list, adapterIntent);

            appWidgetManager.updateAppWidget(currentWidgetId, views);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_SHARE)) {
            String url = intent.getStringExtra(FilesContract.FilesEntry.COLUMN_URL);
            Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, url)
                    .setType("text/plain").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(shareIntent);
        } else if (action.equals(ACTION_SHOW)) {
            long id = intent.getLongExtra(EXTRA_ITEM, -1);
            Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).putExtra(FilesContract.FilesEntry._ID, id)
                    .setType("text/plain").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(shareIntent);
        }
        super.onReceive(context, intent);
    }
}
