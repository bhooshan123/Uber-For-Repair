package cse2.bhooshan.uberforrepair;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MechanicServiceListenerActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ServiceRequestAdapter adapter;
    private List<PuncherActivity1.ServiceRequest> serviceRequests = new ArrayList<>();
    private double mechanicLatitude, mechanicLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_service_listener);

        RecyclerView recyclerView = findViewById(R.id.rvServiceRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiceRequestAdapter(this, serviceRequests);
        recyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (currentUser != null) {
            fetchMechanicLocation(currentUser.getUid());
            listenForServiceRequests();
        }
    }

    private void fetchMechanicLocation(String mechanicId) {
        // Assuming mechanic's location is stored under "Mechanics" node
        DatabaseReference mechanicRef = databaseReference.child("Mechanics").child(mechanicId);
        mechanicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Assuming you have "latitude" and "longitude" fields
                mechanicLatitude = dataSnapshot.child("latitude").getValue(Double.class);
                mechanicLongitude = dataSnapshot.child("longitude").getValue(Double.class);
                // Now that we have the mechanic's location, listen for nearby service requests
                listenForServiceRequests();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DBError", "loadMechanicLocation:onCancelled", databaseError.toException());
            }
        });
    }

    private void listenForServiceRequests() {
        DatabaseReference serviceRequestsRef = databaseReference.child("serviceRequests");
        serviceRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceRequests.clear(); // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PuncherActivity1.ServiceRequest request = snapshot.getValue(PuncherActivity1.ServiceRequest.class);
                    if (request != null && isWithinDesiredDistance(request.latitude, request.longitude)) {
                        serviceRequests.add(request);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify the adapter of data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DBError", "loadServiceRequests:onCancelled", databaseError.toException());
            }
        });
    }


        private static final double EARTH_RADIUS_KM = 6371.0; // Earth's radius in kilometers

        private boolean isWithinDesiredDistance(double requestLatitude, double requestLongitude) {
            double dLat = Math.toRadians(requestLatitude - mechanicLatitude);
            double dLon = Math.toRadians(requestLongitude - mechanicLongitude);
            double lat1 = Math.toRadians(mechanicLatitude);
            double lat2 = Math.toRadians(requestLatitude);

            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.sin(dLon / 2) * Math.sin(dLon / 2) *
                            Math.cos(lat1) * Math.cos(lat2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = EARTH_RADIUS_KM * c;

            return distance <= 10; // Adjust this value to your desired threshold
        }


}
