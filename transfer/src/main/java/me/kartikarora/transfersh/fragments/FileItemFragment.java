package me.kartikarora.transfersh.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.io.FilenameUtils;

import me.kartikarora.transfersh.R;
import me.kartikarora.transfersh.models.FileModel;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.fragments
 * Project : ProjectSevenEight
 * Date : 21/6/16
 */
public class FileItemFragment extends Fragment {

    public static FileItemFragment newInstance(AppCompatActivity activity, FileModel fileModel) {
        FileItemFragment fragment = new FileItemFragment();
        fragment.fileModel = fileModel;
        fragment.activity = activity;
        return fragment;
    }

    private AppCompatActivity activity;
    private FileModel fileModel;
    private TextView fileNameTextView;
    private ImageView fileTypeImageView;
    private ImageButton fileInfoImageButton;
    private ImageButton fileShareImageButton;
    private ImageButton fileDownloadImageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViewsAndListeners(view);
    }

    private void setUpViewsAndListeners(View view) {
        fileNameTextView = (TextView) view.findViewById(R.id.file_item_name_text_view);
        fileTypeImageView = (ImageView) view.findViewById(R.id.file_item_type_image_view);
        fileInfoImageButton = (ImageButton) view.findViewById(R.id.file_item_info_image_button);
        fileShareImageButton = (ImageButton) view.findViewById(R.id.file_item_share_image_button);
        fileDownloadImageButton = (ImageButton) view.findViewById(R.id.file_item_download_image_button);

        Context context = activity.getApplicationContext();
        String name = fileModel.getFileName();
        fileNameTextView.setText(name);
        String ext = FilenameUtils.getExtension(name);
        int identifier = context.getResources().getIdentifier(ext, "drawable", context.getPackageName());
        try {
            fileTypeImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), identifier, null));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            fileTypeImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.blank, null));
        }
    }

}
