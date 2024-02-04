package cse2.bhooshan.uberforrepair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DashboardActivity extends AppCompatActivity {
    private ImageButton backButton;
    private ImageButton logOutButton;
    private ImageButton puncher;
    private ImageButton waterWash;
    private ImageButton completeService;
    private ImageButton replace;
    private Button Addaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        backButton = findViewById(R.id.backB);
        logOutButton = findViewById(R.id.logOutB);
        puncher = findViewById(R.id.imageButton);
        waterWash = findViewById(R.id.imageButton2);
        completeService = findViewById(R.id.imageButton3);
        replace = findViewById(R.id.imageButton5);
        Addaddress=findViewById(R.id.button2);

        backButton.setOnClickListener(view -> onBackPressed());
        logOutButton.setOnClickListener(view -> showLogoutConfirmationDialog());
        Addaddress.setOnClickListener(view -> {
            Intent intent=new Intent(DashboardActivity.this,AddaddressActivity.class);
            startActivity(intent);
        });
        puncher.setOnClickListener(view -> {
            Intent in1=new Intent(DashboardActivity.this,PuncherActivity1.class);
            startActivity(in1);
        });
        completeService.setOnClickListener(view-> {
            Intent in2=new Intent(DashboardActivity.this,ScheduleRepairActivity.class);
            startActivity(in2);
        });
    }


    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    Intent intent = new Intent(DashboardActivity.this, RegActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
        super.onBackPressed();
    }

}
