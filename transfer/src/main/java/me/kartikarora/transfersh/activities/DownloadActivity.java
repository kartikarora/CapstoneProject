package me.kartikarora.transfersh.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;

import org.apache.commons.io.FilenameUtils;

import me.kartikarora.transfersh.BuildConfig;
import me.kartikarora.transfersh.R;

public class DownloadActivity extends AppCompatActivity {
    private static final int PERM_REQUEST_CODE = BuildConfig.VERSION_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        final CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        Intent intent = getIntent();
        final String name, type, url;
        url = intent.getData().toString();
        name = FilenameUtils.getName(url);
        type = MimeTypeMap.getFileExtensionFromUrl(url);

        new AlertDialog.Builder(DownloadActivity.this)
                .setMessage(getString(R.string.download_file) + " " + getString(R.string.app_name) + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkForDownload(name, type, url, layout);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create().show();

    }

    private void beginDownload(String name, String type, String url) {
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDescription(getString(R.string.app_name));
        request.setTitle(name);
        String dir = "/" + getString(R.string.app_name) + "/" + type + "/" + name;
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, dir);
        manager.enqueue(request);
    }

    private void showRationale(View view) {
        Snackbar.make(view, R.string.permission_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPermissionDialog();
                    }
                }).show();
    }

    private void showPermissionDialog() {
        ActivityCompat.requestPermissions(DownloadActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_REQUEST_CODE);
    }

    private void checkForDownload(String name, String type, String url, View view) {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            beginDownload(name, type, url);
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DownloadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                showRationale(view);
            else {
                showPermissionDialog();
                checkForDownload(name, type, url, view);
            }
        }
    }
}
