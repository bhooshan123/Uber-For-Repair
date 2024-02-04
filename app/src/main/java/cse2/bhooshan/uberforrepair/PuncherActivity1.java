package cse2.bhooshan.uberforrepair;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class PuncherActivity1 extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_LOCATION_PERMISSION = 102;
    private EditText editTextDescription;
    private DatabaseReference databaseReference;
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puncher1);

        editTextDescription = findViewById(R.id.editTextDescription);
        Button btnRequestService = findViewById(R.id.btnRequestService);
        Button uploadImageButton = findViewById(R.id.button3);
        Button sendLocationButton = findViewById(R.id.button5);

        btnRequestService.setOnClickListener(view -> requestService());
        uploadImageButton.setOnClickListener(view -> uploadImage());
        sendLocationButton.setOnClickListener(view -> captureLocation());

        databaseReference = FirebaseDatabase.getInstance().getReference("serviceRequests");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void requestService() {
        if (lastLocation != null) {
            String key = databaseReference.push().getKey();
            ServiceRequest serviceRequest = new ServiceRequest(
                    editTextDescription.getText().toString(),
                    lastLocation.getLatitude(),
                    lastLocation.getLongitude()
            );
            if (key != null) {
                databaseReference.child(key).setValue(serviceRequest)
                        .addOnSuccessListener(aVoid -> Toast.makeText(PuncherActivity1.this, "Service request sent", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(PuncherActivity1.this, "Failed to send service request", Toast.LENGTH_SHORT).show());
            }
        } else {
            Toast.makeText(this, "Location not captured yet!", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                handleCapturedImage(imageBitmap);
            }
        }
    }

    private void handleCapturedImage(Bitmap imageBitmap) {
        uploadImageToFirebase(imageBitmap);
    }

    private void uploadImageToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        String imageBase64 = Base64.encodeToString(data, Base64.DEFAULT);

        String imageId = databaseReference.push().getKey();
        if (imageId != null) {
            databaseReference.child(imageId).setValue(imageBase64)
                    .addOnSuccessListener(aVoid -> Toast.makeText(PuncherActivity1.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(PuncherActivity1.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
        }
    }

    private void captureLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getLastLocation();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        lastLocation = location;
                        Toast.makeText(PuncherActivity1.this, "Location captured", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PuncherActivity1.this, "Failed to get location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class ServiceRequest {
        public String description;
        public double latitude;
        public double longitude;

        public ServiceRequest() {
            // Default constructor required for calls to DataSnapshot.getValue(ServiceRequest.class)
        }

        public ServiceRequest(String description, double latitude, double longitude) {
            this.description = description;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
