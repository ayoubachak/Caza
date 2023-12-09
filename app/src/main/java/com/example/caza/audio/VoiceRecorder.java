package com.example.caza.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VoiceRecorder {

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String currentFilePath;
    private ArrayList<String> recordedAudioPaths;
    private Context context; // Context for file path

    public VoiceRecorder(Context context) {
        this.context = context;
        recordedAudioPaths = new ArrayList<>();
    }

    public void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        currentFilePath = getUniqueFileName();
        mediaRecorder.setOutputFile(currentFilePath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;

            recordedAudioPaths.add(currentFilePath);
        }
    }

    public void playRecording(String filePath) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getCurrentFilePath() {
        return currentFilePath;
    }
    private String getUniqueFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return context.getExternalCacheDir().getAbsolutePath() + "/REC_" + timeStamp + ".3gp";
    }

    public ArrayList<String> getRecordedAudioPaths() {
        return recordedAudioPaths;
    }

    public boolean isRecording() {
        return mediaRecorder != null;
    }

}
