package me.kartikarora.transfersh.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.io.FilenameUtils;

import java.util.List;

import me.kartikarora.transfersh.R;
import me.kartikarora.transfersh.models.FileModel;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.adapters
 * Project : Transfer.sh
 * Date : 9/6/16
 */
public class FileGridAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<FileModel> files;
    private AppCompatActivity activity;
    private Context context;

    public FileGridAdapter(AppCompatActivity activity, List<FileModel> files) {
        this.context = activity.getApplicationContext();
        this.inflater = LayoutInflater.from(context);
        this.files = files;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public FileModel getItem(int i) {
        return files.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.file_item, viewGroup, false);
            FileItemViewHolder holder = new FileItemViewHolder(view);
            view.setTag(holder);
        }
        FileItemViewHolder holder = (FileItemViewHolder) view.getTag();
        final FileModel fileModel = getItem(i);
        String name = fileModel.getFileName();
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
                        .putExtra(Intent.EXTRA_TEXT, fileModel.getFileUrl())
                        .setType("text/plain")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        holder.fileDownloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(fileModel.getFileUrl());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setDescription(context.getString(R.string.app_name));
                request.setTitle(fileModel.getFileName());
                String dir = "/" + context.getString(R.string.app_name) + "/" + fileModel.getFileType()
                        + "/" + fileModel.getFileName();
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, dir);
                long id = manager.enqueue(request);
            }
        });
        return view;
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
}
