package me.kartikarora.transfersh.adapters;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.io.FilenameUtils;

import me.kartikarora.transfersh.R;
import me.kartikarora.transfersh.contracts.FilesContract;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.adapters
 * Project : Transfer.sh
 * Date : 9/6/16
 */
public class FileGridAdapter extends CursorAdapter {

    private static final int PERM_REQUEST_CODE = 3;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    private Context context;
    private Cursor cursor;

    public FileGridAdapter(AppCompatActivity activity, Cursor cursor) {
        super(activity.getApplicationContext(), cursor);
        this.context = activity.getApplicationContext();
        this.inflater = LayoutInflater.from(context);
        this.activity = activity;
        this.cursor = cursor;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.file_item, parent, false);
        FileItemViewHolder holder = new FileItemViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        FileItemViewHolder holder = (FileItemViewHolder) view.getTag();
        int nameCol = cursor.getColumnIndex(FilesContract.FilesEntry.COLUMN_NAME);
        int typeCol = cursor.getColumnIndex(FilesContract.FilesEntry.COLUMN_TYPE);
        int sizeCol = cursor.getColumnIndex(FilesContract.FilesEntry.COLUMN_SIZE);
        int urlCol = cursor.getColumnIndex(FilesContract.FilesEntry.COLUMN_URL);
        final String name = cursor.getString(nameCol);
        final String type = cursor.getString(typeCol);
        String size = cursor.getString(sizeCol);
        Log.d(this.getClass().getSimpleName(), size);
        final String url = cursor.getString(urlCol);
        holder.fileNameTextView.setText(name);
        String ext = FilenameUtils.getExtension(name);
        int identifier = context.getResources().getIdentifier(ext, "drawable", context.getPackageName());
        try {
            holder.fileTypeImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), identifier, null));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            holder.fileTypeImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.blank, null));
        }

        holder.fileInfoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Info coming soon", Snackbar.LENGTH_SHORT).show();
            }
        });

        holder.fileShareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, url)
                        .setType("text/plain")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        holder.fileDownloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForDownload(name, type, url, view);
            }
        });
    }

    private class FileItemViewHolder {

        private TextView fileNameTextView;
        private ImageView fileTypeImageView;
        private ImageButton fileInfoImageButton;
        private ImageButton fileShareImageButton;
        private ImageButton fileDownloadImageButton;

        public FileItemViewHolder(View view) {
            fileNameTextView = (TextView) view.findViewById(R.id.file_item_name_text_view);
            fileTypeImageView = (ImageView) view.findViewById(R.id.file_item_type_image_view);
            fileInfoImageButton = (ImageButton) view.findViewById(R.id.file_item_info_image_button);
            fileShareImageButton = (ImageButton) view.findViewById(R.id.file_item_share_image_button);
            fileDownloadImageButton = (ImageButton) view.findViewById(R.id.file_item_download_image_button);
        }
    }

    private void beginDownload(String name, String type, String url) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDescription(context.getString(R.string.app_name));
        request.setTitle(name);
        String dir = "/" + context.getString(R.string.app_name) + "/" + type + "/" + name;
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
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_REQUEST_CODE);
    }

    private void checkForDownload(String name, String type, String url, View view) {

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            beginDownload(name, type, url);
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                showRationale(view);
            else {
                showPermissionDialog();
                checkForDownload(name, type, url, view);
            }
        }
    }
}
