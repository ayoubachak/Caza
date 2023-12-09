package com.example.caza.activities;

import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
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

public class DataCollectionActivity extends AppCompatActivity {
    private boolean isRecording = false;
    private MediaRecorder mediaRecorder;
    private String currentRecordingPath;
    private AppDatabase db;
    private RecordingDao recordingDao;
    private MessageDao messageDao;
    private DataCollectionItemDao dataCollectionItemDao;

    private RecyclerView recordingsRecyclerView;
    private RecordingsAdapter recordingsAdapter;
    private List<DataCollectionItem> dataCollectionItems;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collection);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name")
                .allowMainThreadQueries()  // Note: Using the main thread for DB operations is not recommended
                .build();
        recordingDao = db.recordingDao();
        messageDao = db.messageDao();
        dataCollectionItemDao = db.dataCollectionItemDao();

        recordingsRecyclerView = findViewById(R.id.recordingsRecyclerView);
        dataCollectionItems = new ArrayList<>();
        // TODO: Load data into dataCollectionItems here

        recordingsAdapter = new RecordingsAdapter(dataCollectionItems);
        recordingsRecyclerView.setAdapter(recordingsAdapter);
        recordingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton addFab = findViewById(R.id.add_fab);
        addFab.setOnClickListener(view -> {
            if (isRecording) {
                stopRecording();
            } else {
                startRecording();
            }
        });
    }

    private void startRecording() {
        if (!isRecording) {
            currentRecordingPath = getUniqueFilePath();
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(currentRecordingPath);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRecording() {
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            saveRecordingAndPromptForMessage();
        }
    }

    private void saveRecordingAndPromptForMessage() {
        insertRecordingToDatabase(currentRecordingPath);
    }

    private void insertRecordingToDatabase(String filePath) {
        executorService.execute(() -> {
            Recording newRecording = new Recording();
            newRecording.setFilePath(filePath);
            int recordingId = (int) recordingDao.insert(newRecording);

            // Run on UI thread
            runOnUiThread(() -> promptForMessage(recordingId));
        });
    }




    private void promptForMessage(final int recordingId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Message");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String messageContent = input.getText().toString();
            insertMessageAndDataCollectionItem(recordingId, messageContent);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void insertMessageAndDataCollectionItem(int recordingId, String messageContent) {
        executorService.execute(() -> {
            Message newMessage = new Message();
            newMessage.setContent(messageContent);
            int messageId = (int) messageDao.insert(newMessage);

            DataCollectionItem newItem = new DataCollectionItem();
            newItem.setRecordingId(recordingId);
            newItem.setMessageId(messageId);
            dataCollectionItemDao.insert(newItem);

            // Update UI if necessary
            runOnUiThread(() -> {
                // Assuming you have a method to fetch the latest data
                fetchDataCollectionItems();

                // Notify the adapter that the data has changed
                recordingsAdapter.notifyDataSetChanged();
            });
        });
    }

    private void fetchDataCollectionItems() {
        // Execute a database operation to fetch the latest data
        executorService.execute(() -> {
            List<DataCollectionItem> updatedItems = dataCollectionItemDao.getAll();

            // Update the data collection items on the UI thread
            runOnUiThread(() -> {
                dataCollectionItems.clear();
                dataCollectionItems.addAll(updatedItems);
                recordingsAdapter.notifyDataSetChanged();
            });
        });
    }

    private String getUniqueFilePath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File audioDir = new File(getExternalFilesDir(null), "audio_recordings");
        if (!audioDir.exists()) {
            audioDir.mkdirs();
        }
        return new File(audioDir, "REC_" + timeStamp + ".3gp").getAbsolutePath();
    }
    // Additional methods to handle data loading, editing, deleting, etc.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

}
