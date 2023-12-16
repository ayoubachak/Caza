package com.example.caza.activities;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caza.R;
import com.example.caza.db.AppDatabase;
import com.example.caza.entities.DataCollectionItem;
import com.example.caza.entities.Message;
import com.example.caza.entities.Recording;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddDataActivity extends AppCompatActivity {

    private Button recordButton, saveButton;
    private EditText textLabel;
    private boolean isRecording = false;
    private MediaRecorder mediaRecorder;
    private String currentRecordingPath;
    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        recordButton = findViewById(R.id.record_button);
        textLabel = findViewById(R.id.text_label);
        saveButton = findViewById(R.id.save_button);

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        recordButton.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording();
            } else {
                startRecording();
            }
        });

        saveButton.setOnClickListener(v -> saveData());
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
                recordButton.setText("Stop Recording");
            } catch (IOException e) {
                Toast.makeText(this, "Recording failed", Toast.LENGTH_SHORT).show();
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
            recordButton.setText("Start Recording");
        }
    }

    private void saveData() {
        String textContent = textLabel.getText().toString().trim();
        if (!textContent.isEmpty() && currentRecordingPath != null) {
            executorService.execute(() -> {
                // Save the recording
                Recording recording = new Recording();
                recording.setFilePath(currentRecordingPath);
                long recordingId = db.recordingDao().insert(recording);

                // Save the message
                Message message = new Message();
                message.setContent(textContent);
                message.setType("text");  // Set type to text
                Log.d("LOGGER", "TextContent: " + textContent);
                if (textContent.startsWith("/")) {
                    int firstSpaceIndex = textContent.indexOf(" ");
                    if (firstSpaceIndex == -1) { // No space found, entire textContent is the command
                        message.setCommandName(textContent.substring(1));
                        message.setCommandArgs(""); // No arguments
                    } else {
                        message.setCommandName(textContent.substring(1, firstSpaceIndex));
                        message.setCommandArgs(textContent.substring(firstSpaceIndex + 1).trim());
                        // Log the command name and args
                        Log.d("CommandLogging", "Command Name: " + message.getCommandName());
                        Log.d("CommandLogging", "Command Args: " + message.getCommandArgs());
                    }
                }
                long messageId = db.messageDao().insert(message);

                // Create and save DataCollectionItem
                DataCollectionItem dataCollectionItem = new DataCollectionItem();
                dataCollectionItem.setRecordingId((int) recordingId);
                dataCollectionItem.setMessageId((int) messageId);
                db.dataCollectionItemDao().insert(dataCollectionItem);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close this activity and return to the previous one
                });
            });
        } else {
            Toast.makeText(this, "Please record a message and enter text", Toast.LENGTH_SHORT).show();
        }
    }


    private String getUniqueFilePath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File audioDir = new File(getExternalFilesDir(null), "audio_recordings");
        if (!audioDir.exists()) {
            audioDir.mkdirs();
        }
        return new File(audioDir, "REC_" + timeStamp + ".3gp").getAbsolutePath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}