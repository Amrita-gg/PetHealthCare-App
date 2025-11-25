package com.example.pethealthcare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pethealthcare.R;
import com.example.pethealthcare.activities.AlarmUtils;
import com.example.pethealthcare.models.TaskReminderModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskReminderAdapter extends RecyclerView.Adapter<TaskReminderAdapter.ViewHolder> {

    private Context context;
    private List<TaskReminderModel> remindersList;
    private DatabaseReference databaseReference;

    public TaskReminderAdapter(Context context, List<TaskReminderModel> remindersList, String userId) {
        this.context = context;
        this.remindersList = remindersList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Reminders").child(userId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskReminderModel reminder = remindersList.get(position);
        holder.taskTitle.setText(reminder.getTitle());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        holder.taskTime.setText(sdf.format(reminder.getTimestamp()));

        holder.deleteButton.setOnClickListener(v -> {
            // Cancel the alarm before deleting from Firebase
            AlarmUtils.cancelAlarm(context, reminder.getId());

            databaseReference.child(reminder.getId()).removeValue().addOnSuccessListener(aVoid -> {
                if (position < remindersList.size()) {
                    remindersList.remove(position);
                }

                if (remindersList.isEmpty()) {
                    notifyDataSetChanged(); // Full refresh to prevent crash
                } else {
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, remindersList.size()); // Fix indexes
                }

                Toast.makeText(context, "Reminder deleted", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show());
        });

    }

    @Override
    public int getItemCount() {
        return remindersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskTime;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskTime = itemView.findViewById(R.id.taskTime);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
