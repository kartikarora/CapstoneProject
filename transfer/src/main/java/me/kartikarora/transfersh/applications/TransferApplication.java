package me.kartikarora.transfersh.applications;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import me.kartikarora.transfersh.R;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.applications
 * Project : ProjectSevenEight
 * Date : 30/6/16
 */
public class TransferApplication extends Application {
    private Tracker mTracker;

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
