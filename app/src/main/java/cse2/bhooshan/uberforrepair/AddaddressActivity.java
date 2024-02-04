package cse2.bhooshan.uberforrepair;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.location.LocationServices;

public class AddaddressActivity extends AppCompatActivity {
    private static final String FRAGMENT_TAG = "customLocationFragment";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaddress);

        Button customLocationButton = findViewById(R.id.customLocationButton);
        Button currentLocationButton = findViewById(R.id.currentLocationButton);

        customLocationButton.setOnClickListener(v -> showCustomLocationFragment());
        currentLocationButton.setOnClickListener(v -> checkPermissionAndStartLocationService());
    }

    private void showCustomLocationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new CustomLocationFragment(); // This needs to be your actual Fragment
            transaction.replace(R.id.fragmentContainer, fragment, FRAGMENT_TAG);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void checkPermissionAndStartLocationService() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationService();
        } else {
            Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show();
        }
    }

    private void startLocationService() {
        Intent serviceIntent = new Intent(this, LocationUpdateService.class);
        startService(serviceIntent);
        Toast.makeText(this, "Starting location service", Toast.LENGTH_SHORT).show();
    }
}
