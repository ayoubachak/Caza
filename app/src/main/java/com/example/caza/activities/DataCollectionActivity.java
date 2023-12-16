package com.example.caza.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.caza.R;
import com.example.caza.dao.DataCollectionItemDao;
import com.example.caza.dao.MessageDao;
import com.example.caza.dao.RecordingDao;
import com.example.caza.data_collection.RecordingsAdapter;
import com.example.caza.db.AppDatabase;
import com.example.caza.entities.DataCollectionItem;
import com.example.caza.entities.Message;
import com.example.caza.entities.Recording;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataCollectionActivity extends AppCompatActivity {
    private AppDatabase db;
    private MessageDao messageDao;
    private RecordingDao recordingDao;
    private DataCollectionItemDao dataCollectionItemDao;

    private RecyclerView recordingsRecyclerView;
    private RecordingsAdapter recordingsAdapter;
    private List<DataCollectionItem> dataCollectionItems;
    private ExecutorService executorService;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collection);

        // Initialize database and ExecutorService
        // Use getInstance method to ensure proper initialization
        db = AppDatabase.getInstance(getApplicationContext());
        dataCollectionItemDao = db.dataCollectionItemDao();
        messageDao = db.messageDao();
        recordingDao = db.recordingDao();

        executorService = Executors.newSingleThreadExecutor();
        progressBar = findViewById(R.id.progressBar);

        // Setup RecyclerView
        setupRecyclerView();

        // Setup FloatingActionButton
        FloatingActionButton addFab = findViewById(R.id.add_fab);
        addFab.setOnClickListener(view -> {
            Intent intent = new Intent(DataCollectionActivity.this, AddDataActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        recordingsRecyclerView = findViewById(R.id.recordingsRecyclerView);
        dataCollectionItems = new ArrayList<>();
        recordingsAdapter = new RecordingsAdapter(dataCollectionItems);
        recordingsRecyclerView.setAdapter(recordingsAdapter);
        recordingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchDataCollectionItems();
    }

    private void fetchDataCollectionItems() {
        showLoadingIndicator();

        executorService.execute(() -> {
            List<DataCollectionItem> updatedItems = dataCollectionItemDao.getAll();
            // load up the messages and the recordings from manually because the data manager in android is still kinda ass
            for (DataCollectionItem item : updatedItems) {
                // Fetch the related Message and Recording
                Message message = messageDao.getMessageById(item.getMessageId());
                Recording recording = recordingDao.getRecordingById(item.getRecordingId());
                item.setMessage(message);
                item.setRecording(recording);
            }
            runOnUiThread(() -> {
                hideLoadingIndicator();
                dataCollectionItems.clear();
                dataCollectionItems.addAll(updatedItems);
                recordingsAdapter.notifyDataSetChanged();
            });
        });
    }

    private void showLoadingIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        progressBar.setVisibility(View.GONE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }


}
