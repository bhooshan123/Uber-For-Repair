package cse2.bhooshan.uberforrepair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText phoneno;
    Button otp;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the FragmentManager
        manager = getSupportFragmentManager();

        phoneno = findViewById(R.id.editTextPhone);
        otp = findViewById(R.id.btnSend);

        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneno.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("phoneno", phoneNumber);

                // if (PhoneNumberValidator.isValidPhoneNumber(phoneNumber)) {
                Toast.makeText(MainActivity.this, "Valid Phone Number", Toast.LENGTH_SHORT).show();
                OTPFragment OTPFragment = new OTPFragment();
                OTPFragment.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.containerOTPFragment, OTPFragment, "fragA");
                transaction.commit();
                // }
                // else {
                // Toast.makeText(MainActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                // }
            }
        });
    }
}
