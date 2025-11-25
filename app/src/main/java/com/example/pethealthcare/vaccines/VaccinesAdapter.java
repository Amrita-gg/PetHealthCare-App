package com.example.pethealthcare.vaccines;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pethealthcare.R;
import java.util.Calendar;
import java.util.List;

public class VaccinesAdapter extends RecyclerView.Adapter<VaccinesAdapter.VaccineViewHolder> {
    private final Context context;
    private final List<Vaccine> vaccineList;
    private final VaccineNotificationManager notificationManager;

    public VaccinesAdapter(Context context, List<Vaccine> vaccineList) {
        this.context = context;
        this.vaccineList = vaccineList;
        this.notificationManager = new VaccineNotificationManager();
    }

    @NonNull
    @Override
    public VaccineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vaccine_item, parent, false);
        return new VaccineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VaccineViewHolder holder, int position) {
        Vaccine vaccine = vaccineList.get(position);
        holder.tvPettype.setText(vaccine.getType());
        holder.tvVaccineName.setText(vaccine.getVaccineName());
        holder.tvVaccineDesc.setText(vaccine.getDesc());

        // Corrected button click event
        holder.btnSetAlarm.setOnClickListener(v -> showDateTimePicker(vaccine));

    }

    private void showDateTimePicker(Vaccine vaccine) {
        Calendar calendar = Calendar.getInstance();

        // Date Picker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                            (timeView, selectedHour, selectedMinute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                calendar.set(Calendar.MINUTE, selectedMinute);
                                calendar.set(Calendar.SECOND, 0);

                                // Schedule Notification
                                notificationManager.scheduleNotification(
                                        context,
                                        vaccine.getVaccineName(),
                                        calendar.getTimeInMillis()
                                );

                                Toast.makeText(context, "Alarm set for " + vaccine.getVaccineName() +
                                        " on " + dayOfMonth + "/" + (month + 1) + "/" + year +
                                        " at " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                    timePickerDialog.show();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public int getItemCount() {
        return vaccineList != null ? vaccineList.size() : 0;
    }

    public static class VaccineViewHolder extends RecyclerView.ViewHolder {
        TextView tvVaccineName, tvVaccineDesc,tvPettype;
        Button btnSetAlarm;

        public VaccineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVaccineName = itemView.findViewById(R.id.tvVaccineName);
            tvVaccineDesc = itemView.findViewById(R.id.tvVaccineDesc);
            btnSetAlarm = itemView.findViewById(R.id.btnSetAlarm);
            tvPettype = itemView.findViewById(R.id.tvPettype);
        }
    }
}