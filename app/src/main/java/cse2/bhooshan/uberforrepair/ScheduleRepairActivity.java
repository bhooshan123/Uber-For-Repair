package cse2.bhooshan.uberforrepair;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ScheduleRepairActivity extends AppCompatActivity {
    private EditText editTextProblemDescription;
    private TimePicker timePicker;
    private Button buttonScheduleRepair;

    // Firebase reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_repair);

        editTextProblemDescription = findViewById(R.id.editTextProblemDescription);
        timePicker = findViewById(R.id.timePicker);
        buttonScheduleRepair = findViewById(R.id.buttonScheduleRepair);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Repairs");

        buttonScheduleRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleRepair();
            }
        });
    }

    private void scheduleRepair() {
        String problem = editTextProblemDescription.getText().toString().trim();
        long scheduleTimeMillis = getScheduleTimeMillis();

        // Generate a unique key for the repair
        String repairId = databaseReference.push().getKey();
        RepairJob repairJob = new RepairJob(repairId, problem, scheduleTimeMillis);

        // Save repair job details under this key
        assert repairId != null;
        databaseReference.child(repairId).setValue(repairJob).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ScheduleRepairActivity.this, "Repair scheduled successfully!", Toast.LENGTH_LONG).show();
                scheduleReminder(scheduleTimeMillis, repairId);
            } else {
                Toast.makeText(ScheduleRepairActivity.this, "Failed to schedule repair", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private long getScheduleTimeMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        return calendar.getTimeInMillis();
    }

    private void scheduleReminder(long scheduleTimeMillis, String repairId) {
        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        intent.putExtra("repairId", repairId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Set the alarm to trigger 30 minutes before the scheduled repair time
        alarmManager.set(AlarmManager.RTC_WAKEUP, scheduleTimeMillis - 1800000, pendingIntent);

        Toast.makeText(this, "Reminder set!", Toast.LENGTH_SHORT).show();
    }

    // Define the RepairJob class with appropriate constructors and getters
    public class RepairJob {
        private String id;
        private String problemDescription;
        private long scheduleTimeMillis;

        public RepairJob() {
            // Default constructor required for calls to DataSnapshot.getValue(RepairJob.class)
        }

        public RepairJob(String id, String problemDescription, long scheduleTimeMillis) {
            this.id = id;
            this.problemDescription = problemDescription;
            this.scheduleTimeMillis = scheduleTimeMillis;
        }

        public String getId() {
            return id;
        }

        public String getProblemDescription() {
            return problemDescription;
        }

        public long getScheduleTimeMillis() {
            return scheduleTimeMillis;
        }
    }

}
