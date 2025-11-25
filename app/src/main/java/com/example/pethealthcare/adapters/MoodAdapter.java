package com.example.pethealthcare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pethealthcare.R;
import com.example.pethealthcare.models.Mood;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {

    private Context context;
    private List<Mood> moodList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MoodAdapter(Context context, List<Mood> moodList) {
        this.context = context;
        this.moodList = moodList;
    }

    @NonNull
    @Override
    public MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mood, parent, false);
        return new MoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodViewHolder holder, int position) {
        Mood mood = moodList.get(position);

        holder.tvPetName.setText(mood.getPetName());
        holder.moodNotes.setText(mood.getMoodNotes());

        // Ensure moodTypes is not null
        List<String> moodTypes = mood.getMoodTypes();
        if (moodTypes == null) {
            moodTypes = new ArrayList<>(); // Initialize empty list if null
        }

        // Display moods as a single string
        holder.moodType.setText(String.join(", ", moodTypes)); // Requires API 26+

        // Delete mood from Firestore when delete icon is clicked
        holder.deleteMood.setOnClickListener(view -> deleteMoodFromFirestore(mood.getId(), position));
    }

    @Override
    public int getItemCount() {
        return moodList.size();
    }

    public static class MoodViewHolder extends RecyclerView.ViewHolder {
        TextView moodType, moodNotes, moodDateTime, tvPetName;
        ImageView deleteMood;

        public MoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            moodType = itemView.findViewById(R.id.mood_type);
            moodNotes = itemView.findViewById(R.id.mood_notes);
            moodDateTime = itemView.findViewById(R.id.mood_date_time);
            deleteMood = itemView.findViewById(R.id.delete_mood);
        }
    }

    private void deleteMoodFromFirestore(String moodId, int position) {
        if (moodId == null || moodId.isEmpty()) {
            Toast.makeText(context, "Error: Mood ID is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("Moods").document(moodId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Remove from list and update RecyclerView
                    moodList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, moodList.size());
                    Toast.makeText(context, "Mood deleted successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Failed to delete mood: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
