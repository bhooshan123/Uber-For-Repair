package cse2.bhooshan.uberforrepair;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocationSaveService extends Service {
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Mechanics");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String mechanicId = intent.getStringExtra("mechanicId");
        saveCurrentLocation(mechanicId);
        return START_NOT_STICKY;
    }

    private void saveCurrentLocation(String mechanicId) {
        if (mechanicId != null && !mechanicId.isEmpty()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    databaseReference.child(mechanicId).child("latitude").setValue(location.getLatitude())
                            .addOnFailureListener(e -> Log.d("FirebaseError", "Failed to update latitude: " + e.getMessage()));
                    databaseReference.child(mechanicId).child("longitude").setValue(location.getLongitude())
                            .addOnFailureListener(e -> Log.d("FirebaseError", "Failed to update longitude: " + e.getMessage()));

                }
            });
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
