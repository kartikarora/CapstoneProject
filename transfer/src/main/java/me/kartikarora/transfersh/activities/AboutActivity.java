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

package me.kartikarora.transfersh.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.License;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;
import me.kartikarora.transfersh.BuildConfig;
import me.kartikarora.transfersh.R;
import me.kartikarora.transfersh.applications.TransferApplication;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.activities
 * Project : Transfer.sh
 * Date : 30/6/16
 */
public class AboutActivity extends AppCompatActivity {
    private Tracker mTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TransferApplication application = (TransferApplication) getApplication();
        mTracker = application.getDefaultTracker();

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Activity : " + this.getClass().getSimpleName())
                .setAction("Launched")
                .build());

        CardView aboutCardView = (CardView) findViewById(R.id.about_card);
        CardView openSourceLicensesCardView = (CardView) findViewById(R.id.open_source_licenses_card);
        CardView feedbackCardView = (CardView) findViewById(R.id.feedback_card);
        TextView devTextView = (TextView) findViewById(R.id.dev_text_view);

        aboutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://kartikarora.me", name = "Transfer.sh", copyright = "Copyright 2016 Kartik Arora";
                License license = new ApacheSoftwareLicense20();
                Notice notice = new Notice(name, url, copyright, license);
                new LicensesDialog.Builder(AboutActivity.this)
                        .setNotices(notice)
                        .setTitle(R.string.app_name)
                        .build()
                        .show();

            }
        });

        openSourceLicensesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notices notices = new Notices();
                String url = "", name, copyright;

                name = "android-support-v4, android-support-v7-appcompat, android-support-design";
                copyright = "Copyright (C) 2015 The Android Open Source Project";
                License license = new ApacheSoftwareLicense20();
                Notice notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "android-support-v7-cardview";
                copyright = "Copyright (C) 2014 The Android Open Source Project";
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "Google Analytics, Google Admob";
                copyright = "Copyright (C) 2015 Google Inc.";
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "Retrofit";
                url = "http://square.github.io/retrofit/";
                copyright = "Copyright (C) 2012 Square, Inc.";
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "Apache Commons IO";
                copyright = "Copyright 2002-2016 The Apache Software Foundation";
                url = "http://www.apache.org/";
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "transfer.sh";
                copyright = "Copyright (c) 2014 DutchCoders [https://github.com/dutchcoders/]";
                url = "https://github.com/dutchcoders/";
                license = new MITLicense();
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                new LicensesDialog.Builder(AboutActivity.this)
                        .setNotices(notices)
                        .setIncludeOwnLicense(true)
                        .setTitle(R.string.open_source_licenses)
                        .build()
                        .show();

            }
        });

        feedbackCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "aawaazdo@kartikarora.me";
                String subject = getString(R.string.app_name) + " - Feedback/Sugesstion";
                String body = "==========\n" +
                        BuildConfig.VERSION_NAME + "\n" +
                        Build.VERSION.SDK_INT + "\n" +
                        Build.MANUFACTURER + " - " + Build.MODEL
                        + "\n==========";
                Log.d("Test", body);
                String uri = "mailto:" + Uri.encode(email) + "?subject=" + subject +
                        "&body=" + body;
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(uri)));
            }
        });

        devTextView.setText(Html.fromHtml(getString(R.string.dev_text)));
        devTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://kartikarora.me")));
            }
        });
    }
}
