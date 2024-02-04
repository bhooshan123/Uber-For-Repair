package cse2.bhooshan.uberforrepair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MechanicRegistrationActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextName, editTextCity, editTextZipCode;
    private Button buttonSaveLocation, buttonRegisterMechanic;
    private DatabaseReference databaseReference;
    private FusedLocationProviderClient fusedLocationClient;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_registration);

        editTextEmail = findViewById(R.id.editTextMechanicEmail);
        editTextName = findViewById(R.id.editTextMechanicName);
        editTextCity = findViewById(R.id.editTextMechanicCity);
        editTextZipCode = findViewById(R.id.editTextMechanicZipCode);
        buttonSaveLocation = findViewById(R.id.button4);
        buttonRegisterMechanic = findViewById(R.id.buttonRegisterMechanic);

        databaseReference = FirebaseDatabase.getInstance().getReference("Mechanics");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        buttonSaveLocation.setOnClickListener(view -> saveCurrentLocationAndRegister());
        buttonRegisterMechanic.setOnClickListener(view -> startActivity(new Intent(MechanicRegistrationActivity.this, MechanicServiceListenerActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }

    private void saveCurrentLocationAndRegister() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                registerMechanic(editTextEmail.getText().toString(), editTextName.getText().toString(), editTextCity.getText().toString(), editTextZipCode.getText().toString(), location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(MechanicRegistrationActivity.this, "Failed to get current location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerMechanic(String email, String name, String city, String zipCode, double latitude, double longitude) {
        String mechanicId = databaseReference.push().getKey();
        Map<String, Object> mechanic = new HashMap<>();
        mechanic.put("email", email);
        mechanic.put("name", name);
        mechanic.put("city", city);
        mechanic.put("zipCode", zipCode);
        mechanic.put("latitude", latitude);
        mechanic.put("longitude", longitude);

        databaseReference.child(mechanicId).setValue(mechanic).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MechanicRegistrationActivity.this, "Mechanic registered successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MechanicRegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isNetworkAvailable(context)) {
                Toast.makeText(context, "Internet is not available. Please connect to the internet.", Toast.LENGTH_LONG).show();
            }
        }

        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
    }
}