package cse2.bhooshan.uberforrepair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserregActivity extends AppCompatActivity {
    private EditText editTextName, editTextMobile, editTextEmail;
    private Spinner spinnerVehicleType;
    private Button btnRegister;
    private DatabaseReference mDatabase;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userreg);

        // Initialize Firebase
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI components
        editTextName = findViewById(R.id.editTextName);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerVehicleType = findViewById(R.id.spinnerVehicleType);
        btnRegister = findViewById(R.id.btnRegister);

        // Setup spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.vehicle_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicleType.setAdapter(adapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                Intent intent=new Intent(UserregActivity.this,DashboardActivity.class);
                startActivity(intent);
            }
        });

        spinnerVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Handle item selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle no item selected
            }
        });
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
        unregisterReceiver(networkChangeReceiver);
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String vehicleType = spinnerVehicleType.getSelectedItem().toString();

        if (name.isEmpty() || mobile.isEmpty() || email.isEmpty()) {
            Toast.makeText(UserregActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(name, mobile, email, vehicleType);
        String userId = mDatabase.push().getKey();
        mDatabase.child(userId).setValue(newUser);
        Toast.makeText(UserregActivity.this, "User registered successfully!", Toast.LENGTH_LONG).show();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerTwoWheelerFragment, fragment); // Ensure you have a container in your layout
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
