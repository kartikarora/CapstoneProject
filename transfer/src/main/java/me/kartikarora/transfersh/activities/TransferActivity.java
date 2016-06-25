package me.kartikarora.transfersh.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import me.kartikarora.transfersh.R;
import me.kartikarora.transfersh.adapters.FileGridAdapter;
import me.kartikarora.transfersh.models.FileModel;
import me.kartikarora.transfersh.network.TransferClient;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.activities
 * Project : Transfer.sh
 * Date : 9/6/16
 */
public class TransferActivity extends AppCompatActivity {

    private static final int FILE_RESULT_CODE = 1;
    private CoordinatorLayout mCoordinatorLayout;
    private List<FileModel> mFiles = new ArrayList<>();
    private TextView mNoFilesTextView;
    private RecyclerView mFileItemsRecyclerView;
    private FileGridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        mNoFilesTextView = (TextView) findViewById(R.id.no_files_text_view);
        mFileItemsRecyclerView = (RecyclerView) findViewById(R.id.file_grid_recycler_view);
        FloatingActionButton uploadFileButton = (FloatingActionButton) findViewById(R.id.upload_file_fab);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        setupRecyclerView();

        if (uploadFileButton != null) {
            uploadFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, FILE_RESULT_CODE);
                }
            });
        }

        checkValidity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_RESULT_CODE && resultCode == RESULT_OK) {
            Log.d("URI", data.getData().getPath());
            try {
                uploadFile(data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupRecyclerView() {
        if (mFileItemsRecyclerView != null && mNoFilesTextView != null) {
            mFileItemsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),
                    getResources().getInteger(R.integer.col_count), GridLayoutManager.VERTICAL, false));
            mAdapter = new FileGridAdapter(TransferActivity.this, mFiles);
            mFileItemsRecyclerView.setAdapter(mAdapter);
        }
    }

    private void checkValidity() {
        if (mAdapter.getItemCount() == 0) {
            mFileItemsRecyclerView.setVisibility(View.GONE);
            mNoFilesTextView.setVisibility(View.VISIBLE);
        } else {
            mFileItemsRecyclerView.setVisibility(View.VISIBLE);
            mNoFilesTextView.setVisibility(View.GONE);
        }
    }

    private void uploadFile(Uri uri) throws IOException {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            final String name = cursor.getString(nameIndex);
            final String mimeType = getContentResolver().getType(uri);
            Log.d(this.getClass().getSimpleName(), cursor.getString(0));
            Log.d(this.getClass().getSimpleName(), name);
            Log.d(this.getClass().getSimpleName(), mimeType);
            InputStream inputStream = getContentResolver().openInputStream(uri);
            OutputStream outputStream = openFileOutput(name, MODE_PRIVATE);
            if (inputStream != null) {
                IOUtils.copy(inputStream, outputStream);
                final File file = new File(getFilesDir(), name);
                TypedFile typedFile = new TypedFile(mimeType, file);
                TransferClient.getInterface().uploadFile(typedFile, name, new ResponseCallback() {
                    @Override
                    public void success(Response response) {
                        BufferedReader reader;
                        StringBuilder sb = new StringBuilder();
                        try {
                            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                            String line;
                            try {
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String result = sb.toString();
                        Snackbar.make(mCoordinatorLayout, result, Snackbar.LENGTH_SHORT).show();
                        FileModel f = new FileModel()
                                .setFileName(name)
                                .setFileSize(String.valueOf(file.getTotalSpace()))
                                .setFileType(mimeType)
                                .setFileUrl(result);
                        mFiles.add(0, f);
                        setupRecyclerView();
                        checkValidity();
                        FileUtils.deleteQuietly(file);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            } else
                Snackbar.make(mCoordinatorLayout, "Unable to read file", Snackbar.LENGTH_SHORT).show();
        }
    }


}
