package cse2.bhooshan.uberforrepair;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUpdateService extends Service {
    private DatabaseReference databaseReference;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseReference = FirebaseDatabase.getInstance().getReference("user_locations");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocationUpdates();
        return START_STICKY;
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    double latitude = locationResult.getLastLocation().getLatitude();
                    double longitude = locationResult.getLastLocation().getLongitude();

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        String address = addresses.get(0).getAddressLine(0); // Get the first address line
                        String userId = getUserId(); // Implement this method to fetch the user ID dynamically
                        saveLocationToFirebase(userId, latitude, longitude, address);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        try {
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, null);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void saveLocationToFirebase(String userId, double latitude, double longitude, String address) {
        UserLocation userLocation = new UserLocation(userId, latitude, longitude, address);
        databaseReference.child(userId).setValue(userLocation);
    }

    private String getUserId() {
        // TODO: Implement logic to retrieve the user's ID dynamically, e.g., from FirebaseAuth
        return "unique_user_id";
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
