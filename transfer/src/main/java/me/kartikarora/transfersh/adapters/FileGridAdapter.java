package me.kartikarora.transfersh.adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import me.kartikarora.transfersh.R;
import me.kartikarora.transfersh.fragments.FileItemFragment;
import me.kartikarora.transfersh.models.FileModel;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.adapters
 * Project : Transfer.sh
 * Date : 9/6/16
 */
public class FileGridAdapter extends RecyclerView.Adapter<FileItemViewHolder> {

    private LayoutInflater inflater;
    private List<FileModel> files;
    private AppCompatActivity activity;

    public FileGridAdapter(AppCompatActivity activity, List<FileModel> files) {
        this.inflater = LayoutInflater.from(activity.getApplicationContext());
        this.files = files;
        this.activity = activity;
    }

    @Override
    public FileItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.file_item, parent, false);
        return new FileItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FileItemViewHolder holder, int position) {
        FileItemFragment fragment = FileItemFragment.newInstance(activity, files.get(position));
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.item_container_frame, fragment).commit();
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

}

class FileItemViewHolder extends RecyclerView.ViewHolder {

    FrameLayout layout;

    public FileItemViewHolder(View itemView) {
        super(itemView);
        layout = (FrameLayout) itemView.findViewById(R.id.item_container_frame);
    }
}
