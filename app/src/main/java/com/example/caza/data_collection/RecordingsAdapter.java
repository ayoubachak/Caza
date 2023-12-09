package com.example.caza.data_collection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caza.R;
import com.example.caza.entities.DataCollectionItem;

import java.util.List;

public class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.ViewHolder> {
    private List<DataCollectionItem> dataCollectionItems;

    public RecordingsAdapter(List<DataCollectionItem> dataCollectionItems) {
        this.dataCollectionItems = dataCollectionItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView commandName;
        TextView commandArgs;
        // Add more UI elements if needed

        public ViewHolder(View view) {
            super(view);
            commandName = view.findViewById(R.id.command_name);
            commandArgs = view.findViewById(R.id.command_args);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditOptions(); // Method to show edit options
                }
            });
            // Initialize other UI elements
        }
        private void showEditOptions() {
            // TODO: Implement a method to show editing options (e.g., using a PopupMenu)
        }
    }

    @Override
    public RecordingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recording_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataCollectionItem item = dataCollectionItems.get(position);
        // Assuming getCommandName and getCommandArgs methods are in your Message model
        holder.commandName.setText(item.getMessage().getCommandName());
        holder.commandArgs.setText(item.getMessage().getCommandArgs());
        // Set other UI elements based on the recording and message details
    }

    @Override
    public int getItemCount() {
        return dataCollectionItems.size();
    }
}
